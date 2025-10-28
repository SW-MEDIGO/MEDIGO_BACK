# 예약 API 테스트 가이드

## 🚀 빠른 시작

### 1. 애플리케이션 실행

```powershell
# 터미널에서 실행 (새 터미널)
.\gradlew.bat bootRun

# 또는 IDE에서 OswcApplication.java 실행
```

서버가 시작되면: **http://localhost:8080**

---

## 📋 테스트 순서

예약 API는 **인증이 필요**합니다. 먼저 회원가입/로그인을 해야 합니다.

### Step 1: 회원가입

```powershell
$body = @{
    id = "testuser"
    password = "test1234!"
    name = "테스트 사용자"
    email = "test@example.com"
    role = "USER"
    agreements = @{
        termsOfService = $true
        privacyPolicy = $true
        LocationService = $true
        AgeLimit = $true
        marketingConsent = $false
    }
} | ConvertTo-Json

Invoke-RestMethod -Uri "http://localhost:8080/auth/signup" `
    -Method POST `
    -ContentType "application/json" `
    -Body $body
```

**성공 응답:**
```json
{
  "status": "success",
  "data": {
    "message": "회원가입이 성공적으로 완료되었습니다."
  }
}
```

### Step 2: 로그인 (토큰 발급)

```powershell
$body = @{
    id = "testuser"
    password = "test1234!"
} | ConvertTo-Json

$response = Invoke-RestMethod -Uri "http://localhost:8080/auth/login" `
    -Method POST `
    -ContentType "application/json" `
    -Body $body

# 토큰 저장
$TOKEN = $response.data.accessToken
$REFRESH_TOKEN = $response.data.refreshToken

Write-Host "Access Token: $TOKEN"
Write-Host "Refresh Token: $REFRESH_TOKEN"
```

**성공 응답:**
```json
{
  "status": "success",
  "data": {
    "accessToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
    "refreshToken": "deFfGgHhIiJjKkLlMmNnOoPpQqRrSsTt...",
    "user": {
      "id": "65...",
      "name": "테스트 사용자",
      "role": "USER"
    }
  }
}
```

---

## 🧪 예약 API 테스트

### 1. 예약 생성 (POST /reservations)

```powershell
# Step 2에서 받은 토큰 사용
$headers = @{
    "Authorization" = "Bearer $TOKEN"
    "Content-Type" = "application/json"
}

$body = @{
    hospitalName = "서울대학교병원"
    reservationDatetime = "2025-12-20T14:30:00Z"
    content = "안과 정기 검진 동행 부탁드립니다."
    paymentCardId = "card123"
    managerGender = "FEMALE"
} | ConvertTo-Json

$reservation = Invoke-RestMethod -Uri "http://localhost:8080/reservations" `
    -Method POST `
    -Headers $headers `
    -Body $body

Write-Host "예약 ID: $($reservation.data.reservation_id)"
```

**성공 응답:**
```json
{
  "status": "success",
  "data": {
    "reservation_id": "67890abc...",
    "message": "예약이 성공적으로 접수되었습니다."
  }
}
```

### 2. 예약 목록 조회 (GET /reservations)

```powershell
# 전체 예약 조회
$reservations = Invoke-RestMethod -Uri "http://localhost:8080/reservations" `
    -Method GET `
    -Headers $headers

$reservations | ConvertTo-Json -Depth 10

# 페이징 조회
$url = "http://localhost:8080/reservations?page=1&limit=10&status=ALL"
Invoke-RestMethod -Uri $url -Headers $headers | ConvertTo-Json
```

**성공 응답:**
```json
{
  "status": "success",
  "data": {
    "pagination": {
      "currentPage": 1,
      "totalPages": 1,
      "totalItems": 1
    },
    "reservations": [
      {
        "id": "67890abc...",
        "hospitalName": "서울대학교병원",
        "reservationDatetime": "2025-12-20T14:30:00Z",
        "status": "PENDING",
        "managerName": null,
        "managerGender": "FEMALE"
      }
    ]
  }
}
```

### 3. 예약 상세 조회 (GET /reservations/{id})

```powershell
# 위에서 받은 예약 ID 사용
$RESERVATION_ID = $reservation.data.reservation_id

$detail = Invoke-RestMethod -Uri "http://localhost:8080/reservations/$RESERVATION_ID" `
    -Method GET `
    -Headers $headers

