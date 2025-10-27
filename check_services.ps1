# MongoDB 및 Redis 연결 상태 확인

Write-Host "=== 서비스 연결 상태 확인 ===" -ForegroundColor Cyan

# MongoDB 확인
Write-Host "`nMongoDB (포트 27017):" -ForegroundColor Yellow
try {
    $mongo = Test-NetConnection -ComputerName localhost -Port 27017 -WarningAction SilentlyContinue
    if ($mongo.TcpTestSucceeded) {
        Write-Host "✓ MongoDB 연결 가능" -ForegroundColor Green
    } else {
        Write-Host "✗ MongoDB 연결 실패 - MongoDB를 시작하세요" -ForegroundColor Red
    }
} catch {
    Write-Host "✗ MongoDB 연결 실패 - MongoDB를 시작하세요" -ForegroundColor Red
}

# Redis 확인
Write-Host "`nRedis (포트 6379):" -ForegroundColor Yellow
try {
    $redis = Test-NetConnection -ComputerName localhost -Port 6379 -WarningAction SilentlyContinue
    if ($redis.TcpTestSucceeded) {
        Write-Host "✓ Redis 연결 가능" -ForegroundColor Green
    } else {
        Write-Host "✗ Redis 연결 실패 - Redis를 시작하세요" -ForegroundColor Red
    }
} catch {
    Write-Host "✗ Redis 연결 실패 - Redis를 시작하세요" -ForegroundColor Red
}

# 애플리케이션 확인
Write-Host "`n애플리케이션 (포트 8080):" -ForegroundColor Yellow
try {
    $app = Test-NetConnection -ComputerName localhost -Port 8080 -WarningAction SilentlyContinue
    if ($app.TcpTestSucceeded) {
        Write-Host "✓ 애플리케이션 실행 중" -ForegroundColor Green
        Write-Host "  Swagger UI: http://localhost:8080/swagger-ui.html" -ForegroundColor Cyan
    } else {
        Write-Host "✗ 애플리케이션이 실행되지 않음" -ForegroundColor Red
    }
} catch {
    Write-Host "✗ 애플리케이션이 실행되지 않음" -ForegroundColor Red
}

