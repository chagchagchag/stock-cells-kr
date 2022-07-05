# react-bootstrap-table, react-bootstrap-table2

> 원래 이렇게 정리할 만한 글은 아니지만, 어딘가에 참고자료를 백업해두어야 하고, 데스크탑 크롬의 탭을 얼른 얼른 지워줘야 머리도 깔끔해지기에 정리를 시작했다.

<br>

원래는 테이블 구현시 순수 bootstrap 기반 기능인 것으로 보이는 bootstrap-table 을 사용하려고 했었다.<br>

참고)

- https://bootstrap-table.com/
- https://bootstrap-table.com/docs/extensions/export/
- https://examples.bootstrap-table.com/#extensions/export.html

<br>

그런데… 코드를 직접 작성하다보니, 결국은 어찌어찌 해서 jquery 를 로딩하는 부분에 도달하게 되었는데, react 컴포넌트 내에서 jquery 를 임포트해서 쓸 바에야, 컴포넌트 라이브러리를 찾는게 낫겠다 싶었다.<br>

<br>

그러던 중에 찾은 것은 react-bootstrap-table, react-bootstrap-table2 다.<br>

<br>

**react-bootstrap-table**

react-bootstrap-table의 레거시 버전(초창기버전)은 react-bootstrap-table 이다.

- https://react-bootstrap.github.io/

- http://allenfang.github.io/react-bootstrap-table/index.html

<br>

그리고 http://allenfang.github.io/react-bootstrap-table/index.html 에서 `GET REACT-BOOTSTRAP-TABLE2` 버튼을 눌르면 react-bootstrap-table2 를 발견하게 된다.<br>

<br>

**react-bootstrap-table2**

- https://react-bootstrap-table.github.io/react-bootstrap-table2/

<br>

**Getting Started**

- https://react-bootstrap-table.github.io/react-bootstrap-table2/docs/getting-started.html

<br>

**Live Demo**

- [Live Demo](https://react-bootstrap-table.github.io/react-bootstrap-table2/storybook/index.html?selectedKind=Welcome&selectedStory=react%20bootstrap%20table%202%20&full=0&addons=1&stories=1&panelRight=0&addonPanel=storybook%2Factions%2Factions-panel)

<br>

react-bootstrap-table 기능을 둘러보던 중에 사실, export 기능이 빌트인으로 제공되지 않는다는 점이 꽤 불편하게 다가왔었는데, react-bootstrap-table2 기능은 export 기능이 제공된다. 그래서 react-bootstrap-table2 를 사용하기로 결정했다.<br>

또 한걸음의 삽질을 시작했다.<br>

<br>