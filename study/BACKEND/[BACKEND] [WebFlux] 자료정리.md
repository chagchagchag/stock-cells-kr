# 자료정리

> Reactive Programming 을 공부할 수 있을 만한 학습자료들을 정리해봤다. 처음에는 한국자료가 거의 없는줄 알고 외국 강의를 들었었다. 그런데, 알고보니 한국자료가 꽤 있었다.<br>
>
> = 내가 다시 보기 위한 자료정리

<br>

제일 자주 봤었던 자료 순으로 정리했다<br>

<hr>

**최범균님 블로그** (https://javacan.tistory.com/)<br>

boundedElastic() 을 어떻게 쓰는지 감이 안잡혀서 찾아보던 차에 [스프링 리액터 시작하기 6 - 쓰레드 스케줄링 :: 자바캔(Java Can Do IT)](https://javacan.tistory.com/entry/Reactor-Start-6-Thread-Scheduling?category=699082) 을 찾게 되었고, 여러가지 자료들이 많아서 감동했...었다. ㅠㅠ

- ['Reactive' 카테고리의 글 목록 :: 자바캔(Java Can Do IT)](https://javacan.tistory.com/category/Reactive) 

<br>

**백기선님 강의**<br>

> 백기선님 영상이 따로 있는줄 그저께 처음 알았다. 몰랐다. 시간될때 한번 정주행해야겠다.흐흐흐

- [리액티브 프로그래밍 1부 리액티브 프로그래밍 소개 - YouTube](https://www.youtube.com/watch?v=VeSHa_Xsd2U&list=PLfI752FpVCS9hh_FE8uDuRVgPPnAivZTY)
- [리액티브 프로그래밍 2부 Flux와 Mono - YouTube](https://www.youtube.com/watch?v=v0BnqWLxYjQ&list=PLfI752FpVCS9hh_FE8uDuRVgPPnAivZTY&index=2)
- [리액티브 프로그래밍 3부 StepVerifier - YouTube](https://www.youtube.com/watch?v=iqV5eKjnbFs) 
- [리액티브 프로그래밍 4. Transform - YouTube](https://www.youtube.com/watch?v=yaE2jyRdk_I&list=PLfI752FpVCS9hh_FE8uDuRVgPPnAivZTY&index=4)
- [flatMap 보충 학습 - YouTube](https://www.youtube.com/watch?v=sbPFDLZirnw&list=PLfI752FpVCS9hh_FE8uDuRVgPPnAivZTY&index=5)
- [리액티브 프로그래밍 5. Merge - YouTube](https://www.youtube.com/watch?v=Mu188MJXkh8&list=PLfI752FpVCS9hh_FE8uDuRVgPPnAivZTY&index=6)
- [리액티브 프로그래밍 6부. Request (backpressure, 토비님과 함께) - YouTube](https://www.youtube.com/watch?v=eZbssAcTem4&list=PLfI752FpVCS9hh_FE8uDuRVgPPnAivZTY&index=7)
- [리액티브 프로그래밍, Backpressure 보충 학습 - YouTube](https://www.youtube.com/watch?v=8hB1C4OCbz0&list=PLfI752FpVCS9hh_FE8uDuRVgPPnAivZTY&index=8)
- [리액티브 프로그래밍 7부 에러 처리 - YouTube](https://www.youtube.com/watch?v=27ugpDCLoG0&list=PLfI752FpVCS9hh_FE8uDuRVgPPnAivZTY&index=9)
- [리액티브 프로그래밍 8부 Adapt - YouTube](https://www.youtube.com/watch?v=AXwZgh3cAh0&list=PLfI752FpVCS9hh_FE8uDuRVgPPnAivZTY&index=10)
- [리액티브 프로그래밍 9부 기타 등등 - YouTube](https://www.youtube.com/watch?v=cKzwa9kl2Ts&list=PLfI752FpVCS9hh_FE8uDuRVgPPnAivZTY&index=11)
- [리액티브 프로그래밍 10부. 블록킹을 리액티브로 또는 반대로 - YouTube](https://www.youtube.com/watch?v=Bh8vTfO_4CE&list=PLfI752FpVCS9hh_FE8uDuRVgPPnAivZTY&index=12)

<br>

**Flux의 스케쥴러 관련해서 찾아봤던 자료들**<br>

- [Flux 3 - 스레드와 스케줄러 알아보기 : 네이버 블로그](https://m.blog.naver.com/PostView.naver?isHttpsRedirect=true&blogId=sthwin&logNo=221956619428)

<br>

**Reactor 의 각 Operator 에 대한 설명 요약된 자료들 (요약이 잘 되어있어서 훑어보기 좋다.)**<br>

- [Reactor 언제 어떤 Operator를 써야 할까?](https://luvstudy.tistory.com/100) 

<br>

**Mono -> Flux, Flux -> Mono 헷갈릴때 봤던 자료**<br>

- [Reactor map, flatMap method는 언제 써야할까?](https://luvstudy.tistory.com/95) 
- [spring - How to get a Flux from a Mono flatmap? - Stack Overflow](https://stackoverflow.com/questions/58674939/how-to-get-a-flux-from-a-mono-flatmap)
- [WebFlux에서 Mono, Flux에 Map 또는 flatMap을 사용할 때 null을 리턴하는 경우 — wedul](https://wedul.site/670)
- [Reactor Java #2 How to manipulate the data inside Mono and Flux ? | by Antoine Cheron | Medium](https://medium.com/@cheron.antoine/reactor-java-2-how-to-manipulate-the-data-inside-mono-and-flux-b36ae383b499)
  - flatMap
  - zip, take, takeLast, takeUntil, takeWhile
- [reactor.core.publisher.Mono#flatMap](https://www.programcreek.com/java-api-examples/?class=reactor.core.publisher.Mono&method=flatMap)
- [Reactor map, flatMap method는 언제 써야할까?](https://luvstudy.tistory.com/95)

<br>

**자세히 읽어봐야 하는데, 시간이 부족해서 잠시 스킵해둔 자료**<br>

- [Spring Webflux의 Functional Endpoints 사용법. (with RouterFunction)](https://gardeny.tistory.com/47) 
- [사용하면서 알게 된 Reactor, 예제 코드로 살펴보기 – tech.kakao.com](https://tech.kakao.com/2018/05/29/reactor-programming/) 

<br>

**Web on Reactive Stack**<br>

- [Web on Reactive Stack :: Spring Docs](https://spring.getdocs.org/en-US/spring-framework-docs/docs/spring-web-reactive/spring-web-reactive.html) 
- [Functional Endpoints :: Spring Docs](https://spring.getdocs.org/en-US/spring-framework-docs/docs/spring-web-reactive/webflux/webflux-fn.html)
- [java - How to iterate Flux and mix with Mono - Stack Overflow](https://stackoverflow.com/questions/46743973/how-to-iterate-flux-and-mix-with-mono)
- [java - Reactor : How to convert a Flux of entities into a Flux of DTO objects - Stack Overflow](https://stackoverflow.com/questions/58686680/reactor-how-to-convert-a-flux-of-entities-into-a-flux-of-dto-objects)

<br>

**블로그자료들**<br>

- [StepVerifier 사용 방법 (4/12)](https://sangpire.tistory.com/183) 
- [리액터 강의 정리 ( 3강 ~ 6강 : StepVerifier, Map, flatMap, Merge)](https://kok202.tistory.com/171)

<br>

<hr>

# 문제해결을 하면서 찾아봤던 자료들

StepVerifier, TestPublisher<br>

[Testing Reactive Streams Using StepVerifier and TestPublisher | Baeldung](https://www.baeldung.com/reactive-streams-step-verifier-test-publisher) <br>

<br>

`Flux<String>` 을 ServerResponse 로<br>

[mono - How to return Flux<String> in ServerResponse in WebFlux - Stack Overflow](https://stackoverflow.com/questions/70605765/how-to-return-fluxstring-in-serverresponse-in-webflux)<br>

-  결과적으로는 굉장히 간단하지만, 아무것도 모르는 상태에서 찾아보느라 꽤 헤맸었던듯 하다.

```java
Flux<String> strings = Flux.just("a", "b", "c");
ServerResponse.ok().body(strings, String.class);
```

<br>

내 경우는 아래와 같이 해결했다.

`ServerResponse.ok().body(Flux 변수, Flux 자료형)` 을 주면 내부의 데이터들은 `ServerResponse<T>` 로 변환되어 말려들어간다.

```java
public Mono<ServerResponse> searchTickersByCompanyName(ServerRequest request){
    return request.queryParam("companyName")
        .map(companyName -> ok()
             .contentType(MediaType.APPLICATION_JSON)
             .body(
                 redisService.searchCompanyNames(companyName, 0D, 5D, 0, 30),
                 TickerStockDto.class
             )
             .switchIfEmpty(notFound().build())
            )
        .orElse(notFound().build());
}
```

<br>

<br>

transform 메서드<br>

[Reactive Programming with Reactor - Using transform Operator](https://www.logicbig.com/tutorials/misc/reactive-programming/reactor/transform-operation.html)<br>

<br>

webflux get value from mono

[Webflux get value from Mono - TedBlob](https://tedblob.com/webflux-get-value-from-mono/)<br>

- 1 ) block
- 2 ) blockOptional
- 3 ) chain multiple consumers (doOnNext, doOnNext, subscribe)

<br>

**compose() vs transform() vs as() vs map (Flux/Mono)**<br>

[reactive programming - compose() vs. transform() vs. as() vs. map() in Flux and Mono - Stack Overflow](https://stackoverflow.com/questions/47348706/compose-vs-transform-vs-as-vs-map-in-flux-and-mono)

<br>

[WebFlux: Reactive Programming With Spring, Part 3 - DZone Java](https://dzone.com/articles/webflux-reactive-programming-with-spring-part-3)
