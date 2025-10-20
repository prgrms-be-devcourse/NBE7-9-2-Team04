"use client";

import { useState, useEffect } from "react";
import { useRouter } from "next/navigation";

export default function MyPremiumPage() {
  const router = useRouter();
  const [isLoading, setIsLoading] = useState(true);
  const [isPremium, setIsPremium] = useState(false);

  useEffect(() => {
    // 로그인 시뮬레이션 (로컬스토리지 대신 임시값 사용)
    const timer = setTimeout(() => setIsLoading(false), 200);
    return () => clearTimeout(timer);
  }, []);

  // 결제 처리
  const handleSubscribe = async () => {
    try {

        router.push("/mypage/premium/checkout");
      } catch (error) {
        console.error(error);
        alert("결제 처리 중 오류가 발생했습니다.");
      }
  };

  // 구독 취소 처리
  const handleCancel = async () => {
    const confirmCancel = confirm("정말 구독을 취소하시겠습니까?");
    if (!confirmCancel) return;

    try {
      // await fetch("/api/payment", { method: "DELETE" });
      setIsPremium(false);
      alert("구독이 취소되었습니다.");
    } catch (error) {
      console.error(error);
      alert("구독 취소 중 오류가 발생했습니다.");
    }
  };

  if (isLoading) {
    return (
      <div className="flex items-center justify-center min-h-[60vh] text-gray-500">
        로딩 중...
      </div>
    );
  }

  return (
    <>
    <div className="bg-white p-8 rounded-lg shadow-md border border-gray-200 max-w-3xl mx-auto">
      <h2 className="text-2xl font-bold mb-2 flex items-center gap-2">
        💳 유료 서비스 관리
      </h2>
      <p className="text-gray-500 mb-8">프리미엄 멤버십을 관리하세요.</p>

      {isPremium ? (
        <div className="space-y-6">
          <div className="p-6 bg-blue-50 border border-blue-200 rounded-lg">
            <p className="font-semibold text-lg mb-2 text-blue-800">
              ✅ 프리미엄 멤버십 활성화 중
            </p>
            <p className="text-sm text-gray-600">다음 결제일: 2025-11-15</p>
          </div>

          <button
            onClick={handleCancel}
            className="w-full border border-gray-300 text-gray-700 font-medium py-2 rounded-md hover:bg-gray-100"
          >
            구독 취소
          </button>
        </div>
      ) : (
        <div className="space-y-8">

          <div className="p-8 border-blue-500 rounded-lg bg-blue-50">
            <div className="mb-6">
              <h3 className="text-2xl font-bold mb-2">
                프리미엄 멤버십
              </h3>
              <p className="text-4xl font-bold text-blue-600">
                9,900원{" "}
                <span className="text-lg font-normal text-gray-500">/ 월</span>
              </p>
            </div>


            <div>
              <h4 className="font-semibold mb-3 text-gray-800">혜택</h4>
              <ul className="space-y-2 text-gray-700">
                <li className="flex items-center gap-2">
                  ✅ 모집 게시글 상단 고정 기능
                </li>
                <li className="flex items-center gap-2">
                  ✅ 포트폴리오 면접 질문 횟수 증가
                </li>
                <li className="flex items-center gap-2">
                  ✅ 포트폴리오 첨삭 서비스
                </li>
              </ul>
            </div>
          </div>

          <button
            onClick={handleSubscribe}
            className="w-full bg-blue-600 text-white text-lg font-semibold py-3 rounded-md hover:bg-blue-700"
          >
            결제하기
          </button>
        </div>
      )}
    </div>
    </>
  );
}
