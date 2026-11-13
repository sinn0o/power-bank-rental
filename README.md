# 캠퍼스 보조배터리 대여 서비스

Spring Boot 기반 REST API. 회원/보조배터리/대여는 H2(JPA)에, 고장 신고는 MongoDB에 저장한다.

## 기술 스택

- Java 21, Spring Boot 4.1.2, Gradle
- Spring Data JPA + H2 (파일 기반, `./data`) — Member, PowerBank, Rental
- Spring Data MongoDB — DamageReport (`damage_report` 컬렉션)
- Validation, Lombok

## 실행 방법

### 사전 조건

- Java 21 설치
- 로컬 MongoDB가 `localhost:27017`에서 실행 중일 것 (인증 없음). `powerbank` DB와 `damage_report` 컬렉션은 앱이 처음 저장할 때 자동 생성된다.
- H2는 별도 설치가 필요 없고, `./data/powerbank.mv.db` 파일에 저장되어 **앱을 재시작해도 회원/배터리/대여 데이터가 유지**된다. 최초 실행 시 학생 5명 / 보조배터리 10개 / 대여 이력 8건이 예시 데이터로 자동 등록된다 (`DataInitializer`).

### 기동

```powershell
.\gradlew.bat bootRun
```

서버는 `http://localhost:8080` 에서 뜬다.

## 데모 페이지
<img width="1213" height="756" alt="image" src="https://github.com/user-attachments/assets/845be271-83ca-4d31-a108-bbe4df2e1bb4" />

`http://localhost:8080/` 에 접속하면 `src/main/resources/static/index.html` 이 뜬다. 순수 HTML/CSS/JavaScript(`fetch`)로만 작성했고 빌드 도구나 외부 라이브러리 없이 서버만 켜면 바로 동작한다 (같은 origin이라 CORS 설정도 필요 없다).

- 왼쪽: 등록된 학생 / 보조배터리 목록 (검색·상태 필터는 서버 호출 없이 화면에서 바로 거른다). 행을 클릭하면 오른쪽 폼의 ID 입력칸이 자동으로 채워진다.
- 오른쪽: 회원 등록, 보조배터리 등록, 대여, 내 대여 이력(반납 포함), 고장 신고 조회/등록 카드.
- 등록/대여/반납/신고 등 변경이 일어나면 왼쪽 두 목록이 자동으로 새로고침된다.
- API 에러 응답의 `message`는 화면 우상단 토스트로 뜬다.

## API 목록 (9개)

| # | Method | URL | 설명 |
|---|--------|-----|------|
| 1 | POST | `/members` | 회원 등록 |
| 2 | POST | `/power-banks` | 보조배터리 등록 (초기 상태 AVAILABLE) |
| 3 | GET | `/power-banks?status=` | 목록 조회 (status 선택) |
| 4 | POST | `/rentals` | 대여 |
| 5 | PATCH | `/rentals/{id}/return` | 반납 |
| 6 | GET | `/rentals?memberId=` | 회원별 대여 이력 |
| 7 | POST | `/power-banks/{id}/reports` | 고장 신고 등록 (MongoDB) |
| 8 | GET | `/power-banks/{id}/reports` | 배터리별 신고 목록 (MongoDB) |
| 9 | GET | `/members` | 전체 회원 목록 조회 (데모 페이지용으로 추가) |

## 에러 응답 형식

```json
{"status": 409, "code": "ALREADY_RENTED", "message": "이미 대여 중이거나 사용할 수 없는 보조배터리입니다. id=1"}
```

| 상태 | 코드 | 상황 |
|---|---|---|
| 400 | `INVALID_REQUEST` | `@Valid` 검증 실패 |
| 400 | `INVALID_PARAMETER` | 쿼리 파라미터 값이 잘못됨 (예: `status=FOO`) |
| 404 | `MEMBER_NOT_FOUND` | 존재하지 않는 회원 |
| 404 | `POWER_BANK_NOT_FOUND` | 존재하지 않는 보조배터리 |
| 404 | `RENTAL_NOT_FOUND` | 존재하지 않는 대여 내역 |
| 409 | `DUPLICATE_STUDENT_NO` | 학번 중복 등록 |
| 409 | `DUPLICATE_SERIAL_NO` | 시리얼 번호 중복 등록 |
| 409 | `ALREADY_RENTED` | AVAILABLE이 아닌 배터리를 대여 시도 |
| 409 | `DUPLICATE_RENTAL` | 이미 RENTING 중인 회원이 또 대여 시도 |
| 409 | `ALREADY_RETURNED` | 이미 반납된 대여를 다시 반납 시도 |
| 409 | `OPTIMISTIC_LOCK_CONFLICT` | 동시 요청으로 인한 낙관적 락 충돌 |
