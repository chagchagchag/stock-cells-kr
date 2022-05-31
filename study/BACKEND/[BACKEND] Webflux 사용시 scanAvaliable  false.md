# Webflux 사용시 scanAvaliable : false

> 정리할게 산더미인데, 이렇게 사소한거라도 하나씩 쳐내야지 하나씩 줄어들것 같아서 `Typora` 에디터를 연 김에 정리를 시작하게 되었다.<br>

<br>

Webflux 사용시에 `scanAvaliable: false` 라는 문구를 발견했었다. 인터넷에서 검색하기로는 보통 Mono 타입에 Flux를 줄때 이런 에러가 난다는 이야기들이 많았다.<br>

나중에 알고보니 내 경우는 이런 케이스의 문제가 아니었다.<br>

Exception 이 발생했을때 Exception 전용 핸들러에서 예외를 처리하도록 처리하고 있는데, 이때 Return 하는 값은 `Mono<T>` 였다. 아무리 제너릭을 썼다고 하더라도 타입까지 맞춰줬는데 에러가 나는건 아무래도 이상했었다.<br>

그래서 이것 저것 찾아보고 해결하느라 1시간 정도를 소비했는데, 알고보니 ExceptionHandler 에서 리턴하는 데이터 타입은 `Flux`, `Mono` 같은 타입이 아닌 일반 자바 자료형을 전달해줘야 하는 것이었다. 즉, Spring Webflux 관련 이슈였다. 문법적인 이슈가 아니었다.<br>

<br>

<br>

**참고했던 자료들**<br>

[Spring Boot - Reative Rest Api 리액티브 서비스  (tistory.com)](https://imleaf.tistory.com/93)<br>

[How To Return A Reactive Flux That Contains A Reactive Mono And Flux? (tutorialmeta.com)](https://tutorialmeta.com/question/how-to-return-a-reactive-flux-that-contains-a-reactive-mono-and-flux)<br>

[In the Response, i am getting scanAvailable=true when returning flux as ResponseEntity – Java (tutorialink.com)](https://java.tutorialink.com/in-the-response-i-am-getting-scanavailabletrue-when-returning-flux-as-responseentity/)<br>

<br>

<br>

<br>

<br>

<br>

<br>

<br>

<br>

<br>

<br>

<br>

<br>

<br>



<br>

<br>

<br>

<br>

<br>

<br>

몇주 전에 90달러 아래로 [TSM](https://finance.yahoo.com/quote/TSM?p=TSM&.tsrc=fin-srch) 을 깨작 깨작 사서 모으고 있었는데, 갑자기 90달러 위로 올라가고 있다. 이런 덴장...<br>

가격 떨어지라고 적는 거임.. <br>

[LULU](https://finance.yahoo.com/quote/LULU?p=LULU&.tsrc=fin-srch) 도 관심있다!!!  [LULU](https://finance.yahoo.com/quote/LULU?p=LULU&.tsrc=fin-srch) 너도 얼른 가격 좀 떨어지거라...<br>

2분기 실적발표 후에 떨어지기를 바래야하는 걸까...<br>

<br>