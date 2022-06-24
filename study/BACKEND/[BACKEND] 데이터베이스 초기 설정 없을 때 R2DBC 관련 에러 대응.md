# 데이터베이스 초기 설정 없을 때 R2DBC 관련 에러 대응

> 참고자료 : [Spring Boot R2DBC Autoconfig Disable](https://btakeya.tistory.com/42)

<br>

아직 DB를 어떤 걸 쓸지는 확실하게 결정하지는 않았다. DynamoDB를 쓰기로 거의 마음을 굳혔지만, 나중에 다른 DB로 교체하게 될 가능성이 있다. DynamoDB를 쓰려면 어느 정도는 온디맨드로 설정을 바꾸거나 사양을 살짝 커스터마이징하게 될 수 있기 때문이다.<br>복잡하게 이것 저것 할 바에야 mysql이나 postgresql 기반으로 세팅하고 r2dbc로 연결해서 쓰면 될수도 있다. 그런 이유로 이번 프로젝트에 r2dbc를 추가해두었다.<br>

<br>

그런 과정에서 DB를 설정해두지 않은 상태로 애플리케이션의 기능을 중점으로 두어 개발을 진행하면서 확인했던 이슈가 있었다. Application 을 구동할 때 나타나는 에러였다. 에러 문구는 아래와 같다.<br>

<br>

```plain
***************************
APPLICATION FAILED TO START
***************************

Description:

Failed to configure a ConnectionFactory: 'url' attribute is not specified and no embedded database could be configured.

Reason: Failed to determine a suitable R2DBC Connection URL


Action:

Consider the following:
	If you want an embedded database (H2), please put it on the classpath.
	If you have database settings to be loaded from a particular profile you may need to activate it (the profiles live are currently active).


Process finished with exit code 1
```

<br>

이 문제는 아래와 같이 `R2dbcAutoConfiguration.class` 를 exclude 해두는 것으로 해결 가능하다.<br>

물론 Repository나 Mapper등을 개발하기 시작하면, exclude 속성은 반드시 지워야 한다.<br>

```java
// 아래 설정은 임시. DB 관련 내용들이 확정되면 제거 예정
@SpringBootApplication(exclude = R2dbcAutoConfiguration.class)
public class ReactiveDataApplication {

	public static void main(String[] args) {
		SpringApplication.run(ReactiveDataApplication.class, args);
	}

}
```



