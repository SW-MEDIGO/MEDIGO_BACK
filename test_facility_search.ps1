# 시설 검색 API 테스트 스크립트

Write-Host "=== 시설 검색 API 테스트 ===" -ForegroundColor Cyan
Write-Host ""

$baseUrl = "http://localhost:8080"

try {
    # 서울 중심 좌표
    $latitude = 37.5665
    $longitude = 127.0308
    
    Write-Host "1. 서울 중심에서 전체 시설 검색 (반경 5km)..." -ForegroundColor Yellow
    $params = @{
        latitude = $latitude
        longitude = $longitude
        radius = 5000
        page = 0
        size = 10
    }
    
    $response = Invoke-RestMethod -Uri "$baseUrl/facilities/search" -Method Get -Body $params
    Write-Host "✓ 검색 성공" -ForegroundColor Green
    Write-Host "  총 시설 수: $($response.data.totalElements)" -ForegroundColor Gray
    Write-Host "  현재 페이지: $($response.data.content.Count)개" -ForegroundColor Gray
    
    if ($response.data.content.Count -gt 0) {
        Write-Host "`n  첫 번째 시설:" -ForegroundColor Cyan
        $facility = $response.data.content[0]
        Write-Host "    - 이름: $($facility.name)" -ForegroundColor Gray
        Write-Host "    - 유형: $($facility.type)" -ForegroundColor Gray
        Write-Host "    - 주소: $($facility.address)" -ForegroundColor Gray
    }
    
    # 병원만 검색
    Write-Host "`n2. 병원만 검색..." -ForegroundColor Yellow
    $params['type'] = 'HOSPITAL'
    $response = Invoke-RestMethod -Uri "$baseUrl/facilities/search" -Method Get -Body $params
    Write-Host "✓ 병원 검색 성공: $($response.data.totalElements)개" -ForegroundColor Green
    
    # 약국만 검색
    Write-Host "`n3. 약국만 검색..." -ForegroundColor Yellow
    $params['type'] = 'PHARMACY'
    $response = Invoke-RestMethod -Uri "$baseUrl/facilities/search" -Method Get -Body $params
    Write-Host "✓ 약국 검색 성공: $($response.data.totalElements)개" -ForegroundColor Green
    
    # 운영중인 시설만 검색
    Write-Host "`n4. 운영중인 시설 검색..." -ForegroundColor Yellow
    Remove-Variable params -ErrorAction SilentlyContinue
    $params = @{
        latitude = $latitude
        longitude = $longitude
        radius = 5000
        isOperating = $true
        page = 0
        size = 10
    }
    $response = Invoke-RestMethod -Uri "$baseUrl/facilities/search" -Method Get -Body $params
    Write-Host "✓ 운영중인 시설: $($response.data.totalElements)개" -ForegroundColor Green
    
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

