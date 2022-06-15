# Spring Profile 설정 변경사항 (2.4+)

실무에서는 2.4 이후의 버전을 제대로 반영하고 있는 경우를 많이 보지 못했다. <br>

<br>

# 참고자료

- [spring-boot\] 2.4 부터 변경된 구성파일 처리방식 살펴보기 - I'm honeymon(JiHeon Kim).](http://honeymon.io/tech/2021/01/16/spring-boot-config-data-migration.html)
- [Config file processing in Spring Boot 2.4](https://spring.io/blog/2020/08/14/config-file-processing-in-spring-boot-2-4)

<br>

# spring boot 2.4 이전 버전

`test-in-memory` , `test-containers` 와 같은 프로필들을 각각 작성해두고 `spring.profiles.active` 항목에 원하는 프로필을 명시해두는 방식이었다.

```yaml
spring:
  profiles:
    active: test-containers
---
spring:
	profiles: test-in-memory
# 그냥 이것 저것 설정들
---
spring:
	profiles: test-containers
# ... 이것 저것 설정들
```

<br>

# (2.4 이전) spring.profiles.active (디폴트 프로필 설정)

[Config file processing in Spring Boot 2.4](https://spring.io/blog/2020/08/14/config-file-processing-in-spring-boot-2-4) 에서는 아래와 같이 `spring.profiles.active` 는 사용가능하다고 이야기하고 있다

> You can still use `spring.profiles.active`properties to activate or include profiles from an `application.properties`or `application.yaml`file.

<br>

다만, 아래와 같이 `spring.profiles.active` 와 `spring.config.activate.on-profile` 을 하나의 프로필 내에서 같이 쓰는 것은 안된다. 예를 들면 아래와 같은 방식이다.

```yaml
test=value
#---
spring.config.activate.on-profile=dev
spring.profiles.active=local # will fail
test=overridden value
```

<br>

# (2.4 이후) spring.config.import

spring 2.4 부터는 yaml 파일 하나에 `---` 구분자로 문서를 나눠서 profiles를 구분짓는 방식이 더 이상 유효하지 않다고 한다.

가급적이면 필요한 profile 들을 개별 파일로 선언해두고 application.yml 에서는 해당 파일을 import 하는 방식으로 읽어들이는 방식으로 하는 것이 권장된다.

예제를 만들어봤다.

**application.yml**<br>

```<yaml
spring:
	config:
		import: classpath:test-docker.yml, classpath:application-dev-beta.yml
	profiles:
		include:
			- test-docker
		group:
			test-docker: application-test-docker
			dev: dev
			beta: beta
```

<br>
**test-docker.yml**

```yaml
spring:
	config:
		activate:
			on-profile: test-docker
  redis:
    host: localhost
    port: 6379
amazon:
  dynamodb:
    endpoint: "<http://localhost:5555>"
    region: "local"
  aws:
    accessKey: "key"
    secretKey: "key"
```

<br>

**application-dev-beta.yml**

```yaml
spring:
	config:
		activate:
			on-profile: dev
  redis:
    host: [IP주소]
    port: 6379
amazon:
  dynamodb:
    endpoint: "ip 주소"
    region: "local"
  aws:
    accessKey: "key"
    secretKey: "key"
---
spring:
	config:
		activate:
			on-profile: beta
  redis:
    host: [IP주소]
    port: 6379
amazon:
  dynamodb:
    endpoint: "ip 주소"
    region: "local"
  aws:
    accessKey: "key"
    secretKey: "key"
```

<br>

# 테스트 코드

