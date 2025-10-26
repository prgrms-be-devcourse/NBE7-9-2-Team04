"use client";

import { useEffect, useState } from "react";
import { useRouter } from "next/navigation";
import { fetchApi } from "@/lib/client"; // Adjust the import based on your project structure

export default function NewFeedbackPage() {
  const router = useRouter();
  const [progress, setProgress] = useState(0);
  const [isPremium, setIsPremium] = useState(false);

  useEffect(() => {
    // Check if the user is a premium member
    const checkPremiumStatus = async () => {
      try {
        const response = await fetchApi("/api/v1/user"); // Adjust endpoint as needed
        setIsPremium(response.data.isPremium);
      } catch (error) {
        console.error("Failed to fetch user data:", error);
        alert("사용자 정보를 불러오는 데 실패했습니다.");
        router.replace("/portfolio_review");
      }
    };

    checkPremiumStatus();
  }, [router]);

  useEffect(() => {
    if (!isPremium) return; // Prevent non-premium users from proceeding

    const interval = setInterval(() => {
      setProgress((prev) => {
        if (prev >= 100) {
          clearInterval(interval);
          return 100;
        }
        return prev + 10;
      });
    }, 300);

    return () => {
      clearInterval(interval);
    };
  }, [isPremium]);

  useEffect(() => {
    if (!isPremium) return; // Prevent non-premium users from proceeding

    const createFeedback = async () => {
      try {
        const response = await fetchApi("/api/v1/portfolio-review", {
          method: "POST",
        });
        const reviewId = response.data.reviewId; // API 응답에서 reviewId 추출
        alert("✅ AI 포트폴리오 분석이 완료되었습니다!");
        router.replace(`/portfolio_review/${reviewId}`); // reviewId를 사용해 결과 페이지로 이동
      } catch (error) {
        if (error.response?.data?.code === "AI_FEEDBACK_FOR_PREMIUM_ONLY") {
          alert("프리미엄 사용자만 AI 분석을 사용할 수 있습니다.");
        } else {
          alert("AI 포트폴리오 분석 생성에 실패했습니다.");
        }
        console.error("Failed to create feedback:", error);
        router.replace("/portfolio_review");
      }
    };

    createFeedback();
  }, [router, isPremium]);

  if (!isPremium) {
    return (
      <div className="max-w-4xl mx-auto px-6 py-20 text-center">
        <h1 className="text-3xl font-bold mb-4">프리미엄 전용 기능</h1>
        <p className="text-gray-600 mb-10">
          이 기능은 프리미엄 사용자만 사용할 수 있습니다. 프리미엄으로 업그레이드하세요.
        </p>
        <button
          onClick={() => router.push("/upgrade")}
          className="bg-blue-600 hover:bg-blue-700 text-white px-5 py-2 rounded-md font-semibold text-sm transition"
        >
          프리미엄 업그레이드
        </button>
      </div>
    );
  }

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
