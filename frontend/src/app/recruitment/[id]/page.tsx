"use client";

import Link from "next/link";
import { useState } from "react";

// 임시 데이터
const postData = {
    id: "1",
    title: "Next.js 14 프로젝트 팀원 모집",
    description:
      "Next.js 14 App Router를 활용한 커머스 사이트 제작 프로젝트입니다. 디자이너 1명, 프론트엔드 개발자 2명을 찾습니다.",
    category: "프로젝트",
    deadline: "2025-11-25",
    members: "2/4",
    author: "김개발",
    createdAt: "2025-10-10",
    content: `
  ## 프로젝트 소개
  Next.js 14의 최신 기능을 활용하여 현대적인 커머스 사이트를 제작하는 프로젝트입니다.
  
  ## 모집 인원
  - 디자이너 1명
  - 프론트엔드 개발자 2명
  
  ## 필요 기술
  - Next.js 14 (App Router)
  - TypeScript
  - Tailwind CSS
  - Supabase (선택)
  
  ## 프로젝트 기간
  약 3개월 예상
  
  ## 진행 방식
  - 주 2회 온라인 미팅
  - GitHub를 통한 협업
  - Notion으로 문서 관리
  
  ## 지원 방법
  댓글로 간단한 자기소개와 포트폴리오를 남겨주세요!
    `,
  }
  
  const commenData = [
    {
      commentId: "1",
      author: "이프론트",
      content: "안녕하세요! 프론트엔드 개발자로 지원하고 싶습니다. React 3년 경력이 있습니다.",
      createdAt: "2025-10-11",
    },
    {
      commentId: "2",
      author: "박디자인",
      content: "디자이너로 참여하고 싶습니다. 포트폴리오 링크 남깁니다: portfolio.com",
      createdAt: "2025-10-12",
    },
  ]

export default function RecruitmentDetailPage() {
    const [comments, setComments] = useState(commenData);
    const [newComment, setNewComment] = useState("");
  
    const handleSubmitComment = () => {
      if (!newComment.trim()) return
  
      const comment = {
        commentId: `comments.length + 1`,
        author: "현재사용자",
        content: newComment,
        createdAt: new Date().toISOString().split("T")[0],
      }
  
      setComments([...comments, comment])
      setNewComment("")
    }

  
    return(
        <>
            <div className="max-w-4xl mx-auto px-6 py-10">
            <Link
                href="/recruitment"
                className="inline-flex items-center text-sm text-gray-600 hover:text-blue-600 mb-6"
            >
                ← 목록으로
            </Link>

            <div className="bg-white border border-gray-200 rounded-lg shadow-sm p-6">

                <div className="mb-6">
                <div className="flex items-start justify-between mb-3">
                    <span
                    className={`px-2 py-0.5 text-xs font-medium rounded-full ${
                        postData.category === "프로젝트"
                        ? "bg-indigo-50 text-indigo-700"
                        : "bg-green-50 text-green-700"
                    }`}
                    >
                    {postData.category}
                    </span>
                </div>

                <h1 className="text-3xl font-bold mb-2">{postData.title}</h1>
                <p className="text-gray-600 mb-4">{postData.description}</p>

                <div className="flex flex-wrap gap-4 text-sm text-gray-500">
                    <div className="flex items-center gap-2">
                    <span>작성일: {postData.createdAt}</span>
                    </div>
                    <div className="flex items-center gap-2">
                    <span>마감: {postData.deadline}</span>
                    </div>
                    <div className="flex items-center gap-2">
                    <span>모집 인원: {postData.members}</span>
                    </div>
                </div>
                </div>

                <hr className="my-5  border-gray-300" />

                <div className="prose prose-sm max-w-none whitespace-pre-wrap text-gray-800 mb-10">
                {postData.content}
                </div>

                <hr className="my-8  border-gray-300" />

                <div>
                <h3 className="text-xl font-semibold mb-4">
                    댓글 {comments.length}개
                </h3>

                {/* 댓글 */}
                <div className="space-y-4 mb-6">
                    {comments.map((comment) => (
                    <div key={comment.commentId} className="flex gap-3">
                        {/* 사용자 이름 첫글자 아이콘 */}
                        <div className="w-10 h-10 flex items-center justify-center rounded-full bg-gray-200 text-gray-700 font-semibold">
                        {comment.author[0]}
                        </div>


                        <div className="flex-1">
                        <div className="flex items-center gap-2 mb-1">
                            <span className="font-semibold text-sm">
                            {comment.author}
                            </span>
                            <span className="text-xs text-gray-400">
                            {comment.createdAt}
                            </span>
                        </div>
                        <p className="text-sm text-gray-700">{comment.content}</p>
                        </div>
                    </div>
                    ))}
                </div>


                <div>
                    <textarea
                    placeholder="댓글을 작성해주세요"
                    value={newComment}
                    onChange={(e) => setNewComment(e.target.value)}
                    rows={4}
                    className="w-full border border-gray-300 rounded-md px-3 py-2 text-sm focus:outline-none focus:ring-2 focus:ring-blue-500"
                    />
                    <div className="flex justify-end mt-3">
                    <button
                        onClick={handleSubmitComment}
                        className="px-4 py-2 rounded-md bg-blue-600 text-white text-sm hover:bg-blue-700"
                    >
                        댓글 작성
                    </button>
                    </div>
                </div>
                </div>
            </div>
            </div>
        
        
        </>
    )
}
