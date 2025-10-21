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


const mockRecruitmentPosts = [
  {
    id: "r1",
    title: "Next.js 14 프로젝트 팀원 모집",
    author: "김개발",
    category: "프로젝트",
    status: "visible",
    createdAt: "2025-10-10",
  },
  {
    id: "r2",
    title: "알고리즘 스터디 모집",
    author: "박알고",
    category: "스터디",
    status: "visible",
    createdAt: "2025-10-12",
  },
  {
    id: "r3",
    title: "부적절한 모집글",
    author: "악의사용자",
    category: "프로젝트",
    status: "hidden",
    createdAt: "2025-10-13",
  },
  {
    id: "r4",
    title: "알고리즘 스터디 모집",
    author: "박알고",
    category: "스터디",
    status: "visible",
    createdAt: "2025-10-12",
  },
  {
    id: "r5",
    title: "알고리즘 스터디 모집",
    author: "박알고",
    category: "스터디",
    status: "visible",
    createdAt: "2025-10-12",
  },
  {
    id: "r6",
    title: "알고리즘 스터디 모집",
    author: "박알고",
    category: "스터디",
    status: "visible",
    createdAt: "2025-10-12",
  },
  {
    id: "r7",
    title: "알고리즘 스터디 모집",
    author: "박알고",
    category: "스터디",
    status: "visible",
    createdAt: "2025-10-12",
  },
  {
    id: "r8",
    title: "알고리즘 스터디 모집",
    author: "박알고",
    category: "스터디",
    status: "visible",
    createdAt: "2025-10-12",
  },
  {
    id: "r9",
    title: "알고리즘 스터디 모집",
    author: "박알고",
    category: "스터디",
    status: "visible",
    createdAt: "2025-10-12",
  },
  {
    id: "r10",
    title: "알고리즘 스터디 모집",
    author: "박알고",
    category: "스터디",
    status: "visible",
    createdAt: "2025-10-12",
  },
  {
    id: "r11",
    title: "알고리즘 스터디 모집",
    author: "박알고",
    category: "스터디",
    status: "visible",
    createdAt: "2025-10-12",
  },

]

const mockQnAPosts = [
  {
    id: "qna1",
    title: "프리미엄 멤버십 결제는 어떻게 하나요?",
    author: "김질문",
    category: "결제",
    status: "visible",
    createdAt: "2025-10-15",
  },
  {
    id: "qna2",
    title: "티어 시스템은 어떻게 작동하나요?",
    author: "이궁금",
    category: "시스템",
    status: "visible",
    createdAt: "2025-10-14",
  },
]


export default function AdminPostsPage() {
  const [postType, setPostType] = useState<"recruitment" | "qna">("recruitment")
  const [recruitmentPosts, setRecruitmentPosts] = useState(mockRecruitmentPosts)
  const [qnaPosts, setQnaPosts] = useState(mockQnAPosts)

  const currentPosts = postType === "recruitment" ? recruitmentPosts : qnaPosts
  const setCurrentPosts = postType === "recruitment" ? setRecruitmentPosts : setQnaPosts


  const handleHide = (postId: string) => {
    setCurrentPosts(currentPosts.map((p) => (p.id === postId ? { ...p, status: "hidden" } : p)))
    alert("게시글이 숨김 처리되었습니다.")
  }

  const handleShow = (postId: string) => {
    setCurrentPosts(currentPosts.map((p) => (p.id === postId ? { ...p, status: "visible" } : p)))
    alert("게시글이 다시 표시됩니다.")
  }

  const handleDelete = (postId: string) => {
    setCurrentPosts(currentPosts.filter((p) => p.id !== postId))
    alert("게시글이 삭제되었습니다.")
  }

  const getStatusBadge = (status: string) => {
    const base = "px-2 py-1 text-sm rounded"
    return status === "visible" ? (
      <span className={`${base} bg-green-100 text-green-700`}>진행중</span>
    ) : (
      <span className={`${base} bg-gray-100 text-gray-700`}>마감</span>
    )
  }

  return (
    <div className="max-w-7xl mx-auto p-8 space-y-6">

      <div>
        <h1 className="text-3xl font-bold mb-2">📰 게시글 관리</h1>
        <p className="text-gray-500">모집글과 QnA 게시글을 관리합니다</p>
      </div>


      <div className="flex gap-3 mb-4">
        <button
          onClick={() => setPostType("recruitment")}
          className={`px-4 py-2 rounded-md border text-sm font-medium transition ${
            postType === "recruitment"
              ? "bg-blue-600 text-white border-blue-600"
              : "bg-white border-gray-300 text-gray-700 hover:bg-gray-50"
          }`}
        >
          모집글
        </button>
        <button
          onClick={() => setPostType("qna")}
          className={`px-4 py-2 rounded-md border text-sm font-medium transition ${
            postType === "qna"
              ? "bg-blue-600 text-white border-blue-600"
              : "bg-white border-gray-300 text-gray-700 hover:bg-gray-50"
          }`}
        >
          QnA
        </button>
      </div>


      <div className="overflow-x-auto bg-white border border-gray-200 shadow-sm rounded-lg">
        <Table>
          <TableHeader>
            <TableRow>
              <TableHead>제목</TableHead>
              <TableHead>작성자</TableHead>
              <TableHead>카테고리</TableHead>
              <TableHead>상태</TableHead>
              <TableHead>작성일</TableHead>
              <TableHead>작업</TableHead>
            </TableRow>
          </TableHeader>

          <TableBody>
            {currentPosts.map((post) => {
              const { ref, open, setOpen } = useDropdown()
              return (
                <TableRow key={post.id}>
                  <TableCell className="max-w-xs">
                    <Link
                      href={
                        postType === "recruitment"
                          ? `/recruitment/${post.id}`
                          : `/qna/${post.id}`
                      }
                      className="font-medium line-clamp-1 hover:text-blue-600 hover:underline"
                    >
                      {post.title}
                    </Link>
                  </TableCell>

                  <TableCell>{post.author}</TableCell>
                  <TableCell>
                    <span className="px-2 py-1 text-sm border border-gray-200 rounded bg-gray-50">
                      {post.category}
                    </span>
                  </TableCell>
                  <TableCell>{getStatusBadge(post.status)}</TableCell>
                  <TableCell>{post.createdAt}</TableCell>

                  {/* Dropdown 메뉴 */}
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
                        {post.status === "visible" ? (
                          <DropdownMenuItem
                            onClick={() => {
                              handleHide(post.id)
                              setOpen(false)
                            }}
                          >
                            마감 처리
                          </DropdownMenuItem>
                        ) : (
                          <DropdownMenuItem
                            onClick={() => {
                              handleShow(post.id)
                              setOpen(false)
                            }}
                          >
                            표시하기
                          </DropdownMenuItem>
                        )}
                        <DropdownMenuItem
                          onClick={() => {
                            handleDelete(post.id)
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
