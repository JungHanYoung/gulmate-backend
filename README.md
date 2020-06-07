# gulmate-backend


# 귤메이트 앱 백엔드

귤메이트 앱의 백엔드 코드입니다. [귤메이트 앱 저장소](https://github.com/JungHanYoung/gulmate-flutter)

해당 서버에는...

- 회원
  - [x] 구글 로그인
  - [x] 페이스북 로그인
  - [x] 가족 구성 만들기
  - [x] 한 가족에서의 회원의 이름 바꾸기
- 가족
  - [x] 회원을 가족에 초대하기
  - [x] 가족 배경사진 바꾸기
- 일정
  - [x] 일정 생성
  - [x] 일정 수정
  - [x] 해당하는 월의 일정 조회
  - [x] 일정 삭제
- 장보기
  - [x] 아이템 생성
  - [x] 아이템 수정
  - [x] 장보기 완료 체크
  - [x] 아이템 삭제
  - [ ] 장보기 추천 기능
- 채팅
  - [x] 실시간 채팅

## Developing

### Built With

Spring Boot 2.2.4, Postgresql >= 10, Spring Data JPA, Spring Security, Spring Websocket, JWT, AWS SDK(S3)

### Prerequisites

1. 실행을 위한 Java 1.8, 빌드를 위한 Gradle 6.0.1 등의 각 버전이 필요합니다.
2. Postgresql(version >= 10)이 로컬에 실행되고 있어야 합니다.
3. src/main/resources 경로에 application-local-jwt.properties, application-local-db.properties에 다음과 같은 속성을 작성해야합니다.

```
# src/main/resources/application-local-jwt.properties
jwt.token.issuer=gulmate-developer
jwt.token.clientId=clientId1
jwt.token.clientSecret=clientSecret1
jwt.token.expirySeconds=360000

# src/main/resources/application-local-db.properties
spring.jpa.show_sql=true
spring.jpa.hibernate.ddl-auto=create

spring.datasource.url=jdbc:postgresql://localhost:5432/gulmate
spring.datasource.username=postgres
spring.datasource.password=secret
```

### 개발환경 세팅

```shell
git clone https://github.com/JungHanYoung/gulmate-backend.git
cd gulmate-backend
idea .
```

### 서버 실행

1. application-local-jwt.properties, application.local-db.properties를 다음과 같은 속성과 함께 작성

```
// appplication-local-jwt.properties
jwt.token.issuer=gulmate-developer
jwt.token.clientId=clientId1
jwt.token.clientSecret=clientSecret1
jwt.token.expirySeconds=360000

// application-local-db.properties
spring.jpa.show_sql=true
spring.jpa.hibernate.ddl-auto=update

spring.datasource.url=jdbc:postgresql://localhost:5432/gulmate
spring.datasource.username=postgres
spring.datasource.password=secret
```

2. 실행

```shell
./gradlew bootRun
```

로컬에 서버가 돌아가게 되고, 플러터 앱과 연동하여 사용할 수 있게 됩니다.


## Database

Explaining what database (and version) has been used. Provide download links.
Documents your database design and schemas, relations etc... 

## Licensing

State what the license is and how to find the text version of the license.
