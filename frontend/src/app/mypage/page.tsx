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
    totalPoints: 850,
    rank: 15,
    questionsSubmitted: 5,
    isPremium: false,
  };

  const userPosts = [
    { id: "1", title: "Next.js 14 프로젝트 팀원 모집", type: "모집", date: "2025-10-10" },
    { id: "2", title: "React 스터디 모집", type: "모집", date: "2025-10-08" },
  ];

  const userComments = [
    { id: "1", content: "좋은 프로젝트네요! 참여하고 싶습니다.", postTitle: "AI 챗봇 프로젝트", date: "2025-10-12" },
    { id: "2", content: "저도 같은 문제를 겪었는데...", postTitle: "포트폴리오 분석 오류", date: "2025-10-11" },
  ];

  const solvedProblems = [
    { id: "q1", title: "TCP와 UDP의 차이점", category: "네트워크", solvedAt: "2025-10-15" },
    { id: "q2", title: "프로세스와 스레드", category: "운영체제", solvedAt: "2025-10-14" },
  ];

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
      {/* 프로필 카드 */}
      <div className="bg-white rounded-lg p-6 shadow-md">
        <div className="flex justify-between items-start">
          <div className="flex items-center gap-6">
            <div className="w-24 h-24 bg-gray-200 rounded-full flex items-center justify-center text-3xl font-bold">
              {userData.name[0]}
            </div>
            <div>
              <h2 className="text-2xl font-bold">{userData.name}</h2>
              <p className="text-gray-500">{userData.email}</p>
              <p className="text-blue-600 mt-1 font-semibold">
                {userData.isPremium ? "프리미엄 회원" : "일반 회원"}
              </p>
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
            <p className="text-2xl font-bold mt-1">{userData.problemsSolved}</p>
          </div>
          <div className="bg-gray-50 p-4 rounded-lg border-gray-900">
            <p className="text-gray-500 text-sm">총 포인트</p>
            <p className="text-2xl font-bold mt-1">{userData.totalPoints}</p>
          </div>
          <div className="bg-gray-50 p-4 rounded-lg border-gray-900">
            <p className="text-gray-500 text-sm">제출한 질문</p>
            <p className="text-2xl font-bold mt-1">{userData.questionsSubmitted}</p>
          </div>
        </div>
      </div>

      {/* 작성한 글 */}
      <div className="bg-white rounded-lg p-6 shadow-md">
        <h3 className="text-xl font-semibold mb-4">📝 작성한 글</h3>
        <div className="space-y-3">
          {userPosts.map((post) => (
            <div key={post.id} className="flex justify-between items-center p-3 border  border-gray-200 rounded-md">
              <div>
                <p className="font-medium">{post.title}</p>
                <p className="text-sm text-gray-500">{post.date}</p>
              </div>
              <span className="px-2 py-1 text-xs rounded text-gray-700 bg-blue-50">{post.type}</span>
            </div>
          ))}
        </div>
      </div>

      {/* 작성한 댓글 */}
      <div className="bg-white rounded-lg p-6 shadow-md">
        <h3 className="text-xl font-semibold mb-4">💬 작성한 댓글</h3>
        <div className="space-y-3">
          {userComments.map((comment) => (
            <div key={comment.id} className="p-3 border border-gray-200 rounded-md">
              <p className="text-sm mb-1">{comment.content}</p>
              <p className="text-xs text-gray-500">
                {comment.postTitle} • {comment.date}
              </p>
            </div>
          ))}
        </div>
      </div>

      {/* 해결한 문제 */}
      <div className="bg-white rounded-lg p-6 shadow-md">
        <h3 className="text-xl font-semibold mb-4">💡 해결한 문제</h3>
        <div className="space-y-3">
          {solvedProblems.map((problem) => (
            <div key={problem.id} className="flex justify-between items-center p-3 border border-gray-200 rounded-md">
              <div>
                <p className="font-medium">{problem.title}</p>
                <p className="text-sm text-gray-500">{problem.solvedAt}</p>
              </div>
              <span className="px-2 py-1 text-xs rounded text-gray-700 bg-blue-50">{problem.category}</span>
            </div>
          ))}
        </div>
      </div>
    </div>
    </>
  );
}
