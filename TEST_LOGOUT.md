# 로그아웃 기능 테스트 가이드

## 🔍 테스트 방법

### 방법 1: Swagger UI (권장)

1. **애플리케이션 실행 후 브라우저 접속:**
   - URL: http://localhost:8080/swagger-ui.html

2. **전체 플로우 테스트:**

#### 1단계: 회원가입
- **엔드포인트:** `POST /auth/signup`
- **요청 본문:**
```json
{
  "username": "testuser",
  "password": "password123",
  "name": "Test User",
  "email": "test@example.com",
  "termsOfService": true,
  "privacyPolicy": true,
  "locationService": true,
  "ageLimit": true,
  "marketingConsent": false
}
```

#### 2단계: 로그인 (토큰 획득)
- **엔드포인트:** `POST /auth/login`
- **요청 본문:**
```json
{
  "username": "testuser",
  "password": "password123"
}
```
- **응답에서 `refreshToken` 저장!**

#### 3단계: 로그아웃 테스트
- **엔드포인트:** `POST /auth/logout`
- **요청 본문:**
```json
{
  "refreshToken": "위에서 받은 refreshToken 값"
}
```

#### 4단계: 로그아웃된 토큰으로 재사용 시도 (에러 확인)
- **엔드포인트:** `POST /auth/refresh`
- **요청 본문:**
```json
{
  "refreshToken": "같은 refreshToken"
}
```
- **예상 결과:** 401 Unauthorized with code "REUSED_REFRESH_TOKEN" or "INVALID_REFRESH_TOKEN"

---

### 방법 2: curl 명령어로 테스트

#### 터미널/PowerShell에서 실행:

```powershell
# 1. 회원가입
curl -X POST http://localhost:8080/auth/signup `
  -H "Content-Type: application/json" `
  -d '{"username":"testuser","password":"password123","name":"Test User","email":"test@example.com","termsOfService":true,"privacyPolicy":true,"locationService":true,"ageLimit":true,"marketingConsent":false}'

# 2. 로그인 (refreshToken을 변수에 저장)
$response = curl -X POST http://localhost:8080/auth/login `
  -H "Content-Type: application/json" `
  -d '{"username":"testuser","password":"password123"}'

# PowerShell에서 refreshToken 추출하기 (복잡하므로 직접 복사해서 사용)
Write-Host "위 응답에서 refreshToken을 복사하세요"

# 3. 로그아웃 (YOUR_REFRESH_TOKEN을 실제 값으로 교체)
curl -X POST http://localhost:8080/auth/logout `
  -H "Content-Type: application/json" `
  -d '{"refreshToken":"YOUR_REFRESH_TOKEN"}'

# 예상 응답:
# {"data":{"message":"Successfully logged out","loggedOut":true}}

# 4. 로그아웃된 토큰으로 refresh 시도
curl -X POST http://localhost:8080/auth/refresh `
  -H "Content-Type: application/json" `
  -d '{"refreshToken":"YOUR_REFRESH_TOKEN"}'

# 예상 응답:
# {"status":401,"code":"REUSED_REFRESH_TOKEN","message":"REUSED_REFRESH_TOKEN"}
```

---

## ✅ 성공 기준

### 로그아웃이 정상 동작하는 경우:
1. ✅ `/auth/logout` 호출 시 200 OK 응답 받음
2. ✅ 응답에 `"loggedOut": true` 포함
3. ✅ 동일한 refreshToken으로 `/auth/refresh` 호출 시 **401 에러** 발생
4. ✅ 에러 메시지에 `"REUSED_REFRESH_TOKEN"` 또는 `"INVALID_REFRESH_TOKEN"` 포함

### Redis 확인 (선택사항):
Redis가 실행 중인 경우:
```bash
# Redis CLI 실행 후
redis-cli

# 모든 refresh token 확인
KEYS rt:*

# 특정 사용자의 토큰 확인 (userId 확인 필요)
KEYS rt:USER_ID:*
```

---

## 🐛 문제 해결

### 응답이 오지 않는 경우:
- 애플리케이션이 실행 중인지 확인 (`http://localhost:8080/health` 접속)
- MongoDB와 Redis가 실행 중인지 확인

### 401 에러가 발생하지 않는 경우:
- 로그아웃이 Redis에 실제로 반영되지 않았을 수 있음
- Redis 연결 상태 확인
- 다른 refreshToken으로 다시 테스트

### 로그아웃 후에도 refresh가 되는 경우:
- `RefreshService.logout()` 메서드가 Redis에서 토큰을 제거하는지 확인
- Redis 상태 확인 필요

