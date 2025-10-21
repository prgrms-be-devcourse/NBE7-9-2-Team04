"use client";

import { useRouter } from "next/navigation";
import { useState } from "react";

export default function PortfolioReviewPage() {
  const router = useRouter();

  const [isAnalyzing, setIsAnalyzing] = useState(false);
  const [feedback, setFeedback] = useState<string | null>(null);

  const portfolioUrl = "";

  const handleAnalyze = async () => {
    if (!portfolioUrl.trim()) {
      alert(
        "등록된 포트폴리오가 없습니다. 마이페이지에서 노션 포트폴리오 URL을 등록해주세요."
      );
      router.replace("/mypage/resume");
      return;
    }

    setIsAnalyzing(true);
    setFeedback(null);

    //여기에 fetch 추가
    setTimeout(() => {
      setFeedback(`
## 📊 포트폴리오 분석 결과

### ✅ 강점
- 프로젝트 설명이 구체적이고 구조가 명확합니다.
- 사용한 기술 스택이 잘 정리되어 있습니다.
- 문제 해결 과정이 논리적으로 작성되어 있습니다.

### 🛠️ 개선 제안
1. **프로젝트 성과 추가**
   - 각 프로젝트의 정량적 지표를 넣어보세요.  
     (예: 트래픽 증가율, 성능 개선률 등)
2. **기술적 깊이 강화**
   - 기술 선택 이유와 트레이드오프를 구체적으로 설명하면 좋습니다.
3. **시각적 보강**
   - 아키텍처 다이어그램이나 스크린샷을 포함해 시각적인 완성도를 높이세요.
4. **협업 경험 강조**
   - 팀 내 역할과 커뮤니케이션 과정을 더 구체적으로 작성해보세요.

### 💬 예상 면접 질문
1. 이 프로젝트에서 가장 어려웠던 점은 무엇이었나요?  
2. 해당 기술 스택을 선택한 이유는 무엇인가요?  
3. 프로젝트 성능을 어떻게 개선했나요?
      `);
      setIsAnalyzing(false);
      alert("✅ AI 포트폴리오 분석이 완료되었습니다!");
    }, 2500);
  };

  return (
    <>
      <div className="max-w-4xl mx-auto px-4 py-10">
        <div className="mb-10">
          <h1 className="text-3xl font-bold mb-2">포트폴리오 첨삭</h1>
          <p className="text-gray-500">
            AI가 당신의 포트폴리오를 분석하고 개선 방향을 제안합니다.
          </p>
        </div>

        <div className="bg-white rounded-lg shadow-md border border-gray-200 p-6 mb-8 text-center">
          <h2 className="text-2xl font-semibold mb-2">AI 포트폴리오 분석</h2>
          <p className="text-sm text-gray-500 mb-4">
            등록된 포트폴리오를 기반으로 AI가 자동으로 분석합니다.
          </p>

          <button
            onClick={handleAnalyze}
            disabled={isAnalyzing}
            className={`w-full py-3 rounded-md font-semibold transition ${
              isAnalyzing
                ? "bg-gray-300 text-gray-700 cursor-not-allowed"
                : "bg-blue-600 hover:bg-blue-700 text-white"
            }`}
          >
            {isAnalyzing ? "🔍 AI가 분석 중입니다..." : "✨ AI 첨삭 시작"}
          </button>
        </div>

        {isAnalyzing && (
          <div className="bg-gray-50 border border-gray-200 rounded-lg p-6 text-center">
            <p className="text-gray-600 text-lg animate-pulse">
              ⏳ 포트폴리오 분석 중입니다. 잠시만 기다려주세요...
            </p>
          </div>
        )}

        {feedback && !isAnalyzing && (
          <div className="bg-white rounded-lg border-2 border-blue-500 shadow-md p-6">
            <h2 className="text-2xl font-semibold mb-4 flex items-center gap-2">
              <span className="text-blue-600 text-2xl">✅</span> AI 첨삭 결과
            </h2>
            <p className="text-gray-500 mb-6">
              포트폴리오 분석 및 개선 제안 내용입니다.
            </p>

            <div className="bg-gray-50 border border-gray-200 rounded-md p-4 whitespace-pre-wrap leading-relaxed text-gray-800 text-sm">
              {feedback}
            </div>
          </div>
        )}

        {!feedback && !isAnalyzing && (
          <div className="bg-white rounded-lg shadow-sm border border-gray-200 p-6 mt-8">
            <h3 className="text-lg font-semibold mb-3">AI가 분석하는 항목</h3>
            <ul className="list-disc ml-6 text-gray-600 text-sm space-y-1 mb-6">
              <li>프로젝트 설명의 명확성 및 구체성</li>
              <li>기술 스택 선택 이유의 타당성</li>
              <li>문제 해결 과정의 논리성</li>
              <li>성과 및 결과물의 구체성</li>
              <li>협업 경험 및 소프트 스킬 표현</li>
            </ul>

            <h3 className="text-lg font-semibold mb-3">
              포트폴리오 준비 가이드
            </h3>
            <ul className="list-disc ml-6 text-gray-600 text-sm space-y-1">
              <li>노션 페이지를 공개 설정으로 변경하세요.</li>
              <li>프로젝트별로 명확한 제목과 설명을 작성하세요.</li>
            </ul>
          </div>
        )}
      </div>
    </>
  );
}
