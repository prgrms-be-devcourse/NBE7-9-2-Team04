"use client";

import { useEffect, useState } from "react";
import { useRouter } from "next/navigation";

export default function NewFeedbackPage() {
  const router = useRouter();
  const [progress, setProgress] = useState(0);

  // ✅ 임시 피드백 ID
  const mockNewFeedbackId = 3;

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

    // 3초 후 결과 페이지 이동
    const timer = setTimeout(() => {
      alert("✅ AI 포트폴리오 분석이 완료되었습니다!");
      router.replace(`/portfolio_review/${mockNewFeedbackId}`);
    }, 3000);

    return () => {
      clearInterval(interval);
      clearTimeout(timer);
    };
  }, [router]);

  return (
    <div className="max-w-4xl mx-auto px-6 py-20 text-center">
      <h1 className="text-3xl font-bold mb-4">AI 포트폴리오 분석 중...</h1>
      <p className="text-gray-600 mb-10">
        AI가 당신의 포트폴리오를 정밀 분석하고 있습니다. 잠시만 기다려주세요.
      </p>

      {/* 진행 상태 */}
      <div className="w-full max-w-md mx-auto bg-gray-200 rounded-full h-3 mb-6">
        <div
          className="bg-blue-600 h-3 rounded-full transition-all duration-300"
          style={{ width: `${progress}%` }}
        />
      </div>

      <p className="text-gray-500 mb-16">{progress}% 완료</p>

      <div className="flex flex-col gap-2 items-center">
        <div className="text-sm text-gray-500 animate-pulse">
          📄 프로젝트 내용을 분석 중...
        </div>
        <div className="text-sm text-gray-500 animate-pulse delay-75">
          🧠 기술 스택과 문제 해결 과정을 평가 중...
        </div>
        <div className="text-sm text-gray-500 animate-pulse delay-150">
          💬 개선 포인트를 정리 중...
        </div>
      </div>

      <div className="mt-10 flex justify-center">
        <div className="animate-spin rounded-full h-10 w-10 border-4 border-blue-500 border-t-transparent" />
      </div>

      <div className="mt-10">
        <button
          onClick={() => router.push("/portfolio_review")}
          className="text-gray-600 text-sm underline hover:text-gray-800"
        >
          분석 취소하고 목록으로 돌아가기
        </button>
      </div>
    </div>
  );
}
