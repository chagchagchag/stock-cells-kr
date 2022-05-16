# DynamoDB LSI (Local Secondary Index)

Local Secondary Index 는 테이블을 Composite Primary 를 가진 테이블에만 추가할 수 있다. Local Secondary Index 는 서로 다른 RANGE 키를 가지는 테이블에 대한 동일한 HASH 키 를 유지한다.<br>

<br>

# 참고자료

> - [로컬 보조 인덱스 - Amazon DynamoDB](https://docs.aws.amazon.com/ko_kr/amazondynamodb/latest/developerguide/LSI.html) 
> - [Local Secondary Indexes | DynamoDB, explained. (dynamodbguide.com)](https://www.dynamodbguide.com/local-secondary-indexes)

<br>

# Local Secondary Index 의 특징

**테이블 생성시에 정의되어야 한다.**<br>

- 이미 존재하는 테이블에 대해 local secondary index를 추가하는 것은 불가능하다. 반드시 테이블 생성시에만 추가되어야 한다. (글로벌 세컨더리 인덱스와는 다른점)

<br>

**HASH 키 하나에 대해서 데이터를 10GB까지만 저장할 수 있다.**<br>

- 자세히 말하면, 기본테이블의 항목크기와 로컬 보조 인덱스의 크기를 모두 합해서 10GB를 저장할 수 있다.
- 이렇게 데이터 저장을 테이블의 항목들의 크기를 포함해 10GB까지만 저장할 수 있다는 점은 Projected Attribute 에 대해 현명하게 결정해야 하는 이유다.

<br>

**일관성**<br>

- 일관성 옵션을 선택할 수 있다.
- Local Secondary Index 는 기본 테이블 처럼 강력한 일관성(Strong Consistency), 최종적인 일관성(Eventual Consistency) 중 하나를 선택할 수 있다. 일관성이 강하면 읽기 용량이 더 많이 소모된다.

<br>

**기본테이블(base table)과 처리량 공유**<br>

- 모든 Local Secondary Index 는 기본 테이블의 읽기,쓰기 용량 단위를 사용한다.
- 개인적인의견이지만, 기본테이블에 저장할 수 있는 스토리지의 한계를 Local Secondary Index도 공유하기에, 최대로 저장할 수 있는 데이터의 건 수가 한정될 수도 있다.

<br>

# Creating a Local Secondary Index

`Local Secondary Index` 는 `Composite Primary Key` 와 함께 사용하는 것만 가능하다. 따라서, 단일 키를 사용하는 테이블에서는 `Local Secondary Index` 를 사용하는 것은 불가능하다.<br>

> 위 참고자료에 명시해둔 사이트의 예제에서는 `Users` 테이블의 경우 단일 키 테이블이기에 `Users` 테이블은 Local Secondary Index 를 사용할 수 없다고 이야기 하고 있고, 복합키를 사용하는 `UserOrdersTable` 을 예제로 사용해야 한다고 이야기하고 있다.

<br>

[Filtering](https://www.dynamodbguide.com/filtering#using-filters)  섹션에서는 특정 사용자의 주문을 검색하는 예제를 다루고 있다. 이 예제에서는 금액(Amount)이 기본키에 속하지 않아서, 모든 사용자의 주문을 검색한 다음 필터를 적용해 특정 금액을 초과하는 주문만 반환한다.<br>

데이터가 꽤 많이 쌓이게 되면서, 비교적 많은 수의 행을 처리해야 할 때, 모든 행을 모두 불러와서 필터링을 하는 이러한 방식은 비효율 적일 수 있다. 이때 `Amount` 속성을 정렬키로 사용해서 `Local Secondary Index`를 추가하면, `Amount` 속성을 빠르게 조회를 수행할 수 있다.<br>

**ex)** <br>

**예제 수행을 위해 기존에 테이블이 존재할 경우 테이블을 삭제**

```bash
$ aws dynamodb delete-table \
    --table-name UserOrdersTable \
    $LOCAL
```

<br>

**ex) 테이블 생성, `Local Secondary Index` 추가**<br>

- 아래 CLI를 자세히 보면, `--local-secondary-indexes` 를 따로 추가한 것을 확인할 수 있다.
- Username 이라는 Primary Key 와 함게, `Amount` 의 검색도 빠르게 되기 위해 Range 키로 `Amount` 속성을 정의하고 있다.

```bash
$ aws dynamodb create-table \
    --table-name UserOrdersTable \
    --attribute-definitions '[
      {
          "AttributeName": "Username",
          "AttributeType": "S"
      },
      {
          "AttributeName": "OrderId",
          "AttributeType": "S"
      },
      {
          "AttributeName": "Amount",
          "AttributeType": "N"
      }
    ]' \
    --key-schema '[
      {
          "AttributeName": "Username",
          "KeyType": "HASH"
      },
      {
          "AttributeName": "OrderId",
          "KeyType": "RANGE"
      }
    ]' \
    --local-secondary-indexes '[
      {
          "IndexName": "UserAmountIndex",
          "KeySchema": [
              {
                  "AttributeName": "Username",
                  "KeyType": "HASH"
              },
              {
                  "AttributeName": "Amount",
                  "KeyType": "RANGE"
              }
          ],
          "Projection": {
              "ProjectionType": "KEYS_ONLY"
          }
      }
    ]' \
    --provisioned-throughput '{
      "ReadCapacityUnits": 1,
      "WriteCapacityUnits": 1
    }' \
    $LOCAL
```

<br>

# Local Secondary Index 를 이용한 조회

Local Secondary Index를 이용해, Amont 속성의 값이 100 이상인 항목들을 조회하는 예제는 아래와 같다.

```bash
$ aws dynamodb query \
    --table-name UserOrdersTable \
    --index-name UserAmountIndex \
    --key-condition-expression "Username = :username AND Amount > :amount" \
    --expression-attribute-values '{
        ":username": { "S": "daffyduck" },
        ":amount": { "N": "100" }
    }' \
    $LOCAL
```

<br>

`--index-name`<br>
- Local Secondary Index 으로 생성해둔 `UserAmountIndex` 를 `--index-name` 의 옵션에 대한 값으로 지정했다.
- 즉, 위의 쿼리 수행시 `UserAmountIndex` 라는 Local Secondary Index 를 사용하겠음을 명시하고 있다.

<br>

`--filter-expression`<br>

- Local Secondary Index를 조건값으로 하는 조회를 할 때는 `--filter-expression` 을 사용하지 않는다. 위의 예제 CLI 코드를 보면 `--filter-expression` 이 사용되지 않은 것을 확인가능하다.
- 대신 `--key-condition-expression` 을 사용해 조건 값을 지정한다.

<br>
`--key-condition-expression`<br>

- “Username = :username AND Amount > :amount” 와 같은 표현식으로 조건값을 지정하고 있다.
- Local Secondary Index를 CLI로 조회할 때는 `--key-condition-expression` 을 사용한다.

<br>

# filter 를 사용할 때와의 차이점

`filter` 를 사용했을 경우 위의 예제는 `ScannedCount` 가 4가 나온다.<br>

하지만, `Local Secondary Index` 를 사용해 조회했을 때는 `ScannedCount` 가 1이 나온다.<br>

`filter` 를 사용했을 때에 비해 조회시 읽기를 위해 거치는 스캔 횟수가 줄어든다는 점이 인덱스를 사용했을 때의 장점이다.<br>

<br>
