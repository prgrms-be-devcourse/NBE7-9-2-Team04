"use client";

import { useEffect, useState } from "react";
import { fetchApi } from "@/lib/client";
import {
  Table,
  TableBody,
  TableCell,
  TableHead,
  TableHeader,
  TableRow,
} from "@/components/ui/table";
import {
  DropdownMenuContent,
  DropdownMenuItem,
} from "@/components/ui/dropdown-menu";
import { Qna } from "@/types/qna";

export default function AdminQnaPage() {
  const [qnaList, setQnaList] = useState<Qna[]>([]);
  const [isLoading, setIsLoading] = useState(true);
  const [errorMsg, setErrorMsg] = useState("");
  const [openMenuId, setOpenMenuId] = useState<number | null>(null);

  //모달 관련 상태
  const [selectedQna, setSelectedQna] = useState<Qna | null>(null);
  const [answerText, setAnswerText] = useState("");
  const [isSubmitting, setIsSubmitting] = useState(false);

  //전체 QnA 불러오기
  const fetchQnaList = async () => {
    try {
      setIsLoading(true);
      const apiResponse = await fetchApi("/api/v1/admin/qna", { method: "GET" });

      if (apiResponse.status === "OK") {
        setQnaList(apiResponse.data);
      } else {
        setErrorMsg(apiResponse.message || "목록을 불러오지 못했습니다.");
      }
    } catch (error: any) {
      setErrorMsg(error.message || "서버 오류가 발생했습니다.");
    } finally {
      setIsLoading(false);
    }
  };

  useEffect(() => {
    fetchQnaList();
  }, []);

  //QnA 삭제
  const handleDelete = async (qnaId: number) => {
    if (!confirm("정말 삭제하시겠습니까?")) return;

    try {
      const apiResponse = await fetchApi(`/api/v1/admin/qna/${qnaId}`, {
        method: "DELETE",
      });

      if (apiResponse.status === "OK") {
        alert("QnA가 삭제되었습니다.");
        setQnaList((prev) => prev.filter((q) => q.qnaId !== qnaId));
      } else {
        alert(apiResponse.message || "삭제에 실패했습니다.");
      }
    } catch {
      alert("서버 오류가 발생했습니다.");
    }
  };

  //답변 등록
  const handleSubmitAnswer = async () => {
    if (!selectedQna) return;
    if (!answerText.trim()) {
      alert("답변 내용을 입력해주세요.");
      return;
    }

    try {
      setIsSubmitting(true);
      const apiResponse = await fetchApi(`/api/v1/admin/qna/${selectedQna.qnaId}/answer`, {
        method: "PUT",
        body: JSON.stringify({ answer: answerText }),
      });

      if (apiResponse.status === "OK") {
        alert("답변이 등록되었습니다!");
        setSelectedQna(null);
        setAnswerText("");
        fetchQnaList();
      } else {
        alert(apiResponse.message || "등록에 실패했습니다.");
      }
    } catch {
      alert("서버 오류가 발생했습니다.");
    } finally {
      setIsSubmitting(false);
    }
  };

  //상태 뱃지
  const getAnswerBadge = (isAnswered: boolean) => {
    const base = "px-2 py-1 text-sm rounded";
    return isAnswered ? (
      <span className={`${base} bg-green-100 text-green-700`}>답변 완료</span>
    ) : (
      <span className={`${base} bg-yellow-100 text-yellow-700`}>대기 중</span>
    );
  };

  //카테고리 뱃지
  const getCategoryBadge = (category: string) => {
    const colorMap: Record<string, string> = {
      계정: "bg-blue-50 text-blue-600 border-blue-200",
      결제: "bg-pink-50 text-pink-600 border-pink-200",
      시스템: "bg-indigo-50 text-indigo-600 border-indigo-200",
      모집: "bg-emerald-50 text-emerald-600 border-emerald-200",
      제안: "bg-orange-50 text-orange-600 border-orange-200",
      기타: "bg-gray-50 text-gray-600 border-gray-200",
    };
    return (
      <span
        className={`px-2 py-1 text-sm border rounded ${colorMap[category] || ""}`}
      >
        {category}
      </span>
    );
  };

  const handleSelectQna = (qna: Qna) => {
    setSelectedQna(qna);
    setAnswerText(qna.adminAnswer || "");
  };

  if (isLoading)
    return <div className="flex justify-center items-center py-20 text-gray-500">로딩 중...</div>;
  if (errorMsg) return <div className="text-center text-red-600 py-20">{errorMsg}</div>;

  return (
    <div className="max-w-7xl mx-auto p-8 space-y-6">
      <div>
        <h1 className="text-3xl font-bold mb-2">💬 QnA 관리</h1>
        <p className="text-gray-500">사용자 QnA를 관리합니다</p>
      </div>

      {/* 테이블 */}
      <div className="overflow-x-auto bg-white border border-gray-200 shadow-sm rounded-lg">
        <Table>
          <TableHeader>
            <TableRow>
              <TableHead>제목</TableHead>
              <TableHead>작성자</TableHead>
              <TableHead>카테고리</TableHead>
              <TableHead>답변 상태</TableHead>
              <TableHead>작성일</TableHead>
              <TableHead>작업</TableHead>
            </TableRow>
          </TableHeader>

          <TableBody>
            {qnaList.map((qna) => (
              <TableRow key={qna.qnaId}>
                <TableCell
                  className="max-w-xs cursor-pointer hover:text-blue-600 hover:underline"
                  onClick={() => handleSelectQna(qna)}
                >
                  {qna.title}
                </TableCell>
                <TableCell>{qna.authorNickname}</TableCell>
                <TableCell>{getCategoryBadge(qna.categoryName)}</TableCell>
                <TableCell>{getAnswerBadge(qna.isAnswered)}</TableCell>
                <TableCell>{new Date(qna.createdDate).toLocaleDateString()}</TableCell>
                <TableCell className="text-right">
                  <div className="relative inline-block">
                    <button
                      onClick={() =>
                        setOpenMenuId(openMenuId === qna.qnaId ? null : qna.qnaId)
                      }
                      className="p-2 rounded hover:bg-gray-100 text-gray-600"
                    >
                      ⋮
                    </button>

                    {openMenuId === qna.qnaId && (
                      <DropdownMenuContent open align="end">
                        <DropdownMenuItem
                          onClick={() => handleDelete(qna.qnaId)}
                          className="text-red-600"
                        >
                          삭제
                        </DropdownMenuItem>
                      </DropdownMenuContent>
                    )}
                  </div>
                </TableCell>
              </TableRow>
            ))}
          </TableBody>
        </Table>
      </div>

      {/* QnA 상세 모달 */}
      {selectedQna && (
        <div className="fixed inset-0 bg-black/40 flex justify-center items-center z-50">
          <div className="bg-white rounded-lg shadow-lg w-full max-w-2xl p-6 relative">
            <button
              className="absolute top-3 right-4 text-gray-400 hover:text-gray-600"
              onClick={() => setSelectedQna(null)}
            >
              ✕
            </button>

            <h2 className="text-2xl font-bold mb-4">{selectedQna.title}</h2>

            <div className="text-gray-800 whitespace-pre-wrap border p-4 rounded-lg bg-gray-50 mb-4">
              {selectedQna.content}
            </div>

            <div className="border-t pt-4 mt-4">
              <h3 className="text-lg font-semibold mb-2">관리자 답변</h3>

              {/* 답변이 이미 존재할 때 */}
              {selectedQna.isAnswered && selectedQna.adminAnswer ? (
                <div className="border rounded-lg p-4 bg-green-50 text-gray-800 whitespace-pre-wrap">
                  {selectedQna.adminAnswer}
                </div>
              ) : (
                <>
                  <textarea
                    className="w-full border rounded-lg px-3 py-2 min-h-[150px] focus:ring-2 focus:ring-blue-200"
                    placeholder="답변을 입력하세요..."
                    value={answerText}
                    onChange={(e) => setAnswerText(e.target.value)}
                  />

                  <div className="flex justify-end gap-3 mt-4">
                    <button
                      className="px-4 py-2 border rounded-md hover:bg-gray-50"
                      onClick={() => setSelectedQna(null)}
                    >
                      취소
                    </button>
                    <button
                      className={`px-4 py-2 rounded-md text-white ${
                        isSubmitting
                          ? "bg-gray-400 cursor-not-allowed"
                          : "bg-blue-600 hover:bg-blue-700"
                      }`}
                      onClick={handleSubmitAnswer}
                      disabled={isSubmitting}
                    >
                      {isSubmitting ? "등록 중..." : "답변 등록"}
                    </button>
                  </div>
                </>
              )}
            </div>
          </div>
        </div>
      )}
    </div>
  );
}
