"use client";

import {useEffect, useState} from "react";
import { useRouter } from "next/navigation";
import CategoryTab from "@/components/categoryTab";
import Pagination from "@/components/pagination";
import {fetchApi} from "@/lib/client";
import { UserPostDto, UserCommentDto, UserQuestionDto } from "@/lib/activity";
import {UserMyPageResponse} from "@/lib/userMyPage";
import React from "react";


export default function MyActivityPage() {
  const [posts, setPosts] = useState<UserPostDto[]>([]);
  const [comments, setComments] = useState<UserCommentDto[]>([]);
  const [questions, setQuestions] = useState<UserQuestionDto[]>([]);
  const [userMyPage, setUserMyPage] = useState<UserMyPageResponse | null>(null);
  const [isLoading, setIsLoading] = useState(false);
  const router = useRouter();
  const [postPage, setPostPage] = useState(1);
  const [commentPage, setCommentPage] = useState(1);
  const [questionPage, setQuestionPage] = useState(1);
  const itemsPerPage = 6;



    useEffect(() => {
        async function loadUserMyPage() {
            try {
                const apiResponse = await fetchApi(`/api/v1/users/{userId}/posts`, {
                    method: "GET",
                });
                setUserMyPage(apiResponse.data);
            } catch (err) {
                console.error("작성한 글 탐색 중 오류 발생:", err);
            } finally {
                setIsLoading(false);
            }
        }
        loadUserMyPage();
    }, []);

    useEffect(() => {
        async function loadUserMyPage() {
            try {
                const apiResponse = await fetchApi(`/api/v1/users/{userId}/comments`, {
                    method: "GET",
                });
                setUserMyPage(apiResponse.data);
            } catch (err) {
                console.error("작성한 댓글 탐색 중 오류 발생:", err);
            } finally {
                setIsLoading(false);
            }
        }
        loadUserMyPage();
    }, []);

    useEffect(() => {
        async function loadUserMyPage() {
            try {
                const apiResponse = await fetchApi(`/api/v1/users/{userId}/answers`, {
                    method: "GET",
                });
                setUserMyPage(apiResponse.data);
            } catch (err) {
                console.error("작성한 답글 탐색 중 오류 발생:", err);
            } finally {
                setIsLoading(false);
            }
        }
        loadUserMyPage();
    }, []);



  const getPagedData = (data: any[], page: number) => {
    const start = (page - 1) * itemsPerPage;
    return data.slice(start, start + itemsPerPage);
  };

  const pagedPosts = getPagedData(posts, postPage);
  const pagedComments = getPagedData(comments, commentPage);
  const pagedQuestions = getPagedData(questions, questionPage);

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
                totalItems={posts.length}
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
                totalItems={comments.length}
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
                totalItems={questions.length}
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
