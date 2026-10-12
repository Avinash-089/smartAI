from predictor import predictor
from chatbot import chatbot
from facial_attendance import facial_processor

def test_ai_modules():
    print("Testing ML Performance Predictor...")
    res = predictor.predict(attendance=55.0, internal_marks=48.0, assignment_score=50.0, study_hours=5)
    print("Prediction result for at-risk student:", res)
    assert res["riskLevel"] == "HIGH", f"Expected HIGH risk, got {res['riskLevel']}"
    assert len(res["riskFactors"]) > 0
    assert len(res["recommendations"]) > 0

    res_good = predictor.predict(attendance=92.0, internal_marks=88.0, assignment_score=90.0, study_hours=15)
    print("Prediction result for good student:", res_good)
    assert res_good["riskLevel"] == "LOW", f"Expected LOW risk, got {res_good['riskLevel']}"

    print("\nTesting Chatbot NLP...")
    chat_res = chatbot.answer_query("What is the attendance criteria?")
    print("Chatbot reply:", chat_res["reply"])
    assert "75%" in chat_res["reply"]
    assert chat_res["intent"] == "ATTENDANCE_POLICY"

    fee_res = chatbot.answer_query("How can I pay my pending tuition fees?")
    print("Fee reply:", fee_res["reply"])
    assert fee_res["intent"] == "FEES_AND_PAYMENTS"

    print("\nTesting Facial Attendance Processor...")
    face_res = facial_processor.process_image("data:image/jpeg;base64,/9j/4AAQSkZJRg==", classroom="Room 101")
    print("Facial scan result:", face_res)
    assert face_res["success"] is True
    assert face_res["recognizedCount"] >= 2

    print("\nALL AI MICROSERVICE TESTS PASSED SUCCESSFULLY!")

if __name__ == "__main__":
    test_ai_modules()
