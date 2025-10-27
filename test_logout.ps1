# 로그아웃 기능 테스트 스크립트 (PowerShell)

Write-Host "=== 로그아웃 기능 테스트 ===" -ForegroundColor Cyan
Write-Host ""

# 기본 URL
$baseUrl = "http://localhost:8080"

try {
    # 1. 회원가입
    Write-Host "1. 회원가입 중..." -ForegroundColor Yellow
    $signupBody = @{
        username = "testuser_$(Get-Random -Maximum 9999)"
        password = "password123"
        name = "Test User"
        email = "test$(Get-Random -Maximum 9999)@example.com"
        termsOfService = $true
        privacyPolicy = $true
        locationService = $true
        ageLimit = $true
        marketingConsent = $false
    } | ConvertTo-Json
    
    $signupResponse = Invoke-RestMethod -Uri "$baseUrl/auth/signup" -Method Post -Body $signupBody -ContentType "application/json"
    Write-Host "✓ 회원가입 성공 (ID: $($signupResponse.data.id))" -ForegroundColor Green
    $username = $signupBody | ConvertFrom-Json | Select-Object -ExpandProperty username
    
    # 2. 로그인
    Write-Host "`n2. 로그인 중..." -ForegroundColor Yellow
    $loginBody = @{
        username = $username
        password = "password123"
    } | ConvertTo-Json
    
    $loginResponse = Invoke-RestMethod -Uri "$baseUrl/auth/login" -Method Post -Body $loginBody -ContentType "application/json"
    $refreshToken = $loginResponse.data.refreshToken
    Write-Host "✓ 로그인 성공 (refreshToken 획득)" -ForegroundColor Green
    Write-Host "  AccessToken: $($loginResponse.data.accessToken.Substring(0, 20))..." -ForegroundColor Gray
    Write-Host "  RefreshToken: $($refreshToken.Substring(0, 20))..." -ForegroundColor Gray
    
    # 3. 로그아웃
    Write-Host "`n3. 로그아웃 중..." -ForegroundColor Yellow
    $logoutBody = @{
        refreshToken = $refreshToken
    } | ConvertTo-Json
    
    $logoutResponse = Invoke-RestMethod -Uri "$baseUrl/auth/logout" -Method Post -Body $logoutBody -ContentType "application/json"
    Write-Host "✓ 로그아웃 성공" -ForegroundColor Green
    Write-Host "  응답: $($logoutResponse.data | ConvertTo-Json -Compress)" -ForegroundColor Gray
    
    # 4. 로그아웃된 토큰으로 refresh 시도 (에러 확인)
    Write-Host "`n4. 로그아웃된 토큰으로 refresh 시도 (에러 확인)..." -ForegroundColor Yellow
    $refreshBody = @{
        refreshToken = $refreshToken
    } | ConvertTo-Json
    
    try {
        $refreshResponse = Invoke-RestMethod -Uri "$baseUrl/auth/refresh" -Method Post -Body $refreshBody -ContentType "application/json"
        Write-Host "✗ FAIL: 로그아웃된 토큰이 여전히 유효함!" -ForegroundColor Red
    }
    catch {
        $statusCode = $_.Exception.Response.StatusCode.value__
        $errorBody = $_.ErrorDetails.Message | ConvertFrom-Json
        Write-Host "✓ SUCCESS: 로그아웃된 토큰으로 refresh 시도 시 에러 발생 (예상된 동작)" -ForegroundColor Green
        Write-Host "  상태 코드: $statusCode" -ForegroundColor Gray
        Write-Host "  에러 코드: $($errorBody.code)" -ForegroundColor Gray
        Write-Host "  메시지: $($errorBody.message)" -ForegroundColor Gray
    }
    
    Write-Host "`n=== 모든 테스트 통과 ===" -ForegroundColor Green
    
} catch {
    Write-Host "`n✗ 테스트 실패: $($_.Exception.Message)" -ForegroundColor Red
    if ($_.Exception.Response) {
        $statusCode = $_.Exception.Response.StatusCode.value__
        $errorBody = $_.ErrorDetails.Message
        Write-Host "  상태 코드: $statusCode" -ForegroundColor Red
        Write-Host "  응답: $errorBody" -ForegroundColor Red
    }
}

