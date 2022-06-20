# WebFlux - Annotated Controller vs Functional Endpoint 방식

WebFlux 가 맞는지 Webflux가 맞는지 아직도 헷갈린다.<br>

<br>

# 참고자료

- [WebFlux: Reactive Programming With Spring, Part 3 - DZone Java](https://dzone.com/articles/webflux-reactive-programming-with-spring-part-3)

- [RouterFunction (Spring Framework 5.3.21 API)](https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/web/reactive/function/server/RouterFunction.html)
- [HandlerFunction (Spring Framework 5.3.21 API)](https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/web/reactive/function/server/HandlerFunction.html)

<br>

# Spring MVC vs Webflux

WebFlux 는 Spring Framework 5.0 의 일부분으로 소개됐었다. Spring MVC와는 다르게 WebFlux 는 Servlet API 를 필요로 하지 않는다. WebFlux 는 비동기(asynchronous), 논 블로킹 (non-blocking) 방식이다.<br>

WebFlux 는 [Project Reactor](https://projectreactor.io/) 를 통해 Reactive Streams 표준을 구현하고 있다.<br>

<br>

# Annotation 기반 vs Functional 기반

Reactive Stream 을 지원하는 WebFlux 기반 웹 애플리케이션 개발시 @Controller, @GetMapping 과 같은 어노테이션도 지원한다. 이런 방식을 Annotation 기반 모델이라고 부르는 듯 하다.<br>

<br>

## Annotation 기반

```java
@RestController
@RequestMapping("/employees")
public class StudentController {
    @Autowired
    private EmployeeService employeeService;

    public EmployeeController() {}

    @GetMapping("/{id}")
    public Mono<ResponseEntity<Employee>> getStudent(@PathVariable long id) {
        return employeeService.findEmployeeById(id)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @GetMapping
    public Flux<Employee> listStudents(@RequestParam(name = "name", required = false) String name) {
        return employeeService.findEmployeesByName(name);
    }

    @PostMapping
    public Mono<Employee> addNewEmployee(@RequestBody Employee employee) {
        return employeeService.addNewEmployee(employee);
    }

    @PutMapping("/{id}")
    public Mono<ResponseEntity<Employee>> updateStudent(@PathVariable long id, @RequestBody Student student) {
        return employeeService.updateEmployee(id, student)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> deleteEmployee(@PathVariable long id) {
        return employeeService.findEmployeeById(id)
                .flatMap(s ->
                        employeeService.deleteEmployee(s)
                                .then(Mono.just(new ResponseEntity<Void>(HttpStatus.OK)))
                )
                .defaultIfEmpty(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

}
```

<br>

## Functional Endpoint 기반 

> 참고)
>
> - [RouterFunction (Spring Framework 5.3.21 API)](https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/web/reactive/function/server/RouterFunction.html)
> - [HandlerFunction (Spring Framework 5.3.21 API)](https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/web/reactive/function/server/HandlerFunction.html)

<br>

람다 기반의 요청처리를 수행하는 방식을 `Functional Endpoint` 라고 부른다. `HandlerFunction` , `RouterFunction` 을 기반으로 하고 있다. 개인적으로 사용해본 결과 `Spring MVC` 에 비해 계층이 분리가 되어서 테스트 코드를 작성하기에도 조금 더 좋았던 것 같다.<br>

<br>

HandlerFunction

```java
@FunctionalInterface
public interface HandlerFunction<T extends ServerResponse> {
    Mono<T> handle(ServerRequest request);
}
```

RouterFunction

```java
@FunctionalInterface
public interface RouterFunction<T extends ServerResponse> {
    Mono<HandlerFunction<T>> route(ServerRequest request);
    ...
}
```

<br>

위에서 살펴봤던 Controller 를 Router, Handler 로 분리해보자

**EmployeeRouter.java**

```java
@Configuration
public class EmployeeRouter {

    @Bean
    public RouterFunction<ServerResponse> route(EmployeeHandler employeeHandler){
        return RouterFunctions
            .route(
                GET("/employees/{id:[0-9]+}")
                    .and(accept(APPLICATION_JSON)), employeeHandler::getEmployee)
            .andRoute(
                GET("/employees")
                    .and(accept(APPLICATION_JSON)), employeeHandler::listEmployees)
            .andRoute(
                POST("/employees")
                    .and(accept(APPLICATION_JSON)),employeeHandler::addNewEmployee)
            .andRoute(
                PUT("employees/{id:[0-9]+}")
                    .and(accept(APPLICATION_JSON)), employeeHandler::updateEmployee)
            .andRoute(
                DELETE("/employees/{id:[0-9]+}")
                    .and(accept(APPLICATION_JSON)), employeeHandler::deleteEmployee);
    }
}
```

<br>

**EmployeeHandler.java**

```java
@Component
public class EmployeeHandler {
    private EmployeeService employeeService;
    public EmployeeHandler(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    public Mono<ServerResponse> getEmployee(ServerRequest serverRequest) {
        Mono<Employee> employeeMono = employeeService.findEmployeeById(
                Long.parseLong(serverRequest.pathVariable("id")));
        return employeeMono.flatMap(employee -> ServerResponse.ok()
                .body(fromValue(employee)))
                .switchIfEmpty(ServerResponse.notFound().build());
    }

    public Mono<ServerResponse> listEmployees(ServerRequest serverRequest) {
        String name = serverRequest.queryParam("name").orElse(null);
        return ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(employeeService.findEmployeesByName(name), Employee.class);
    }

    public Mono<ServerResponse> addNewEmployee(ServerRequest serverRequest) {
        Mono<Employee> employeeMono = serverRequest.bodyToMono(Employee.class);
        return employeeMono.flatMap(employee ->
                ServerResponse.status(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(employeeService.addNewEmployee(employee), Employee.class));
    }

    public Mono<ServerResponse> updateEmployee(ServerRequest serverRequest) {
        final long employeeId = Long.parseLong(serverRequest.pathVariable("id"));
        Mono<Employee> employeeMono = serverRequest.bodyToMono(Employee.class);
        return employeeMono.flatMap(employee ->
                ServerResponse.status(HttpStatus.OK)
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(employeeService.updateEmployee(employeeId, employee), Employee.class));
    }

    public Mono<ServerResponse> deleteEmployee(ServerRequest serverRequest) {
        final long employeeId = Long.parseLong(serverRequest.pathVariable("id"));
        return employeeService
                .findEmployeeById(employeeId)
                .flatMap(s -> ServerResponse.noContent().build(employeeService.deleteEmployee(s)))
                .switchIfEmpty(ServerResponse.notFound().build());
    }

}
```





