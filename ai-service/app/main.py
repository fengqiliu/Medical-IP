from fastapi import FastAPI
from fastapi.middleware.cors import CORSMiddleware
from app.routers import lab_summary, imaging_summary

app = FastAPI(title="医技360 AI 服务", version="1.0.0")

app.add_middleware(
    CORSMiddleware,
    allow_origins=["*"],
    allow_credentials=True,
    allow_methods=["*"],
    allow_headers=["*"],
)

app.include_router(lab_summary.router)
app.include_router(imaging_summary.router)

@app.get("/health")
async def health():
    return {"status": "ok"}

if __name__ == "__main__":
    import uvicorn
    uvicorn.run(app, host="0.0.0.0", port=8000)
