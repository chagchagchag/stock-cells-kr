# [FRONTEND] 차트라이브러리 조사 - RECHART

RECHARTS를 써봤는데, 일단, 나중에 쓸지도 모르는 기능인 `Sychronized Chart` 기능이 제공된다는 점에서 괜찮다 싶었다. 제일 처음 적용해본 예제는 `Synchronized Chart`다. <br>

예제를 모두 react 프로젝트를 개설해서 손으로 직접 따라해보면서 작성해봤는데, 예전에 사용해봤었던 `highchart`, `amchart`, `morris` 차트에 비해 비교적 직관적으로 차트를 조합한다는 점이 마음에 들었다. <br>

물론, 여기에 더해 개발자 기능이 좋아서, 프로젝트를 생성하거나, 파일을 만들지 않고도 공식 페이지에서 직접 수정해보며 테스트해볼 수 있었다.<br>

신기한 점은 일반적인 React 컴포넌트가 아닌 `PureComponent` 라는 것을 사용한다는 것인데, 그리드 등의 컴포넌트와 사용할때 충돌되는 것은 없는지 확인이 필요하겠다 싶었다.<br>

<br>

## SynchronizedLineChart

> 예제 링크 : 
>
> - [SynchronizedLine Chart](https://recharts.org/en-US/examples/SynchronizedLineChart)
> - 직접 작성한 예제의 링크 : [https://github.com/soon-good/kr-stock-calculator/tree/main/study/FRONTEND/demo-try1-rechart](github.com/soon-good/kr-stock-calculator/tree/main/study/FRONTEND/demo-try1-rechart) <br>

<br>

작성이 완료된 모습은 아래와 같다. `SynchronizedLineChart` 는 여러개의 차트가 `syncId` 라는 `props`에 넘겨지는 아이디값으로 같은 x축을 가지면서 공유할수 있도록 되어 있는 차트다.<br>

만약 가장 마지막의 컴포넌트에서 `syncId` 를 누락하면?<br>

- 가장 마지막의 차트만 `tooltip` 으로 표시되는 차트가 된다. 
- syncId가 깨졌으니, 모든 설정을 가장 마지막 차트가 덮어쓰는 것으로 보인다.

<br>

![ASDF](./img/CHART-STUDY-1-RECHARTS/1.png)



