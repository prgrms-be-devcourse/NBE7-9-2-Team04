"use client";

import { useState, useEffect } from "react";
import { useRouter } from "next/navigation";
import { fetchApi } from "@/lib/client";
import { RankingResponse } from "@/types/ranking";
import { Subscription } from "@/types/subscription";

export default function ProfilePage() {
  const router = useRouter();
  const [isLoading, setIsLoading] = useState(true);
  const [myRanking, setMyRanking] = useState<RankingResponse | null>(null);
  const [subscription, setSubscription] = useState<Subscription | null>(null);

  useEffect(() => {
    async function loadProfileData() {
      try {
        // 동시에 두 API 호출
        const [rankingRes, subRes] = await Promise.all([
          fetchApi("/api/v1/rankings", { method: "GET" }),
          fetchApi("/api/v1/subscriptions/me", { method: "GET" }),
        ]);

        const rankingData = rankingRes.data.myRanking as RankingResponse;
        const subData = subRes.data as Subscription;

        setMyRanking(rankingData);
        setSubscription(subData);
      } catch (err) {
        console.error("프로필 데이터 불러오기 실패:", err);
        router.push("/login");
      } finally {
        setIsLoading(false);
      }
    }
    loadProfileData();
  }, [router]);

  if (isLoading || !myRanking || !subscription) {
    return (
      <div className="flex items-center justify-center min-h-[60vh]">
        <p className="text-gray-500">로딩 중...</p>
      </div>
    );
  }

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

  const tierOf = (tier: string) => tierStyles[tier] || tierStyles.UNRATED;

  return (
    <>
      <div className="space-y-8">
        <div className="bg-white rounded-lg p-6 shadow-md">
          <div className="flex justify-between items-start">
            <div className="flex items-center gap-6">
              <div className="w-24 h-24 bg-gray-200 rounded-full flex items-center justify-center text-3xl font-bold">
                {myRanking.nickName[0]}
              </div>
              <div>
                <h2 className="text-2xl font-bold">{myRanking.nickName}</h2>
                <p className="text-gray-500">{myRanking.email}</p>
                <div className="flex items-center gap-2 mt-2">
                  <p
                    className={`font-semibold ${
                      subscription.subscriptionType === "PREMIUM"
                        ? "text-blue-600"
                        : "text-gray-500"
                    }`}
                  >
                    {subscription.subscriptionType === "PREMIUM"
                      ? "프리미엄 회원"
                      : "일반 회원"}
                  </p>
                  <div
                    className={`inline-flex items-center gap-2 px-3 py-1 rounded-full text-sm font-semibold border ${
                      tierOf(myRanking.currentTier).bgColor
                    } ${tierOf(myRanking.currentTier).color}`}
                  >
                    <span>{tierOf(myRanking.currentTier).icon}</span>
                    <span>{myRanking.currentTier}</span>
                  </div>
                </div>
              </div>
            </div>

            {/* 순위 */}
            <div className="text-right">
              <p className="text-gray-500 text-sm mb-1">랭킹</p>
              <p className="text-3xl font-bold">{myRanking.rankValue}위</p>
            </div>
          </div>

          {/* 통계 */}
          <div className="grid md:grid-cols-3 gap-6 mt-8">
            <Stat label="해결한 문제" value={myRanking.solvedCount} />
            <Stat label="총 점수" value={myRanking.totalScore} />
            <Stat label="제출한 질문" value={myRanking.questionCount} />
          </div>
        </div>
      </div>
    </>
  );
}

function Stat({ label, value }: { label: string; value: number }) {
  return (
    <div className="bg-gray-50 p-4 rounded-lg border border-gray-200 text-center">
      <p className="text-gray-500 text-sm">{label}</p>
      <p className="text-2xl font-bold mt-1">{value}</p>
    </div>
  );
}
