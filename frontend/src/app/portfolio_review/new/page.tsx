"use client";

import { useEffect, useState } from "react";
import { useRouter } from "next/navigation";
import { fetchApi } from "@/lib/client";
import { marked } from "marked";

export default function NewFeedbackPage() {
  const router = useRouter();

  // 상태 정의
  const [progress, setProgress] = useState(0);
  const [feedbackContent, setFeedbackContent] = useState("");
  const [isAnalysisComplete, setIsAnalysisComplete] = useState(false);
  const [createDate, setCreateDate] = useState("");
  const [isLoading, setIsLoading] = useState(false);
  const [requested, setRequested] = useState(false); // Strict Mode 대응
  const [isPremium, setIsPremium] = useState(false); // 프리미엄 여부 상태 추가

  // 진행 상태 애니메이션
  useEffect(() => {
    const interval = setInterval(() => {
      setProgress((prev) => {
        if (prev >= 100) {
          clearInterval(interval);
          return 100;
        }
        return prev + 10;
      });
    }, 300);

    return () => clearInterval(interval);
  }, []);

  // 피드백 생성 함수
  const createFeedback = async () => {
    if (!isPremium) {
      alert("포트폴리오 첨삭은 PREMIUM 등급 사용자만 이용 가능합니다.");
      router.push("/mypage/premium");
      return;
    }

    if (isLoading) return; // 중복 방지
    setIsLoading(true);

    try {
      const response = await fetchApi("/api/v1/portfolio-review", {
        method: "POST",
      });

      console.log("생성된 리뷰 데이터:", response.data);
      const { feedbackContent, createDate } = response.data;

      const parsedContent = await marked.parse(feedbackContent);
      setFeedbackContent(parsedContent);
      setCreateDate(createDate);
      setIsAnalysisComplete(true);
      alert("✅ AI 포트폴리오 분석이 완료되었습니다!");
    } catch (error) {
      console.error("리뷰 생성 실패:", error);
      setIsAnalysisComplete(false);
      setFeedbackContent("");
      alert("❌ AI 포트폴리오 분석 생성에 실패했습니다.");
    } finally {
      setIsLoading(false);
    }
  };

  // 마운트 시 한 번만 호출
  useEffect(() => {
    if (!requested) {
      setRequested(true);
      createFeedback();
    }
  }, [requested]);

  return (
    <div className="max-w-4xl mx-auto px-6 py-20 relative">
      {/* 뒤로가기 버튼 */}
      {isAnalysisComplete && (
        <div className="absolute top-4 left-4">
          <a
            href="/portfolio_review"
            className="text-blue-500 hover:underline flex items-center gap-2"
          >
            <svg
              xmlns="http://www.w3.org/2000/svg"
              fill="none"
              viewBox="0 0 24 24"
              strokeWidth="1.5"
              stroke="currentColor"
              className="w-5 h-5"
            >
              <path
                strokeLinecap="round"
                strokeLinejoin="round"
                d="M15.75 19.5L8.25 12l7.5-7.5"
              />
            </svg>
            첨삭 목록으로
          </a>
        </div>
      )}

      {/* 분석 중 화면 */}
      {!isAnalysisComplete && (
        <div className="text-center">
          <h1 className="text-3xl font-bold mb-4">AI 포트폴리오 분석 중...</h1>
          <p className="text-gray-600 mb-10">
            AI가 당신의 포트폴리오를 정밀 분석하고 있습니다. 잠시만 기다려주세요.
          </p>

          {/* 진행 바 */}
          <div className="w-full max-w-md mx-auto bg-gray-200 rounded-full h-3 mb-6">
            <div
              className="bg-blue-600 h-3 rounded-full transition-all duration-300"
              style={{ width: `${progress}%` }}
            />
          </div>

          <p className="text-gray-500 mb-16">{progress}% 완료</p>

          <div className="flex flex-col gap-2 items-center">
            <div className="animate-spin rounded-full h-10 w-10 border-4 border-blue-500 border-t-transparent" />
          </div>
        </div>
      )}

      {/* 분석 결과 화면 */}
      {isAnalysisComplete && feedbackContent && (
        <div className="mt-10">
          <h2 className="text-3xl font-bold mb-6 text-center">AI 첨삭 결과</h2>
          <div className="bg-white shadow-md rounded-lg p-6">
            <h3 className="text-2xl font-semibold mb-4">포트폴리오 분석 결과</h3>
            <p className="text-sm text-gray-500 mb-4">
              생성일: {new Date(createDate).toLocaleString()}
            </p>
            <div
              className="prose prose-blue max-w-none"
              dangerouslySetInnerHTML={{ __html: feedbackContent }}
            />
          </div>
        </div>
      )}
    </div>
  );
}
