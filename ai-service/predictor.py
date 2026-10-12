try:
    import numpy as np
except ImportError:
    np = None

from typing import Dict, Any, List

class StudentPerformancePredictor:
    def __init__(self):
        # Academic regression weights:
        # attendance (30%), internal marks (45%), assignment score (20%), study dedication (5%)
        self.weights = [0.30, 0.45, 0.20, 0.05]
        self.bias = 2.5

    def predict(self, attendance: float, internal_marks: float, assignment_score: float, study_hours: int = 10) -> Dict[str, Any]:
        # Normalize inputs to 0-100 scale
        att = max(0.0, min(100.0, float(attendance)))
        internal = max(0.0, min(100.0, float(internal_marks)))
        assign = max(0.0, min(100.0, float(assignment_score)))
        hours = max(0, min(40, int(study_hours)))

        features = [att, internal, assign, hours * 2.5]
        predicted_score = float(sum(f * w for f, w in zip(features, self.weights)) + self.bias)
        predicted_score = round(max(10.0, min(100.0, predicted_score)), 1)

        # Convert to 4.0 GPA scale
        predicted_gpa = round(min(4.0, max(0.0, predicted_score / 25.0)), 2)

        risk_factors = []
        recommendations = []

        if att < 65.0:
            risk_factors.append(f"Critical attendance deficit: currently {att}% (below 65% mandatory threshold)")
            recommendations.append("Mandatory attendance recovery through remedial laboratory sessions.")
        elif att < 75.0:
            risk_factors.append(f"Sub-optimal attendance: currently {att}% (institutional benchmark is 75%)")
            recommendations.append("Ensure attendance in upcoming high-credit core lectures.")

        if internal < 50.0:
            risk_factors.append(f"Failed internal threshold: score {internal}%")
            recommendations.append("Schedule one-on-one tutorial with subject faculty.")
        elif internal < 70.0:
            risk_factors.append(f"Inconsistent internal performance: {internal}%")
            recommendations.append("Focus on concept reinforcement and weekly quizzes.")

        if assign < 60.0:
            risk_factors.append(f"Low assignment completion rate: {assign}%")
            recommendations.append("Utilize peer study groups to submit assignments on schedule.")

        if study_hours < 8:
            risk_factors.append(f"Low weekly study dedication: {study_hours} hrs/week")
            recommendations.append("Allocate at least 12-15 hours per week for independent study.")

        # Determine overall risk classification
        if predicted_score < 55.0 or att < 65.0 or internal < 45.0:
            risk_level = "HIGH"
            recommendations.insert(0, "URGENT: Flag student for Dean's Academic Intervention Committee.")
        elif predicted_score < 75.0 or att < 75.0 or internal < 65.0:
            risk_level = "MEDIUM"
            recommendations.insert(0, "Academic Advisor alert: Student is at moderate risk of GPA deterioration.")
        else:
            risk_level = "LOW"
            recommendations.append("Consistent progress: eligible for accelerated elective courses and honors.")

        return {
            "predictedScore": predicted_score,
            "predictedGpa": predicted_gpa,
            "riskLevel": risk_level,
            "riskFactors": risk_factors,
            "recommendations": recommendations
        }

    def evaluate_batch(self, students: List[Dict[str, Any]]) -> List[Dict[str, Any]]:
        results = []
        for s in students:
            pred = self.predict(
                attendance=s.get("attendancePercentage", 75.0),
                internal_marks=s.get("internalMarks", 70.0),
                assignment_score=s.get("assignmentScore", 70.0),
                study_hours=s.get("studyHours", 10)
            )
            results.append({
                "studentId": s.get("id"),
                "name": s.get("name"),
                "email": s.get("email"),
                "course": s.get("course"),
                **pred
            })
        # Sort so high-risk students appear first
        risk_rank = {"HIGH": 0, "MEDIUM": 1, "LOW": 2}
        results.sort(key=lambda x: risk_rank.get(x["riskLevel"], 3))
        return results

predictor = StudentPerformancePredictor()
