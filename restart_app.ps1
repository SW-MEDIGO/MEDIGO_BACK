# 애플리케이션 재시작 스크립트

Write-Host "애플리케이션 재시작 중..." -ForegroundColor Yellow

# 기존 프로세스 종료
Get-Process | Where-Object {$_.ProcessName -like "*java*" -and $_.MainWindowTitle -eq ""} | Select-Object -First 5 | ForEach-Object {
    Write-Host "Java 프로세스 종료: $($_.Id)" -ForegroundColor Gray
    Stop-Process -Id $_.Id -Force
}

Write-Host "새 애플리케이션 시작..." -ForegroundColor Yellow
Start-Process powershell -ArgumentList "-NoExit", "-Command", "cd '$PWD'; .\gradlew.bat bootRun"

