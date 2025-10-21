// tier.ts
export type TierLevel =
  | "Unrated"
  | "Bronze"
  | "Silver"
  | "Gold"
  | "Platinum"
  | "Diamond"
  | "Ruby"
  | "Master";

export interface TierInfo {
  level: TierLevel;
  minProblems: number;
  maxProblems: number;
  color: string;      
  bgColor: string;    
  icon: string;       
}

export const TIER_ORDER: TierLevel[] = [
  "Unrated",
  "Bronze",
  "Silver",
  "Gold",
  "Platinum",
  "Diamond",
  "Ruby",
  "Master",
];

export const TIER_SYSTEM: Record<TierLevel, TierInfo> = {
  Unrated: {
    level: "Unrated",
    minProblems: 0,
    maxProblems: 29,
    color: "text-gray-600",
    bgColor: "bg-gray-100",
    icon: "⚪",
  },
  Bronze: {
    level: "Bronze",
    minProblems: 30,
    maxProblems: 59,
    color: "text-amber-700",
    bgColor: "bg-amber-100",
    icon: "🥉",
  },
  Silver: {
    level: "Silver",
    minProblems: 60,
    maxProblems: 89,
    color: "text-gray-500",
    bgColor: "bg-gray-200",
    icon: "🥈",
  },
  Gold: {
    level: "Gold",
    minProblems: 90,
    maxProblems: 119,
    color: "text-yellow-700",
    bgColor: "bg-yellow-100",
    icon: "🥇",
  },
  Platinum: {
    level: "Platinum",
    minProblems: 120,
    maxProblems: 149,
    color: "text-cyan-700",
    bgColor: "bg-cyan-100",
    icon: "💠",
  },
  Diamond: {
    level: "Diamond",
    minProblems: 150,
    maxProblems: 179,
    color: "text-blue-700",
    bgColor: "bg-blue-100",
    icon: "💎",
  },
  Ruby: {
    level: "Ruby",
    minProblems: 180,
    maxProblems: 209,
    color: "text-red-700",
    bgColor: "bg-red-100",
    icon: "🔴",
  },
  Master: {
    level: "Master",
    minProblems: 210,
    maxProblems: Number.POSITIVE_INFINITY,
    color: "text-purple-700",
    bgColor: "bg-purple-100",
    icon: "👑",
  },
};

//문제 수로 티어 결정
export function getTierByProblems(problemsSolved: number): TierInfo {
  for (const tier of TIER_ORDER) {
    const info = TIER_SYSTEM[tier];
    if (problemsSolved >= info.minProblems && problemsSolved <= info.maxProblems) {
      return info;
    }
  }
  return TIER_SYSTEM.Unrated;
}

//  다음 티어 찾기
export function getNextTier(currentTier: TierLevel): TierInfo | null {
  const idx = TIER_ORDER.indexOf(currentTier);
  return idx >= 0 && idx < TIER_ORDER.length - 1
    ? TIER_SYSTEM[TIER_ORDER[idx + 1]]
    : null;
}

// 다음 티어까지의 진행률 계산 
export function getProgressToNextTier(problemsSolved: number): number {
  const currentTier = getTierByProblems(problemsSolved);
  const nextTier = getNextTier(currentTier.level);

  if (!nextTier) return 100; // 이미 최고 티어

  const progressWithinTier = problemsSolved - currentTier.minProblems;
  const requiredToNext = nextTier.minProblems - currentTier.minProblems;

  return Math.min(100, Math.round((progressWithinTier / requiredToNext) * 100));
}
