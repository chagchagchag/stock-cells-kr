# WebFluxTest and WebTestClient

> `WebFluxTest` , `WebTestClient` 는 Controller 에 관련된 HTTP 계층을 서버기동 없이  테스트할때 사용하는 어노테이션이다.<br>
>
> <br>
>
> 참고) WebFluxTest 또는 WebTestClient 같은 Controller 계층 한정 테스트가 아닌, 전체테스트를 해야 할 경우<br>
>
> - 만약, 전체 애플리케이션 컨텍스트를 필요로 하는 통합테스트를 작성해야 한다면, `@SpringBootTest` 를 사용하면 된다. `@SpringBootTest` 는 `@AutoConfigureWebTestClient` 와 함께 조합되어 있다.<br>

<br>

# 참고자료

[Spring Boot - @WebFluxTest and WebTestClient with JUnit 5 (howtodoinjava.com)](https://howtodoinjava.com/spring-webflux/webfluxtest-with-webtestclient/) <br>

[Writing Unit Test in Reactive Spring Boot Application | by BRAJENDRA PANDEY | Medium](https://medium.com/@BPandey/writing-unit-test-in-reactive-spring-boot-application-32b8878e2f57)<br>

[Spring 5 Web Reactive: Flux, Mono, and JUnit Testing - DZone Java](https://dzone.com/articles/spring-5-web-reactive-flux-and-mono)<br>

<br>

# 의존성

```xml
<dependency>
  <groupId>io.projectreactor</groupId>
  <artifactId>reactor-test</artifactId>
  <scope>test</scope>
</dependency>
```

<br>

# `@WebFluxTest` 어노테이션

`@WebFluxTest` 어노테이션을 사용하면 Full Auto-Configuration 을 비활성화하고, `WebFluxTest`와 관련된 설정들만을 적용시킨다.<br>

예를 들면, `@Controller`, `@ControllerAdvice` , `@JsonComponent`, `JsonConverter`, `WebFluxConfigurer` 빈들을 활성화한다.<br>

`@Component`, `@Service`, `@Repository` 는 스캔 대상에서 제외된다.<br>

<br>

# @MockBean

Controller 를 생성시에 필요한 다른 객체가 있을 때 보통 `@MockBean` 또는 `@Import` 를 사용해 주입받아서 테스트 한다. 쉽게 이야기하면, Controller 클래스의 생성자에 의존성을 명시하면 이것을 `@MockBean` 으로 주입받아서 사용하고, 만약 생성자/Setter 주입이 아닌 `@Autowired` 를 통한 필드 인젝션일 경우는 `@Import` 를 사용해 의존성을 해결한다. 이렇게 의존성이 있는 클래스를 [Spring Boot - @WebFluxTest and WebTestClient with JUnit 5 (howtodoinjava.com)](https://howtodoinjava.com/spring-webflux/webfluxtest-with-webtestclient/)  에서는 `collaborator` (협력자) 라고 이야기한다.<br>

<br>

# @MockUser

Mock User로 스프링 시큐리티를 테스트할 때 사용하는 어노테이션이다. 쉽게 이야기하면, 스프링 시큐리티 테스트시 가짜 유저로 테스트하는 것을 의미함.<br>

@MockUser로 받아오는 객체내의 속성,Attribute(=필드)들은 아래와 같다.

- username
  - 
- roles
  - 테스트할 `role` 을 할당한다. 기본 값은 `USER.ROLE_` 자동으로 붙는다.
- password
  - 테스트하려는 패스워드. 기본값은 `password`

<br>

# `WebTestClient`

`WebTestClient` 는 내부적으로 reactive `WebClient` 를 사용하는 웹 서버를 테스트하기 위한 non-blocking 이면서 reactive 한 client다.<br>

`WebTestClient` 는 mock request와 response 객체를 이용해서, HTTP 상의 어떤 서버든 접속할수 있고, WebFlux 애플리케이션에 바로 bind 할 수도 있다.<br>

`WebTestClient` 는 `MockMvc` 와 유사한 역할을 한다. 다만 `WebTestClient` 는 WebFlux 엔드포인트를 테스트하기위한 목적의 클래스이다.<br>

<br>

## 예제1) 정말 단순한 GET 요청 테스트

> 아주 단순한 형태의 GET 요청 테스트 코드<br>
>
> (예제2 까지는 단순하게 요청을 보내는 것만 다뤄보고, 예제 3부터 조금 복잡한 예제들을 만들어서 계속 추가해둘 예정)<br>
>
> 소스코드의 자세한 부분은 [tdd-you-can-do-it/example_codes/webflux_study/src/test/java/io/study/tdd/webflux_study/hello/hellocontroller at main · soon-good/tdd-you-can-do-it (github.com)](https://github.com/soon-good/tdd-you-can-do-it/tree/main/example_codes/webflux_study/src/test/java/io/study/tdd/webflux_study/hello/hellocontroller) 에서 확인할 수 있다.

<br>

**HelloController.java**<br>

```java
@RestController
public class HelloController {

    private final HelloService helloService;

    public HelloController(HelloService helloService){
        this.helloService = helloService;
    }

    @GetMapping("/hello/messages")
    public Flux<Message> getMessages(){
        return helloService.selectMessages();
    }

    @GetMapping("/hello/messages/{id}")
    public Mono<Message> getMessageById(@PathVariable("id") final String id){
        if(Optional.ofNullable(id).isEmpty()) return Mono.empty();
        if(id.isEmpty()) return Mono.empty();

        return helloService
                .findById(Long.parseLong(id))
                .orElse(Mono.empty());
    }

}
```

<br>

**`/hello/messages` API 테스트**<br>

```java
package io.study.tdd.webflux_study.hello.hellocontroller;

import io.study.tdd.webflux_study.hello.HelloController;
import io.study.tdd.webflux_study.hello.HelloService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;

@ExtendWith(SpringExtension.class)
@WebFluxTest(controllers = HelloController.class)
public class GetMessagesTest {

    @Autowired
    private WebTestClient webClient;

    @MockBean
    private HelloService helloService;

    @Test
    public void SHOULD_RETURN_OK() {
        webClient.get().uri("/hello/messages")
                .exchange()
                .expectStatus().isEqualTo(HttpStatus.OK);
    }

}
```

<br>

**`/hello/messages/{id}` API 테스트**<br>

```java
package io.study.tdd.webflux_study.hello.hellocontroller;

import io.study.tdd.webflux_study.hello.HelloController;
import io.study.tdd.webflux_study.hello.HelloService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;

@ExtendWith(SpringExtension.class)
@WebFluxTest(controllers = HelloController.class)
public class GetMessageByIdTest {

    @Autowired
    private WebTestClient webClient;

    @MockBean
    private HelloController helloController;

    @Test
    public void 일반적인_정상적인_요청에_대한_테스트(){
        webClient.get().uri("/hello/messages/1")
                .exchange()
                .expectStatus().isEqualTo(HttpStatus.OK);
    }
}
```

<br>

## 예제2) 정말 단순한 POST 요청 테스트

> 아주 단순한 형태의 POST 요청 테스트 코드<br>
>
> (예제2 까지는 단순하게 요청을 보내는 것만 다뤄보고, 예제 3부터 여러가지 메서드와 경우의 수를 예로 들은 예제들을 만들어서 계속 추가해둘 예정)<br>
>
> 소스코드의 자세한 부분은 [tdd-you-can-do-it/example_codes/webflux_study/src/test/java/io/study/tdd/webflux_study/hello/hellocontroller at main · soon-good/tdd-you-can-do-it (github.com)](https://github.com/soon-good/tdd-you-can-do-it/tree/main/example_codes/webflux_study/src/test/java/io/study/tdd/webflux_study/hello/hellocontroller) 에서 확인할 수 있다.

<br>

추가예정...<br>

<br>

## 예제3) 다양한 예제...







