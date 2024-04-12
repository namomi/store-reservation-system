# 💻 매장 테이블 예약 서비스 구현
회원 가입 및 매장 예약과 점주분이 예약을 승인/ 거절을 할 수 있으며 리뷰를 달 수 있는 API입니다.

#개발기간
- `2024년 3월 19일 ~ 2024년 4월 6일(19일)`

# ⚙ 개발 환경
- Project : `Gradle - Groovy`
- Language : `Java`
- Spring Boot : `3.2.3`
- Packaging : `Jar`
- Java : `17`
- Dependencies : `Lombok`, `Spring Web`, `JPA`, `Spring Security`, `Spring Validation`, `jjwt`
- IDEA : `IntelliJ`
- DB : `h2`, `MySQL`

# ⚙ 기술 스택
- Spring Boot
- Java
- JPA
- h2
- MySql
- jjwt
- logback
- swagger


## 과제
- [x] 매장의 점장은 예약 서비스 앱에 상점을 등록한다.
- [x] 매장을 등록하기 위해서는 파트너 회원 가입이 되어야 한다(승인조건 X).
- [x] 매장 이용자는 앱을 통해서 매장 검색, 상세정보 확인한다.
- [x] 매장의 상세정보를 보고, 예약을 진행한다.
- [x] 서비스를 통해서 예약한 이후에, 예약 10분 전에 도착하여 키오스크를 통해서 방문 확인 진행
- [x] 예약 및 사용 이후에 리뷰 작성
- [x] 리뷰 경우, 수정은 리뷰 작성자만, 삭제는 리뷰 작성자와 매장 관리자만 삭제 가능
- [x] 예약이 들어오면 점장이 확인하여 승인/ 거절 기능


# 🔎 주요 기능
- `POST /user/register/partner` : 점주 회원 등록
- `POST /user/register/customer` : 일반 회원 등록
- `POST /store/register/{email}` : 매장 등록
- `GET /store` : 매장 정보 조회
- `GET /store/search` : 매장명으로 매장 정보 조회
- `GET /store/{id}` : 해당 매장 상세정보 조회
- `POST /reservation` : 예약 등록
- `PATCH /reservation/{reservationId}/confirmArrival` : 매장 방문
- `PATCH /reservation/{reservationId}/approve` : 예약 승인
- `PATCH /reservation/{reservationId}/reject` : 예약 취소
- `POST /reviews` : 리뷰 등록
- `PATCH /reviews/{reviewId}` : 리뷰 수정
- `DELETE /reviews/{reviewId}` : 리뷰 삭제


# 🔎 추가 기능
- Json 으로 로그인/ 로그아웃 구현
- 로그인시 jwt 토근 발행하여 회원 관리
- Spring Security 설정
- ExceptionHandler를 이용한 예외처리
- Swagger를 이용한 API documentation
