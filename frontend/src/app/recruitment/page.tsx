"use client";

import { useState, useEffect } from "react";
import { fetchApi } from "@/lib/client";
import Link from "next/link";
import Pagination from "@/components/pagination";
import CategoryTab from "@/components/categoryTab";
import { Post } from "@/types/post";

export default function RecruitmentPage() {
  const [pinnedPosts, setPinnedPosts] = useState<any[]>([]);
  const [posts, setPosts] = useState<Post[]>([]);
  const [loading, setLoading] = useState(true);
  const [currentSlide, setCurrentSlide] = useState(0);
  const [selectedCategory, setSelectedCategory] = useState("전체");
  const [currentPage, setCurrentPage] = useState(1);
  const postsPerPage = 9;
  const categories = ["전체", "프로젝트", "스터디"];

  // 고정 게시글 불러오기
  const fetchPinnedPosts = async () => {
    try {
      const res = await fetchApi(`/api/v1/posts/pinned`);
      if (res.status === "OK" && res.data) {
        const formatted = res.data.map((p: any) => ({
          ...p,
          categoryType:
            p.categoryType === "PROJECT"
              ? "프로젝트"
              : p.categoryType === "STUDY"
              ? "스터디"
              : p.categoryType,
          createDate: p.createDate?.split("T")[0],
          modifyDate: p.modifyDate?.split("T")[0],
          deadline: p.deadline?.split("T")[0],
        }));
        setPinnedPosts(formatted);
      } else {
        console.error("게시글 불러오기 실패", res.message);
      }
    } catch (err) {
      console.error("게시글 불러오기 실패", err);
    }
  };

  useEffect(() => {
    fetchPinnedPosts();
  }, []);

  // 게시글 불러오기
  useEffect(() => {
    const fetchPosts = async () => {
      try {
        const apiResponse = await fetchApi(`/api/v1/posts`, {
          method: "GET",
          cache: "no-store",
        });
        setPosts(apiResponse.data ?? []);
      } catch (err: any) {
        console.error("게시글 불러오기 실패:", err);
        alert(err.message);
      } finally {
        setLoading(false);
      }
    };

    fetchPosts();
  }, []);

  const premiumPosts = posts.filter((p) => p.pinStatus === "PINNED");
  const regularPosts = posts.filter((p) => p.pinStatus === "NOT_PINNED");

  // 프리미엄 글 슬라이드
  useEffect(() => {
    if (premiumPosts.length === 0) return;
    const timer = setInterval(() => {
      setCurrentSlide((prev) => (prev + 1) % pinnedPosts.length);
    }, 5000);
    return () => clearInterval(timer);
  }, [premiumPosts.length, pinnedPosts.length]);

  const nextSlide = () =>
    setCurrentSlide((prev) => (prev + 1) % pinnedPosts.length);
  const prevSlide = () =>
    setCurrentSlide(
      (prev) => (prev - 1 + pinnedPosts.length) % pinnedPosts.length
    );

  // 카테고리 필터링
  const filteredPosts =
    selectedCategory === "전체"
      ? regularPosts
      : regularPosts.filter((post) =>
          selectedCategory === "프로젝트"
            ? post.categoryType === "PROJECT"
            : post.categoryType === "STUDY"
        );

  const indexOfLastPost = currentPage * postsPerPage;
  const indexOfFirstPost = indexOfLastPost - postsPerPage;
  const currentPosts = filteredPosts.slice(indexOfFirstPost, indexOfLastPost);

  return (
    <div className="max-w-screen-xl mx-auto px-6 py-10">
      {/* 헤더 */}
      <div className="mb-10">
        <h1 className="text-3xl font-bold mb-2">팀 프로젝트 & 스터디 모집</h1>
        <p className="text-gray-500">함께 성장할 팀원을 찾아보세요</p>
      </div>

      {/* 프리미엄 모집글 */}
      <div className="mb-10">
        <div className="flex items-center justify-between mb-4">
          <h2 className="text-xl font-semibold">프리미엄 모집글</h2>
          <div className="flex gap-2">
            <button
              onClick={prevSlide}
              className="h-8 w-8 rounded-md border border-gray-300 hover:bg-gray-100 text-gray-600"
            >
              &lt;
            </button>
            <button
              onClick={nextSlide}
              className="h-8 w-8 rounded-md border border-gray-300 hover:bg-gray-100 text-gray-600"
            >
              &gt;
            </button>
          </div>
        </div>

        <div className="relative overflow-hidden rounded-lg">
          <div
            className="flex transition-transform duration-500 ease-in-out"
            style={{ transform: `translateX(-${currentSlide * 100}%)` }}
          >
            {pinnedPosts.map((post) => (
              <div key={post.postId || post.id} className="w-full flex-shrink-0">
                <div className="border border-blue-500 bg-blue-50 p-8 rounded-lg flex justify-between min-h-[160px]">
                  <div className="flex flex-col justify-between flex-1 pr-4">
                    <div className="space-y-2">
                      <div className="flex items-center gap-3">
                        <span className="bg-blue-500 text-white text-xs font-semibold rounded-full px-2.5 py-0.5">
                          프리미엄
                        </span>
                        <span className="bg-gray-100 text-gray-700 text-xs font-medium rounded-full px-2.5 py-0.5">
                          {post.categoryType}
                        </span>
                      </div>
                      <h3 className="text-lg font-bold line-clamp-1">
                        {post.title}
                      </h3>
                      <p className="text-gray-700 text-sm line-clamp-2">
                        {post.introduction}
                      </p>
                    </div>
                    <div className="flex items-center gap-1 text-gray-700 text-sm mt-3">
                      🧑‍🤝‍🧑 <span>{post.recruitCount}명</span>
                    </div>
                  </div>

                  <div className="flex flex-col justify-between items-end">
                    <div className="text-sm text-gray-500">
                      ⏰ 마감: {post.deadline}
                    </div>
                    <Link
                      href={`/recruitment/${post.postId || post.id}`}
                      className="bg-blue-500 text-white hover:bg-blue-600 text-sm px-4 py-2 rounded-md"
                    >
                      자세히 보기
                    </Link>
                  </div>
                </div>
              </div>
            ))}
          </div>

          {/* 하단 슬라이드 dot */}
          <div className="flex justify-center gap-2 mt-3">
            {pinnedPosts.map((_, i) => (
              <button
                key={`slide-${i}`}
                onClick={() => setCurrentSlide(i)}
                className={`h-2 rounded-full transition-all ${
                  i === currentSlide ? "w-8 bg-blue-600" : "w-2 bg-gray-300"
                }`}
              />
            ))}
          </div>
        </div>
      </div>

      {/* 카테고리 */}
      <CategoryTab
        categories={categories}
        selected={selectedCategory}
        onSelect={(c) => {
          setSelectedCategory(c);
          setCurrentPage(1);
        }}
      />

      {/* 일반 모집글 */}
      <div className="grid md:grid-cols-2 lg:grid-cols-3 gap-6">
        {currentPosts.map((post) => (
          <div
            key={post.postId}
            className="bg-white border border-gray-200 rounded-lg p-4 shadow-sm hover:shadow-md transition"
          >
            <div className="flex items-center justify-between mb-2 text-sm">
              <div className="flex items-center gap-1.5">
                <span
                  className={`px-2 py-0.5 rounded-full text-xs font-medium ${
                    post.categoryType === "PROJECT"
                      ? "bg-indigo-50 text-indigo-700"
                      : "bg-green-50 text-green-700"
                  }`}
                >
                  {post.categoryType === "PROJECT" ? "프로젝트" : "스터디"}
                </span>
                <span
                  className={`px-2 py-0.5 rounded-full text-xs font-medium ${
                    post.status === "ING"
                      ? "bg-red-50 text-red-700"
                      : "bg-gray-100 text-gray-500"
                  }`}
                >
                  {post.status === "ING" ? "모집중" : "마감"}
                </span>
              </div>

              <span className="text-gray-500 text-xs">
                마감일 {post.deadline?.split("T")[0]}
              </span>
            </div>

            <h3 className="text-lg font-semibold mb-1 line-clamp-2">
              {post.title}
            </h3>
            <p className="text-gray-600 text-sm line-clamp-3 mb-4">
              {post.introduction}
            </p>

            <div className="flex items-center justify-between text-sm text-gray-500 mb-3">
              <span>🧑‍🤝‍🧑 {post.recruitCount}명</span>
            </div>

            <Link
              href={`/recruitment/${post.postId}`}
              className="block text-center border border-gray-300 rounded-md py-2 text-sm font-medium text-gray-700 hover:bg-gray-100"
            >
              자세히 보기
            </Link>
          </div>
        ))}
      </div>

      {/* 페이지네이션 */}
      <Pagination
        currentPage={currentPage}
        totalItems={filteredPosts.length}
        itemsPerPage={postsPerPage}
        onPageChange={setCurrentPage}
      />
    </div>
  );
}
