"use client";

import { useEffect, useState } from "react";
import { useRouter } from "next/navigation";
import CategoryTab from "@/components/categoryTab";
import Pagination from "@/components/pagination";
import { fetchApi } from "@/lib/client";
import { UserResponse } from "@/types/user";
import { PostResponse, PostPageResponse } from "@/types/post";
import { CommentMypageResponse, CommentPageResponse } from "@/types/comment";
import { QuestionResponse, QuestionPageResponse } from "@/types/question";

export default function MyActivityPage() {
  const router = useRouter();

  const [userId, setUserId] = useState<number | null>(null);
  const [selectedCategory, setSelectedCategory] = useState("작성한 글");
  const categories = ["작성한 글", "작성한 댓글", "등록한 질문"];

  const [userPosts, setUserPosts] = useState<PostResponse[]>([]);
  const [userComments, setUserComments] = useState<CommentMypageResponse[]>([]);
  const [userQuestions, setUserQuestions] = useState<QuestionResponse[]>([]);

  const [postPage, setPostPage] = useState(1);
  const [commentPage, setCommentPage] = useState(1);
  const [questionPage, setQuestionPage] = useState(1);

  const itemsPerPage = 15;

  const [totalPost, setTotalPost] = useState(0);
  const [totalComment, setTotalComment] = useState(0);
  const [totalQuestion, setTotalQuestion] = useState(0);

  // 로그인된 유저 정보
  useEffect(() => {
    const fetchUser = async () => {
      try {
        const res = await fetchApi("/api/v1/users/check", { method: "GET" });
        if (res.status === "OK") {
          const user: UserResponse = res.data;
          setUserId(user.id);
        }
      } catch (err) {
        console.error("유저 정보 조회 실패:", err);
      }
    };
    fetchUser();
  }, []);

  const fetchUserPosts = async (id: number) => {
    try {
      const res = await fetchApi(`/api/v1/users/${id}/posts?page=${postPage}`);
      if (res.status === "OK") {
        const data: PostPageResponse<PostResponse> = res.data;
        setUserPosts(data.posts);
        setTotalPost(data.totalCount);
      }
    } catch (err) {
      console.error("게시글 조회 실패:", err);
    }
  };

  const fetchUserComments = async (id: number) => {
    try {
      const res = await fetchApi(
        `/api/v1/users/${id}/comments?page=${commentPage}`
      );
      if (res.status === "OK") {
        const data: CommentPageResponse<CommentMypageResponse> = res.data;
        setUserComments(data.comments);
        setTotalComment(data.totalCount);
      }
    } catch (err) {
      console.error("댓글 조회 실패:", err);
    }
  };

  const fetchUserQuestions = async (id: number) => {
    try {
      const res = await fetchApi(
        `/api/v1/users/${id}/questions?page=${questionPage}`
      );
      if (res.status === "OK") {
        const data: QuestionPageResponse<QuestionResponse> = res.data;
        setUserQuestions(data.questions);
        setTotalQuestion(data.totalCount);
      }
    } catch (err) {
      console.error("질문 조회 실패:", err);
    }
  };

  useEffect(() => {
    if (!userId) return;
    if (selectedCategory === "작성한 글") fetchUserPosts(userId);
    if (selectedCategory === "작성한 댓글") fetchUserComments(userId);
    if (selectedCategory === "등록한 질문") fetchUserQuestions(userId);
  }, [selectedCategory, userId, postPage, commentPage, questionPage]);

  return (
    <div className="max-w-screen-lg mx-auto px-6 py-10">
      <div className="mb-8">
        <h1 className="text-3xl font-bold mb-2">📝 내 활동</h1>
        <p className="text-gray-500">
          내가 작성한 글, 댓글, 질문을 확인할 수 있습니다.
        </p>
      </div>

      <CategoryTab
        categories={categories}
        selected={selectedCategory}
        onSelect={(c) => {
          setSelectedCategory(c);
          setPostPage(1);
          setCommentPage(1);
          setQuestionPage(1);
        }}
      />

      <div className="mt-6 space-y-6">
        {/* 🟢 작성한 글 */}
        {selectedCategory === "작성한 글" && (
          <div>
            <div className="space-y-3">
              {userPosts.length === 0 ? (
                <div className="text-center text-gray-400 py-16 border border-gray-200 rounded-md">
                  아직 작성한 글이 없습니다.
                </div>
              ) : (
                userPosts.map((post) => (
                  <div
                    key={post.postId}
                    onClick={() =>
                      router.replace(`/recruitment/${post.postId}`)
                    }
                    className="flex justify-between items-center p-3 border border-gray-200 rounded-md hover:bg-gray-50 transition cursor-pointer"
                  >
                    <div>
                      <p className="font-medium">{post.title}</p>
                      <p className="text-sm text-gray-500">
                        {new Date(post.createDate).toLocaleDateString()}
                      </p>
                    </div>
                    <span
                      className={`px-2 py-1 text-xs rounded font-medium ${
                        post.status === "ING"
                          ? "bg-green-50 text-green-700"
                          : "bg-gray-100 text-gray-500"
                      }`}
                    >
                      {post.status === "ING" ? "모집중" : "마감"}
                    </span>
                  </div>
                ))
              )}
            </div>

            <Pagination
              currentPage={postPage}
              totalItems={totalPost}
              itemsPerPage={itemsPerPage}
              onPageChange={setPostPage}
            />
          </div>
        )}

        {/* 🟡 작성한 댓글 */}
        {selectedCategory === "작성한 댓글" && (
          <div>
            <div className="space-y-3">
              {userComments.length === 0 ? (
                <div className="text-center text-gray-400 py-16 border border-gray-200 rounded-md">
                  아직 작성한 댓글이 없습니다.
                </div>
              ) : (
                userComments.map((comment) => (
                  <div
                    key={comment.id}
                    onClick={() =>
                      router.replace(`/recruitment/${comment.postId}`)
                    }
                    className="p-3 border border-gray-200 rounded-md hover:bg-gray-50 transition cursor-pointer"
                  >
                    <p className="font-medium">{comment.content}</p>
                    <p className="text-xs text-gray-500">
                      {comment.postTitle} •{" "}
                      {new Date(comment.createDate).toLocaleDateString()}
                    </p>
                  </div>
                ))
              )}
            </div>

            <Pagination
              currentPage={commentPage}
              totalItems={totalComment}
              itemsPerPage={itemsPerPage}
              onPageChange={setCommentPage}
            />
          </div>
        )}

        {/* 🟣 등록한 질문 */}
        {selectedCategory === "등록한 질문" && (
          <div>
            <div className="space-y-3">
              {userQuestions.length === 0 ? (
                <div className="text-center text-gray-400 py-16 border border-gray-200 rounded-md">
                  아직 등록한 질문이 없습니다.
                </div>
              ) : (
                userQuestions.map((question) => (
                  <div
                    key={question.questionId}
                    onClick={() =>
                      router.replace(`/interview/cs/${question.questionId}`)
                    }
                    className="flex justify-between items-center p-3 border border-gray-200 rounded-md hover:bg-gray-50 transition cursor-pointer"
                  >
                    <div>
                      <p className="font-medium">{question.title}</p>
                      <p className="text-sm text-gray-500">
                        {question.categoryType}
                      </p>
                    </div>
                    <span
                      className={`px-2 py-1 text-xs rounded font-medium ${
                        question.isApproved
                          ? "bg-green-50 text-green-700"
                          : "bg-yellow-50 text-yellow-700"
                      }`}
                    >
                      {question.isApproved ? "승인됨" : "검토 중"}
                    </span>
                  </div>
                ))
              )}
            </div>

            <Pagination
              currentPage={questionPage}
              totalItems={userQuestions.length}
              itemsPerPage={itemsPerPage}
              onPageChange={setQuestionPage}
            />
          </div>
        )}
      </div>
    </div>
  );
}
