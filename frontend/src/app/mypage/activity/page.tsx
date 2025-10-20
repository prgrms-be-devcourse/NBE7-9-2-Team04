"use client";

import { useState } from "react";
import { useRouter } from "next/navigation";
import CategoryTab from "@/components/categoryTab";
import Pagination from "@/components/pagination";

const userPosts = [
  {
    id: "1",
    title: "Next.js 14 프로젝트 팀원 모집",
    status: "모집중",
    date: "2025-10-10",
  },
  { id: "2", title: "React 스터디 모집", status: "마감", date: "2025-10-08" },
];

const userComments = [
  {
    id: "1",
    postId: "1",
    content: "좋은 프로젝트네요! 참여하고 싶습니다.",
    postTitle: "Next.js 14 프로젝트 팀원 모집",
    date: "2025-10-12",
  },
  {
    id: "2",
    postId: "2",
    content: "저도 같은 문제를 겪었는데...",
    postTitle: "React 스터디 모집",
    date: "2025-10-11",
  },
];

const userQuestions = [
  {
    id: "1",
    title: "TCP와 UDP의 차이점을 설명하세요",
    category: "네트워크",
    score: 10,
    isApproved: false,
  },
  {
    id: "2",
    title: "프로세스와 스레드의 차이는 무엇인가요?",
    category: "운영체제",
    score: 5,
    isApproved: true,
  },
  {
    id: "3",
    title: "데이터베이스 정규화에 대해 설명하세요",
    category: "데이터베이스",
    score: 10,
    isApproved: false,
  },
  {
    id: "4",
    title: "데이터베이스 정규화에 대해 설명하세요",
    category: "데이터베이스",
    score: 10,
    isApproved: false,
  },
  {
    id: "5",
    title: "데이터베이스 정규화에 대해 설명하세요",
    category: "데이터베이스",
    score: 10,
    isApproved: false,
  },
  {
    id: "6",
    title: "데이터베이스 정규화에 대해 설명하세요",
    category: "데이터베이스",
    score: 10,
    isApproved: false,
  },
  {
    id: "7",
    title: "데이터베이스 정규화에 대해 설명하세요",
    category: "데이터베이스",
    score: 10,
    isApproved: false,
  },
  {
    id: "8",
    title: "데이터베이스 정규화에 대해 설명하세요",
    category: "데이터베이스",
    score: 10,
    isApproved: false,
  },
  {
    id: "9",
    title: "데이터베이스 정규화에 대해dd 설명하세요",
    category: "데이터베이스",
    score: 10,
    isApproved: false,
  },
];

export default function MyActivityPage() {
  const router = useRouter();
  const [postPage, setPostPage] = useState(1);
  const [commentPage, setCommentPage] = useState(1);
  const [questionPage, setQuestionPage] = useState(1);
  const itemsPerPage = 6;

  const getPagedData = (data: any[], page: number) => {
    const start = (page - 1) * itemsPerPage;
    return data.slice(start, start + itemsPerPage);
  };

  const pagedPosts = getPagedData(userPosts, postPage);
  const pagedComments = getPagedData(userComments, commentPage);
  const pagedQuestions = getPagedData(userQuestions, questionPage);

  const [selectedCategory, setSelectedCategory] = useState("작성한 글");
  const categories = ["작성한 글", "작성한 댓글", "등록한 질문"];

  return (
    <>
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
          {selectedCategory === "작성한 글" && (
            <div>
              <div className="space-y-3">
                {pagedPosts.map((post) => (
                  <div
                    key={post.id}
                    onClick={() => router.replace(`/recruitment/${post.id}`)}
                    className="flex justify-between items-center p-3 border border-gray-200 rounded-md hover:bg-gray-50 transition cursor-pointer"
                  >
                    <div>
                      <p className="font-medium">{post.title}</p>
                      <p className="text-sm text-gray-500">{post.date}</p>
                    </div>
                    <span
                      className={`px-2 py-1 text-xs rounded font-medium ${
                        post.status === "모집중"
                          ? "bg-green-50 text-green-700"
                          : "bg-gray-100 text-gray-500"
                      }`}
                    >
                      {post.status}
                    </span>
                  </div>
                ))}
              </div>

              <Pagination
                currentPage={postPage}
                totalItems={userPosts.length}
                itemsPerPage={itemsPerPage}
                onPageChange={setPostPage}
              />
            </div>
          )}

          {selectedCategory === "작성한 댓글" && (
            <div>
              <div className="space-y-3">
                {pagedComments.map((comment) => (
                  <div
                    key={comment.id}
                    onClick={() =>
                      router.replace(`/recruitment/${comment.postId}`)
                    }
                    className="p-3 border border-gray-200 rounded-md hover:bg-gray-50 transition cursor-pointer"
                  >
                    <p className="font-medium">{comment.content}</p>
                    <p className="text-xs text-gray-500">
                      {comment.postTitle} • {comment.date}
                    </p>
                  </div>
                ))}
              </div>

              <Pagination
                currentPage={commentPage}
                totalItems={userComments.length}
                itemsPerPage={itemsPerPage}
                onPageChange={setCommentPage}
              />
            </div>
          )}

          {selectedCategory === "등록한 질문" && (
            <div>
              <div className="space-y-3">
                {pagedQuestions.map((question) => (
                  <div
                    key={question.id}
                    onClick={() =>
                      router.replace(`/interview/cs/${question.id}`)
                    }
                    className="flex justify-between items-center p-3 border border-gray-200 rounded-md hover:bg-gray-50 transition cursor-pointer"
                  >
                    <div>
                      <p className="font-medium">{question.title}</p>
                      <p className="text-sm text-gray-500">
                        {question.category}
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
                ))}
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
    </>
  );
}
