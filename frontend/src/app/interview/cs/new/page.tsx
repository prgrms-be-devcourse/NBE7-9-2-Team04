"use client";

import Link from "next/link";
import { useRouter } from "next/navigation";
import { useState } from "react";

export default function CsQuestionCreatePage() {
  const router = useRouter();
  const [formData, setFormData] = useState({
    title: "",
    category: "",
    content: "",
  });

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    router.replace("/interview/cs");
  };

  return (
    <>
      <div className="max-w-4xl mx-auto px-6 py-10">
        <Link
          href="/recruitment"
          className="inline-flex items-center text-sm text-gray-600 hover:text-blue-600 mb-6"
        >
          ← 목록으로
        </Link>

        <div className="border border-yellow-200 bg-yellow-50 p-4 rounded-md mb-6">
          <p className="font-semibold mb-1">질문 등록 안내</p>
          <p className="text-sm">
            등록한 질문은 관리자의 검토 후 승인됩니다. 승인된 질문은 일정
            포인트를 획득합니다.
          </p>
        </div>

        <div className="mb-8">
          <h1 className="text-3xl font-bold mb-2">CS 질문 등록</h1>
          <p className="text-gray-500">새로운 CS 면접 질문을 등록해주세요</p>
        </div>

        <form
          onSubmit={handleSubmit}
          className="space-y-6 bg-white border border-gray-200 rounded-lg shadow-sm p-6"
        >
          <div>
            <label
              htmlFor="title"
              className="block text-sm font-medium text-gray-700 mb-1"
            >
              질문 제목
            </label>
            <input
              id="title"
              type="text"
              placeholder="예: TCP와 UDP의 차이점을 설명하세요"
              value={formData.title}
              onChange={(e) =>
                setFormData({ ...formData, title: e.target.value })
              }
              required
              className="w-full rounded-md border border-gray-300 px-3 py-2 text-sm focus:outline-none focus:ring-2 focus:ring-blue-500"
            />
          </div>

          <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
            <div>
              <label
                htmlFor="category"
                className="block text-sm font-medium text-gray-700 mb-1"
              >
                카테고리
              </label>
              <select
                id="category"
                value={formData.category}
                onChange={(e) =>
                  setFormData({ ...formData, category: e.target.value })
                }
                required
                className="w-full rounded-md border border-gray-300 px-3 py-2 text-sm focus:outline-none focus:ring-2 focus:ring-blue-500"
              >
                <option value="">카테고리 선택</option>
                <option value="네트워크">네트워크</option>
                <option value="운영체제">운영체제</option>
                <option value="데이터베이스">데이터베이스</option>
                <option value="자료구조">자료구조</option>
                <option value="알고리즘">알고리즘</option>
              </select>
            </div>
          </div>

          <div>
            <label
              htmlFor="content"
              className="block text-sm font-medium text-gray-700 mb-1"
            >
              질문 설명
            </label>
            <textarea
              id="content"
              placeholder="질문에 대한 상세한 설명을 작성해주세요."
              value={formData.content}
              onChange={(e) =>
                setFormData({ ...formData, content: e.target.value })
              }
              rows={10}
              required
              className="w-full rounded-md border border-gray-300 px-3 py-2 text-sm focus:outline-none focus:ring-2 focus:ring-blue-500"
            />
          </div>

          <div className="flex justify-end gap-3">
            <Link
              href="/interview/cs"
              className="inline-flex items-center justify-center px-4 py-2 border border-gray-300 rounded-md text-sm text-gray-700 hover:bg-gray-100"
            >
              취소
            </Link>
            <button
              type="submit"
              className="inline-flex items-center justify-center px-4 py-2 rounded-md bg-blue-600 text-white text-sm hover:bg-blue-700"
            >
              등록하기
            </button>
          </div>
        </form>
      </div>
    </>
  );
}
