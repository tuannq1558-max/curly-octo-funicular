# AURA AI Core

FastAPI microservice.

POST `/predict` with multipart field `file`.

This starter uses a deterministic demo score so the whole system can be tested without pretending that an untrained model is medically valid.

For the real project:
1. Obtain a licensed retinal dataset.
2. Preprocess and anonymize data.
3. Train/validate a retinal vessel segmentation model.
4. Train a risk model only with clinically appropriate labels.
5. Export the validated model.
6. Replace `analyze_image()` with model inference.
