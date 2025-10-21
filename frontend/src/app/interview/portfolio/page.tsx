"use client";

import { useState } from "react";
import { useRouter } from "next/navigation";

// 임시 데이터
const mockPortfolios = [
  {
    id: "p1",
    githubUrl: "https://github.com/user/ecommerce-app",
    analyzedAt: "2025-10-10",
    questionsCount: 8,
  },
  {
    id: "p2",
    githubUrl: "https://github.com/user/chat-app",
    analyzedAt: "2025-10-05",
    questionsCount: 5,
  },
];

export default function PortfolioQuestionPage() {
  const router = useRouter();
  const [portfolios] = useState(mockPortfolios);

  const handleAddPortfolioQuestion = () => {
    router.replace("/mypage/resume");
  };

  return (
    <>
      <div className="max-w-6xl mx-auto px-4 py-10">
        <div className="mb-10">
          <h1 className="text-3xl font-bold mb-2">포트폴리오 면접 질문</h1>
          <p className="text-gray-500">
            AI가 포트폴리오를 분석하고 예상 면접 질문을 생성해드립니다.
          </p>
        </div>

        <div className="flex items-center justify-between mb-10">
          <h2 className="text-2xl font-semibold">분석된 포트폴리오</h2>
          <button
            onClick={handleAddPortfolioQuestion}
            className="flex items-center gap-2 bg-blue-500 text-white px-4 py-2 rounded-md  hover:bg-blue-600 transition"
          >
            새로운 면접 시작
          </button>
        </div>

        {portfolios.length === 0 ? (
          <div className="bg-white border border-gray-200 rounded-lg shadow-sm text-center py-16">
            <p className="text-gray-500 mb-2">
              아직 분석된 포트폴리오가 없습니다.
            </p>
            <p className="text-sm text-gray-400">
              상단의 <b>‘포트폴리오 질문 추가’</b> 버튼을 눌러 포트폴리오를
              등록하세요.
            </p>
          </div>
        ) : (
          <div className="grid md:grid-cols gap-5">
            {portfolios.map((portfolio) => (
              <div
                key={portfolio.id}
                className="bg-white border border-gray-200 rounded-lg shadow-sm hover:shadow-md transition p-5"
              >
                <div className="flex items-start justify-between mb-4">
                  <div className="flex-1">
                    <h3 className="text-lg font-semibold">
                      {portfolio.githubUrl}
                    </h3>
                  </div>
                  <span className="text-sm bg-gray-100 border border-gray-200 px-2 py-1 rounded-md text-gray-700">
                    {portfolio.questionsCount}개 질문
                  </span>
                </div>

                <div className="flex items-center justify-between">
                  <span className="text-sm text-gray-500">
                    분석일: {portfolio.analyzedAt}
                  </span>
                  <button
                    onClick={() =>
                      router.replace(`/interview/portfolio/${portfolio.id}`)
                    }
                    className="text-blue-600 border border-blue-500 px-3 py-1.5 rounded-md hover:bg-blue-50 transition text-sm font-medium"
                  >
                    면접 시작
                  </button>
                </div>
              </div>
            ))}
          </div>
        )}
      </div>
    </>
  );
}
