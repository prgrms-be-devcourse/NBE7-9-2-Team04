"use client";

import {useEffect, useState} from "react";
import { useRouter } from "next/navigation";
import Pagination from "@/components/pagination";
import {fetchApi} from "@/lib/client";
import {SolvedProblem} from "@/lib/solved";

export default function MySolvedPage() {
  const [solvedList, setSolvedList] = useState<SolvedProblem[]>([]);
  const [isLoading, setIsLoading] = useState(false);
  const router = useRouter();
  const [questionPage, setQuestionPage] = useState(1);
  const itemsPerPage = 6;

  const getPagedData = (data: any[], page: number) => {
    const start = (page - 1) * itemsPerPage;
    return data.slice(start, start + itemsPerPage);
  };

  const pagedQuestions = getPagedData(solvedList, questionPage);

    useEffect(() => {
        async function loadUserMyPage() {
            try {
                const apiResponse = await fetchApi(`/api/v1/users`, {
                    method: "GET",
                });
                setSolvedList(apiResponse.data);
            } catch (err) {
                console.error("해결한 문제 탐색 중 오류 발생:", err);
            } finally {
                setIsLoading(false);
            }
        }
        loadUserMyPage();
    }, []);

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
            totalItems={solvedList.length}
            itemsPerPage={itemsPerPage}
            onPageChange={setQuestionPage}
          />
        </div>
      </div>
    </>
  );
}
