from fastapi import FastAPI, HTTPException
from fastapi.middleware.cors import CORSMiddleware
from pydantic import BaseModel, Field
from typing import Optional, List, Dict, Any

from predictor import predictor
from chatbot import chatbot
from facial_attendance import facial_processor

app = FastAPI(
    title="Smart AI Student Management Microservice",
    description="Machine Learning, Face Attendance, and NLP Query Services",
    version="1.0.0"
)

# Enable CORS for React Frontend and Spring Boot Backend
app.add_middleware(
    CORSMiddleware,
    allow_origins=["*"],
    allow_credentials=True,
    allow_methods=["*"],
    allow_headers=["*"],
)

# Pydantic Request Models
class PredictPerformanceRequest(BaseModel):
    studentId: Optional[int] = None
    attendancePercentage: float = Field(..., ge=0, le=100)
    internalMarks: float = Field(..., ge=0, le=100)
    assignmentScore: float = Field(..., ge=0, le=100)
    studyHoursPerWeek: Optional[int] = Field(10, ge=0, le=60)

class AtRiskBatchRequest(BaseModel):
    students: List[Dict[str, Any]]

class ChatRequest(BaseModel):
    message: str
    role: Optional[str] = "STUDENT"
    userEmail: Optional[str] = None

class FacialAttendanceRequest(BaseModel):
    imageBase64: str
    subjectId: Optional[int] = None
    classroom: Optional[str] = "Lecture Hall A"

@app.get("/health")
def health_check():
    return {
        "status": "UP",
        "service": "Smart AI Student Management Microservice",
        "version": "1.0.0"
    }

@app.post("/api/ai/predict-performance")
def predict_performance(req: PredictPerformanceRequest):
    try:
        prediction = predictor.predict(
            attendance=req.attendancePercentage,
            internal_marks=req.internalMarks,
            assignment_score=req.assignmentScore,
            study_hours=req.studyHoursPerWeek or 10
        )
        return {
            "studentId": req.studentId,
            "predictedGpa": prediction["predictedGpa"],
            "predictedPercentage": prediction["predictedScore"],
            "riskLevel": prediction["riskLevel"],
            "riskFactors": prediction["riskFactors"],
            "recommendations": prediction["recommendations"]
        }
    except Exception as e:
        raise HTTPException(status_code=500, detail=str(e))

@app.post("/api/ai/at-risk-batch")
def at_risk_batch(req: AtRiskBatchRequest):
    try:
        return predictor.evaluate_batch(req.students)
    except Exception as e:
        raise HTTPException(status_code=500, detail=str(e))

@app.post("/api/ai/chat")
def chat(req: ChatRequest):
    try:
        return chatbot.answer_query(
            message=req.message,
            role=req.role or "STUDENT",
            user_email=req.userEmail
        )
    except Exception as e:
        raise HTTPException(status_code=500, detail=str(e))

@app.post("/api/ai/facial-attendance")
def facial_attendance(req: FacialAttendanceRequest):
    try:
        return facial_processor.process_image(
            image_base64=req.imageBase64,
            classroom=req.classroom or "Lecture Hall A"
        )
    except Exception as e:
        raise HTTPException(status_code=500, detail=str(e))

if __name__ == "__main__":
    import uvicorn
    uvicorn.run("app:app", host="0.0.0.0", port=5001, reload=True)
