$ErrorActionPreference = 'Stop'

Write-Host "=== 예약 취소 E2E 테스트 ===" -ForegroundColor Cyan
$BASE = "http://localhost:8080"

function Json($s) { return $s -replace "\r?\n", "" }

# 1) 회원가입
$ts = Get-Date -Format 'yyyyMMddHHmmss'
$uid = "user$ts"
$email = "$uid@example.com"
$signupBody = @"
{
  "id": "$uid",
  "password": "test1234!",
  "name": "Tester",
  "email": "$email",
  "role": "USER",
  "agreements": {
    "termsOfService": true,
    "privacyPolicy": true,
    "LocationService": true,
    "AgeLimit": true,
    "marketingConsent": false
  }
}
"@
$signup = Invoke-RestMethod -Uri "$BASE/auth/signup" -Method POST -ContentType 'application/json' -Body (Json $signupBody)
Write-Host "✓ 회원가입: $uid" -ForegroundColor Green

# 2) 로그인
$loginBody = @"{""id"":""$uid"",""password"":""test1234!""}"@
$login = Invoke-RestMethod -Uri "$BASE/auth/login" -Method POST -ContentType 'application/json' -Body (Json $loginBody)
$TOKEN = $login.data.accessToken
$Headers = @{ Authorization = "Bearer $TOKEN"; 'Content-Type' = 'application/json' }
Write-Host "✓ 로그인" -ForegroundColor Green

# 3) 미래 예약 생성 -> 취소 성공
$futureBody = @"
{
  "hospitalName": "서울대병원",
  "reservationDatetime": "2099-12-20T14:30:00Z",
  "content": "안과",
  "paymentCardId": "101"
}
"@
$r1 = Invoke-RestMethod -Uri "$BASE/reservations" -Method POST -Headers $Headers -Body (Json $futureBody)
$RID_F = $r1.data.reservation_id
Write-Host "✓ 미래 예약 생성: $RID_F" -ForegroundColor Green

$cancelOk = Invoke-RestMethod -Uri "$BASE/reservations/cancel/$RID_F" -Method PATCH -Headers $Headers
Write-Host "✓ 미래 예약 취소 응답: $($cancelOk.data.message)" -ForegroundColor Green

# 4) 과거 예약 생성 -> 취소 409
$pastBody = @"
{
  "hospitalName": "강남세브란스",
  "reservationDatetime": "2000-01-01T10:00:00Z",
  "content": "정형외과",
  "paymentCardId": "102"
}
"@
$r2 = Invoke-RestMethod -Uri "$BASE/reservations" -Method POST -Headers $Headers -Body (Json $pastBody)
$RID_P = $r2.data.reservation_id
Write-Host "✓ 과거 예약 생성: $RID_P" -ForegroundColor Green

try {
  Invoke-RestMethod -Uri "$BASE/reservations/cancel/$RID_P" -Method PATCH -Headers $Headers | Out-Null
  Write-Host "✗ 과거 예약이 취소 성공으로 나왔습니다 (오류)" -ForegroundColor Red
} catch {
  $resp = $_.ErrorDetails.Message
  Write-Host "✓ 과거 예약 취소 409 확인: $resp" -ForegroundColor Green
}

Write-Host "=== 테스트 완료 ===" -ForegroundColor Cyan

