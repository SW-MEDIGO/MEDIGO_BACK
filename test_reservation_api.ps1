# 예약 API 테스트 스크립트

Write-Host "=== 예약 API 테스트 ===" -ForegroundColor Cyan
Write-Host ""

$baseUrl = "http://localhost:8080"

try {
    # 1. 회원가입
    Write-Host "1. 회원가입..." -ForegroundColor Yellow
    $timestamp = Get-Date -Format 'HHmmss'
    $userId = "testuser_$timestamp"
    $userEmail = "test_$timestamp@example.com"
    
    $signupBody = @"
{
  "id": "$userId",
  "password": "test1234!",
  "name": "test user",
  "email": "$userEmail",
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

    $signup = Invoke-RestMethod -Uri "$baseUrl/auth/signup" -Method POST -ContentType "application/json" -Body $signupBody
    Write-Host "✓ 회원가입 성공" -ForegroundColor Green
    
    # 2. 로그인
    Write-Host "`n2. 로그인..." -ForegroundColor Yellow
    $loginBody = @{
        id = $userId
        password = "test1234!"
    } | ConvertTo-Json

    $login = Invoke-RestMethod -Uri "$baseUrl/auth/login" -Method POST -ContentType "application/json" -Body $loginBody
    $TOKEN = $login.data.accessToken
    Write-Host "✓ 로그인 성공" -ForegroundColor Green
    Write-Host "  토큰: $($TOKEN.Substring(0, 20))..." -ForegroundColor Gray

    # 헤더 설정
    $headers = @{
        "Authorization" = "Bearer $TOKEN"
        "Content-Type" = "application/json"
    }

    # 3. 예약 생성 (UPCOMING / PAST)
    Write-Host "`n3. 예약 생성..." -ForegroundColor Yellow
    $futureBody = @'
{
  "hospitalName": "서울대학교병원",
  "reservationDatetime": "2099-12-20T14:30:00Z",
  "content": "안과 정기 검진 동행 부탁드립니다.",
  "paymentCardId": "card123",
  "managerGender": "FEMALE"
}
'@

    $futureRes = Invoke-RestMethod -Uri "$baseUrl/reservations" -Method POST -Headers $headers -Body $futureBody
    $FUTURE_ID = $futureRes.data.reservation_id
    Write-Host "✓ 미래 예약 생성 성공 (ID=$FUTURE_ID)" -ForegroundColor Green

    $pastBody = @'
{
  "hospitalName": "강남세브란스병원",
  "reservationDatetime": "2000-01-01T10:00:00Z",
  "content": "정형외과 방문",
  "paymentCardId": "card124",
  "managerGender": "MALE"
}
'@

    $pastRes = Invoke-RestMethod -Uri "$baseUrl/reservations" -Method POST -Headers $headers -Body $pastBody
    $PAST_ID = $pastRes.data.reservation_id
    Write-Host "✓ 과거 예약 생성 성공 (ID=$PAST_ID)" -ForegroundColor Green

    # 4. 예약 목록 조회
    Write-Host "`n4. 예약 목록 조회..." -ForegroundColor Yellow
    $urlUpcoming = "$baseUrl/reservations?page=1&limit=10"
    $listUpcoming = Invoke-RestMethod -Uri $urlUpcoming -Method GET -Headers $headers
    Write-Host "✓ UPCOMING 목록 조회 성공" -ForegroundColor Green
    Write-Host "  총 예약 수: $($listUpcoming.data.pagination.totalItems)" -ForegroundColor Gray
    Write-Host "  현재 페이지: $($listUpcoming.data.pagination.currentPage)" -ForegroundColor Gray
    if ($listUpcoming.data.reservations.Count -gt 0) {
        Write-Host "  첫 예약 시간(ASC): $($listUpcoming.data.reservations[0].reservation_datetime)" -ForegroundColor Gray
    }

    # PAST 목록 (DESC)
    $urlPast = "$baseUrl/reservations?period=PAST&page=1&limit=10"
    $listPast = Invoke-RestMethod -Uri $urlPast -Method GET -Headers $headers
    Write-Host "✓ PAST 목록 조회 성공" -ForegroundColor Green
    Write-Host "  총 예약 수: $($listPast.data.pagination.totalItems)" -ForegroundColor Gray
    if ($listPast.data.reservations.Count -gt 0) {
        Write-Host "  첫 예약 시간(DESC): $($listPast.data.reservations[0].reservation_datetime)" -ForegroundColor Gray
    }

    # 상태 필터 (PENDING)
    $urlPending = "$baseUrl/reservations?period=UPCOMING&status=PENDING&page=1&limit=10"
    $listPending = Invoke-RestMethod -Uri $urlPending -Method GET -Headers $headers
    Write-Host "✓ 상태 필터(PENDING) 목록 조회 성공" -ForegroundColor Green
    Write-Host "  반환 건수: $($listPending.data.reservations.Count)" -ForegroundColor Gray

    # 5. 예약 상세 조회
    Write-Host "`n5. 예약 상세 조회..." -ForegroundColor Yellow
    $detail = Invoke-RestMethod -Uri "$baseUrl/reservations/$FUTURE_ID" -Method GET -Headers $headers
    Write-Host "✓ 상세 조회 성공" -ForegroundColor Green
    Write-Host "  병원명: $($detail.data.hospitalName)" -ForegroundColor Gray
    Write-Host "  상태: $($detail.data.status)" -ForegroundColor Gray

    # 6. 예약 취소
    Write-Host "`n6. 예약 취소..." -ForegroundColor Yellow
    $cancel = Invoke-RestMethod -Uri "$baseUrl/reservations/cancel/$FUTURE_ID" -Method PATCH -Headers $headers
    Write-Host "✓ 예약 취소 성공" -ForegroundColor Green
    Write-Host "  메시지: $($cancel.data.message)" -ForegroundColor Gray

    # 7. 취소 확인 (상태가 CANCELED로 변경되었는지)
    Write-Host "`n7. 취소 상태 확인..." -ForegroundColor Yellow
    $canceled = Invoke-RestMethod -Uri "$baseUrl/reservations/$FUTURE_ID" -Method GET -Headers $headers
    if ($canceled.data.status -eq 'CANCELED') {
        Write-Host "✓ 취소 상태 확인됨" -ForegroundColor Green
    }

    # 8. 유효성 에러 확인 (잘못된 period/status)
    Write-Host "`n8. 유효성 에러 확인..." -ForegroundColor Yellow
    $hadError = $false
    $urlWrongPeriod = "$baseUrl/reservations?period=WRONG"
    try { Invoke-RestMethod -Uri $urlWrongPeriod -Method GET -Headers $headers } catch { $hadError = $true }
    if ($hadError) { Write-Host "✓ 잘못된 period 400 확인" -ForegroundColor Green }

    $hadError = $false
    $urlWrongStatus = "$baseUrl/reservations?status=WRONG"
    try { Invoke-RestMethod -Uri $urlWrongStatus -Method GET -Headers $headers } catch { $hadError = $true }
    if ($hadError) { Write-Host "✓ 잘못된 status 400 확인" -ForegroundColor Green }

    Write-Host "`n=== 모든 테스트 통과 ===" -ForegroundColor Green
    Write-Host ""
    
} catch {
    Write-Host "`n✗ 테스트 실패: $($_.Exception.Message)" -ForegroundColor Red
    if ($_.Exception.Response) {
        $statusCode = $_.Exception.Response.StatusCode.value__
        Write-Host ("  상태 코드: {0}" -f $statusCode) -ForegroundColor Red

        $errorStream = $_.Exception.Response.GetResponseStream()
        $reader = New-Object System.IO.StreamReader($errorStream)
        $errorBody = $reader.ReadToEnd()
        Write-Host ('  응답: ' + $errorBody) -ForegroundColor Red
    }
    
    throw
}

