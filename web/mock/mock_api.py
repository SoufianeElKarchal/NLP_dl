from fastapi import FastAPI, HTTPException
from pydantic import BaseModel
import time

app = FastAPI(title="Arabic Summarization Mock API")


class TextRequest(BaseModel):
    text: str


@app.post("/api/v1/summarize")
def mock_summarize(payload: TextRequest):

    if not payload.text.strip():
        raise HTTPException(
            status_code=400,
            detail="Le texte ne doit pas être vide."
        )

    start_time = time.perf_counter()

    time.sleep(1.0)

    tokens = ["هذا", "ملخص", "تجريبي"]

    return {
        "status": "success",
        "summary": " ".join(tokens),
        "generated_tokens": tokens + ["<EOS>"],
        "metadata": {
            "processing_time_seconds": round(
                time.perf_counter() - start_time, 3
            )
        }
    }
@app.get("/")
def home():
    return {
        "status": "running",
        "message": "Arabic Summarization Mock API"
    }

if __name__ == "__main__":
    import uvicorn

    uvicorn.run(
        app,
        host="127.0.0.1",
        port=8000
    )