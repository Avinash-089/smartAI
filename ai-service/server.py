import json
import urllib.parse
from http.server import HTTPServer, BaseHTTPRequestHandler
from predictor import predictor
from chatbot import chatbot
from facial_attendance import facial_processor

class AIRequestHandler(BaseHTTPRequestHandler):
    def _send_cors_headers(self):
        self.send_header("Access-Control-Allow-Origin", "*")
        self.send_header("Access-Control-Allow-Methods", "GET, POST, OPTIONS")
        self.send_header("Access-Control-Allow-Headers", "Content-Type, Authorization, Accept")

    def do_OPTIONS(self):
        self.send_response(204)
        self._send_cors_headers()
        self.end_headers()

    def do_GET(self):
        parsed = urllib.parse.urlparse(self.path)
        if parsed.path == "/health":
            self.send_response(200)
            self.send_header("Content-Type", "application/json")
            self._send_cors_headers()
            self.end_headers()
            response = {
                "status": "UP",
                "service": "Smart AI Student Management Microservice",
                "version": "1.0.0"
            }
            self.wfile.write(json.dumps(response).encode("utf-8"))
        else:
            self.send_response(404)
            self._send_cors_headers()
            self.end_headers()

    def do_POST(self):
        parsed = urllib.parse.urlparse(self.path)
        content_len = int(self.headers.get("Content-Length", 0))
        body = self.rfile.read(content_len).decode("utf-8") if content_len > 0 else "{}"
        
        try:
            data = json.loads(body)
        except Exception:
            data = {}

        if parsed.path == "/api/ai/predict-performance":
            att = float(data.get("attendancePercentage", 75.0))
            internal = float(data.get("internalMarks", 70.0))
            assign = float(data.get("assignmentScore", 70.0))
            hours = int(data.get("studyHoursPerWeek", 10))
            student_id = data.get("studentId")

            res = predictor.predict(att, internal, assign, hours)
            res["studentId"] = student_id
            self._respond_json(res)

        elif parsed.path == "/api/ai/at-risk-batch":
            students = data.get("students", [])
            res = predictor.evaluate_batch(students)
            self._respond_json(res)

        elif parsed.path == "/api/ai/chat":
            msg = data.get("message", "")
            role = data.get("role", "STUDENT")
            user_email = data.get("userEmail")
            res = chatbot.answer_query(msg, role, user_email)
            self._respond_json(res)

        elif parsed.path == "/api/ai/facial-attendance":
            img = data.get("imageBase64", "")
            classroom = data.get("classroom", "Lecture Hall A")
            res = facial_processor.process_image(img, classroom)
            self._respond_json(res)

        else:
            self.send_response(404)
            self._send_cors_headers()
            self.end_headers()

    def _respond_json(self, obj):
        self.send_response(200)
        self.send_header("Content-Type", "application/json")
        self._send_cors_headers()
        self.end_headers()
        self.wfile.write(json.dumps(obj).encode("utf-8"))

def start_server(port=5001):
    server = HTTPServer(("0.0.0.0", port), AIRequestHandler)
    print(f"Smart AI Microservice listening on http://localhost:{port}")
    try:
        server.serve_forever()
    except KeyboardInterrupt:
        print("\nStopping Smart AI Microservice...")
        server.server_close()

if __name__ == "__main__":
    import os
    port = int(os.environ.get("PORT", 5001))
    start_server(port)

