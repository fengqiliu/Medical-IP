from fastapi import APIRouter
from app.models.summary_request import ImagingSummaryRequest
from app.services.llm_service import get_llm
from app.services.prompt_builder import build_imaging_summary_prompt

router = APIRouter(prefix="/imaging", tags=["imaging"])

@router.post("/summary")
async def generate_imaging_summary(request: ImagingSummaryRequest):
    try:
        llm = get_llm()
        prompt = build_imaging_summary_prompt(request)
        response = llm.invoke(prompt)
        return {
            "success": True,
            "summary": response.content,
            "model_version": "gpt-3.5-turbo"
        }
    except Exception as e:
        return {
            "success": False,
            "error": str(e),
            "summary": "摘要生成失败，请查看原始报告"
        }
