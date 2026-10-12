Write-Host "====================================================" -ForegroundColor Cyan
Write-Host "Starting Smart AI Student Management System" -ForegroundColor Cyan
Write-Host "====================================================" -ForegroundColor Cyan

$backendDir = Join-Path $PSScriptRoot "student-management"
$aiDir = Join-Path $PSScriptRoot "ai-service"
$frontendDir = Join-Path $PSScriptRoot "frontend"

Write-Host "Starting Spring Boot Backend (Port 8081)..." -ForegroundColor Green
Start-Process powershell -ArgumentList "-NoExit", "-Command", "Set-Location '$backendDir'; mvn spring-boot:run"

Start-Sleep -Seconds 2

Write-Host "Starting Python AI Microservice (Port 5001)..." -ForegroundColor Yellow
Start-Process powershell -ArgumentList "-NoExit", "-Command", "Set-Location '$aiDir'; python run.py"

Start-Sleep -Seconds 2

Write-Host "Starting React Frontend (Port 5173)..." -ForegroundColor Magenta
Start-Process powershell -ArgumentList "-NoExit", "-Command", "Set-Location '$frontendDir'; npm.cmd run dev"

Write-Host "`nAll 3 services launched in separate windows!" -ForegroundColor Green
Write-Host "Backend:    http://localhost:8081" -ForegroundColor Cyan
Write-Host "AI Service: http://localhost:5001" -ForegroundColor Cyan
Write-Host "Frontend:   http://localhost:5173" -ForegroundColor Cyan
