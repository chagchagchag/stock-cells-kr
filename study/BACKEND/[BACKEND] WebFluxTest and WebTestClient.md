# WebFluxTest and WebTestClient

> WebFluxTest, WebTestClient 는 Controller 에 관련된 HTTP 계층을 서버기동 없이  테스트할때 사용하는 어노테이션이다.<br>
>
> <br>
>
> 참고) WebFluxTest 또는 WebTestClient 같은 Controller 계층 한정 테스트가 아닌, 전체테스트를 해야 할 경우<br>
>
> - 만약, 전체 애플리케이션 컨텍스트를 필요로 하는 통합테스트를 작성해야 한다면, `@SpringBootTest` 를 사용하면 된다. `@SpringBootTest` 는 `@AutoConfigureWebTestClient` 와 함께 조합되어 있다.<br>

<br>

# 참고자료

[Spring Boot - @WebFluxTest and WebTestClient with JUnit 5 (howtodoinjava.com)](https://howtodoinjava.com/spring-webflux/webfluxtest-with-webtestclient/) <br>

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

Controller 를 생성시에 필요한 다른 객체가 있을 때 보통 `@MockBean` 또는 `@Import` 를 사용해 주입받아서 테스트 한다. 쉽게 이야기하면, Controller 클래스의 생성자에 의존성을 명시하면 이것을 `@MockBean` 으로 주입받아서 사용하고, 만약 생성자/Setter 주입이 아닌 `@Autowired` 를 통한 필드 인젝션일 경우는 `@Import` 를 사용해 의존성을 해결한다. 이렇게 의존성이 있는 클래스를 [Spring Boot - @WebFluxTest and WebTestClient with JUnit 5 (howtodoinjava.com)](https://howtodoinjava.com/spring-webflux/webfluxtest-with-webtestclient/)  에서는 `collaborator` (협력자) 라고 이야기한다.<br>

<br>

# `WebTestClient`

내부적으로 reactive `WebClient` 를 사용하는 웹 서버를 테스트하기 위한 non-blocking 이면서 reactive 한 client다.<br>

`WebTestClient` 는 mock request와 response 객체를 이용해서, HTTP 상의 어떤 서버든 접속할수 있고, WebFlux 애플리케이션에 바로 bind 할 수도 있다.<br>

`WebTestClient` 는 `MockMvc` 와 유사한 역할을 한다. 다만 `WebTestClient` 는 WebFlux 엔드포인트를 테스트하기위한 목적의 클래스이다.<br>

<br>



