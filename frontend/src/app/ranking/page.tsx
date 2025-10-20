"use client";

import { useState } from "react";
import {
  getTierByProblems,
  getNextTier,
  getProgressToNextTier,
  TIER_SYSTEM,
} from "@/lib/tier";

//임시 데이터
const currentUser = {
  id: "u1",
  name: "김개발",
  problemsSolved: 100,
  score: 500,
  rank: 15,
};

const rankings = [
  {
    id: "u2",
    name: "박알고",
    problemsSolved: 185,
    score: 1980,
    rank: 1,
  },
  {
    id: "u3",
    name: "이프론트",
    problemsSolved: 165,
    score: 2000,
    rank: 2,
  },
  {
    id: "u4",
    name: "최백엔드",
    problemsSolved: 142,
    score: 1720,
    rank: 3,
  },
  {
    id: "u5",
    name: "정풀스택",
    problemsSolved: 128,
    score: 1580,
    rank: 4,
  },
  {
    id: "u6",
    name: "강데브옵스",
    problemsSolved: 115,
    score: 1420,
    rank: 5,
  },
  {
    id: "u7",
    name: "윤디자인",
    problemsSolved: 98,
    score: 1250,
    rank: 6,
  },
  {
    id: "u8",
    name: "임모바일",
    problemsSolved: 87,
    score: 1100,
    rank: 7,
  },
  {
    id: "u9",
    name: "한클라우드",
    problemsSolved: 76,
    score: 980,
    rank: 8,
  },
  {
    id: "u10",
    name: "송보안",
    problemsSolved: 68,
    score: 870,
    rank: 9,
  },
  {
    id: "u11",
    name: "조데이터",
    problemsSolved: 55,
    score: 720,
    rank: 10,
  },
];

