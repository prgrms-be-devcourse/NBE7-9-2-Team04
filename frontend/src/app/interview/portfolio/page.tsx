"use client";

import { useState,useEffect } from "react";
import { useRouter } from "next/navigation";
import { fetchApi } from "@/lib/client"; 
import {
  AiQuestionReadResponse,
  AiQuestionReadAllResponse,
} from "@/types/aiquestion";


export default function AiQuestionPage() {
  const [questions, setQuestions] = useState<AiQuestionReadAllResponse | null>(null);
  const [loading, setLoading] = useState<boolean>(true);
  const [error, setError] = useState<string | null>(null);
  const router = useRouter();
  const fetchQuestions = async () => {
    setLoading(true);
    try {
      const res = await fetchApi("/api/v1/ai/questions", { method: "GET" });

      console.log("AI 질문 응답 데이터:", res.data);

      if (!res || !res.data) {
        setQuestions(null);
        setLoading(false);
        return;
      }

      setQuestions(res.data); // AiQuestionReadAllResponse 타입
    } catch (err: any) {
      console.error("AI 질문 조회 실패:", err);
      setError(err.message || "알 수 없는 오류");
    } finally {
      setLoading(false);
    }
  };

  const createQuestions = async () => {
    try {
      const res = await fetchApi("/api/v1/ai/questions", {
        method: "POST"
      });
      alert("질문이 생성되었습니다.");

    } catch (error) {
      handleAddAiQuestion();
    }
  };

  
  const handleSave = () => {
    createQuestions();
  };

  const handleAddAiQuestion = () => {
    router.replace("/mypage/resume");
  };
  useEffect(() => {
    fetchQuestions();
  }, []);
  return (
    <div className="max-w-6xl mx-auto px-4 py-10">
      {/* 페이지 타이틀 */}
      <div className="mb-10">
        <h1 className="text-3xl font-bold mb-2">포트폴리오 면접 질문</h1>
        <p className="text-gray-500">
          AI가 포트폴리오를 분석하고 예상 면접 질문을 생성해드립니다.
        </p>
      </div>

      {/* 상단 섹션 */}
      <div className="flex items-center justify-between mb-10">
        <h2 className="text-2xl font-semibold">분석된 포트폴리오</h2>
        <button
          onClick={handleSave}
          className="flex items-center gap-2 bg-blue-500 text-white px-4 py-2 rounded-md hover:bg-blue-600 transition"
        >
          새로운 면접 시작
        </button>
      </div>

      {/* 카드 목록 or 빈 상태 */}
      {!questions || questions.questions.length === 0 ? (
        <div className="bg-white border border-gray-200 rounded-lg shadow-sm text-center py-16">
          <p className="text-gray-500 mb-2">아직 분석된 포트폴리오가 없습니다.</p>
          <p className="text-sm text-gray-400">
            상단의 <b>‘새로운 면접 시작’</b> 버튼을 눌러 포트폴리오를 등록하세요.
          </p>
        </div>
      ) : (
        <div className="grid md:grid-cols-2 lg:grid-cols-3 gap-6">
          {questions.questions.map((q) => (
            <div
              key={q.groupId}
              className="bg-white border border-gray-200 rounded-lg shadow-sm hover:shadow-md transition p-5"
            >
              <div className="flex items-start justify-between mb-4">
                <div className="flex-1">
                  <h3 className="text-lg font-semibold truncate w-full">{q.title.length > 20 ? `${q.title.slice(0, 25)}...` : q.title}</h3>
                </div>
                <span className="text-sm bg-gray-100 border border-gray-200 px-2 py-1 rounded-md text-gray-700">
                  {q.count}문제
                </span>
              </div>

              <div className="flex items-center justify-between">
                <span className="text-sm text-gray-500">분석일: {q.date}</span>
                <button
                  onClick={() =>
                    router.replace(`/interview/portfolio/${q.groupId}`)
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
  );
}
