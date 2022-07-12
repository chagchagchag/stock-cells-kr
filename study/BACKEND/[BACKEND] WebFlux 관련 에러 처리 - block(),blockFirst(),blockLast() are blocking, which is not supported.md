# block()/blockFirst()/blockLast() are blocking, which is not supported

> 참고자료 : [block()/blockFirst()/blockLast() are blocking, which is not supported in thread reactor-http-nio-3](https://wildeveloperetrain.tistory.com/106) 

<br>

webflux 로 프로그래밍을 처음 하다보면 아래와 같은 문구를 접할때가 있다.

> block()/blockFirst()/blockLast() are blocking, which is not supported.

webflux 는 블로킹을 지원하지 않기에 발생하는 문제다. 해결방법은 두가지가 있다.

- spring-boot-starter-web 의존성 추가
- 완전한 비동기 코드로 변경

내 경우는 두번째 방식으로 해결했다. 처음할때는 어려웠는데, 조금씩 구현코드를 만들면서 그걸 응용해서 뭔가를 또 만들다보니 조금씩 응용이 됐던것 같다.<br>

<br>

참고로,<br>

webflux 의 컨테이너는 Non Blocking 방식의 Netty 로 구동되지만 <br>

spring-starter-web의 컨테이너는 Blocking 방식의 Tomcat 으로 구동된다. 

<br>

나중에 Spring Batch를 사용해야 하는데, reactive 와 spring batch는 서로 잘 호환이 안된다고 한다. 관련되어서 자료를 찾아봤다.

- [Spring Batch와 Reactive는 같이 갈 수 있을까...? 🤔](https://btakeya.tistory.com/43)
- [배달의민족 최전방 시스템! ‘가게노출 시스템’을 소개합니다. | 우아한형제들 기술블로그](https://techblog.woowahan.com/2667/) 