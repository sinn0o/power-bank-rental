# 캠퍼스 보조배터리 대여 서비스

Spring Boot 기반 REST API. 회원/보조배터리/대여는 H2(JPA)에, 고장 신고는 MongoDB에 저장한다.

## 기술 스택

- Java 21, Spring Boot 4.1.2, Gradle
- Spring Data JPA + H2 (인메모리) — Member, PowerBank, Rental
- Spring Data MongoDB — DamageReport (`damage_report` 컬렉션)
- Validation, Lombok

## 실행 방법

### 사전 조건

- Java 21 설치
- 로컬 MongoDB가 `localhost:27017`에서 실행 중일 것 (인증 없음). `powerbank` DB와 `damage_report` 컬렉션은 앱이 처음 저장할 때 자동 생성된다.
- H2는 인메모리 DB라 별도 설치가 필요 없다. 단, **앱을 재시작하면 회원/배터리/대여 데이터는 모두 초기화**된다 (MongoDB 데이터는 유지됨).

### 기동

```powershell
.\gradlew.bat bootRun
```

서버는 `http://localhost:8080` 에서 뜬다.

## 데모 페이지

`http://localhost:8080/` 에 접속하면 `src/main/resources/static/index.html` 한 파일로 만든 데모 페이지가 뜬다. 순수 HTML/CSS/JavaScript(`fetch`)로만 작성했고 빌드 도구나 외부 라이브러리가 없어서, 서버만 켜면 바로 동작한다 (같은 origin이라 CORS 설정도 필요 없다).

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

## 테스트 시나리오

> **주의**: Windows PowerShell 5.1에서 `curl.exe -d '{"a":"b"}'`처럼 JSON을 인라인으로 넘기면, PowerShell이 네이티브 exe로 인자를 넘기는 과정에서 따옴표가 깨져 매번 `400 Bad Request`(JSON 파싱 실패)가 난다 — 실제로 겪은 문제다. 가장 확실한 우회법은 **JSON을 임시 파일에 쓰고 `--data-binary "@파일"`로 보내는 것**이다. 아래 `Invoke-Json` 헬퍼 함수가 이 처리를 대신해준다. (Git Bash라면 이 문제 자체가 없어서 `curl -d '{"a":"b"}'`를 그대로 써도 된다.)

PowerShell에서 먼저 헬퍼 함수를 선언한다:

```powershell
function Invoke-Json($method, $url, $json) {
    if ($json) {
        $tmp = New-TemporaryFile
        Set-Content -Path $tmp -Value $json -Encoding utf8 -NoNewline
        curl.exe -s -w "`nHTTP:%{http_code}`n" -X $method $url -H "Content-Type: application/json" --data-binary "@$tmp"
        Remove-Item $tmp -ErrorAction SilentlyContinue
    } else {
        curl.exe -s -w "`nHTTP:%{http_code}`n" -X $method $url
    }
}
```

### 정상 흐름

```powershell
# 1. 회원 등록
Invoke-Json POST http://localhost:8080/members '{"name":"Hong","studentNo":"20231234"}'

# 2. 보조배터리 등록
Invoke-Json POST http://localhost:8080/power-banks '{"serialNo":"PB-001","capacityMah":10000,"location":"Library"}'

# 3. 목록 조회 (전체 / AVAILABLE 필터)
Invoke-Json GET http://localhost:8080/power-banks
Invoke-Json GET "http://localhost:8080/power-banks?status=AVAILABLE"

# 4. 대여 (memberId=1, powerBankId=1 가정)
Invoke-Json POST http://localhost:8080/rentals '{"memberId":1,"powerBankId":1}'

# 6. 회원별 대여 이력 조회
Invoke-Json GET "http://localhost:8080/rentals?memberId=1"

# 5. 반납 (rentalId=1 가정)
Invoke-Json PATCH http://localhost:8080/rentals/1/return

# 7. 고장 신고 등록 (powerBankId=1 가정)
Invoke-Json POST http://localhost:8080/power-banks/1/reports '{"memberId":1,"type":"NOT_CHARGING","description":"Does not charge"}'

# 8. 배터리별 신고 목록 조회
Invoke-Json GET http://localhost:8080/power-banks/1/reports
```

### 에러 케이스

> 아래는 "정상 흐름"을 먼저 실행한 뒤 이어서 돌리는 것을 전제로 한다 (memberId=1 "Hong", powerBankId=1 "PB-001", rentalId=1이 이미 RETURNED 상태).

```powershell
# 학번 중복 등록 -> 409 DUPLICATE_STUDENT_NO
Invoke-Json POST http://localhost:8080/members '{"name":"Hong2","studentNo":"20231234"}'

# 시리얼 번호 중복 등록 -> 409 DUPLICATE_SERIAL_NO
Invoke-Json POST http://localhost:8080/power-banks '{"serialNo":"PB-001","capacityMah":5000,"location":"Dorm"}'

# 회원2 / 배터리2 등록
Invoke-Json POST http://localhost:8080/members '{"name":"Kim","studentNo":"20245678"}'
Invoke-Json POST http://localhost:8080/power-banks '{"serialNo":"PB-002","capacityMah":4000,"location":"Dorm2"}'

# 회원1이 배터리2를 대여 (201) -> 배터리2는 RENTED 상태가 됨
Invoke-Json POST http://localhost:8080/rentals '{"memberId":1,"powerBankId":2}'

# 회원2가 이미 RENTED인 배터리2를 대여 시도 -> 409 ALREADY_RENTED
Invoke-Json POST http://localhost:8080/rentals '{"memberId":2,"powerBankId":2}'

# 회원1은 이미 RENTING 중(배터리2)인데 배터리1도 대여 시도 -> 409 DUPLICATE_RENTAL
Invoke-Json POST http://localhost:8080/rentals '{"memberId":1,"powerBankId":1}'

# 이미 반납된 rentalId=1을 다시 반납 -> 409 ALREADY_RETURNED
Invoke-Json PATCH http://localhost:8080/rentals/1/return

# 존재하지 않는 리소스 -> 404
Invoke-Json PATCH http://localhost:8080/rentals/999/return
Invoke-Json GET "http://localhost:8080/rentals?memberId=999"
Invoke-Json POST http://localhost:8080/power-banks/999/reports '{"memberId":1,"type":"BROKEN","description":"test"}'

# 잘못된 쿼리 파라미터 -> 400 INVALID_PARAMETER
Invoke-Json GET "http://localhost:8080/power-banks?status=FOO"

# 검증 실패 -> 400 INVALID_REQUEST
Invoke-Json POST http://localhost:8080/members '{"name":"","studentNo":""}'
```

위 두 시나리오는 `Invoke-Json` 헬퍼로 실제 PowerShell에서 이 순서 그대로 끝까지 실행해, 각 호출의 상태 코드와 응답 바디가 기대한 에러 코드와 정확히 일치하는 것을 확인했다 (정상 흐름 1~8번 + 에러 케이스 전부, 총 2회 반복 검증).
