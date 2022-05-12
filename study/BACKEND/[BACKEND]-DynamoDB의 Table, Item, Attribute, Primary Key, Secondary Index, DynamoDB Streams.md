# DynamoDB의 Table, Item, Attribute, Primary Key, Secondary Index, DynamoDB Streams

> 참고자료: 
>
> - [Core Components of Amazon DynamoDB](https://docs.aws.amazon.com/amazondynamodb/latest/developerguide/HowItWorks.CoreComponents.html)
> - [Working with Tables and Data in DynamoDB](https://docs.aws.amazon.com/amazondynamodb/latest/developerguide/WorkingWithTables.html) 

<br>

나름 나이가 어렸을 때였다면, DynamoDB도 뭐 그냥 DB랑 똑같겠지 뭐... 하면서 쌩으로 부딪히면서 했을 것 같다. 이제는 새로운 DB나 새로운 플랫폼을 도전적으로 적용해볼 때는 기본적인 문서정도는 한번 훑어보는게 훨씬 좋다는 생각이 자주 드는 편이다.<br>

삽질을 자주해본 결과여서인가 싶기도 하다. 개발할때 적어도 최소 3시간 정도만 시간 내서 기본 문서 및 Core Concept 들을 정리하면 된다. 3시간만이라도 시간내서 기본 개념을 정리하는 정도는 나쁘지 않은 것 같다. 물론 가끔 실무에서는 가끔 꽤 많이 매우 눈치보일때도 있었던것 같기는 하다...<br>

오늘은 몸이 너무 축났다 ㅋㅋ 하루 쉬어간다고 생각하고 기본 용어를 정리해보려고 한다. 평소보다 한시간 늦게 새벽 2시에 재깍 일어났지만, 그래도 굉장히 피곤하다. 그래서 한시간 정도 딴짓을...ㅋㅋ <br>

작년 9월 부터 올해까지, 그리고 2주일 전 까지 항상 비슷하게 하루 3시간 \~ 4시간 취침하는 방식으로 생활해왔는데, 매번 수/목 요일이 체력적으로 고비였던것 같다. 항상 중간에 수/목요일에 슬럼프 겪고나서, 금요일날 불타오른다. (미국주식 데이터 개발하겠다고 하루에 4시간 내지 3시간씩 잤었다. 지금 생각하면 미치지 않았나 싶다.)<br>

이때 느낀게, 슬럼프일때 내가 왜 이렇게 못할까? 왜 이렇게 의지력이 딸릴까? 체력이 더 좋으면 안되나? 이런 생각을 자주 했다가, 자주 겪다보니, 슬럼프는 항상오는 거였다고 깨달았었다. 가끔 슬럼프일때마다 낙천적으로 `쉬어가야겠네..` 하고 생각하면 되는 것 같다.<br>

<br>

## 목차 

There are limits in DynamoDB. For more information, see [Service, Account, and Table Quotas in Amazon DynamoDB](https://docs.aws.amazon.com/amazondynamodb/latest/developerguide/ServiceQuotas.html).

**Topics**

- [Tables, Items, and Attributes](https://docs.aws.amazon.com/amazondynamodb/latest/developerguide/HowItWorks.CoreComponents.html#HowItWorks.CoreComponents.TablesItemsAttributes)
- [Primary Key](https://docs.aws.amazon.com/amazondynamodb/latest/developerguide/HowItWorks.CoreComponents.html#HowItWorks.CoreComponents.PrimaryKey)
- [Secondary Indexes](https://docs.aws.amazon.com/amazondynamodb/latest/developerguide/HowItWorks.CoreComponents.html#HowItWorks.CoreComponents.SecondaryIndexes)
- [DynamoDB Streams](https://docs.aws.amazon.com/amazondynamodb/latest/developerguide/HowItWorks.CoreComponents.html#HowItWorks.CoreComponents.Streams)

<BR>

## 기본 용어들

### Tables, Items, Attributes

Tables

- 일반적인 Database 와 유사하게 DynamoDB는 데이터를 테이블에 저장한다.

<br>

Items

- 데이터베이스에서 이야기하는 테이블의 각 행을 DynamoDB 에서는 Item 이라고 이야기한다.
- Item(=로우) 은 Attribute(=컬럼)들의 집합이고, 다른 Item(=로우)들과는 유일하게 다르게 식별될 수 있어야 한다.

<br>

Attributes

- 데이터베이스에서 흔히 이야기하는 컬럼을 DynamoDB에서는 Attribute 라고 이야기한다.
- 각 Item(=로우)은 하나 또는 하나 이상의 Attribute(=컬럼)으로 구성된다.
- Attribute는 더 이상 세분화할 필요가 없는 기본적인 데이터 요소다.
- 기본키를 제외하면 `People` 테이블은 스키마가 없다.(`Schemaless`)

<br>

### 주요 기본 용어들

스키마리스(Schemaless)

- 기본키 또는 고유 식별키 조합을 제외하고, 스키마가 없는 형식을 스키마리스하다고 이야기한다.
- 기본키/기본키 조합을 제외한 Attribute 들에 대해 데이터 유형을 미리 정의할 필요가 없다.

<br>

스칼라 속성 (Scalar Attribute)

- 하나의 값만 가질 수 있는 속성을 의미한다.
- 문자열, 숫자가 스칼라 속성의 일반적인 예이다.

<br>

중첩된 속성(Nested Attribute)

- 속성(Attribute) 내에 하나의 Attribute를 더 선언하는 것을 의미한다.
- 쉽게 이야기해서 중첩된 속성(Attribute)을 의미한다.
- DynamoDB는 최대 32단계까지 중첩된 속성을 지원한다.

<br>

<br>

## ex. People 테이블, Music 테이블

> 자세한 내용은 [Working with Tables and Data in DynamoDB](https://docs.aws.amazon.com/amazondynamodb/latest/developerguide/WorkingWithTables.html) 을 참고.

### `People` 

> 이미지 출처 : https://docs.aws.amazon.com/amazondynamodb/latest/developerguide/images/HowItWorksPeople.png <br>

![People 테이블](https://docs.aws.amazon.com/amazondynamodb/latest/developerguide/images/HowItWorksPeople.png)

<br>

- People 테이블을 보면, 고유한 식별자 또는 기본키를 가지고 있어 다른 Item 과는 구분된다.
- 위의 People 테이블은 기본키를 단일키 구성으로 하고 있다. (단순 기본 키 조합)
- 기본키를 제외하면 `People` 테이블은 스키마가 없다.(Schemaless)
- 스키마가 없기때문에 Attribute나 데이터 유형을 미리 정의할 필요가 없다. 각 항목은 고유한 속성을 가질 수 있다.
- `People` 테이블은 대부분, 스칼라 속성을 가지고 있다. 스칼라 속성은 하나의 값만 가질 수 있는 속성을 의미한다.

<br>

### `Music` 테이블

> 이미지 출처 : https://docs.aws.amazon.com/amazondynamodb/latest/developerguide/images/HowItWorksMusic.png

![Music 테이블](https://docs.aws.amazon.com/amazondynamodb/latest/developerguide/images/HowItWorksMusic.png)



- Music 테이블은 `Artist` , `SongTitle` 속성을 기본키로 구성하고 있다.
- 따라서, 테이블이 각 Item은 `Artist`, `SongTitle` 조합으로 다른 Item 과 구분할 수 있다.
- 기본키(Artist, SongTitle)를 제외하고 `Music` 테이블은 스키마가 없다(Schemaless). 데이터 유형을 미리 정의할 필요가 없다.
- 위의 그림에서 항목 중 하나에 중첩된 속성으로 `PromotionInfo` 가 있다. 여기에는 `RadioStationsPlaying` 이라는 중첩된 속성이 하나 더 있다.
- DynamoDB는 최대 32단계까지 중첩된 속성을 지원한다.

 <br>

## Primary Key, Partition Key, Partition Key and sort key

>  공식문서의 설명은 한국/영어문서 모두 조금 이상하고 부자연스럽다. 그래서 내가 이해한대로 정리해두었다.<br>

<br>

테이블을 생성할 때 테이블 이름 외에도 테이블의 기본 키를 지정해야 한다. 기본 키는 테이블의 각 항목을 고유하게 식별한다. 따라서 두 항목이 동일한 키를 가질 수 없다.<br>

DynamoDB는 `Partition Key`, `Partition key and sort key` 라는 두가지 종류의 Primary Key 를 지원한다.<br>

<br>

**파티션 키(Partition Key)**<br>

파티션 키는 Hash Attribute (해시속성)으로도 인식될 수 있다. 해시속성은 다이나모DB 내부의 해시 함수로부터 생성된다. 다이나모 디비는 파티션 키 값을 기준으로 여러 파티션에 데이터를 균등하게 분배한다.<br>

예를 들면 어노테이션 중 `@DynamoDBHashKey` 라는 어노테이션이 있는데, 이것이 여기에 해당된다.<br>

<br>

**정렬 키(Sort Key)**<br>

정렬 키는 Range Attribute (정렬속성)으로도 인식될 수 있다. 정렬속성은 동일한 파티션 키를 가진 항목을 정렬 킷값에 따라 정렬된 순서로 저장하는 방식으로 생성된다.<br>

<br>

### Partition Key

- 파티션 키라고 하는 하나의 속성만으로 구성된 간단한 기본 키
- 파티션 키만 있는 테이블에서는 두 항목이 동일한 파티션 키 값을 가질 수 없다.
- DynamoDB는 파티션 키의 값을 해시함수를 이용해 UUID 라는 값으로 변환해 저장한다.
- 위에서 정리한 `People` 테이블은 단일 기본키만 있는 테이블이다. `People` 테이블은 `PersonId` 값을 이용해서 Item 에 접근 가능하다.

<br>

### Partition Key and Sort Key

> 위에서 살펴봤던 `Music` 테이블이 복합키를 가진 테이블의 대표적인 예다.

- composite primary key 라고 불린다.(한국말로는 뭐라고 하는지 잘 모르겠다. 그냥 `복합키` 라고 정리해야 겠다.)
- `Partition Key and Sort Key` 는 두개의 속성(attribute)으로 구성된다.
- 첫번째 속성(attribute)은 `partition key` 다.
- 두번째 속성(attribute)은 `sort key` 다.

- `partition key and sort key` 가 적용된 테이블은 여러개의 아이템이 같은 `partition key` 를 가질 수 있다. 단, 서로 다른 `sort key` 를 가져야 한다.<br>
- 위에서 살펴봤던 `Music` 테이블이 복합키를 가진 테이블의 대표적인 예다. `Artist`, `SongTitle` 을 이용해서 Music 테이블에 접근하는 것이 가능하다.

<br>

## Secondary Indexes

### Global Secondary Index

### Local Secondary Index

