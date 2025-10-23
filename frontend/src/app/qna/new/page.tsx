"use client";

import { useEffect, useState } from "react";
import { useRouter } from "next/navigation";
import { fetchApi } from "@/lib/client";

export default function NewQnAPage() {
  const router = useRouter();
  const [formData, setFormData] = useState({
    title: "",
    category: "",
    content: "",
  });

  //로그인 확인 상태
  const [isCheckingLogin, setIsCheckingLogin] = useState(true);
  const [isLoading, setIsLoading] = useState(false);

  const categoryMap: Record<string, string> = {
    계정: "ACCOUNT",
    결제: "PAYMENT",
    시스템: "SYSTEM",
    모집: "RECRUITMENT",
    제안: "SUGGESTION",
    기타: "OTHER",
  };

  //로그인 여부 확인
  useEffect(() => {
    const checkLogin = async () => {
      try {
        const res = await fetchApi("/api/v1/users/check", { method: "GET" });

        if (res.status !== "OK") {
          // 로그인 안 되어 있으면 바로 이동
          router.replace("/auth?returnUrl=/qna/new");
          return;
        }
        setIsCheckingLogin(false);
      } catch {
        router.replace("/auth?returnUrl=/qna/new");
      }
    };

    checkLogin();
  }, [router]);

  // 로그인 확인 중이면 로딩 화면만 표시
  if (isCheckingLogin) {
    return (
      <div className="fixed inset-0 flex items-center justify-center bg-gray-50">
        <div className="animate-pulse text-gray-400 text-sm">
          로그인 상태 확인 중...
        </div>
      </div>
    );
  }

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();

    if (!formData.title || !formData.category || !formData.content) {
      alert("모든 필드를 입력해주세요.");
      return;
    }

    const payload = {
      title: formData.title,
      content: formData.content,
      categoryType: categoryMap[formData.category],
    };

    try {
      setIsLoading(true);
      const apiResponse = await fetchApi(`/api/v1/qna`, {
        method: "POST",
        body: JSON.stringify(payload),
      });

      if (apiResponse.status === "CREATED" || apiResponse.status === "OK") {
        router.push("/qna");
      } else {
        alert(apiResponse.message || "등록에 실패했습니다.");
      }
    } catch (error) {
      alert("서버 오류가 발생했습니다.");
    } finally {
      setIsLoading(false);
    }
  };

  return (
    <div className="max-w-4xl mx-auto px-4 py-10 space-y-8">
      <button
        onClick={() => router.push("/qna")}
        className="text-sm text-gray-500 flex items-center gap-1 hover:text-blue-600"
      >
        ← 목록으로
      </button>

      <div className="bg-white rounded-lg shadow p-8">
        <h1 className="text-3xl font-bold mb-2">질문하기</h1>
        <p className="text-gray-500 mb-6">궁금한 점을 자유롭게 질문해주세요.</p>

        <form onSubmit={handleSubmit} className="space-y-6">
          {/* 제목 */}
          <div>
            <label htmlFor="title" className="block text-sm font-semibold mb-2">
              제목
            </label>
            <input
              id="title"
              type="text"
              placeholder="질문 제목을 입력하세요"
              value={formData.title}
              onChange={(e) => setFormData({ ...formData, title: e.target.value })}
              className="w-full border rounded-lg px-4 py-2 focus:ring-2 focus:ring-blue-200"
              required
            />
          </div>

          {/* 카테고리 */}
          <div>
            <label htmlFor="category" className="block text-sm font-semibold mb-2">
              카테고리
            </label>
            <select
              id="category"
              value={formData.category}
              onChange={(e) => setFormData({ ...formData, category: e.target.value })}
              className="w-full border rounded-lg px-4 py-2 bg-white focus:ring-2 focus:ring-blue-200"
              required
            >
              <option value="">카테고리 선택</option>
              <option value="계정">계정</option>
              <option value="결제">결제</option>
              <option value="시스템">시스템</option>
              <option value="모집">모집</option>
              <option value="제안">제안</option>
              <option value="기타">기타</option>
            </select>
          </div>

          {/* 내용 */}
          <div>
            <label htmlFor="content" className="block text-sm font-semibold mb-2">
              내용
            </label>
            <textarea
              id="content"
              placeholder="질문 내용을 자세히 작성해주세요"
              value={formData.content}
              onChange={(e) => setFormData({ ...formData, content: e.target.value })}
              rows={10}
              className="w-full border rounded-lg px-4 py-3 focus:ring-2 focus:ring-blue-200"
              required
            />
          </div>

          {/* 버튼 */}
          <div className="flex justify-end gap-3">
            <button
              type="button"
              onClick={() => router.push("/qna")}
              className="px-4 py-2 border rounded-md hover:bg-gray-50"
            >
              취소
            </button>
            <button
              type="submit"
              disabled={isLoading}
              className={`px-4 py-2 rounded-md text-white transition-colors ${
                isLoading
                  ? "bg-gray-400 cursor-not-allowed"
                  : "bg-blue-600 hover:bg-blue-700"
              }`}
            >
              {isLoading ? "등록 중..." : "질문 등록"}
            </button>
          </div>
        </form>
      </div>
    </div>
  );
}