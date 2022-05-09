# React 의 useMemo

아주 오래전에 리액트 책도 없을때, 패캠 강의를 오프라인으로 수강했었는데, 이 때는 함수형 컴포넌트 자료가 거의 없었다. 시간이 많이 지나서 사이드 프로젝트를 준비하면서 함수형 컴포넌트를 사용해야만 하는 시점이 왔는데, 그냥 이번 기회에 함수형 컴포넌트도 스터디하기로 결정했다. <br>

2022년도... 지금 현재는 좋은 책도 많아서 `useState`, `useEffect` 와 같은 기본적인 함수에 대해서는 쉽게 배울 기회가 자주 있다. `useEffect`, `useState` 와 관련해서 쉽게 설명한 책도 있고, 블로그도 있다.<br>

<br>

오늘 정리할 `useMemo` 는 내가 주 교재로 사용하는 책에서는 설명이 없었다. 그래서 인터넷에서 자료를 찾던 중에 마침 잘 정리된 자료가 있어서 그 내용들을 응용해서 내가 진행중인 사이드 프로젝트애 대입해서 정리해보고자 한다.<br>

**참고자료**<br>

[daleseo.com - React Hooks : useMemo 사용법](https://www.daleseo.com/?tag=Java8)<br>

- 이분 자료 중에 자바 자료들도 있는데, 예전에 몇번 보면서 설명을 너무 잘하셔서 자주 감탄했던 것 같다.
- React에 GraphQL 까지 하시는 분이었다니... 놀라울 따름이다.

<br>

## useMemo 언제 쓰지?

예를 들어 아래와 같은 함수를 컴포넌트 내에서 렌더링 시마다 호출하고 있다고 해보자.

```javascript
function selectPriceList(from, to){
    // ... some implementation
}
```

렌더링이 발생할 때 마다 `from` , `to` 의 값이 항상 바뀌는게 아니라면 굳이 `selectPriceList(from, to)` 함수를 계속 호출할 필요가 없다. 이와 같은 불편한 사용자 경험은 `memoization` 기법을 적용해서 개선 가능하다.<br>

렌더링이 발생할 때, 이전 렌더링 버전과 현재 렌더링 버전을 비교해서, `from`, `to` 가 변경된게 없으면 메모리에 저장해둔 데이터를 그대로 사용하면 된다.<br>

이런 메모이제이션 로직은 직접 구현할 수도 있지만, React 에서는 `useMemo` 라는 기능을 `hook` 함수 중 하나로 제공하고 있다.<br>

<br>

## useMemo hook API 

selectPriceList 메서드에 useMemo 를 적용하는 예제를 살펴보자. 

```jsx
function ClosePriceComponent({x, y}){
    const z = useMemo(() => selectPriceList(from, to), [from, to]);
    return <div>{z}</div>
}
```

위 함수에서 useMemo 함수는 두개의 인자를 받는다.

- 첫번째 인자 : `() => selectPriceList(from, to)` 
  - 결과값을 생성해주는 팩토리 메서드
- 두번째 인자 : [from, to]
  - 팩토리 메서드를 호출할지, 재활용할지를 판단할 기준이 되는 입력변수들
  - 예를 들어 매개변수`from`, `to`  를 모두 `[from, to]` 와 같이 모두 지정해주면, `from`, `to` 둘중 하나라도 바뀌면, 메모이제이션 된 값을 재사용하는 대신 첫번째 인자의 팩토리 메서드를 호출해 새로운 값으로 메모이제이션 한다.<br>

<br>

## 예제

> 이건 조금 뒤에 정리할 예정. 영업이익/매출액/당기순이익 그래프 컴포넌트를 만들기 시작하면 정리를 시작하게 될 것 같다. 지금 뭘 작성해봐야 시간만 낭비되서... 