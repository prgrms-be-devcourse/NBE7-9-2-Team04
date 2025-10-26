"use client";

import { useRouter } from "next/navigation";
import { useState, useEffect } from "react";
import { fetchApi } from "@/lib/client"; // API 호출을 위한 헬퍼 함수 임포트

export default function PortfolioReviewMainPage() {
  const router = useRouter();
  const [feedbacks, setFeedbacks] = useState<any[]>([]);
  const [isLoading, setIsLoading] = useState(true);

  useEffect(() => {
    const fetchFeedbacks = async () => {
      setIsLoading(true);
      try {
        const response = await fetchApi("/api/v1/portfolio-review/reviews");
        setFeedbacks(response.data);
      } catch (error) {
        console.error("Failed to fetch feedbacks:", error);
        alert("피드백 목록을 불러오는 데 실패했습니다.");
      } finally {
        setIsLoading(false);
      }
    };

    fetchFeedbacks();
  }, []);

  const handleStartAnalyze = () => {
    router.push("/portfolio_review/new");
  };

  const formatDate = (dateStr: string) => {
    const date = new Date(dateStr);
    return date.toLocaleDateString("ko-KR", {
      year: "numeric",
      month: "2-digit",
      day: "2-digit",
    });
  };

  return (
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
          onClick={handleStartAnalyze}
          className="w-full py-3 rounded-md font-semibold bg-blue-600 hover:bg-blue-700 text-white transition"
        >
          ✨ AI 첨삭 시작
        </button>
      </div>

      {/* 피드백 목록 */}
      <div className="bg-white rounded-lg shadow-sm border border-gray-200 p-6">
        <h3 className="text-lg font-semibold mb-4">이전 첨삭 내역</h3>

        {isLoading ? (
          <p className="text-gray-500 text-center py-6 animate-pulse">
            불러오는 중...
          </p>
        ) : feedbacks.length === 0 ? (
          <p className="text-gray-500 text-center py-6">
            아직 받은 첨삭 내역이 없습니다.
          </p>
        ) : (
          <ul className="divide-y divide-gray-200">
            {feedbacks.map((f) => (
              <li
                key={f.reviewId} // 수정: reviewId 사용
                className="py-4 cursor-pointer hover:bg-gray-50 transition px-2 rounded-md"
                onClick={() => router.push(`/portfolio_review/${f.reviewId}`)} // 수정: reviewId 사용
              >
                <div className="flex justify-between items-center">
                  <span className="font-medium text-gray-800">{f.summary}</span>
                  <span className="text-sm text-gray-500">
                    {formatDate(f.createdAt)}
                  </span>
                </div>
              </li>
            ))}
          </ul>
        )}
      </div>

      {/* 안내문 */}
      <div className="bg-white rounded-lg shadow-sm border border-gray-200 p-6 mt-8">
        <h3 className="text-lg font-semibold mb-3">AI가 분석하는 항목</h3>
        <ul className="list-disc ml-6 text-gray-600 text-sm space-y-1 mb-6">
          <li>프로젝트 설명의 명확성 및 구체성</li>
          <li>기술 스택 선택 이유의 타당성</li>
          <li>문제 해결 과정의 논리성</li>
          <li>성과 및 결과물의 구체성</li>
          <li>협업 경험 및 소프트 스킬 표현</li>
        </ul>

        <h3 className="text-lg font-semibold mb-3">포트폴리오 준비 가이드</h3>
        <ul className="list-disc ml-6 text-gray-600 text-sm space-y-1">
          <li>노션 페이지를 공개 설정으로 변경하세요.</li>
          <li>프로젝트별로 명확한 제목과 설명을 작성하세요.</li>
        </ul>
      </div>
    </div>
  );
}