export default function RankingPage() {
  const [sortBy, setSortBy] = useState<"score" | "problems">("score");

  const currentTier = getTierByProblems(currentUser.problemsSolved);
  const nextTier = getNextTier(currentTier.level);
  const progress = getProgressToNextTier(currentUser.problemsSolved);

  const sortedRankings = [...rankings]
    .sort(
      (a, b) =>
        sortBy === "score"
          ? b.score - a.score
          : b.problemsSolved - a.problemsSolved //내림차순 정렬
    )
    .map((user, i) => ({ ...user, rank: i + 1 })); // 정렬 후 rank 재계산

  const getRankEmoji = (rank: number) => {
    if (rank === 1) return "🥇";
    if (rank === 2) return "🥈";
    if (rank === 3) return "🥉";
    return `${rank}`;
  };

  return (
    <div className="max-w-screen-xl mx-auto px-6 py-10">
      <div className="mb-10">
        <h1 className="text-3xl font-bold mb-2">랭킹 & 티어</h1>
        <p className="text-gray-500">문제를 풀고 티어를 올려보세요!</p>
      </div>

      <div className="bg-white rounded-lg p-6 shadow-md border-2 border-blue-200 mb-10">
        <div className="flex justify-between items-start">
          <div className="flex items-center gap-6">
            <div className="w-24 h-24 bg-gray-200 rounded-full flex items-center justify-center text-3xl font-bold">
              {currentUser.name[0]}
            </div>
            <div>
              <h2 className="text-2xl font-bold">{currentUser.name}</h2>
              <p className="text-gray-500">현재 랭킹: {currentUser.rank}위</p>
              <div
                className={`mt-2 inline-flex items-center gap-2 px-3 py-1 rounded-full text-sm font-semibold ${currentTier.bgColor} ${currentTier.color}`}
              >
                <span>{currentTier.icon}</span>
                <span>{currentTier.level}</span>
              </div>
            </div>
          </div>
        </div>

        <div className="grid md:grid-cols-2 gap-4 mt-6">
          <div className="bg-gray-50 rounded-lg p-4 text-center border border-gray-300">
            <p className="text-gray-500 text-sm">해결한 문제</p>
            <p className="text-3xl font-bold mt-1">
              {currentUser.problemsSolved}
            </p>
          </div>
          <div className="bg-gray-50 rounded-lg p-4 text-center border border-gray-300">
            <p className="text-gray-500 text-sm">총 점수</p>
            <p className="text-3xl font-bold mt-1">{currentUser.score}</p>
          </div>
        </div>

        {/* 진행률 */}
        {nextTier && (
          <div className="mt-6 space-y-2">
            <div className="flex justify-between text-sm text-gray-600">
              <span>다음 티어까지</span>
              <span className="font-semibold">
                {nextTier.minProblems - currentUser.problemsSolved}문제 남음
              </span>
            </div>
            <div className="w-full bg-gray-200 rounded-full h-3 overflow-hidden">
              <div
                className="bg-blue-500 h-3 transition-all duration-300"
                style={{ width: `${progress}%` }}
              ></div>
            </div>
            <div className="flex items-center justify-between text-sm mt-1">
              <span>
                {currentTier.icon} {currentTier.level}
              </span>

              <span>
                {nextTier.icon} {nextTier.level}
              </span>
            </div>
          </div>
        )}
      </div>

      <div className="bg-white rounded-lg p-6 shadow-sm border border-gray-300 mb-10">
        <div className="flex items-center gap-2 mb-4">
          <span className="text-lg">🥇</span>
          <h3 className="text-xl font-semibold">티어 시스템</h3>
        </div>
        <p className="text-sm text-gray-500 mb-4">
          30문제를 풀 때마다 티어가 상승합니다
        </p>

        <div className="grid grid-cols-2 md:grid-cols-4 gap-4">
          {Object.values(TIER_SYSTEM).map((tier) => (
            <div
              key={tier.level}
              className="text-center border border-gray-300 rounded-lg p-3 bg-gray-50 hover:bg-gray-100 transition"
            >
              <div
                className={`inline-flex items-center gap-1 px-2 py-1 rounded-full text-sm font-semibold ${tier.bgColor} ${tier.color}`}
              >
                <span>{tier.icon}</span>
                <span>{tier.level}</span>
              </div>
              <p className="text-xs text-gray-500 mt-1">
                {tier.minProblems}~
                {tier.maxProblems === Infinity ? "∞" : tier.maxProblems} 문제
              </p>
            </div>
          ))}
        </div>
      </div>

      <div className="bg-white rounded-lg p-6 shadow-sm border border-gray-300">
        <div className="flex justify-between items-center mb-4">
          <h3 className="text-xl font-semibold">전체 랭킹</h3>

          <div className="flex border border-gray-200 rounded-md overflow-hidden">
            <button
              onClick={() => setSortBy("score")}
              className={`px-4 py-2 text-sm transition ${
                sortBy === "score"
                  ? "bg-blue-500 text-white"
                  : "bg-white text-gray-700 hover:bg-gray-100"
              }`}
            >
              점수순
            </button>
            <button
              onClick={() => setSortBy("problems")}
              className={`px-4 py-2 text-sm transition ${
                sortBy === "problems"
                  ? "bg-blue-500 text-white"
                  : "bg-white text-gray-700 hover:bg-gray-100"
              }`}
            >
              문제수순
            </button>
          </div>
        </div>

        <div className="space-y-3">
          {sortedRankings.map((user) => {
            const tier = getTierByProblems(user.problemsSolved);
            return (
              <div
                key={user.id}
                className="flex items-center gap-4 p-4 rounded-lg border border-gray-300 bg-gray-50 hover:bg-gray-100 transition"
              >
                <div className="w-8 flex justify-center text-lg font-bold">
                  {getRankEmoji(user.rank)}
                </div>
                <div className="w-10 h-10 bg-gray-200 rounded-full flex items-center justify-center font-bold">
                  {user.name[0]}
                </div>
                <div className="flex-1">
                  <p className="font-semibold">{user.name}</p>
                  <div className="flex items-center gap-2 text-sm text-gray-500">
                    <span>{user.problemsSolved}문제</span>
                    <span>•</span>
                    <span>{user.score}점</span>
                  </div>
                </div>
                <div
                  className={`inline-flex items-center gap-1 px-2 py-1 rounded-full text-sm font-semibold ${tier.bgColor} ${tier.color}`}
                >
                  <span>{tier.icon}</span>
                  <span>{tier.level}</span>
                </div>
              </div>
            );
          })}
        </div>
      </div>
    </div>
  );
}