$detail | ConvertTo-Json -Depth 10
```

**성공 응답:**
```json
{
  "status": "success",
  "data": {
    "id": "67890abc...",
    "hospitalName": "서울대학교병원",
    "reservationDatetime": "2025-12-20T14:30:00Z",
    "content": "안과 정기 검진 동행 부탁드립니다.",
    "status": "PENDING",
    "requestedManagerPreferences": {
      "gender": "FEMALE"
    },
    "paymentInfo": {
      "card_brand": "KB국민카드",
      "card_last_four": "1234"
    },
    "user": {
      "id": "65...",
      "name": "사용자명"
    },
    "manager": null
  }
}
```

### 4. 예약 취소 (PATCH /reservations/cancel/{id})

```powershell
$cancel = Invoke-RestMethod -Uri "http://localhost:8080/reservations/cancel/$RESERVATION_ID" `
    -Method PATCH `
    -Headers $headers

$cancel | ConvertTo-Json
```

**성공 응답:**
```json
{
  "status": "success",
  "data": {
    "message": "예약이 정상적으로 취소되었습니다."
  }
}
```

---

## 🔧 Swagger UI로 테스트

1. 브라우저에서 **http://localhost:8080/swagger-ui.html** 접속
2. 스크롤하여 `reservation-controller` 찾기
3. `/reservations` API들을 클릭하여 테스트

**참고:** Swagger UI에서 인증 토큰을 설정하려면:
- 상단의 🔓 "Authorize" 버튼 클릭
- Value에 `Bearer {token}` 입력

---

## 📊 전체 테스트 시나리오

```powershell
# 1. 회원가입
# 위의 Step 1 실행

# 2. 로그인
$body = @{id="testuser"; password="test1234!"} | ConvertTo-Json
$response = Invoke-RestMethod -Uri "http://localhost:8080/auth/login" `
    -Method POST -ContentType "application/json" -Body $body
$TOKEN = $response.data.accessToken
$headers = @{"Authorization"="Bearer $TOKEN"; "Content-Type"="application/json"}

# 3. 예약 생성
$body = @{
    hospitalName = "서울대학교병원"
    reservationDatetime = "2025-12-20T14:30:00Z"
    content = "안과 정기 검진"
    paymentCardId = "card123"
} | ConvertTo-Json
$reservation = Invoke-RestMethod -Uri "http://localhost:8080/reservations" `
    -Method POST -Headers $headers -Body $body
$RESERVATION_ID = $reservation.data.reservation_id

# 4. 예약 목록 조회
Invoke-RestMethod -Uri "http://localhost:8080/reservations" `
    -Method GET -Headers $headers

# 5. 예약 상세 조회
Invoke-RestMethod -Uri "http://localhost:8080/reservations/$RESERVATION_ID" `
    -Method GET -Headers $headers

# 6. 예약 취소
Invoke-RestMethod -Uri "http://localhost:8080/reservations/cancel/$RESERVATION_ID" `
    -Method PATCH -Headers $headers
```

---

## ✅ 성공 기준

1. ✅ 애플리케이션 실행 성공
2. ✅ 회원가입 성공 (201 Created)
3. ✅ 로그인 성공 (토큰 발급)
4. ✅ 예약 생성 성공 (201 Created)
5. ✅ 예약 목록 조회 성공 (200 OK)
6. ✅ 예약 상세 조회 성공 (200 OK)
7. ✅ 예약 취소 성공 (200 OK)

---

## 🐛 문제 해결

### "401 Unauthorized" 에러
- 토큰이 올바른지 확인
- 헤더에 `Bearer ` 접두사 포함 확인

### "403 Forbidden" 에러
- 본인의 예약만 조회/취소 가능
- 다른 사용자의 예약 ID로 접근 시 발생

### "404 Not Found" 에러
- 예약 ID가 올바른지 확인
- 이미 취소된 예약일 수 있음

---

## 📁 구현된 파일

- `ReservationDocument.java` - 예약 엔티티
- `ReservationStatus.java` - 예약 상태 enum
- `ManagerGender.java` - 매니저 성별 enum
- `ReservationRepository.java` - MongoDB 저장소
- `CreateReservationRequest.java` - 요청 DTO
- `ReservationResponse.java` - 목록 응답 DTO
- `ReservationDetailResponse.java` - 상세 응답 DTO
- `ReservationService.java` - 비즈니스 로직
- `ReservationController.java` - REST API
- `GlobalExceptionHandler.java` - 예외 처리 (업데이트됨)

