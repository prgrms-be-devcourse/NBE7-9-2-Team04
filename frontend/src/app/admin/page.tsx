"use client"

import { useState } from "react"
import {
  Table,
  TableBody,
  TableCell,
  TableHead,
  TableHeader,
  TableRow,
} from "@/components/ui/table"
import {
  DropdownMenuContent,
  DropdownMenuItem,
  useDropdown,
} from "@/components/ui/dropdown-menu"

// ===============================
// 🔸 Mock 데이터
// ===============================
const mockUsers = [
  {
    id: "u1",
    name: "김개발",
    email: "kim@example.com",
    problemsSolved: 75,
    totalPoints: 850,
    status: "active",
    joinedAt: "2025-09-01",
    isPremium: false,
  },
  {
    id: "u2",
    name: "박알고",
    email: "park@example.com",
    problemsSolved: 185,
    totalPoints: 2150,
    status: "active",
    joinedAt: "2025-08-15",
    isPremium: true,
  },
  {
    id: "u3",
    name: "이프론트",
    email: "lee@example.com",
    problemsSolved: 165,
    totalPoints: 1980,
    status: "active",
    joinedAt: "2025-08-20",
    isPremium: true,
  },
  {
    id: "u4",
    name: "최백엔드",
    email: "choi@example.com",
    problemsSolved: 142,
    totalPoints: 1720,
    status: "suspended",
    joinedAt: "2025-07-10",
    isPremium: false,
  },
  {
    id: "u5",
    name: "정풀스택",
    email: "jung@example.com",
    problemsSolved: 128,
    totalPoints: 1580,
    status: "active",
    joinedAt: "2025-09-05",
    isPremium: false,
  },
]


function getStatusBadge(status: string) {
  const base = "px-2 py-1 text-sm rounded"
  switch (status) {
    case "active":
      return <span className={`${base} bg-green-100 text-green-700`}>활성</span>
    case "suspended":
      return <span className={`${base} bg-yellow-100 text-yellow-700`}>정지</span>
    case "banned":
      return <span className={`${base} bg-red-100 text-red-700`}>차단</span>
    default:
      return null
  }
}

// 티어 계산
function getTierByProblems(problemsSolved: number) {
  if (problemsSolved >= 180) return { level: "Diamond", color: "text-blue-700", bg: "bg-blue-100", icon: "💎" }
  if (problemsSolved >= 150) return { level: "Platinum", color: "text-cyan-700", bg: "bg-cyan-100", icon: "🔷" }
  if (problemsSolved >= 100) return { level: "Gold", color: "text-yellow-700", bg: "bg-yellow-100", icon: "🥇" }
  if (problemsSolved >= 50) return { level: "Silver", color: "text-gray-700", bg: "bg-gray-100", icon: "🥈" }
  return { level: "Bronze", color: "text-amber-700", bg: "bg-amber-100", icon: "🥉" }
}

// 티어 뱃지
function TierBadge({ problemsSolved }: { problemsSolved: number }) {
  const tier = getTierByProblems(problemsSolved)
  return (
    <span
      className={`inline-flex items-center gap-1 px-2 py-0.5 text-xs rounded-full font-medium ${tier.bg} ${tier.color}`}
    >
      <span>{tier.icon}</span>
      {tier.level}
    </span>
  )
}

// 프리미엄 태그
function PremiumTag() {
  return (
    <span className="text-xs font-medium text-blue-700 bg-blue-50 border border-blue-200 rounded px-2 py-0.5">
      프리미엄
    </span>
  )
}


