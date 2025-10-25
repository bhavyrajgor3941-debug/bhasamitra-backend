from fastapi import FastAPI
from fastapi.middleware.cors import CORSMiddleware
from pydantic import BaseModel
import base64
from io import BytesIO
from PIL import Image

try:
    import easyocr
    reader = easyocr.Reader(['hi', 'gu', 'bn', 'ta', 'te', 'kn'])
except Exception:
    reader = None

try:
    # Placeholder import; replace with actual ai4bharat transliteration lib if available
    from indic_transliteration.sanscript import transliterate
    from indic_transliteration import sanscript
    def simple_transliterate(txt: str) -> str:
        return transliterate(txt, sanscript.DEVANAGARI, sanscript.ITRANS)
except Exception:
    def simple_transliterate(txt: str) -> str:
        return txt

app = FastAPI(title="BhashaMitra OCR + Transliteration API")
app.add_middleware(
    CORSMiddleware,
    allow_origins=["*"],
    allow_methods=["*"],
    allow_headers=["*"],
)

class ImageRequest(BaseModel):
    image: str  # base64

@app.get("/")
def root():
    return {"message": "BhashaMitra OCR+Transliteration API running"}

@app.post("/transliterate")
def transliterate_endpoint(req: ImageRequest):
    try:
        img_bytes = base64.b64decode(req.image)
        img = Image.open(BytesIO(img_bytes))
        detected_text = ""
        if reader is not None:
            # easyocr expects path or bytes; convert to bytes
            buf = BytesIO()
            img.save(buf, format='PNG')
            detected_lines = reader.readtext(buf.getvalue(), detail=0)
            detected_text = " ".join(detected_lines)
        else:
            detected_text = ""
        transliterated = simple_transliterate(detected_text)
        return {"detected_text": detected_text, "transliterated_text": transliterated}
    except Exception as e:
        return {"error": str(e)}

if __name__ == "__main__":
    import uvicorn
    uvicorn.run(app, host="0.0.0.0", port=10000)





