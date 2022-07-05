# (그리드 라이브러리 선정) react-bootstrap-table2 제외, material-table 사용결정

어제\~오늘 사이에 선택한 라이브러리인 `react-bootstrap-table2` 대신`material-table` 기능을 사용하기로 결정했다. 지금 쓰고 있는 이 글은 결정을 내리고 나서, `이 글을 쓸 정도로 중요한가?` 하는 고민을 버뮤다 삼각지대처럼 반복하다가... `아, 내가 계속 똑같은 고민을 하고 있었구나?` 하면서 결정을 왜 하게 됬는지 간단하게 정리하기로 했다.<br>

<br>

react-bootstrap-table2 사용을 포기하게 된 이유

- 지금 만들고 있는 기능에서 csv export 기능이 꽤 중요하다. 그런데 react-bootstrap-table2 는 한글이 깨져서 저장되는 이슈가 있다.
- 이 이슈를 해결하기 위해 두시간 정도를 소모했고, 그 이후 다른 옵션들도 테스트해보는데, 지금 당장은 사용하기 어렵겠다는 생각이들었다.
- 다른 라이브러리의 사용성이 떨어지면 돌아와서 사용을 검토하기로 했다.
- 그래서 오늘 잠깐 개발했던 내용은 커밋해두기로 했다.

<br>

[material-table](https://github.com/mbrn/material-table)

- csv export example : [material-table](https://material-table.com/#/docs/features/export) 
- 다국어가 지원되는지 여부를 먼저 조사했다.
- 잘되는 것 같다.
- 일단 내일 부터는 이것으로 진행하게 될 것 같다.

<br>

jquery 기반 순수 bootstrap-table (첫번째, 두번째 모두 보류해야 할 경우)

- jquery 를 import 해서 사용해야한다는 생각에 잠깐 보류를 했었는데, 다른 기능이 모두 마음에 들지 않으면 순수 bootstrap-table 을 사용하는 것을 검토할 예정이다.
- 순수 bootstrap-table 라이브러리는 csv, pdf, excel 모두 지원했던걸로 기억한다.
- 물론 다국어지원이 되는지는 테스트를 해봐야할듯 하다.
- [Bootstrap Table Examples](https://examples.bootstrap-table.com/#extensions/export.html#view-source) 
- [Online Editor - Bootstrap Table](https://live.bootstrap-table.com/example/extensions/export.html)
  - 이건 직접 작성해봐야 한다. `data-url` 에 사용된 json 데이터를 쓰기에 직접 코드를 작성해봐야 지원이되는지를 알 수 있다.

<br>

참고할 자료들

- [Top 5 React Table Libraries](https://blog.bitsrc.io/top-5-react-table-libraries-170505f75da7)

- [RC-TABLE](https://github.com/react-component/table) 

- [React Table](https://react-table.tanstack.com/) 

- [Material UI Data Tables](https://github.com/gregnb/mui-datatables) 를 모두 사용해보고 어떤걸 사용할 지 결정하게 될 듯 하다. <br>

<br>