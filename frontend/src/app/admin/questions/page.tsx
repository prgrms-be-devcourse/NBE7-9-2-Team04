"use client"

import { useState } from "react"
import Link from "next/link"
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

// ✅ Mock questions data
const mockQuestions = [
  {
    id: "q1",
    title: "TCP와 UDP의 차이점을 설명하세요",
    category: "네트워크",
    status: "approved",
    submittedBy: "관리자",
    submittedAt: "2025-09-01",
  },
  {
    id: "q2",
    title: "프로세스와 스레드의 차이는 무엇인가요?",
    category: "운영체제",
    status: "approved",
    submittedBy: "관리자",
    submittedAt: "2025-09-01",
  },
  {
    id: "q3",
    title: "데이터베이스 인덱스의 동작 원리",
    category: "데이터베이스",
    status: "pending",
    submittedBy: "김개발",
    submittedAt: "2025-10-15",
  },
  {
    id: "q4",
    title: "RESTful API 설계 원칙",
    category: "네트워크",
    status: "pending",
    submittedBy: "박알고",
    submittedAt: "2025-10-14",
  },
  {
    id: "q5",
    title: "가비지 컬렉션의 종류와 특징",
    category: "운영체제",
    status: "rejected",
    submittedBy: "이프론트",
    submittedAt: "2025-10-13",
  },
]

export default function AdminQuestionsPage() {
  const [questions, setQuestions] = useState(mockQuestions)

  const handleApprove = (id: string) => {
    setQuestions((prev) => prev.map((q) => (q.id === id ? { ...q, status: "approved" } : q)))
    alert("질문이 승인되었습니다.")
  }

  const handleReject = (id: string) => {
    setQuestions((prev) => prev.map((q) => (q.id === id ? { ...q, status: "rejected" } : q)))
    alert("질문이 거부되었습니다.")
  }

  const handleDelete = (id: string) => {
    setQuestions((prev) => prev.filter((q) => q.id !== id))
    alert("질문이 삭제되었습니다.")
  }


  const getStatusBadge = (status: string) => {
    const base = "px-2 py-1 text-sm rounded"
    switch (status) {
      case "approved":
        return <span className={`${base} bg-green-100 text-green-700`}>승인됨</span>
      case "pending":
        return <span className={`${base} bg-yellow-100 text-yellow-700`}>대기중</span>
      case "rejected":
        return <span className={`${base} bg-red-100 text-red-700`}>거부됨</span>
      default:
        return null
    }
  }


  return (
    <div className="max-w-7xl mx-auto p-8 space-y-6">
      {/* Header */}
      <div>
        <h1 className="text-3xl font-bold mb-2">📝 질문 관리</h1>
        <p className="text-gray-500">CS 면접 질문을 관리하고 사용자 제출 질문을 승인합니다</p>
      </div>

      {/* Table */}
      <div className="overflow-x-auto bg-white border border-gray-200 shadow-sm rounded-lg">
        <Table>
          <TableHeader>
            <TableRow>
              <TableHead>질문</TableHead>
              <TableHead>카테고리</TableHead>
              <TableHead>상태</TableHead>
              <TableHead>작성자</TableHead>
              <TableHead>제출일</TableHead>
              <TableHead>작업</TableHead>
            </TableRow>
          </TableHeader>

          <TableBody>
            {questions.map((q) => {
              const { ref, open, setOpen } = useDropdown()
              return (
                <TableRow key={q.id}>
                  {/* 질문 제목 */}
                  <TableCell className="max-w-xs">
                    <Link
                      href={`/interview/cs/${q.id}`}
                      className="font-medium line-clamp-1 hover:text-blue-600 hover:underline"
                    >
                      {q.title}
                    </Link>
                  </TableCell>

                  {/* 카테고리 */}
                  <TableCell>
                    <span className="px-2 py-1 text-sm border border-gray-200 rounded bg-gray-50">
                      {q.category}
                    </span>
                  </TableCell>

                  {/* 난이도 / 상태 */}
                  <TableCell>{getStatusBadge(q.status)}</TableCell>

                  {/* 작성자 */}
                  <TableCell>{q.submittedBy}</TableCell>
                  <TableCell>{q.submittedAt}</TableCell>

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
                        {q.status === "pending" && (
                          <>
                            <DropdownMenuItem
                              onClick={() => {
                                handleApprove(q.id)
                                setOpen(false)
                              }}
                            >
                              승인
                            </DropdownMenuItem>
                            <DropdownMenuItem
                              onClick={() => {
                                handleReject(q.id)
                                setOpen(false)
                              }}
                            >
                              거부
                            </DropdownMenuItem>
                          </>
                        )}
                        <DropdownMenuItem
                          onClick={() => alert("✏️ 수정 기능은 준비 중입니다.")}
                        >
                          수정
                        </DropdownMenuItem>
                        <DropdownMenuItem
                          onClick={() => {
                            handleDelete(q.id)
                            setOpen(false)
                          }}
                          className="text-red-600"
                        >
                          삭제
                        </DropdownMenuItem>
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
