"use client";

import { useEffect, useState } from "react";
import { useParams, useRouter } from "next/navigation";

// 임시 첨삭 결과 데이터
const mockFeedbacks = [
  {
    id: 1,
    createdAt: "2025-10-18T14:23:00",
    content: `
## 📊 포트폴리오 분석 결과

### ✅ 강점
- 프로젝트 구조가 명확하고 기술 스택이 일관성 있게 구성되어 있습니다.
- 성과와 지표가 구체적으로 제시되어 있어 신뢰도가 높습니다.
- 협업 경험을 잘 드러내는 커뮤니케이션 포인트가 있습니다.

### 🛠️ 개선 제안
1. **기술적 깊이 강화**
   - 선택한 기술의 트레이드오프를 간략히 언급해보세요.
2. **시각적 요소 추가**
   - 아키텍처 다이어그램이나 UI 캡처 이미지를 추가하면 좋습니다.
3. **성과 강조**
   - 개선 전/후의 수치를 구체적으로 작성하면 설득력이 올라갑니다.

### 💬 예상 면접 질문
1. 이 프로젝트에서 가장 큰 기술적 어려움은 무엇이었나요?  
2. 선택한 기술 스택의 장단점은 무엇이라고 생각하나요?
3. 협업 중 발생한 문제를 어떻게 해결했나요?
    `,
  },
];

export default function FeedbackDetailPage() {
  const router = useRouter();
  const { id } = useParams();
  const [feedback, setFeedback] = useState<any | null>(null);
  const [isLoading, setIsLoading] = useState(true);

  useEffect(() => {
    const timer = setTimeout(() => {
      const found = mockFeedbacks.find((f) => f.id === Number(id));
      setFeedback(found || null);
      setIsLoading(false);
    }, 800);
    return () => clearTimeout(timer);
  }, [id]);

  const formatDate = (dateStr: string) => {
    const date = new Date(dateStr);
    return date.toLocaleString("ko-KR", {
      year: "numeric",
      month: "2-digit",
      day: "2-digit",
      hour: "2-digit",
      minute: "2-digit",
    });
  };

  if (isLoading) {
    return (
      <div className="max-w-4xl mx-auto px-6 py-20 text-center text-gray-500">
        ⏳ 첨삭 결과를 불러오는 중입니다...
      </div>
    );
  }

  if (!feedback) {
    return (
      <div className="max-w-4xl mx-auto px-6 py-20 text-center">
        <p className="text-gray-600 mb-6">존재하지 않는 첨삭 결과입니다.</p>
        <button
          onClick={() => router.push("/portfolio_review")}
          className="bg-blue-600 hover:bg-blue-700 text-white px-5 py-2 rounded-md font-semibold text-sm transition"
        >
          목록으로 돌아가기
        </button>
      </div>
    );
  }

  return (
    <div className="max-w-4xl mx-auto px-6 py-10">

      <div className="flex justify-between items-center mb-8">
        <div>
          <h1 className="text-2xl font-bold mb-1">AI 첨삭 결과</h1>
          <p className="text-gray-500 text-sm">
            생성일: {formatDate(feedback.createdAt)}
          </p>
        </div>

        <button
          onClick={() => router.push("/portfolio_review")}
          className="bg-gray-200 hover:bg-gray-300 text-gray-800 px-4 py-2 rounded-md text-sm font-medium transition"
        >
          목록으로 돌아가기
        </button>
      </div>

      <div className="bg-white border border-gray-200 rounded-lg shadow-sm p-6">
        <div
          className="prose prose-gray max-w-none whitespace-pre-wrap leading-relaxed"
          dangerouslySetInnerHTML={{ __html: feedback.content }}
        />
      </div>
    </div>
  );
}
