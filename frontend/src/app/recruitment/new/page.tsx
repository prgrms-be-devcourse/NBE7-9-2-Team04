"use client";

import Link from "next/link";
import { useRouter } from "next/navigation";
import { useState } from "react";

export default function RecruitmentCreatePage() {
    const router = useRouter();
    const [formData, setFormData] = useState({
        title: "",
        description: "",
        category: "",
        deadline: "",
        maxMembers: "",
        content: "",
      })
    
      const handleSubmit = (e: React.FormEvent) => {
        e.preventDefault();
        router.replace("/recruitment");
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


            <div className="mb-8">
                <h1 className="text-3xl font-bold mb-2">모집글 작성</h1>
                <p className="text-gray-500">팀 프로젝트 또는 스터디 모집글을 작성해주세요</p>
            </div>

            <form onSubmit={handleSubmit} className="space-y-6 bg-white border border-gray-200 rounded-lg shadow-sm p-6">
                <div>
                <label htmlFor="title" className="block text-sm font-medium text-gray-700 mb-1">
                    제목
                </label>
                <input
                    id="title"
                    type="text"
                    placeholder="모집글 제목을 입력하세요"
                    value={formData.title}
                    onChange={(e) => setFormData({ ...formData, title: e.target.value })}
                    required
                    className="w-full rounded-md border border-gray-300 px-3 py-2 text-sm focus:outline-none focus:ring-2 focus:ring-blue-500"
                />
                </div>


                <div>
                <label htmlFor="description" className="block text-sm font-medium text-gray-700 mb-1">
                    한 줄 소개
                </label>
                <input
                    id="description"
                    type="text"
                    placeholder="한 줄로 프로젝트를 소개해주세요"
                    value={formData.description}
                    onChange={(e) => setFormData({ ...formData, description: e.target.value })}
                    required
                    className="w-full rounded-md border border-gray-300 px-3 py-2 text-sm focus:outline-none focus:ring-2 focus:ring-blue-500"
                />
                </div>


                <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
                <div>
                    <label htmlFor="category" className="block text-sm font-medium text-gray-700 mb-1">
                    카테고리
                    </label>
                    <select
                    id="category"
                    value={formData.category}
                    onChange={(e) => setFormData({ ...formData, category: e.target.value })}
                    required
                    className="w-full rounded-md border border-gray-300 px-3 py-2 text-sm focus:outline-none focus:ring-2 focus:ring-blue-500"
                    >
                    <option value="">카테고리 선택</option>
                    <option value="프로젝트">프로젝트</option>
                    <option value="스터디">스터디</option>
                    </select>
                </div>

                <div>
                    <label htmlFor="deadline" className="block text-sm font-medium text-gray-700 mb-1">
                    모집 마감일
                    </label>
                    <input
                    id="deadline"
                    type="date"
                    value={formData.deadline}
                    onChange={(e) => setFormData({ ...formData, deadline: e.target.value })}
                    required
                    className="w-full rounded-md border border-gray-300 px-3 py-2 text-sm focus:outline-none focus:ring-2 focus:ring-blue-500"
                    />
                </div>
                </div>


                <div>
                <label htmlFor="maxMembers" className="block text-sm font-medium text-gray-700 mb-1">
                    모집 인원
                </label>
                <input
                    id="maxMembers"
                    type="number"
                    placeholder="최대 모집 인원"
                    value={formData.maxMembers}
                    onChange={(e) => setFormData({ ...formData, maxMembers: e.target.value })}
                    required
                    className="w-full rounded-md border border-gray-300 px-3 py-2 text-sm focus:outline-none focus:ring-2 focus:ring-blue-500"
                />
                </div>


                <div>
                <label htmlFor="content" className="block text-sm font-medium text-gray-700 mb-1">
                    상세 내용
                </label>
                <textarea
                    id="content"
                    placeholder="프로젝트 소개, 필요 기술, 진행 방식 등을 자세히 작성해주세요"
                    value={formData.content}
                    onChange={(e) => setFormData({ ...formData, content: e.target.value })}
                    rows={15}
                    required
                    className="w-full rounded-md border border-gray-300 px-3 py-2 text-sm focus:outline-none focus:ring-2 focus:ring-blue-500"
                />
                </div>


                <div className="flex justify-end gap-3">
                <Link
                    href="/recruitment"
                    className="inline-flex items-center justify-center px-4 py-2 border border-gray-300 rounded-md text-sm text-gray-700 hover:bg-gray-100"
                >
                    취소
                </Link>
                <button
                    type="submit"
                    className="inline-flex items-center justify-center px-4 py-2 rounded-md bg-blue-600 text-white text-sm hover:bg-blue-700"
                >
                    작성 완료
                </button>
                </div>
            </form>
            </div>
        </>
    )
  
}
