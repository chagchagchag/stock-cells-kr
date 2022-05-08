# 리액트 차트, 그리드 라이브러리 조사

Amazon Dynamo SDK, Spring Data DynamoDB 를 사용해보면서 느꼈다. 처음 사용해보는 라이브러리는 샘플 프로젝트에서 그냥 사용만 먼저 해보는게 훨씬 낫다.<br>

무턱대고 처음보는 기술을 직접 개발중인 프로젝트에서 TDD로 개발하는 것 말고, 그냥 가벼운 마음으로 하나씩 시도해본 후에 프로젝트에 도입해보는 것이 좋다는 것을 확실하게 느꼈다.<br>

<br>

## 차트 라이브러리 

>  참고자료 : 
>
> - [[React] 리액트 그래프/차트 라이브러리모음](https://velog.io/@eunjin/React-%EB%A6%AC%EC%95%A1%ED%8A%B8-%EA%B7%B8%EB%9E%98%ED%94%84%EC%B0%A8%ED%8A%B8-%EB%9D%BC%EC%9D%B4%EB%B8%8C%EB%9F%AC%EB%A6%AC-%EB%AA%A8%EC%9D%8C)<br>
>   - 크... 이런 자료 감사합니다 ㅠㅠ
> - 

<br>

개인적으로 [nivo](https://nivo.rocks/), [recharts](https://recharts.org/en-US), [chartjs](https://www.chartjs.org/),  [victory](https://formidable.com/open-source/victory/) 를 각각 써보고 디자인적인 요소는 제외하고, 제일 편한걸로 해봐야겠다 하고 생각중이다<br>

- [https://nivo.rocks/](https://nivo.rocks/)
- [https://recharts.org/en-US](https://recharts.org/en-US)

- [https://uber.github.io/react-vis/examples/showcases/radial](https://uber.github.io/react-vis/examples/showcases/radial)
- [https://apexcharts.com/](https://apexcharts.com/)

- [https://formidable.com/open-source/victory/](https://formidable.com/open-source/victory/)

- [https://www.chartjs.org/](https://www.chartjs.org/)

- [https://www.highcharts.com/](https://www.highcharts.com/)

<br>

## 그리드 라이브러리

> 참고자료 : 
>
> - [Top 5 React Table Libraries](https://blog.bitsrc.io/top-5-react-table-libraries-170505f75da7)
>   - Thanks

<br>

개인적으로는 [RC-TABLE](https://github.com/react-component/table) , [React Table](https://react-table.tanstack.com/) , [Material UI Data Tables](https://github.com/gregnb/mui-datatables) 를 모두 사용해보고 어떤걸 사용할지 결정하게 될 듯 하다. <br>

CSV 추출 기능은 두가지 방식으로 해결 가능하다. (물론, 지금 당장 구현할 생각은 절대 없다. 이직 후에 깃랩으로 리포지터리를 이관 후에 진행하게 될 듯하다.)<br>

- 서버 사이드 처리
  - 서버단에 보내는 요청과, 서버의 응답을 잘 맞춘다. 서버에서 응답받은 csv 형식 데이터를 저장하는 기능을 클라이언트 사이드에서 구현. 
  - 다소 서버사이드에 부하가 심하다는 단점이 있다.
  - 또는 POI 등의 라이브러리를 사용하는 방식.
    - 다소 무거운 방식이지만, 아직 그리 대용량 데이터는 사용하지 않는다. 사이드 플젝도 기획을 직전 3개 분기 또는 직전 3개 연도만 검색하게끔 되어 있기에 그리 큰 데이터가 필요하지 않다.
- 클라이언트 사이드 처리
  - 서버에서는 json 데이터만을 전달해준다.
  - 클라이언트 단에서 json 으로 전달받은 데이터를 기반으로 csv/pdf 파일 저장 기능을 구현한다.
  - 이것이 오히려 더 편한 방식이다. client 라이브러리에서 지원하는 csv/pdf 라이브러리가 꽤 많기 때문이다.
  - 필요한 데이터를 요청하는 형식을 클라이언트/서버 사이에 잘 맞춰서 API 로 구현하면 굉장히 유용하게 될 듯 싶다.

<br>

- Material-table - [https://github.com/mbrn/material-table](https://github.com/mbrn/material-table)
  - CSV 추출이 가능하다.
  - 커스텀 컬럼 렌더링, 컬럼 수정기능 등을 개별 로우에 제공된다.
  - documentation 이 좋다.
- RC-TABLE - [https://github.com/react-component/table](https://github.com/react-component/table)
  - 가볍다. data 필터링, 드롭다운 메뉴가 컬럼 헤더에 적용된다.
  - 소스코드와 함께 예제가 매우 많다.
- Resuite-Table - [https://github.com/rsuite/rsuite-table](https://github.com/rsuite/rsuite-table)
  - 정렬 기능이 강력하다.
  - Right to Left, 컬럼 테이블 width 조정이 가능하다. 자식노드 확장이 가능하다.
  - documentation 이 잘되어있다.
  - 단점) 스타일을 커스터마이징 하기 쉽지 않다.
- React Table - [https://react-table.tanstack.com/](https://react-table.tanstack.com/)
  - 가볍다. 빠르다. 커스터마이징이 강력하다. 디자인이 심플하다. 커스터마이징이 쉽다. 
  - 클라이언트 사이드, 서버사이드에서의 페이지네이션을 지원한다.
  - pivot 기능과 Aggregation 기능을 제공한다.

- Matarial UI DataTables - [https://github.com/gregnb/mui-datatables](https://github.com/gregnb/mui-datatables)
  - 커스텀 컴포넌트, 스타일링을 제공
  - 반응형 디자인이 어느 디바이스에서든 잘 적용된다.
  - filtering 기능, 컬럼 보이기/가리기 기능, 페이지네이션,  sorting 기능이 꽤 단순하다.
  - 명확하고, 자세한 documentation

<br>