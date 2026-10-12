import sys
import os

if __name__ == "__main__":
    port = int(os.environ.get("PORT", 5001))
    try:
        import uvicorn
        import fastapi
        print(f"Starting Smart AI Microservice with FastAPI & Uvicorn on port {port}...")
        uvicorn.run("app:app", host="0.0.0.0", port=port, log_level="info")
    except ImportError:
        print(f"FastAPI/Uvicorn not installed. Starting built-in high-performance HTTP AI server on port {port}...")
        from server import start_server
        start_server(port)

