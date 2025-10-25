# main.py
from fastapi import FastAPI, Request, UploadFile, File, Form
from fastapi.middleware.cors import CORSMiddleware
from indic_transliteration.sanscript import transliterate, DEVANAGARI, GUJARATI, BENGALI, TAMIL, TELUGU, IAST
from PIL import Image
import io
import easyocr
from typing import Optional

app = FastAPI(title="BhashaMitra Transliteration API")

# CORS - allow all during development
app.add_middleware(
    CORSMiddleware,
    allow_origins=["*"],
    allow_credentials=True,
    allow_methods=["*"],
    allow_headers=["*"],
)

# Map readable names to indic_transliteration constants
SCRIPT_MAP = {
    "Devanagari": DEVANAGARI,
    "Gujarati": GUJARATI,
    "Bengali": BENGALI,
    "Tamil": TAMIL,
    "Telugu": TELUGU,
    "Latin": IAST,
}

# Initialize EasyOCR reader once (languages you used before)
# This can be slow on first import; keep it global to reuse the model.
try:
    reader = easyocr.Reader(['hi', 'gu', 'bn', 'ta', 'te', 'ml', 'mr', 'kn'], gpu=False)
except Exception as e:
    # If EasyOCR fails to initialize (e.g., missing torch in environment),
    # set reader = None and return helpful error in /predict.
    reader = None
    print("EasyOCR initialization error:", e)


def do_transliteration(text: str, source_str: str, target_str: str):
    """
    Convert friendly language names to script constants and transliterate.
    Returns dict with keys similar to your Android expectation.
    """
    if not text or not text.strip():
        return {"error": "Text is empty"}

    if not source_str or not target_str:
        return {"error": "Source or target script missing"}

    source_script = SCRIPT_MAP.get(source_str)
    target_script = SCRIPT_MAP.get(target_str)

    if not source_script:
        return {"error": f"Unsupported source script: {source_str}"}
    if not target_script:
        return {"error": f"Unsupported target script: {target_str}"}

    try:
        transliterated_text = transliterate(text, source_script, target_script)
    except Exception as e:
        return {"error": f"Transliteration failed: {str(e)}"}

    return {
        "detected_text": text,
        "transliterated_text": transliterated_text
    }


@app.post("/transliterate")
async def transliterate_text(request: Request):
    """
    Existing JSON-based transliteration endpoint.
    Expects JSON body: { "text": "...", "source": "Devanagari", "target": "Latin" }
    """
    try:
        data = await request.json()
        text = data.get("text")
        source_str = data.get("source")
        target_str = data.get("target")

        return do_transliteration(text, source_str, target_str)
    except Exception as e:
        return {"error": f"An unexpected error occurred: {str(e)}"}


@app.post("/predict")
async def predict(
        file: Optional[UploadFile] = File(None),
        source: Optional[str] = Form(None),
        target: Optional[str] = Form(None)
):
    """
    Accepts multipart/form-data:
      - file: image file (required if no 'text' form field)
      - source: optional string like "Devanagari", "Gujarati"
      - target: optional string like "Latin" or "IAST"

    Behavior:
      - If file provided: run OCR to get text, then transliterate.
      - If file not provided: returns an error (or you could accept text field instead).
    """
    # If client didn't send a file, return helpful error
    if file is None:
        return {"error": "Please upload an image file in the 'file' field."}

    # Ensure EasyOCR is available
    if reader is None:
        return {"error": "OCR engine not available on server (EasyOCR initialization failed)."}

    # Read file bytes and make PIL image
    try:
        contents = await file.read()
        image = Image.open(io.BytesIO(contents)).convert("RGB")
    except Exception as e:
        return {"error": f"Failed to read uploaded image: {str(e)}"}

    # Run EasyOCR (detail=0 returns plain strings)
    try:
        ocr_result = reader.readtext(image, detail=0)
        detected_text = " ".join(ocr_result).strip()
    except Exception as e:
        return {"error": f"OCR failed: {str(e)}"}

    # If no source/target provided, pick defaults (assume Devanagari -> Latin)
    source_str = source or "Devanagari"
    target_str = target or "Latin"

    # Transliterate using the helper
    return do_transliteration(detected_text, source_str, target_str)


@app.post("/")
def home():
    return {"message": "✅ BhashaMitra Transliteration API is running successfully"}
