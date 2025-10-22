"use client";

import Link from "next/link";
import { useState, useEffect } from "react";
import { fetchApi } from "@/lib/client";
import { useParams } from "next/navigation";

export default function RecruitmentDetailPage() {
  const params = useParams();
  const postId = params.id
  const [post, setPost] = useState({
    postId: 0,
    title: "",
    introduction: "",
    content: "",
    deadline: "",
    createDate: "",
    modifyDate: "",
    status: "ING",
    pinStatus: "NOT_PINNED",
    recruitCount: 0,
    nickName: "",
    categoryType: "PROJECT"
  });
  
  const [comments, setComments] = useState<any[]>([]);
  const [newComment, setNewComment] = useState("");
  const [totalCount, setTotalCount] = useState(0);
  const [currentPage, setCurrentPage] = useState(1);
  const [totalPages, setTotalPages] = useState(1);
  const [pageSize, setPageSize] = useState(30);

  // 게시글 불러오기
  const fetchPost = async () => {
    try {
      const res = await fetchApi(`/api/v1/posts/${postId}`);
      if (res.status === "OK" && res.data) {
        const formatted = {
          ...res.data,
          categoryType:
            res.data.categoryType === "PROJECT"
              ? "프로젝트"
              : res.data.categoryType === "STUDY"
              ? "스터디"
              : res.data.categoryType, // 그 외는 그대로
            createDate: res.data.createDate?.split("T")[0], // 날짜만
            modifyDate: res.data.modifyDate?.split("T")[0],
            deadline: res.data.deadline?.split("T")[0],
        };
        setPost(formatted);
      } else {
        console.error("게시글 불러오기 실패", res.message);
      }
    } catch (err) {
      console.error("게시글 불러오기 실패:", err);
    }
  };
  
  useEffect(() => {
    fetchPost();
  }, []);

  // 댓글 불러오기
  const fetchComments = async (page = 1) => {
    if (!postId) return; 
    try {
      const res = await fetchApi(
        `/api/v1/posts/${post.postId}/comments?page=${page}`
      );
      if (res.status === "OK" && Array.isArray(res.data.comments)) {
        const formatted = res.data.comments.map((c: any) => ({
          commentId: c.id,
          author: c.authorNickName,
          authorId: c.authorId, 
          content: c.content,
          createdAt: c.createDate.split("T")[0],
        }));
        setComments(formatted);
        setTotalCount(res.data.totalCount || 0);
        setCurrentPage(res.data.currentPage || 1);
        setTotalPages(res.data.totalPages || 1);
        setPageSize(res.data.pageSize || 10);
      } else {
        setComments([]);
        setTotalCount(0);
      }
    } catch (err) {
      console.error("댓글 불러오기 실패:", err);
      setComments([]);
      setTotalCount(0);
    }
  };

  useEffect(() => {
    if (post.postId) fetchComments();
  }, [post.postId]);

  // 댓글 작성
  const handleSubmitComment = async () => {
    if (!newComment.trim()) return;
  
    try {
      const res = await fetchApi(`/api/v1/posts/${post.postId}/comments`, {
        method: "POST",
        body: JSON.stringify({ content: newComment }),
      });
  
      if (res.status === "CREATED" && res.data) {
        setNewComment("");
        // 전체 댓글 수 갱신
        const newTotalCount = totalCount + 1;
        setTotalCount(newTotalCount);
  
        // 마지막 페이지 계산
        const lastPage = Math.ceil(newTotalCount / pageSize);
        handlePageChange(lastPage);
      } else {
        alert(res.message || "댓글 작성 실패");
      }
    } catch (err: any) {
      alert(err.message);
    }
  };

  // 댓글 삭제
  const handleDeleteComment = async (commentId: string) => {
    if (!confirm("정말로 댓글을 삭제하시겠습니까?")) return;

    try {
      const res = await fetchApi(`/api/v1/posts/${post.postId}/comments/${commentId}`, {
        method: "DELETE",
      });

      if (res.status === "OK") {
        // 삭제 성공 시 해당 댓글 제거
        setComments((prev) => prev.filter((c) => c.commentId !== commentId));
        setTotalCount((prev) => prev - 1);
        alert("댓글이 삭제되었습니다.")
      } else {
        alert(res.message || "댓글 삭제 실패");
      }
    } catch (err: any) {
      alert(err.message);
    }
  };

  // 페이지 변경
  const handlePageChange = (page: number) => {
    fetchComments(page);
  };

  return (
    <div className="max-w-4xl mx-auto px-6 py-10">
      <Link
        href="/recruitment"
        className="inline-flex items-center text-sm text-gray-600 hover:text-blue-600 mb-6"
      >
        ← 목록으로
      </Link>

      <div className="bg-white border border-gray-200 rounded-lg shadow-sm p-6">
        {/* 게시글 헤더 */}
        <div className="mb-6">
          <div className="flex items-start justify-between mb-3">
            <span
              className={`px-2 py-0.5 text-xs font-medium rounded-full ${
                post.categoryType === "프로젝트"
                  ? "bg-indigo-50 text-indigo-700"
                  : "bg-green-50 text-green-700"
              }`}
            >
              {post.categoryType}
            </span>
          </div>

          <h1 className="text-3xl font-bold mb-2">{post.title}</h1>
          <p className="text-gray-600 mb-4">{post.introduction}</p>

          <div className="flex flex-wrap gap-4 text-sm text-gray-500">
            <div className="flex items-center gap-2">
              <span>작성일: {post.createDate}</span>
            </div>
            <div className="flex items-center gap-2">
              <span>마감: {post.deadline}</span>
            </div>
            <div className="flex items-center gap-2">
              <span>모집 인원: {post.recruitCount}</span>
            </div>
          </div>
        </div>

        <hr className="my-5 border-gray-300" />

          <div className="prose prose-sm max-w-none whitespace-pre-wrap text-gray-800 mb-10">
            {post.content}
          </div>

        <hr className="my-8 border-gray-300" />

        {/* 댓글 헤더: 댓글 개수와 페이지 버튼 한 줄 */}
        <div className="flex justify-between items-center mb-4">
          <h3 className="text-xl font-semibold">댓글 {totalCount}개</h3>

          <div className="flex items-center gap-2">
            <span className="text-lg font-semibold">
              {currentPage} / {totalPages}
            </span>
            <button
              onClick={() => handlePageChange(currentPage - 1)}
              disabled={currentPage === 1}
              className="px-3 py-1 rounded bg-gray-200 hover:bg-gray-300 cursor-pointer disabled:opacity-50 disabled:cursor-default"
            >
              &lt;
            </button>
            <button
              onClick={() => handlePageChange(currentPage + 1)}
              disabled={currentPage === totalPages}
              className="px-3 py-1 rounded bg-gray-200 hover:bg-gray-300 cursor-pointer disabled:opacity-50 disabled:cursor-default"
            >
              &gt;
            </button>
          </div>
        </div>

        {/* 댓글 목록 */}
        <div className="space-y-4 mb-6">
          {comments.map((comment) => {
            const currentUserId = parseInt(localStorage.getItem("currentUserId") || "0", 10);
            const isMyComment = currentUserId === comment.authorId;

            return (
              <div key={comment.commentId} className="flex gap-3 items-start">
                <div className="w-10 h-10 flex items-center justify-center rounded-full bg-gray-200 text-gray-700 font-semibold">
                  {comment.author[0]}
                </div>

                <div className="flex-1">
                  <div className="flex items-center gap-2 mb-1 justify-between">
                    <div className="flex items-center gap-2">
                      <span className="font-semibold text-sm">{comment.author}</span>
                      <span className="text-xs text-gray-400">{comment.createdAt}</span>
                    </div>

                    {/* 내 댓글일 때만 삭제 버튼 */}
                    {isMyComment && (
                      <button
                        onClick={() => handleDeleteComment(comment.commentId)}
                        className="text-xs hover:underline cursor-pointer"
                      >
                        삭제
                      </button>
                    )}
                  </div>
                  <p className="text-sm text-gray-700">{comment.content}</p>
                </div>
              </div>
            );
          })}
        </div>

        {/* 댓글 작성 */}
        <div className="mb-6">
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
              className="px-4 py-2 rounded-md bg-blue-600 text-white text-sm hover:bg-blue-700 cursor-pointer"
            >
              댓글 작성
            </button>
          </div>
        </div>

        {/* 페이지 번호 UI */}
        <div className="flex gap-2 justify-center">
          {Array.from({ length: totalPages }, (_, i) => i + 1).map((page) => (
            <button
              key={page}
              onClick={() => handlePageChange(page)}
              className={`px-3 py-1 rounded ${
                currentPage === page
                  ? "bg-blue-600 text-white"
                  : "bg-gray-200 hover:bg-gray-300 cursor-pointer"
              }`}
            >
              {page}
            </button>
          ))}
        </div>
      </div>
    </div>
  );
}