export default function AdminUsersPage() {
  const [users, setUsers] = useState(mockUsers)
  const [searchQuery, setSearchQuery] = useState("")

  const filteredUsers = users.filter(
    (user) =>
      user.name.toLowerCase().includes(searchQuery.toLowerCase()) ||
      user.email.toLowerCase().includes(searchQuery.toLowerCase())
  )

  const updateStatus = (id: string, status: string) => {
    setUsers(users.map((u) => (u.id === id ? { ...u, status } : u)))
    alert(
      status === "active"
        ? "사용자가 활성화되었습니다."
        : status === "suspended"
        ? "사용자의 활동이 정지되었습니다."
        : "사용자가 영구 차단되었습니다."
    )
  }

  return (
    <div className="max-w-7xl mx-auto p-8 space-y-6">

      <div>
        <h1 className="text-3xl font-bold mb-2">👥 사용자 관리</h1>
        <p className="text-gray-500">플랫폼 사용자를 관리하고 활동을 모니터링합니다</p>
      </div>


      <div className="relative w-full max-w-md">
        <span className="absolute left-3 top-1/2 -translate-y-1/2 text-gray-400 text-lg">🔍</span>
        <input
          type="text"
          placeholder="이름 또는 이메일로 검색..."
          value={searchQuery}
          onChange={(e) => setSearchQuery(e.target.value)}
          className="w-full border border-gray-300 rounded-md px-10 py-2 focus:outline-none focus:border-blue-500"
        />
      </div>


      <div className="overflow-x-auto bg-white border border-gray-200 shadow-sm rounded-lg">
        <Table>
          <TableHeader>
            <TableRow>
              <TableHead>사용자</TableHead>
              <TableHead>티어</TableHead>
              <TableHead>문제 수</TableHead>
              <TableHead>포인트</TableHead>
              <TableHead>상태</TableHead>
              <TableHead>가입일</TableHead>
              <TableHead>작업</TableHead>
            </TableRow>
          </TableHeader>

          <TableBody>
            {filteredUsers.map((user) => {
              const { ref, open, setOpen } = useDropdown()
              return (
                <TableRow key={user.id}>
                  <TableCell>
                    <div className="flex items-center gap-3">
                      <div className="w-10 h-10 flex items-center justify-center rounded-full bg-gray-200 font-bold text-gray-700">
                        {user.name[0]}
                      </div>
                      <div>
                        <div className="flex items-center gap-2">
                          <p className="font-medium">{user.name}</p>
                          {user.isPremium && <PremiumTag />}
                        </div>
                        <p className="text-sm text-gray-500">{user.email}</p>
                      </div>
                    </div>
                  </TableCell>

                  <TableCell>
                    <TierBadge problemsSolved={user.problemsSolved} />
                  </TableCell>
                  <TableCell>{user.problemsSolved}</TableCell>
                  <TableCell>{user.totalPoints}</TableCell>
                  <TableCell>{getStatusBadge(user.status)}</TableCell>
                  <TableCell>{user.joinedAt}</TableCell>

                  {/* 드롭다운 */}
                  <TableCell className="text-right">
                    <div ref={ref} className="relative inline-block">
                      <button
                        onClick={() => setOpen(!open)}
                        className="p-2 rounded hover:bg-gray-100 text-gray-600"
                        aria-label="더보기"
                      >
                        ⋮
                      </button>

                      <DropdownMenuContent open={open} align="end">
                        {user.status === "active" ? (
                          <>
                            <DropdownMenuItem
                              onClick={() => {
                                updateStatus(user.id, "suspended")
                                setOpen(false)
                              }}
                            >
                              활동 정지
                            </DropdownMenuItem>
                            <DropdownMenuItem
                              onClick={() => {
                                updateStatus(user.id, "banned")
                                setOpen(false)
                              }}
                              className="text-red-600"
                            >
                              영구 정지
                            </DropdownMenuItem>
                          </>
                        ) : (
                          <DropdownMenuItem
                            onClick={() => {
                              updateStatus(user.id, "active")
                              setOpen(false)
                            }}
                          >
                            활성화
                          </DropdownMenuItem>
                        )}
                      </DropdownMenuContent>
                    </div>
                  </TableCell>
                </TableRow>
              )
            })}
          </TableBody>
        </Table>
      </div>
    </div>
  )
}
