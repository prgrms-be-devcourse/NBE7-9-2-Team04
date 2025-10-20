"use client"

import { useMemo } from "react"
import {
  Table,
  TableBody,
  TableCell,
  TableHead,
  TableHeader,
  TableRow,
} from "@/components/ui/table"

const mockPayments = [
  {
    id: "pay1",
    userName: "박알고",
    email: "park@example.com",
    plan: "프리미엄 월간",
    amount: 9900,
    status: "completed",
    paymentMethod: "카드",
    paidAt: "2025-10-15 14:30",
  },
  {
    id: "pay2",
    userName: "이프론트",
    email: "lee@example.com",
    plan: "프리미엄 월간",
    amount: 9900,
    status: "completed",
    paymentMethod: "카드",
    paidAt: "2025-10-14 09:15",
  },
  {
    id: "pay3",
    userName: "최백엔드",
    email: "choi@example.com",
    plan: "프리미엄 월간",
    amount: 9900,
    status: "failed",
    paymentMethod: "카드",
    paidAt: "2025-10-13 16:45",
  },
  {
    id: "pay4",
    userName: "정풀스택",
    email: "jung@example.com",
    plan: "프리미엄 월간",
    amount: 9900,
    status: "completed",
    paymentMethod: "계좌이체",
    paidAt: "2025-10-12 11:20",
  },
  {
    id: "pay5",
    userName: "강데브옵스",
    email: "kang@example.com",
    plan: "프리미엄 월간",
    amount: 9900,
    status: "completed",
    paymentMethod: "카드",
    paidAt: "2025-10-11 13:50",
  },
]

export default function AdminPaymentsPage() {
  const totalRevenue = useMemo(
    () => mockPayments.filter((p) => p.status === "completed").reduce((sum, p) => sum + p.amount, 0),
    []
  )

  const completedCount = mockPayments.filter((p) => p.status === "completed").length

  const getStatusBadge = (status: string) => {
    const base = "px-2 py-1 text-sm rounded font-medium"
    return status === "completed" ? (
      <span className={`${base} bg-green-100 text-green-700`}>완료</span>
    ) : (
      <span className={`${base} bg-red-100 text-red-700`}>실패</span>
    )
  }

  return (
    <div className="max-w-7xl mx-auto p-8 space-y-8">
      {/* Header */}
      <div>
        <h1 className="text-3xl font-bold mb-2">💳 결제 관리</h1>
        <p className="text-gray-500">프리미엄 멤버십 결제 내역과 통계를 조회합니다</p>
      </div>

      {/* Summary Cards */}
      <div className="grid md:grid-cols-3 gap-6">
        <div className="bg-gray-50 p-4 rounded-lg border-gray-900 textcenter">
          <p className="text-sm text-gray-500 mb-1">총 결제 건수</p>
          <p className="text-3xl font-bold">{mockPayments.length.toLocaleString()}</p>
        </div>

        <div className="bg-gray-50 p-4 rounded-lg border-gray-900">
          <p className="text-sm text-gray-500 mb-1">성공한 결제</p>
          <p className="text-3xl font-bold text-green-700">{completedCount.toLocaleString()}</p>
        </div>

        <div className="bg-gray-50 p-4 rounded-lg border-gray-900">
          <p className="text-sm text-gray-500 mb-1">총 수익</p>
          <p className="text-3xl font-bold flex items-center gap-2">
            {totalRevenue.toLocaleString()}원
          </p>
        </div>
      </div>

      {/* Payments Table */}
      <div className="overflow-x-auto bg-white border border-gray-200 shadow-sm rounded-lg">
        <Table>
          <TableHeader>
            <TableRow>
              <TableHead>사용자</TableHead>
              <TableHead>플랜</TableHead>
              <TableHead>금액</TableHead>
              <TableHead>결제 수단</TableHead>
              <TableHead>상태</TableHead>
              <TableHead>결제일시</TableHead>
            </TableRow>
          </TableHeader>

          <TableBody>
            {mockPayments.map((p) => (
              <TableRow key={p.id}>
                <TableCell>
                  <div>
                    <p className="font-medium">{p.userName}</p>
                    <p className="text-gray-500 text-xs">{p.email}</p>
                  </div>
                </TableCell>

                <TableCell>
                  <span className="px-2 py-1 text-sm border border-gray-200 rounded bg-gray-50">
                    {p.plan}
                  </span>
                </TableCell>

                <TableCell className="font-semibold">{p.amount.toLocaleString()}원</TableCell>
                <TableCell>{p.paymentMethod}</TableCell>
                <TableCell>{getStatusBadge(p.status)}</TableCell>
                <TableCell>{p.paidAt}</TableCell>
              </TableRow>
            ))}
          </TableBody>
        </Table>
      </div>
    </div>
  )
}
