from fastapi import FastAPI, Request
from fastapi.middleware.cors import CORSMiddleware
from indic_transliteration.sanscript import transliterate, DEVANAGARI, GUJARATI, BENGALI, TAMIL, TELUGU, IAST

app = FastAPI(title="BhashaMitra Transliteration API")

# Middleware to allow cross-origin requests from any source
app.add_middleware(
    CORSMiddleware,
    allow_origins=["*"],  # Allows all origins
    allow_credentials=True,
    allow_methods=["*"],  # Allows all methods
    allow_headers=["*"],  # Allows all headers
)

# Map string names to the library's script constants
SCRIPT_MAP = {
    "Devanagari": DEVANAGARI,
    "Gujarati": GUJARATI,
    "Bengali": BENGALI,
    "Tamil": TAMIL,
    "Telugu": TELUGU,
    "Latin": IAST,
}

@app.post("/transliterate")
async def transliterate_text(request: Request):
    try:
        data = await request.json()
        text = data.get("text")
        source_str = data.get("source")
        target_str = data.get("target")

        if not text or not text.strip():
            return {"error": "Text field is empty or missing"}
        if not source_str or not target_str:
            return {"error": "Source or target script is missing"}

        source_script = SCRIPT_MAP.get(source_str)
        target_script = SCRIPT_MAP.get(target_str)

        if not source_script:
            return {"error": f"Unsupported source script: {source_str}"}
        if not target_script:
            return {"error": f"Unsupported target script: {target_str}"}

        transliterated_text = transliterate(text, source_script, target_script)

        return {
            "original_text": text,
            "transliteration": transliterated_text,
        }
    # This block is now correctly indented
    except Exception as e:
        return {"error": f"An unexpected error occurred: {str(e)}"}

@app.post("/")
def home():
    return {"message": "✅ BhashaMitra Transliteration API is running successfully"}

