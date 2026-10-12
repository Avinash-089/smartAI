import re
from typing import Dict, Any, List

class AcademicChatbot:
    def __init__(self):
        self.common_suggestions = [
            "What is the minimum attendance criteria?",
            "When will mid-semester results be published?",
            "How do I pay my pending tuition fees?",
            "How does the AI predict at-risk students?",
            "What is the campus class schedule?"
        ]

    def answer_query(self, message: str, role: str = "STUDENT", user_email: str = None) -> Dict[str, Any]:
        msg = message.strip().lower()

        # 1. Attendance Intents
        if any(w in msg for w in ["attendance", "absent", "leave", "present", "bunk"]):
            return {
                "reply": "Institutional guidelines mandate a minimum of 75% attendance overall. "
                         "Students falling below 65% are automatically classified as 'High Risk' and require departmental clearance to appear for end-semester examinations.",
                "intent": "ATTENDANCE_POLICY",
                "suggestions": [
                    "Can I submit a medical certificate for attendance?",
                    "Check my current attendance record"
                ]
            }

        # 2. Fee & Finance Intents
        elif any(w in msg for w in ["fee", "fees", "payment", "tuition", "due", "paid", "receipt", "fine"]):
            return {
                "reply": "Tuition and lab fees must be cleared within 30 days of semester registration. "
                         "You can review your pending balance under the 'Fees' module and mark payments directly.",
                "intent": "FEES_AND_PAYMENTS",
                "suggestions": [
                    "Where do I download my fee receipt?",
                    "Are installment plans available?"
                ]
            }

        # 3. Exam, GPA & Marks Intents
        elif any(w in msg for w in ["exam", "examination", "marks", "grade", "gpa", "cgpa", "result", "score", "midterm"]):
            return {
                "reply": "Midterm examinations are graded out of 100 and converted to grade points (A+ to F). "
                         "Final exam timetables are posted 3 weeks prior to commencement. You can view your subject-wise report card in the 'Grades' section.",
                "intent": "EXAMS_AND_GRADES",
                "suggestions": [
                    "What is the passing mark for core courses?",
                    "How is my CGPA calculated?"
                ]
            }

        # 4. AI & Risk Prediction Intents
        elif any(w in msg for w in ["risk", "predict", "prediction", "ai", "machine learning", "flag", "at-risk"]):
            return {
                "reply": "The AI Student Predictor utilizes historical attendance ratios, internal test scores, and assignment completion rates. "
                         "It computes a predicted end-semester score and assigns proactive risk flags (High, Medium, Low) to enable timely intervention before final exams.",
                "intent": "AI_PREDICTION_DETAILS",
                "suggestions": [
                    "How can I improve my risk level to LOW?",
                    "Run AI performance prediction now"
                ]
            }

        # 5. Timetable & Schedule Intents
        elif any(w in msg for w in ["timetable", "schedule", "class", "time", "routine", "lecture", "lab"]):
            return {
                "reply": "Regular theory lectures operate Monday to Friday between 9:00 AM and 4:30 PM. "
                         "Practical laboratory sessions are scheduled in afternoon blocks from 1:30 PM to 4:30 PM.",
                "intent": "TIMETABLE_INFO",
                "suggestions": [
                    "Are laboratory sessions mandatory every week?",
                    "Where is the departmental library located?"
                ]
            }

        # 6. Faculty & Contact Intents
        elif any(w in msg for w in ["faculty", "teacher", "professor", "hod", "dean", "advisor", "contact"]):
            return {
                "reply": "Faculty office hours are held Monday to Thursday between 3:00 PM and 5:00 PM. "
                         "You can find department designations and contact details in the 'Faculty' directory tab.",
                "intent": "FACULTY_CONTACT",
                "suggestions": [
                    "Who is the current Head of Computer Science?",
                    "Schedule a meeting with academic advisor"
                ]
            }

        # 7. Greetings
        elif any(w in msg for w in ["hi", "hello", "hey", "greetings", "good morning", "good evening"]):
            return {
                "reply": f"Hello! Welcome to the Smart AI Student Management Portal. I am your academic AI assistant. How can I help you today?",
                "intent": "GREETING",
                "suggestions": self.common_suggestions
            }

        # 8. Fallback
        else:
            return {
                "reply": "I can help you navigate attendance guidelines, fee schedules, exam timelines, and AI performance predictions. Please select a question below or specify your query.",
                "intent": "GENERAL_HELP",
                "suggestions": self.common_suggestions
            }

chatbot = AcademicChatbot()
