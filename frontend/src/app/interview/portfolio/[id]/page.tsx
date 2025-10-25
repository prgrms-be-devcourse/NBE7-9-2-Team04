"use client";

import { useState, useEffect } from "react";
import { useRouter, useParams } from "next/navigation";
import { fetchApi } from "@/lib/client";

export default function PortfolioQuestionDetailPage() {
  const router = useRouter();
  const params = useParams();
  const groupId = params?.id;

  const [questions, setQuestions] = useState<{ id: string; content: string }[]>([]);
  const [title, setTitle] = useState("");
  const [currentIndex, setCurrentIndex] = useState(0);
  const [answers, setAnswers] = useState<Record<string, string>>({});
  const [feedback, setFeedback] = useState<Record<string, { score: number; comment: string }>>({});
  const [input, setInput] = useState("");
  const [loading, setLoading] = useState(true);
  const [showFeedback, setShowFeedback] = useState(false);
  const [alertMsg, setAlertMsg] = useState<string | null>(null);

  useEffect(() => {
    const fetchQuestions = async () => {
      if (!groupId) return;
      setLoading(true);
      try {
        const res = await fetchApi(`/api/v1/ai/questions/${groupId}`, { method: "GET" });
        if (res?.data) {
          const mapped = res.data.questions.map((q: any) => ({
            id: q.id,
            content: q.content, // 서버가 보내는 content 사용
          }));
          setQuestions(mapped);
          setTitle(res.data.title);
        }
      } catch (err) {
        console.error("질문 조회 실패:", err);
      } finally {
        setLoading(false);
      }
    };
    fetchQuestions();
  }, [groupId]);

  const current = questions[currentIndex];
  const progress = ((currentIndex + 1) / questions.length) * 100;
  const isLast = currentIndex === questions.length - 1 && showFeedback;

  const handleSubmit = () => {
    if (!input.trim()) {
      setAlertMsg("답변을 입력해주세요!");
      setTimeout(() => setAlertMsg(null), 2000);
      return;
    }

    setLoading(true);
    setTimeout(() => {
      const score = Math.floor(Math.random() * 31) + 70;
      const comment =
        score > 90
          ? "훌륭한 답변입니다! 실무적인 이해도가 잘 드러나네요."
          : score > 80
          ? "좋은 답변이에요. 조금 더 구체적인 예시를 들어주면 완벽할 것 같습니다."
          : "조금 더 구체적인 기술적 근거를 추가해보세요.";

      setAnswers(prev => ({ ...prev, [current.id]: input }));
      setFeedback(prev => ({ ...prev, [current.id]: { score, comment } }));

      setLoading(false);
      setShowFeedback(true);
      setTimeout(() => setAlertMsg(null), 2000);
    }, 1000);
  };

  const handleNext = () => {
    if (currentIndex < questions.length - 1) {
      setCurrentIndex(prev => prev + 1);
      setInput("");
      setShowFeedback(false);
    }
  };

  if (loading) return <p className="text-center mt-10">로딩 중...</p>;
  if (!questions.length) return <p className="text-center mt-10">질문이 없습니다.</p>;

  return (
    <div className="max-w-3xl mx-auto px-6 py-10">
      {/* 제목 및 진행상황 */}
      <div className="flex items-center justify-between mb-6">
        <button
          onClick={() => router.replace("/interview/portfolio")}
          className="text-gray-600 hover:text-gray-800"
        >
          ← 목록으로
        </button>
        <span className="px-3 py-1 text-sm bg-blue-100 rounded-md text-gray-600">
          {currentIndex + 1} / {questions.length}
        </span>
      </div>

      <h2 className="text-2xl font-bold mb-4">{title}</h2>
      <div className="w-full bg-gray-200 rounded-full h-2 mb-6">
        <div
          className="bg-blue-600 h-2 rounded-full transition-all duration-500"
          style={{ width: `${progress}%` }}
        />
      </div>

      {/* 질문 카드 */}
      <div className="border border-blue-200 bg-blue-50 rounded-lg p-5 mb-6">
        <div className="flex items-start gap-3">
          <div className="text-blue-600 text-lg mt-1">✨</div>
          <div>
            <h3 className="text-lg font-semibold mb-1">{current.content}</h3>
            <p className="text-sm text-gray-500">AI가 생성한 예상 면접 질문입니다.</p>
          </div>
        </div>
      </div>

      {/* 답변 입력 or 피드백 */}
      {!showFeedback ? (
        <div className="space-y-4">
          <textarea
            className="w-full border rounded-md p-3 focus:outline-none focus:ring-2 focus:ring-blue-500 resize-none"
            rows={6}
            placeholder="이 질문에 대한 답변을 작성하세요"
            value={input}
            onChange={(e) => setInput(e.target.value)}
            disabled={loading}
          />
          <div className="flex justify-end">
            <button
              onClick={handleSubmit}
              disabled={loading}
              className="bg-blue-600 text-white px-5 py-2 rounded-md hover:bg-blue-700 disabled:opacity-60"
            >
              {loading ? "AI 분석 중..." : "답변 제출"}
            </button>
          </div>
        </div>
      ) : (
        <div className="space-y-6">
          {/* 내 답변 */}
          <div>
            <h4 className="font-semibold mb-2">내 답변</h4>
            <div className="p-4 bg-gray-50 border rounded-md text-gray-800 whitespace-pre-line">
              {answers[current.id]}
            </div>
          </div>

          {/* AI 피드백 */}
          <div className="p-4 border border-blue-200 bg-blue-50 rounded-md">
            <div className="flex items-center justify-between mb-2">
              <h4 className="font-semibold text-blue-700">💡 AI 피드백</h4>
              <span className="px-2 py-1 bg-blue-500 text-white text-sm rounded-md">
                {feedback[current.id]?.score}점
              </span>
            </div>
            <p className="text-gray-700 text-sm">{feedback[current.id]?.comment}</p>
          </div>

          {/* 다음 질문 or 완료 */}
          <div className="flex justify-end">
            {isLast ? (
              <button
                onClick={() => router.replace("/interview/portfolio")}
                className="bg-green-500 text-white px-5 py-2 rounded-md hover:bg-green-600"
              >
                면접 완료
              </button>
            ) : (
              <button
                onClick={handleNext}
                className="bg-blue-500 text-white px-5 py-2 rounded-md hover:bg-blue-600"
              >
                다음 질문
              </button>
            )}
          </div>
        </div>
      )}

      {/* 경고 메시지 */}
      {alertMsg && (
        <div className="mt-4 text-red-500 font-medium text-center">{alertMsg}</div>
      )}
    </div>
  );
}
