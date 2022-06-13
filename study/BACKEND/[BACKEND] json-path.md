# jsonpath



# 참고자료

[json-path/JsonPath: Java JsonPath implementation (github.com)](https://github.com/json-path/JsonPath) <br>

<br>

# 의존성

```xml
<dependency>
    <groupId>com.jayway.jsonpath</groupId>
    <artifactId>json-path</artifactId>
    <version>2.7.0</version>
</dependency>
```

<br>

# 예제

아래와 같은 json 데이터가 있다고 가정.

```json
{
    "store": {
        "book": [
            {
                "category": "reference",
                "author": "Nigel Rees",
                "title": "Sayings of the Century",
                "price": 8.95
            },
            {
                "category": "fiction",
                "author": "Evelyn Waugh",
                "title": "Sword of Honour",
                "price": 12.99
            },
            {
                "category": "fiction",
                "author": "Herman Melville",
                "title": "Moby Dick",
                "isbn": "0-553-21311-3",
                "price": 8.99
            },
            {
                "category": "fiction",
                "author": "J. R. R. Tolkien",
                "title": "The Lord of the Rings",
                "isbn": "0-395-19395-8",
                "price": 22.99
            }
        ],
        "bicycle": {
            "color": "red",
            "price": 19.95
        }
    },
    "expensive": 10
}
```

이때 만들어낼 수 있는 쿼리들의 종류들은 아래와 같다.

| JsonPath (click link to try)                                 | Result                                                       |
| ------------------------------------------------------------ | ------------------------------------------------------------ |
| [$.store.book[*\].author](http://jsonpath.herokuapp.com/?path=$.store.book[*].author) | The authors of all books                                     |
| [$..author](http://jsonpath.herokuapp.com/?path=$..author)   | All authors                                                  |
| [$.store.*](http://jsonpath.herokuapp.com/?path=$.store.*)   | All things, both books and bicycles                          |
| [$.store..price](http://jsonpath.herokuapp.com/?path=$.store..price) | The price of everything                                      |
| [$..book[2\]](http://jsonpath.herokuapp.com/?path=$..book[2]) | The third book                                               |
| [$..book[-2\]](http://jsonpath.herokuapp.com/?path=$..book[2]) | The second to last book                                      |
| [$..book[0,1\]](http://jsonpath.herokuapp.com/?path=$..book[0,1]) | The first two books                                          |
| [$..book[:2\]](http://jsonpath.herokuapp.com/?path=$..book[:2]) | All books from index 0 (inclusive) until index 2 (exclusive) |
| [$..book[1:2\]](http://jsonpath.herokuapp.com/?path=$..book[1:2]) | All books from index 1 (inclusive) until index 2 (exclusive) |
| [$..book[-2:\]](http://jsonpath.herokuapp.com/?path=$..book[-2:]) | Last two books                                               |
| [$..book[2:\]](http://jsonpath.herokuapp.com/?path=$..book[2:]) | Book number two from tail                                    |
| [$..book[?(@.isbn)\]](http://jsonpath.herokuapp.com/?path=$..book[?(@.isbn)]) | All books with an ISBN number                                |
| [$.store.book[?(@.price < 10)\]](http://jsonpath.herokuapp.com/?path=$.store.book[?(@.price < 10)]) | All books in store cheaper than 10                           |
| [$..book[?(@.price <= $['expensive'\])]](http://jsonpath.herokuapp.com/?path=$..book[?(@.price <= $['expensive'])]) | All books in store that are not "expensive"                  |
| [$..book[?(@.author =~ /.*REES/i)\]](http://jsonpath.herokuapp.com/?path=$..book[?(@.author =~ /.*REES/i)]) | All books matching regex (ignore case)                       |
| [$..*](http://jsonpath.herokuapp.com/?path=$..*)             | Give me every thing                                          |
| [$..book.length()](http://jsonpath.herokuapp.com/?path=$..book.length()) | The number of books                                          |

<br>

아래에서부터는 공식 문서에서 제공하는 예제들 중에서 간단하게 훑어볼만한 것들만 추려왔다. 요점정리라기보다는 그냥 백업(?)같은 느낌이다.<br>

<br>

# Predicates

Predicate 방식은 두가지가 있다.

- Inline Predicates
- Filter Predicates

<br>

## Inline Predicates

inline 으로 predicate 하는 방식<br>

```java
List<Map<String, Object>> books =  JsonPath.parse(json)
                                     .read("$.store.book[?(@.price < 10)]");
```

<br>

Predicate 여러개를 `&&` 또는 `||` 으로 조합해서 사용할수 있다. 예를 들면 아래와 같은 방식으로 조합하는 경우가 있다.

- `[?(@.price < 10 && @.category == 'fiction')]`
- `[?(@.category == 'reference' || @.price > 10)]` 

또는 부정연산자(negate predicate)를 이용해 조건식을 반대로 걸수도 있다.<br>

- `[?(!(@.price < 10 && @.category == 'fiction'))]`

<br>

## Filter Predicates

조건식을 이용한 방법외에도 `json-path` API에서 제공하는 메서드를 이용해 원하는 값을 필터링하는 것도 가능하다.<br>

```java
import static com.jayway.jsonpath.JsonPath.parse;
import static com.jayway.jsonpath.Criteria.where;
import static com.jayway.jsonpath.Filter.filter;
...
...

Filter cheapFictionFilter = filter(
   where("category").is("fiction").and("price").lte(10D)
);

List<Map<String, Object>> books =  
   parse(json).read("$.store.book[?]", cheapFictionFilter);

```

두번째 구문을 보면 표현식 안에 `?` 와 같은 표현식이 들어가 있다.<br>

이렇게 `?` 으로 표시된 곳에 Filter 객체를 넣을 수 있도록 하는 것 역시 가능하다<br>

<br>

`Inline` 으로 표현식을 만들어서 값을 얻어내거나, 조회하는 것도 꽤 쓸만하다. 하지만, `json-path` API 에서 제공하는 Filter 객체를 이용한 메서드를 사용하면, 아래와 같이 꽤 유연한 표현식으로 조합이 가능하다는 것 역시 장점이다.

```java
Filter fooOrBar = filter(
   where("foo").exists(true)).or(where("bar").exists(true)
);
   
Filter fooAndBar = filter(
   where("foo").exists(true)).and(where("bar").exists(true)
);
```

<br>

이렇게 Filter 객체로 만들어진 객체들은 `?, ?` 과 같은 표현식으로 여러개의 Filter 객체에 대한 Placeholder 를 두어서 조회가 가능하도록 하는 것 역시 가능하다.<br>

<br>

아래에서부터는 공식문서에서 제공하는 연산자들이다. 이정도 영어는 해석 없이 그냥 읽어도 바로 이해될 것이기에 따로 일일이 정리하기보다는 백업해둔다는 생각으로 발췌해왔다.<br>

<br>

# 연산자, Function, Filter 연산자들 (공식문서 제공)

## Operators

| Operator                  | Description                                                  |
| ------------------------- | ------------------------------------------------------------ |
| `$`                       | The root element to query. This starts all path expressions. |
| `@`                       | The current node being processed by a filter predicate.      |
| `*`                       | Wildcard. Available anywhere a name or numeric are required. |
| `..`                      | Deep scan. Available anywhere a name is required.            |
| `.<name>`                 | Dot-notated child                                            |
| `['<name>' (, '<name>')]` | Bracket-notated child or children                            |
| `[<number> (, <number>)]` | Array index or indexes                                       |
| `[start:end]`             | Array slice operator                                         |
| `[?(<expression>)]`       | Filter expression. Expression must evaluate to a boolean value. |

## Functions

Functions can be invoked at the tail end of a path - the input to a function is the output of the path expression. The function output is dictated by the function itself.

| Function  | Description                                                  | Output type |
| --------- | ------------------------------------------------------------ | ----------- |
| min()     | Provides the min value of an array of numbers                | Double      |
| max()     | Provides the max value of an array of numbers                | Double      |
| avg()     | Provides the average value of an array of numbers            | Double      |
| stddev()  | Provides the standard deviation value of an array of numbers | Double      |
| length()  | Provides the length of an array                              | Integer     |
| sum()     | Provides the sum value of an array of numbers                | Double      |
| keys()    | Provides the property keys (An alternative for terminal tilde `~`) | `Set<E>`    |
| concat(X) | Provides a concatinated version of the path output with a new item | like input  |
| append(X) | add an item to the json path output array                    | like input  |

## Filter Operators

Filters are logical expressions used to filter arrays. A typical filter would be `[?(@.age > 18)]` where `@` represents the current item being processed. More complex filters can be created with logical operators `&&` and `||`. String literals must be enclosed by single or double quotes (`[?(@.color == 'blue')]` or `[?(@.color == "blue")]`).

| Operator | Description                                                  |
| -------- | ------------------------------------------------------------ |
| ==       | left is equal to right (note that 1 is not equal to '1')     |
| !=       | left is not equal to right                                   |
| <        | left is less than right                                      |
| <=       | left is less or equal to right                               |
| >        | left is greater than right                                   |
| >=       | left is greater than or equal to right                       |
| =~       | left matches regular expression [?(@.name =~ /foo.*?/i)]     |
| in       | left exists in right [?(@.size in ['S', 'M'])]               |
| nin      | left does not exists in right                                |
| subsetof | left is a subset of right [?(@.sizes subsetof ['S', 'M', 'L'])] |
| anyof    | left has an intersection with right [?(@.sizes anyof ['M', 'L'])] |
| noneof   | left has no intersection with right [?(@.sizes noneof ['M', 'L'])] |
| size     | size of left (array or string) should match right            |
| empty    | left (array or string) should be empty                       |