from fastapi import APIRouter
from app.models.summary_request import LabSummaryRequest
from app.services.llm_service import get_llm
from app.services.prompt_builder import build_lab_summary_prompt

router = APIRouter(prefix="/lab", tags=["lab"])

@router.post("/summary")
async def generate_lab_summary(request: LabSummaryRequest):
    try:
        llm = get_llm()
        prompt = build_lab_summary_prompt(request)
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
            "summary": "摘要生成失败，请查看原始数据"
        }
