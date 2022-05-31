# Testing Web Layer

> Webflux 테스트를 정리해도 모자랄 판에, 순수 Spring Web Layer 를 테스트하는 방식을 정리하고 있다. 예전회사에서 개인적으로 정리해둔 문서가 있었지만, 예전 회사에서 개인 적으로 정리해둔 문서를 어떤 분이 모두 분리...해버리는 통에 원본을 잃어버렸다.<br>
>
> 그래서 이번 기회에 그냥 다시 처음부터 정리했다. 조금 더 다듬어야 한다.<br>

<br>

## 참고자료

[Getting Started | Testing the Web Layer (spring.io)](https://spring.io/guides/gs/testing-web/) <br>

<br>

## 예제

**HomeController.java**<br>

```java
package com.example.testingweb;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class HomeController {

	@RequestMapping("/")
	public @ResponseBody String greeting() {
		return "Hello, World";
	}

}
```

<br>

**TestingWebApplication.java**<br>

```java
package com.example.testingweb;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class TestingWebApplication {

	public static void main(String[] args) {
		SpringApplication.run(TestingWebApplication.class, args);
	}
}
```



`@SpingBootApplication` 은 `@Configuration`, `@EnableAutoConfiguration`, `@EnableWebMvc` , `@ComponentScan` 을 포함하고 있는 어노테이션이다.<br>

자세한 내용은 모두 번역하기엔 시간이 없기에 그냥 원문으로 대체. 나중에 다시 짧게 요약해둘 예정<br>

- `@Configuration`: Tags the class as a source of bean definitions for the application context.
- `@EnableAutoConfiguration`: Tells Spring Boot to start adding beans based on classpath settings, other beans, and various property settings.
- `@EnableWebMvc`: Flags the application as a web application and activates key behaviors, such as setting up a `DispatcherServlet`. Spring Boot adds it automatically when it sees `spring-webmvc` on the classpath.
- `@ComponentScan`: Tells Spring to look for other components, configurations, and services in the the `com.example.testingweb` package, letting it find the `HelloController` class.

The `main()` method uses Spring Boot’s `SpringApplication.run()` method to launch an application. Did you notice that there is not a single line of XML? There is no `web.xml` file, either. This web application is 100% pure Java and you did not have to deal with configuring any plumbing or infrastructure. Spring Boot handles all of that for you.

Logging output is displayed. The service should be up and running within a few seconds.

<br>

## Test 클래스 정의

### 첫번째 코드 : 단순 실행해보기

`src/test/com.example.testingweb/TestingWebApplicationTests.java`<br>

```java
package com.example.testingweb;

import org.junit.jupiter.api.Test;

import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class TestingWebApplicationTests {

	@Test
	public void contextLoads() {
	}

}
```

`@SpringBootTest` 어노테이션은 스프링부트가 Main Configuration 클래스를 찾으라고 말해주는 역할을 한다. 이 Main Configuration 클래스라는 것은 `@SpringBootApplication` 이 붙은 것을 의미한다.<br>

테스트를 CLI 로 구동시키려면 아래의 명령어를 수행하자.

- `./mvnw test`
- `./gradlew test` 

<br>

### 두번째 코드 : Controller 클래스가 로딩됐는지 확인

그다지 필요한 부분은 아니지만, 원문에 있어서 가져온 예제. 

```java
package com.example.testingweb;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class SmokeTest {

	@Autowired
	private HomeController controller;

	@Test
	public void contextLoads() throws Exception {
		assertThat(controller).isNotNull();
	}
}
```

<br>

### @Autowired

위 코드에서는 `@Autowired` 를 사용했다. 스프링은 테스트 메서드가 시작되기 전에 `@Autowired` 어노테이션을 해석해서 해당 컨트롤러를 주입해준다. (흠...)<br>

<br>

### AssertJ

AssertJ 에 대한 자세한 내용은 아래링크를 참고<br>

[AssertJ / Fluent assertions for java (joel-costigliola.github.io)](https://joel-costigliola.github.io/assertj/) <br>

<br>

### Spring Test 의 Application Context Caching

Spring Test 가 지원하는 좋은 점 중의 하나는, 애플리케이션 컨텍스트(Application Context)가 테스트 사이 사이마다 캐싱된다는 것이다.<br>

같은 설정으로 여러개의 테스트 케이스에서 돌릴때, 스프링은 애플리케이션을 한번 시작하는 만큼의 비용만 발생하게 된다.<br>

만약 캐시를 컨트롤하고 싶다면, `@DirtiesContext` 어노테이션을 사용해야 한다.<br>

<br>

## 웹 테스트) TestRestTemplate 을 이용한 테스트

애플리케이션을 구동시켜서 커넥션 내에서 HTTP Request 를 던져서 올바른 Response를 검증하는 테스트다.

**HttpRequestTest.java**<br>

```java
package com.example.testingweb;

import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class HttpRequestTest {

	@LocalServerPort
	private int port;

	@Autowired
	private TestRestTemplate restTemplate;

	@Test
	public void greetingShouldReturnDefaultMessage() throws Exception {
		assertThat(this.restTemplate.getForObject("http://localhost:" + port + "/",
				String.class)).contains("Hello, World");
	}
}
```

<br>

`webEnvironment=RANDOM_PORT` 

- 서버를 RANDOM PORT 에서 시작하겠다는 옵션<br>

`@LocalServerPort`

- `webEnvironment=RANDOM_PORT` 로 주입한 랜덤 포트 번호는, `@LocalServerPort` 어노테이션으로 얻어올 수 있다.

`TestRestTemplate`

- 스프링부트는 `TestRestTemplate` 기본적으로 제공해준다.

<br>

위의 방식은 스프링의 FULL STACK 이 사용되는 방식이면서, 서버를 시작하는 비용까지 들기에 다소 무거운 테스트 방식이다.<br>

<br>

## MVC 테스트 

MVC 테스트는 서버 전체를 구동하지 않고도 스프링의 full stack 을 구동하고, HTTP 리퀘스트를 처리하는 것과 같은 방식으로 코드가 호출되는 방식의 테스트다.<br>

이렇게 테스트하려면 MockMvc 객체를 이용하면 된다. 그리고 이 MockMvc객체를 주입받는 것은 `@AutoConfigureMockMvc` 어노테이션을 사용하면, MockMvc 객체를 사용할 수 있게 된다.<br>

<br>

### 첫번째 MVC 테스트 - @AutoConfigureMockMvc

> 이번 코드는 스프링의 full stack 을 로딩하는 코드다. 하지만 서버는 시작없이 애플리케이션 컨텍스트를 구동한다.

```java
package com.example.testingweb;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
public class TestingWebApplicationTest {

	@Autowired
	private MockMvc mockMvc;

	@Test
	public void shouldReturnDefaultMessage() throws Exception {
		this.mockMvc.perform(get("/")).andDo(print()).andExpect(status().isOk())
				.andExpect(content().string(containsString("Hello, World")));
	}
}
```

<br>

### 두번째 MVC 테스트 - @WebMvcTest

> 위의 코드는 서버 시작 없이 스프링의 애플리케이션의 컨텍스트를 구동하는 코드다. 스프링의 full stack 을 구동한다.
>
> 이번에는 `@WebMvcTest` 를 사용해 스프링의 모든 full stack 에 대한 애플리케이션 컨텍스트를 구동하는 것이 아니라, Web Layer 에만 한정시켜서 테스트의 범위를 제한한다.

<br>

```java
@WebMvcTest
public class WebLayerTest {

	@Autowired
	private MockMvc mockMvc;

	@Test
	public void shouldReturnDefaultMessage() throws Exception {
		this.mockMvc.perform(get("/")).andDo(print()).andExpect(status().isOk())
				.andExpect(content().string(containsString("Hello, World")));
	}
}
```

<br>

이제 `GreetingController` 라는 컨트롤러를 하나 더 추가했다고 해보자.<br>

<br>

```java
package com.example.testingweb;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;


@Controller
public class GreetingController {

	private final GreetingService service;

	public GreetingController(GreetingService service) {
		this.service = service;
	}

	@RequestMapping("/greeting")
	public @ResponseBody String greeting() {
		return service.greet();
	}

}
```

<br>

그리고 `GreetingService` 역시 만들었다. 코드는 아래와 같다.

```java
package com.example.testingweb;

import org.springframework.stereotype.Service;

@Service
public class GreetingService {
	public String greet() {
		return "Hello, World";
	}
}
```

<br>

### 세번째 MVC 테스트 - @WebMvcTest

이번에는 두번째 예제에서 GreetingController, GreetingService 만을 로딩해서 테스트하게끔 해본다. 이 과정에서 `@MockBean` 이라는 어노테이션을 사용한다. `@MockBean` 은 빈을 가짜로 만들어주는 것인데, 나중에 의존성이 꽤 많을때 일일이 Mockito.mock(...) 을 사용하지 않고 `@MockBean` 과 `@InjectMocks` 를 이용해 빈들을 결합해서 테스트할 수 있다.<br>

이번 문서에서 쓰기에 적합한 말은 아니지만, 항상 테스트를 할때는 테스트를 할 경계를 명확하게 해두면 정말 편하다. (개인적으로는 나도 TDD기반으로 신규플젝을 해봤지만, 아직까지는 이 부분에 대해 영역을 어떻게 잡을지 가끔 헷갈릴때가 있다.)<br>

<br>

```java
package com.example.testingweb;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(GreetingController.class)
public class WebMockTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private GreetingService service;

	@Test
	public void greetingShouldReturnMessageFromService() throws Exception {
		when(service.greet()).thenReturn("Hello, Mock");
		this.mockMvc.perform(get("/greeting")).andDo(print()).andExpect(status().isOk())
				.andExpect(content().string(containsString("Hello, Mock")));
	}
}
```

위의 코드에서는 `GreetingController` 에 대해서 테스트를 진행하는데, `GreetingService` 를 @MockBean 으로 주입받아서 사용한다. 이때 `GreetingService` 는 실제 객체가 아닌 가짜 객체다. 이 GreetingService를 스프링이 주입받을 수 있는 것은 Controller 의 생성자 시그니처를 통해 스프링 내부적으로 생성자의 적절한 타입을 판단해서 주입이 되는 것이다.<br>

<br>



















































