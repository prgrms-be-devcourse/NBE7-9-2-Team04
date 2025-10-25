"use client";

import { useEffect, useState } from "react";
import { fetchApi } from "@/lib/client";
import { RankingResponse, RankingSummaryResponse } from "@/types/ranking";

export default function RankingPage() {
  const [sortBy, setSortBy] = useState<"score" | "problems">("score");
  const [myRanking, setMyRanking] = useState<RankingResponse | null>(null);
  const [rankings, setRankings] = useState<RankingResponse[]>([]);

  useEffect(() => {
    async function loadRankingData() {
      try {
        const apiResponse = await fetchApi("/api/v1/rankings", {
          method: "GET",
        });
        const data = apiResponse.data as RankingSummaryResponse;
        setMyRanking(data.myRanking);
        setRankings(data.topRankings);
      } catch (error) {
        console.error("랭킹 데이터 불러오기 실패:", error);
      }
    }
    loadRankingData();
  }, []);

  if (!myRanking) {
    return (
      <div className="flex justify-center items-center h-screen text-gray-500">
        랭킹 정보를 불러오는 중입니다...
      </div>
    );
  }

  // 티어 스타일 & 아이콘
  const tierStyles: Record<
    string,
    { color: string; bgColor: string; icon: string }
  > = {
    UNRATED: { color: "text-gray-600", bgColor: "bg-gray-100", icon: "⚪" },
    BRONZE: { color: "text-amber-700", bgColor: "bg-amber-100", icon: "🥉" },
    SILVER: { color: "text-gray-500", bgColor: "bg-gray-200", icon: "🥈" },
    GOLD: { color: "text-yellow-700", bgColor: "bg-yellow-100", icon: "🥇" },
    PLATINUM: { color: "text-cyan-700", bgColor: "bg-cyan-100", icon: "💠" },
    DIAMOND: { color: "text-blue-700", bgColor: "bg-blue-100", icon: "💎" },
    RUBY: { color: "text-red-700", bgColor: "bg-red-100", icon: "🔴" },
    MASTER: { color: "text-purple-700", bgColor: "bg-purple-100", icon: "👑" },
  };

  const TIER_SCORE_RANGES: Record<string, string> = {
    UNRATED: "0 ~ 299점",
    BRONZE: "300 ~ 599점",
    SILVER: "600 ~ 899점",
    GOLD: "900 ~ 1199점",
    PLATINUM: "1200 ~ 1499점",
    DIAMOND: "1500 ~ 1799점",
    RUBY: "1800 ~ 2099점",
    MASTER: "2100점 이상",
  };

  const tierOf = (tier: string) => tierStyles[tier] || tierStyles.UNRATED;

  const getRankEmoji = (rank: number) => {
    if (rank === 1) return "🥇";
    if (rank === 2) return "🥈";
    if (rank === 3) return "🥉";
    return `${rank}`;
  };

  const sortedRankings = [...rankings]
    .sort((a, b) => {
      if (sortBy === "score") {
        if (b.totalScore !== a.totalScore) return b.totalScore - a.totalScore;
        return a.nickName.localeCompare(b.nickName, "ko");
      } else {
        if (b.solvedCount !== a.solvedCount)
          return b.solvedCount - a.solvedCount;
        return a.nickName.localeCompare(b.nickName, "ko");
      }
    })
    .map((user, i) => ({ ...user, rankValue: i + 1 }));

  return (
    <div className="max-w-screen-xl mx-auto px-6 py-10">
      {/* 제목 */}
      <div className="mb-10">
        <h1 className="text-3xl font-bold mb-2">랭킹 & 티어</h1>
        <p className="text-gray-500">문제를 풀고 티어를 올려보세요!</p>
      </div>

      {/* 내 랭킹 */}
      <div className="bg-blue-50 rounded-lg p-6 shadow-md border border-blue-200 mb-10">
        <div className="flex justify-between items-start">
          <div className="flex items-center gap-6">
            <div className="w-24 h-24 bg-gray-200 rounded-full flex items-center justify-center text-3xl font-bold">
              {myRanking.nickName[0]}
            </div>
            <div>
              <h2 className="text-2xl font-bold">{myRanking.nickName}</h2>
              <p className="text-gray-500">
                현재 랭킹: {myRanking.rankValue}위
              </p>
              <div
                className={`mt-2 inline-flex items-center gap-2 px-3 py-1 rounded-full text-sm font-semibold ${
                  tierOf(myRanking.currentTier).bgColor
                } ${tierOf(myRanking.currentTier).color}`}
              >
                <span>{tierOf(myRanking.currentTier).icon}</span>
                <span>{myRanking.currentTier}</span>
              </div>
            </div>
          </div>
        </div>

        {/* 통계 */}
        <div className="grid md:grid-cols-3 gap-4 mt-6">
          <StatBox label="해결한 문제" value={myRanking.solvedCount} />
          <StatBox label="총 점수" value={myRanking.totalScore} />
          <StatBox label="제출한 질문" value={myRanking.questionCount} />
        </div>

        {/* 다음 티어 진행률. 300,600 ``해당하면 해당 바 안 보임 */}
        {myRanking.nextTier && myRanking.scoreToNextTier > 0 && (
          <div className="mt-6 space-y-2">
            <div className="flex justify-between text-sm text-gray-600">
              <span>다음 티어까지</span>
              <span className="font-semibold">
                {myRanking.scoreToNextTier}점 남음 ({myRanking.nextTier})
              </span>
            </div>
            <div className="w-full bg-gray-200 rounded-full h-3 overflow-hidden">
              <div
                className="bg-blue-500 h-3 transition-all duration-300"
                style={{
                  width: `${
                    100 -
                    (myRanking.scoreToNextTier /
                      (myRanking.totalScore + myRanking.scoreToNextTier)) *
                      100
                  }%`,
                }}
              ></div>
            </div>
            <div className="flex justify-between text-sm text-gray-600 mt-1">
              <span>
                {tierOf(myRanking.currentTier).icon} {myRanking.currentTier}
              </span>
              <span>
                {tierOf(myRanking.nextTier).icon} {myRanking.nextTier}
              </span>
            </div>
          </div>
        )}
      </div>

      {/* 티어 안내문 */}
      <div className="bg-white rounded-lg p-6 shadow-sm border border-gray-300 mb-10">
        <div className="flex items-center gap-2 mb-4">
          <span className="text-lg">🏆</span>
          <h3 className="text-xl font-semibold">티어 시스템</h3>
        </div>
        <p className="text-sm text-gray-500 mb-4">
          점수에 따라 티어가 결정되며, 300점 단위로 상승합니다.
        </p>

        <div className="grid grid-cols-2 md:grid-cols-4 gap-4">
          {Object.entries(tierStyles).map(([tier, style]) => (
            <div
              key={tier}
              className="text-center border border-gray-300 rounded-lg p-3 bg-gray-50 hover:bg-gray-100 transition"
            >
              <div
                className={`inline-flex items-center gap-1 px-2 py-1 rounded-full text-sm font-semibold ${style.bgColor} ${style.color}`}
              >
                <span>{style.icon}</span>
                <span>{tier}</span>
              </div>
              <p className="text-xs text-gray-500 mt-1">
                {TIER_SCORE_RANGES[tier]}
              </p>
            </div>
          ))}
        </div>
      </div>

      {/* 전체 랭킹 */}
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
          {sortedRankings.map((user) => (
            <div
              key={user.userId}
              className="flex items-center gap-4 p-4 rounded-lg border border-gray-300 bg-gray-50 hover:bg-gray-100 transition"
            >
              <div className="w-8 flex justify-center text-lg font-bold">
                {getRankEmoji(user.rankValue)}
              </div>
              <div className="w-10 h-10 bg-gray-200 rounded-full flex items-center justify-center font-bold">
                {user.nickName[0]}
              </div>
              <div className="flex-1">
                <p className="font-semibold">{user.nickName}</p>
                <div className="flex items-center gap-2 text-sm text-gray-500">
                  <span>해결한 문제 : {user.solvedCount}문제</span>
                  <span>•</span>
                  <span>{user.totalScore}점</span>
                  <span>•</span>
                  <span>제출한 질문 : {user.questionCount}개</span>
                </div>
              </div>
              <div
                className={`inline-flex items-center gap-1 px-2 py-1 rounded-full text-sm font-semibold ${
                  tierOf(user.currentTier).bgColor
                } ${tierOf(user.currentTier).color}`}
              >
                <span>{tierOf(user.currentTier).icon}</span>
                <span>{user.currentTier}</span>
              </div>
            </div>
          ))}
        </div>
      </div>
    </div>
  );
}

function StatBox({ label, value }: { label: string; value: number }) {
  return (
    <div className="bg-gray-50 rounded-lg p-4 text-center border border-gray-300">
      <p className="text-gray-500 text-sm">{label}</p>
      <p className="text-3xl font-bold mt-1">{value}</p>
    </div>
  );
}
