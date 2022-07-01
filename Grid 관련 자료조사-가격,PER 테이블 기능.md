# Grid 관련 자료조사 - 가격,PER 테이블 기능

만들려고 하는 기능은 이렇다.<br>

<br>

**가격/PER 테이블 기능**<br>

해당 주식의 가격이 `10% ~ -10%` 등락을 할때까지의 1% 단위 구간 대의 가격, 해당 가격에 대한 PER, 매수 계획 날짜, 매수(y/n)을 체크하기 위한 Excel 파일을 다운로드받을 수 있도록 해주는 테이블 기능을 만들고자 한다.<br>

<br>

**종목 필수 정보 헤더 기능**<br>

작은 글씨로 유보율, 당좌비율, EPS, 12fwd per, 네이버 파이낸스 바로가기 링크, 한경컨센서스 바로가기 링크, FnGuide 바로가기 링크를 추가해둘 예정이다.<br>

<br>

이중에서 오늘 정리할 기능 자료조사는 가격/PER 테이블 기능에 대한 자료조사 내용이다.<br>

이 기능을 구현하기 위해 종목검색 기능이 필요한데, 종목 기능의 진척은 아래와 같다. 

- UI 기능의 70프로는 완성(redux 연동 필요)
- 백엔드 기능의 70 프로 완성
  - 남아있는 작업은 아래와 같다.
  - 가격 일괄 insert 배치 기능 구현
  - WebClient 를 이용한 현재가 크롤링(Naver Finance) 

<br>

이 기능을 제외하고, 가격대의 `10% ~ -10%`  를 표현하기 위한 테이블은 단순히 더미데이터만 있어도 구현이 가능하기에 7월 1주차에 구현예정이다.<br>

이것과 관련해서 찾아본 자료는 아래와 같다.<br>

- [Table Export · Bootstrap Table](https://bootstrap-table.com/docs/extensions/export/) 
- [Bootstrap Table Examples](https://examples.bootstrap-table.com/#extensions/export.html)

<br>

크롬 창을 정리하려던 중에 열어둔 탭이 있어서 부랴부랴 정리를 시작했다. 아이구... 정리하는 것도 꽤 힘들으..<br>

<br>