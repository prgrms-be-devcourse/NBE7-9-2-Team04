"use client";

import { useState } from "react";
import { useRouter } from "next/navigation";
import Pagination from "@/components/pagination";

const solvedProblems = [
  {
    id: "1",
    title: "TCP와 UDP의 차이점",
    category: "네트워크",
    solvedAt: "2025-10-15",
  },
  {
    id: "2",
    title: "프로세스와 스레드",
    category: "운영체제",
    solvedAt: "2025-10-14",
  },
  {
    id: "3",
    title: "프로세스와 스레드",
    category: "운영체제",
    solvedAt: "2025-10-14",
  },
  {
    id: "4",
    title: "프로세스와 스레드",
    category: "운영체제",
    solvedAt: "2025-10-14",
  },
  {
    id: "5",
    title: "프로세스와 스레드",
    category: "운영체제",
    solvedAt: "2025-10-14",
  },
  {
    id: "6",
    title: "프로세스와 스레드",
    category: "운영체제",
    solvedAt: "2025-10-14",
  },
  {
    id: "7",
    title: "프로세스와 스레드",
    category: "운영체제",
    solvedAt: "2025-10-14",
  },
  {
    id: "8",
    title: "프로세스와 스레드",
    category: "운영체제",
    solvedAt: "2025-10-14",
  },
  {
    id: "9",
    title: "프로세스와 스레드",
    category: "운영체제",
    solvedAt: "2025-10-14",
  },
  {
    id: "10",
    title: "프로세스와 스레드",
    category: "운영체제",
    solvedAt: "2025-10-14",
  },
  {
    id: "11",
    title: "프로세스와 스레드",
    category: "운영체제",
    solvedAt: "2025-10-14",
  },
  {
    id: "12",
    title: "프로세스와 스레드",
    category: "운영체제",
    solvedAt: "2025-10-14",
  },
];

export default function MySolvedPage() {
  const router = useRouter();
  const [questionPage, setQuestionPage] = useState(1);
  const itemsPerPage = 6;

  const getPagedData = (data: any[], page: number) => {
    const start = (page - 1) * itemsPerPage;
    return data.slice(start, start + itemsPerPage);
  };

  const pagedQuestions = getPagedData(solvedProblems, questionPage);

  return (
    <>
      <div className="max-w-screen-lg mx-auto px-6 py-10">
        <div className="mb-8">
          <h1 className="text-3xl font-bold mb-2">💡 해결한 문제</h1>
          <p className="text-gray-500">
            내가 해결한 문제를 확인할 수 있습니다.
          </p>
        </div>

        <div className="mt-6 space-y-6">
          <div>
            <div className="space-y-3">
              {pagedQuestions.map((problem) => (
                <div
                  key={problem.id}
                  className="flex justify-between items-center p-3 border  border-gray-200 rounded-md hover:bg-gray-100"
                  onClick={() => router.replace(`/interview/cs/${problem.id}`)}
                >
                  <div>
                    <p className="font-medium">{problem.title}</p>
                    <p className="text-sm text-gray-500">{problem.solvedAt}</p>
                  </div>
                  <span className="px-2 py-1 text-xs rounded text-gray-700 bg-blue-50">
                    {problem.category}
                  </span>
                </div>
              ))}
            </div>
          </div>

          <Pagination
            currentPage={questionPage}
            totalItems={solvedProblems.length}
            itemsPerPage={itemsPerPage}
            onPageChange={setQuestionPage}
          />
        </div>
      </div>
    </>
  );
}