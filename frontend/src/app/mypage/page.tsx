"use client";

import { useState, useEffect } from "react";
import { useRouter } from "next/navigation";


export default function ProfilePage() {
  const router = useRouter();
  const [isLoading, setIsLoading] = useState(true);

  const userData = {
    name: "김개발",
    email: "kim@example.com",
    problemsSolved: 75,
    totalScores: 850,
    rank: 15,
    questionsSubmitted: 5,
    isPremium: false,
    teer: "Silver",
  };

  // useEffect(() => {
  //   const isLoggedIn = localStorage.getItem("isLoggedIn");
  //   if (!isLoggedIn || isLoggedIn !== "true") {
  //     router.push("/login");
  //   } else {
  //     setIsLoading(false);
  //   }
  // }, [router]);
  useEffect(() => {
    const timer = setTimeout(() => {
      setIsLoading(false);
    }, 300); // 살짝 로딩 연출
    return () => clearTimeout(timer);
  }, []);

  if (isLoading) {
    return (
      <div className="flex items-center justify-center min-h-[60vh]">
        <p className="text-gray-500">로딩 중...</p>
      </div>
    );
  }

  return (
    <>
      <div className="space-y-8">
        <div className="bg-white rounded-lg p-6 shadow-md">
          <div className="flex justify-between items-start">
            <div className="flex items-center gap-6">
              <div className="w-24 h-24 bg-gray-200 rounded-full flex items-center justify-center text-3xl font-bold">
                {userData.name[0]}
              </div>
              <div>
                <h2 className="text-2xl font-bold">{userData.name}</h2>
                <p className="text-gray-500">{userData.email}</p>
                <div className="flex items-center gap-2 mt-1">
                  <p className="text-blue-600 font-semibold">
                    {userData.isPremium ? "프리미엄 회원" : "일반 회원"}
                  </p>
                  <span
                    className={`
                    px-3 py-1 text-sm font-semibold rounded-full border shadow-sm
                    ${
                      userData.teer === "Unrated"
                        ? "bg-gray-100 text-gray-600 border-gray-300"
                        : userData.teer === "Bronze"
                        ? "bg-gradient-to-r from-orange-200 to-orange-400 text-orange-900 border-orange-300"
                        : userData.teer === "Silver"
                        ? "bg-gradient-to-r from-gray-200 to-gray-400 text-gray-800 border-gray-300"
                        : userData.teer === "Gold"
                        ? "bg-gradient-to-r from-yellow-300 to-yellow-500 text-yellow-900 border-yellow-400"
                        : userData.teer === "Platinum"
                        ? "bg-gradient-to-r from-cyan-100 to-cyan-300 text-cyan-900 border-cyan-300"
                        : userData.teer === "Diamond"
                        ? "bg-gradient-to-r from-blue-200 to-blue-500 text-blue-900 border-blue-300"
                        : userData.teer === "Ruby"
                        ? "bg-gradient-to-r from-rose-300 to-rose-500 text-white border-rose-400"
                        : "bg-blue-50 text-blue-700 border-blue-200"
                    }
                  `}
                  >
                    {userData.teer}
                  </span>
                </div>
              </div>
            </div>
            <div className="text-right">
              <p className="text-gray-500 text-sm mb-1">랭킹</p>
              <p className="text-3xl font-bold">{userData.rank}위</p>
            </div>
          </div>

          {/* 통계 */}
          <div className="grid md:grid-cols-3 gap-6 mt-8">
            <div className="bg-gray-50 p-4 rounded-lg border-gray-900">
              <p className="text-gray-500 text-sm">해결한 문제</p>
              <p className="text-2xl font-bold mt-1">
                {userData.problemsSolved}
              </p>
            </div>
            <div className="bg-gray-50 p-4 rounded-lg border-gray-900">
              <p className="text-gray-500 text-sm">총 점수</p>
              <p className="text-2xl font-bold mt-1">{userData.totalScores}</p>
            </div>
            <div className="bg-gray-50 p-4 rounded-lg border-gray-900">
              <p className="text-gray-500 text-sm">제출한 질문</p>
              <p className="text-2xl font-bold mt-1">
                {userData.questionsSubmitted}
              </p>
            </div>
          </div>
        </div>
      </div>
    </>
  );
}
