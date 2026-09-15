from fastapi import FastAPI, UploadFile, File, HTTPException
from PIL import Image
import io, hashlib

app = FastAPI(title="AURA AI Core", version="1.0.0")

@app.get("/health")
def health():
    return {"status": "ok", "service": "aura-ai-core", "modelVersion": "demo-v1"}

def analyze_image(data: bytes):
    # Transparent demo inference: deterministic score based on image bytes.
    # Replace this function with a trained PyTorch/TensorFlow model.
    digest = hashlib.sha256(data).hexdigest()
    score = (int(digest[:8], 16) % 8000) / 10000 + 0.10
    score = min(score, 0.99)
    if score < 0.35:
        level = "LOW"
    elif score < 0.70:
        level = "MEDIUM"
    else:
        level = "HIGH"
    return {
        "riskScore": round(score, 3),
        "riskLevel": level,
        "modelVersion": "demo-v1",
        "findings": [
            "Retinal image quality accepted for screening.",
            "Vascular morphology requires clinical review."
        ],
        "explanation": "Demo result only. This model is not a medical diagnostic system."
    }

@app.post("/predict")
async def predict(file: UploadFile = File(...)):
    if not file.content_type or not file.content_type.startswith("image/"):
        raise HTTPException(400, "Only image files are accepted")
    data = await file.read()
    if len(data) > 20 * 1024 * 1024:
        raise HTTPException(413, "Image is larger than 20 MB")
    try:
        Image.open(io.BytesIO(data)).verify()
    except Exception:
        raise HTTPException(400, "Invalid image")
    return analyze_image(data)
