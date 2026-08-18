# SP26SE025 - AURA
System for Retinal Vascular Health Screening.

## Architecture
Frontend: React + TypeScript + Vite
Backend: Java Spring Boot
AI Core: Python FastAPI
Database: H2 for local demo; PostgreSQL-ready configuration can be added.

## Run
### 1. AI Core
cd Code/AI-Core
python -m venv .venv
# Windows:
.venv\Scripts\activate
pip install -r requirements.txt
uvicorn main:app --reload --port 8000

### 2. Backend
cd Code/Backend
mvn spring-boot:run
Backend: http://localhost:8080

### 3. Frontend
cd Code/Frontend
npm install
npm run dev
Frontend: http://localhost:5173

## Demo flow
Register/Login -> Dashboard -> New Analysis -> Upload image -> Java calls AI Core -> result is stored -> result appears in history.

The current AI Core contains a transparent demo inference layer. Replace `analyze_image()` with a trained retinal model when a properly licensed dataset/model is available.
