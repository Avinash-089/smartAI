@echo off
echo ====================================================
echo Starting Smart AI Student Management System
echo ====================================================

echo Starting Spring Boot Backend (Port 8081)...
start "Backend - Spring Boot" cmd /k "cd student-management && mvn spring-boot:run"

timeout /t 3 /nobreak >nul

echo Starting Python AI Microservice (Port 5001)...
start "AI Microservice - Python" cmd /k "cd ai-service && python run.py"

timeout /t 3 /nobreak >nul

echo Starting React Frontend (Port 5173)...
start "Frontend - React" cmd /k "cd frontend && npm.cmd run dev"

echo ====================================================
echo All services launched!
echo Backend:   http://localhost:8081
echo AI Service: http://localhost:5001
echo Frontend:  http://localhost:5173
echo ====================================================
