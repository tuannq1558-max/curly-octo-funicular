# SP26SE025 - AURA
Hệ Thống Sàng Lọc Sức Khỏe Mạch Máu Võng Mạc.

Dự án hỗ trợ phân tích ảnh võng mạc và cung cấp đánh giá nguy cơ nhằm hỗ trợ bác sĩ trong quá trình sàng lọc.

SP26SE025/
│
├── .gitignore                  # Chặn các file không cần thiết lên GitHub
├── requirements.txt            # Thư viện Python cho AI Core
├── README.md                   # Thông tin và hướng dẫn chạy dự án
│
├── Code/
│   │
│   ├── Backend/                # Java Spring Boot
│   │   ├── pom.xml
│   │   └── src/
│   │
│   ├── Frontend/               # React + TypeScript
│   │   ├── package.json
│   │   ├── vite.config.ts
│   │   └── src/
│   │
│   └── AI-Core/                # Python FastAPI
│       ├── main.py
│       ├── requirements.txt
│       └── README.md
│
├── DOCX/                       # Tài liệu Word
├── PPTX/                       # Slide thuyết trình
└── EXTRA/                      # Tài nguyên bổ sung

Kiến trúc hệ thống

React Frontend
      ↓
Java Spring Boot
      ↓
Python AI Core
      ↓
AI Analysis
      ↓
Kết quả phân tích

Cài đặt môi trường

1. AI Core

cd Code/AI-Core
python -m venv .venv
.venv\Scripts\activate
pip install -r requirements.txt

Chạy AI:

uvicorn main:app --reload --port 8000


2. Backend

cd Code/Backend
mvn spring-boot:run

Backend chạy tại:
http://localhost:8080


3. Frontend

cd Code/Frontend
npm install
npm run dev

Frontend chạy tại:
http://localhost:5173
