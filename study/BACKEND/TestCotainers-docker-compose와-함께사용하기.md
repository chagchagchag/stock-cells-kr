# TestContainers 를 docker-compose와 함께 사용하기

어딘가에 정리해둔줄 감으로만 알고 있었는데, 알고보니 정리해둔 문서조차도 찾기 힘들었다. 그래서 이번 기회에 정리. 10분 컷으로 칼같이 정리됐으면 좋겠다.<br>

이번 문서에서는 `docker-compose`를 스프링 애플리케이션이 테스트가 실행될때에만 구동하고, 테스트가 종료되면 정지시키기 위해 `testcontainers` 를 사용해 환경을 세팅하는 과정을 정리해보려 한다.<br>

<br>

# docker-compose 

docker-compose 파일을 먼저 하나 추가하자. 특별한 내용은 없고 그냥 postgres를 하나 추가하는 것이 내용이다.<br>

`docker-compose.yml` 

```yaml
version: '3'
services:
  postgres-for-test:
    image: postgres
    restart: always
    environment:
      POSTGRES_USER: "${DB_USER_ID}"
      POSTGRES_PASSWORD: "${DB_USER_PASSWORD}"
      POSTGRES_DB: postgres
      TZ: Etc/UTC
    volumes:
      - ./init/:/docker-entry-point-initdb.d/
    ports:
      - 15432:5432

  adminer:
    image: adminer
    restart: always
#    container_name: postgres-test
    ports:
      - 25432:8080
```

<br>

그리고 패스워드 등은 `docker-compose.yml` 파일이 위치한 디렉터리와 같은 계층에 `.env` 라는 파일을 만들고 아래와 같이 입력해두자. (비밀번호등을 따로 `.env` 라는 숨김파일에 저장해두기 위한 용도)<br>

**`.env`**

```plain
DB_USER_ID=postgres
DB_USER_PASSWORD=1111
```

<br>

**docker-compose가 정상적으로 수행되는지 확인해보자.**

```bash
cd .\src\test\resources\docker\docker-compose\
docker-compose up -d
```

별다른 에러 메시지가 안나온다면 잘 실행되고 있느것이다.<br>

혹시 모르니 상태를 확인하기 위해 아래와 같이 `docker container ls`  명령어를 통해 컨테이너의 상태를 확인하자.<br>

**`docker-container-ls`**

```bash
 docker container ls
```

<br>

**container 종료**<br>

```bash
docker-compose down
```

<br>

# testcontainers 의존성 추가

`build.gradle` 에 아래와 같이 입력해주자.

```groovy
testImplementation 'org.testcontainers:testcontainers:1.17.1'
```

<br>

# application.yml

테스트환경에서만 구동할 것이기에 `src/test/resources` 에 `application.yml` 파일을 아래와 같이 만들어두자.<br>

`src/test/resources/application.yml`<br>

```yaml
spring:
  config:
    activate:
      on-profile:
        - test-docker
  datasource:
    driver-class-name: org.postgresql.Driver
    username: postgres
    password: 1111
    url: jdbc:postgresql://localhost:15432/postgres
# #  'script' must not be null or empty (테스트 스키마 적용전 아래 주석 해제)
#  sql:
#    init:
#      schema-locations:
#        - classpath:docker/sql/schema.sql
#      mode: always
```

<br>

# Test 코드 작성

테스트 코드라기보다는, TestContainer가 잘 구동되는지만 확인하기 위한 용도의 테스트코드다. 별 내용없는 빈 껍데기 테스트 코드인데, 위에서 작성한 `test-docker` 프로필이 적용된 테스트코드이다.<br>

<br>

```java
package io.study.transaction.transaction_study.connection;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.containers.DockerComposeContainer;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.io.File;

@Testcontainers
@ActiveProfiles("test-docker")
@SpringBootTest
public class TestContainerConnectionTest {

    static final DockerComposeContainer container;

    static{
        container = new DockerComposeContainer(new File("src/test/resources/docker/docker-compose/docker-compose.yml"));
        container.start();
    }

    @Test
    public void TEST_DOCKER_COMPOSE_CONTAINER_LOADING(){

    }
}
```



# 결과

로그를 쭉 읽어보다보니 제대로 작성했음을 확인할 수 있었다.<br>

<br>





