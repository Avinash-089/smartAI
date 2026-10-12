# Smart AI Student Management System

A comprehensive, full-stack AI-driven institutional management platform that combines student administration functionalities with predictive machine learning and automated workflow intelligence.

---

## Architecture Overview

```
                      +-----------------------------+
                      |   React.js Web Frontend     |
                      |   (Vite, Tailwind, UI)      |
                      |   Port: 5173                |
                      +--------------+--------------+
                                     |
                                     | REST APIs
                                     v
+-----------------------------+              +-----------------------------+
|   Spring Boot Backend       | <----------> |   Python AI Microservice    |
|   Java 25 / Spring Boot 4   |  REST APIs   |   (FastAPI / scikit-learn)  |
|   Port: 8081                |              |   Port: 5001                |
+--------------+--------------+              +-----------------------------+
               |
               | JDBC
               v
+-----------------------------+
|   MySQL 8.0 Database        |
|   Database: student_management
|   Port: 3308                |
+-----------------------------+
```

---

## Technology Stack

- **Frontend**: React.js, Vite, Lucide Icons, Modern responsive UI
- **Backend**: Spring Boot 4.1.1 (Java 25), Spring Data JPA, Spring Security, BCrypt, Maven
- **Database**: MySQL 8.0 on port 3308
- **AI / ML Service**: Python 3.14, FastAPI, scikit-learn, NumPy, Pandas

---

## Key Features

1. **Role-Based Access Control**
   - **Admin**: Full control over student records, faculty, courses, subjects, fees, and analytics.
   - **Faculty**: Attendance entry, grade management, subject assignments.
   - **Student**: View personalized attendance, report cards, fee dues, timetable.

2. **Core Administration**
   - Student enrollment, directory, and profiling.
   - Faculty management and departmental tracking.
   - Course and subject credit curriculum management.
   - Fee tracking with status management (PAID / PENDING / OVERDUE).

3. **Intelligent AI Capabilities**
   - **Predictive Performance Analytics**: Machine learning model evaluates attendance percentage and past grades to predict final GPA and score.
   - **At-Risk Student Flagging**: Automatically classifies students as High, Medium, or Low academic risk with actionable early intervention recommendations.
   - **AI Facial Attendance**: Image and facial recognition verification endpoint for streamlined attendance marking.
   - **AI Academic Chatbot**: NLP conversational assistant answering student questions on fee schedules, exam dates, attendance rules, and course details.

---

## Quick Start Guide

### Prerequisites
- Java 17+ (Java 25 supported)
- Maven 3.9+
- Python 3.10+
- Node.js 18+ and npm
- MySQL running on port 3308 (database: `student_management`, user: `root`, password: `root`)

### 1. Launch All Services (One Click)
On Windows, simply run:
```cmd
start-all.bat
```
Or with PowerShell:
```powershell
.\start-all.ps1
```

### 2. Manual Startup

#### Backend (Spring Boot)
```cmd
cd student-management
mvn spring-boot:run
```
API runs at: `http://localhost:8080`

#### AI Microservice (Python)
```cmd
cd ai-service
pip install -r requirements.txt
python run.py
```
Microservice runs at: `http://localhost:5001`

#### Frontend (React)
```cmd
cd frontend
npm install
npm run dev
```
UI runs at: `http://localhost:5173`

---

## Default Demo Accounts

| Role | Email | Password |
|---|---|---|
| Admin | `admin@example.com` | `admin123` |
| Faculty | `faculty@example.com` | `faculty123` |
| Student | `avinash.student@example.com` | `student123` |

---

## REST API Summary

- `POST /api/auth/login` - User login
- `GET /api/dashboard/stats` - Institutional metrics & KPI overview
- `GET/POST /api/students` - Student CRUD operations
- `GET/POST /api/faculty` - Faculty management
- `GET/POST /api/courses` - Course directory
- `GET/POST /api/attendance` - Attendance recording & statistics
- `GET/POST /api/grades` - Student marks & report cards
- `GET/POST /api/fees` - Fee invoicing & records
- `POST /api/ai/predict-performance` - ML student score predictor
- `POST /api/ai/chat` - Academic AI Assistant
