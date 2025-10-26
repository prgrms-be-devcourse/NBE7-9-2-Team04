"use client";

import { useState, useEffect } from "react";
import { fetchApi } from "@/lib/client";
import Link from "next/link";
import CategoryTab from "@/components/categoryTab";
import { PostResponse, PostPageResponse } from "@/types/post";

export default function RecruitmentPage() {
  const [pinnedPosts, setPinnedPosts] = useState<PostResponse[]>([]);
  const [posts, setPosts] = useState<PostResponse[]>([]);
  const [loading, setLoading] = useState(true);
  const [currentSlide, setCurrentSlide] = useState(0);
  const [selectedCategory, setSelectedCategory] = useState("전체");
  const [currentPage, setCurrentPage] = useState(1);
  const [totalPages, setTotalPages] = useState(1);
  const postsPerPage = 9;
  const categories = ["전체", "프로젝트", "스터디"];

  // ===============================
  // 프리미엄 게시글 불러오기
  // ===============================
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
      }
    } catch (err) {
      console.error("프리미엄 게시글 불러오기 실패", err);
    }
  };

  // ===============================
  // 일반 게시글 페이징 단위로 불러오기
  // ===============================
  const fetchPosts = async (page = 1) => {
    try {
      setLoading(true);

      const categoryQuery =
        selectedCategory === "전체"
          ? ""
          : selectedCategory === "프로젝트"
          ? "PROJECT"
          : "STUDY";

      const res = (await fetchApi(
        `/api/v1/posts?page=${page}&size=${postsPerPage}&category=${categoryQuery}`
      )) as {
        status: string;
        data: PostPageResponse<PostResponse>;
        message?: string;
      };

      if (res.status === "OK") {
        const formatted = res.data.posts.map((p: any) => ({
          ...p,
          createDate: p.createDate?.split("T")[0],
          modifyDate: p.modifyDate?.split("T")[0],
          deadline: p.deadline?.split("T")[0],
        }));
        setPosts(formatted);
        setCurrentPage(res.data.currentPage);
        setTotalPages(res.data.totalPages === 0 ? 1 : res.data.totalPages);
      } else {
        console.error("게시글 불러오기 실패:", res.message);
      }
    } catch (err) {
      console.error("게시글 불러오기 실패:", err);
    } finally {
      setLoading(false);
    }
  };

  // ===============================
  // 초기 데이터
  // ===============================
  useEffect(() => {
    fetchPinnedPosts();
  }, []);

  useEffect(() => {
    fetchPosts(1); // 카테고리 변경 시 첫 페이지
  }, [selectedCategory]);

  // ===============================
  // 프리미엄 글 슬라이드
  // ===============================
  useEffect(() => {
    if (pinnedPosts.length === 0) return;
    const timer = setInterval(() => {
      setCurrentSlide((prev) => (prev + 1) % pinnedPosts.length);
    }, 5000);
    return () => clearInterval(timer);
  }, [pinnedPosts.length]);

  const nextSlide = () =>
    setCurrentSlide((prev) => (prev + 1) % pinnedPosts.length);
  const prevSlide = () =>
    setCurrentSlide((prev) => (prev - 1 + pinnedPosts.length) % pinnedPosts.length);

  // ===============================
  // 렌더링
  // ===============================
  return (
    <div style={{ maxWidth: "1200px", margin: "0 auto", padding: "40px 24px" }}>
      {/* 헤더 */}
      <div style={{ marginBottom: "40px" }}>
        <h1 style={{ fontSize: "2rem", fontWeight: 700, marginBottom: "8px" }}>
          팀 프로젝트 & 스터디 모집
        </h1>
        <p style={{ color: "#6B7280" }}>함께 성장할 팀원을 찾아보세요</p>
      </div>

      {/* 프리미엄 모집글 */}
      <div style={{ marginBottom: "40px" }}>
        <div style={{ display: "flex", justifyContent: "space-between", marginBottom: "16px" }}>
          <h2 style={{ fontSize: "1.25rem", fontWeight: 600 }}>프리미엄 모집글</h2>
          <div style={{ display: "flex", gap: "8px" }}>
            <button
              onClick={prevSlide}
              style={{
                height: "32px",
                width: "32px",
                borderRadius: "4px",
                border: "1px solid #D1D5DB",
                cursor: "pointer",
              }}
            >
              &lt;
            </button>
            <button
              onClick={nextSlide}
              style={{
                height: "32px",
                width: "32px",
                borderRadius: "4px",
                border: "1px solid #D1D5DB",
                cursor: "pointer",
              }}
            >
              &gt;
            </button>
          </div>
        </div>

        <div style={{ position: "relative", overflow: "hidden", borderRadius: "8px" }}>
          <div
            style={{
              display: "flex",
              transition: "transform 0.5s ease-in-out",
              transform: `translateX(-${currentSlide * 100}%)`,
            }}
          >
            {pinnedPosts.map((post) => (
              <div key={post.postId} style={{ minWidth: "100%", flexShrink: 0 }}>
                <div
                  style={{
                    display: "flex",
                    justifyContent: "space-between",
                    padding: "32px",
                    border: "1px solid #3B82F6",
                    backgroundColor: "#DBEAFE",
                    borderRadius: "8px",
                    minHeight: "160px",
                  }}
                >
                  <div style={{ flex: 1, paddingRight: "16px", display: "flex", flexDirection: "column", justifyContent: "space-between" }}>
                    <div>
                      <div style={{ display: "flex", gap: "12px", marginBottom: "8px" }}>
                        <span style={{ backgroundColor: "#3B82F6", color: "white", fontSize: "0.625rem", fontWeight: 600, borderRadius: "9999px", padding: "2px 8px" }}>
                          프리미엄
                        </span>
                        <span style={{ backgroundColor: "#F3F4F6", color: "#374151", fontSize: "0.625rem", fontWeight: 500, borderRadius: "9999px", padding: "2px 8px" }}>
                          {post.categoryType}
                        </span>
                      </div>
                      <h3 style={{ fontSize: "1.125rem", fontWeight: 600, marginBottom: "8px", lineHeight: "1.5rem", display: "-webkit-box", WebkitLineClamp: 2, WebkitBoxOrient: "vertical", overflow: "hidden" }}>
                        {post.title}
                      </h3>
                      <p style={{ fontSize: "0.875rem", color: "#4B5563", lineHeight: "1.25rem", display: "-webkit-box", WebkitLineClamp: 2, WebkitBoxOrient: "vertical", overflow: "hidden" }}>
                        {post.introduction}
                      </p>
                    </div>
                    <div style={{ display: "flex", gap: "4px", fontSize: "0.875rem", color: "#374151", marginTop: "12px" }}>
                      🧑‍🤝‍🧑 <span>{post.recruitCount}명</span>
                    </div>
                  </div>

                  <div style={{ display: "flex", flexDirection: "column", justifyContent: "space-between", alignItems: "flex-end" }}>
                    <div style={{ fontSize: "0.875rem", color: "#6B7280" }}>⏰ 마감: {post.deadline}</div>
                    <Link
                      href={`/recruitment/${post.postId}`}
                      style={{ backgroundColor: "#3B82F6", color: "white", padding: "4px 16px", borderRadius: "4px", textDecoration: "none", textAlign: "center" }}
                    >
                      자세히 보기
                    </Link>
                  </div>
                </div>
              </div>
            ))}
          </div>

          {/* 슬라이드 dot */}
          <div style={{ display: "flex", justifyContent: "center", gap: "8px", marginTop: "12px" }}>
            {pinnedPosts.map((_, i) => (
              <button
                key={`slide-${i}`}
                onClick={() => setCurrentSlide(i)}
                style={{
                  height: "8px",
                  borderRadius: "9999px",
                  transition: "all 0.3s",
                  width: i === currentSlide ? "32px" : "8px",
                  backgroundColor: i === currentSlide ? "#2563EB" : "#D1D5DB",
                  cursor: "pointer",
                }}
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
      {loading ? (
        <div style={{ textAlign: "center", padding: "48px 0" }}>로딩 중...</div>
      ) : posts.length === 0 ? (
        <div style={{ textAlign: "center", padding: "48px 0", border: "1px solid #D1D5DB", borderRadius: "8px" }}>
          <p style={{ color: "#6B7280" }}>게시글이 없습니다.</p>
        </div>
      ) : (
        <div style={{ display: "grid", gridTemplateColumns: "repeat(auto-fill, minmax(300px, 1fr))", gap: "24px" }}>
          {posts.map((post) => (
            <div
              key={post.postId}
              style={{
                backgroundColor: "white",
                border: "1px solid #D1D5DB",
                borderRadius: "8px",
                padding: "16px",
                boxShadow: "0 1px 2px rgba(0,0,0,0.05)",
                transition: "box-shadow 0.2s",
              }}
            >
              <div style={{ display: "flex", justifyContent: "space-between", marginBottom: "8px", fontSize: "0.875rem" }}>
                <div style={{ display: "flex", gap: "6px" }}>
                  <span style={{ padding: "2px 6px", borderRadius: "9999px", fontSize: "0.625rem", fontWeight: 500, backgroundColor: post.categoryType === "PROJECT" ? "#E0E7FF" : "#DCFCE7", color: post.categoryType === "PROJECT" ? "#4338CA" : "#166534" }}>
                    {post.categoryType === "PROJECT" ? "프로젝트" : "스터디"}
                  </span>
                  <span style={{ padding: "2px 6px", borderRadius: "9999px", fontSize: "0.625rem", fontWeight: 500, backgroundColor: post.status === "ING" ? "#FEE2E2" : "#F3F4F6", color: post.status === "ING" ? "#B91C1C" : "#6B7280" }}>
                    {post.status === "ING" ? "모집중" : "마감"}
                  </span>
                </div>
                <span style={{ color: "#6B7280", fontSize: "0.75rem" }}>마감일 {post.deadline}</span>
              </div>

              {/* 제목: 항상 2줄 */}
              <h3 style={{
                fontSize: "1.125rem",
                fontWeight: 600,
                marginBottom: "4px",
                lineHeight: "1.5rem",
                display: "-webkit-box",
                WebkitLineClamp: 2,
                WebkitBoxOrient: "vertical",
                overflow: "hidden",
                minHeight: "3rem",
              }}>
                {post.title}
              </h3>

              {/* 소개: 항상 2줄 */}
              <p style={{
                fontSize: "0.875rem",
                color: "#4B5563",
                marginBottom: "12px",
                lineHeight: "1.25rem",
                display: "-webkit-box",
                WebkitLineClamp: 2,
                WebkitBoxOrient: "vertical",
                overflow: "hidden",
                minHeight: "2.5rem",
              }}>
                {post.introduction}
              </p>

              <div style={{ display: "flex", justifyContent: "space-between", fontSize: "0.875rem", color: "#6B7280", marginBottom: "12px" }}>
                <span>🧑‍🤝‍🧑 {post.recruitCount}명</span>
              </div>

              <Link
                href={`/recruitment/${post.postId}`}
                style={{
                  display: "block",
                  textAlign: "center",
                  border: "1px solid #D1D5DB",
                  borderRadius: "4px",
                  padding: "8px 0",
                  fontSize: "0.875rem",
                  fontWeight: 500,
                  textDecoration: "none",
                  color: "#374151",
                  cursor: "pointer",
                }}
              >
                자세히 보기
              </Link>
            </div>
          ))}
        </div>
      )}

      {/* 페이지네이션 */}
      <div style={{ display: "flex", justifyContent: "center", alignItems: "center", gap: "8px", marginTop: "24px" }}>
        <button
          onClick={() => fetchPosts(1)}
          disabled={currentPage === 1}
          style={{
            padding: "6px 12px",
            borderRadius: "4px",
            backgroundColor: "#E5E7EB",
            cursor: currentPage === 1 ? "not-allowed" : "pointer",
            opacity: currentPage === 1 ? 0.5 : 1,
          }}
        >
          처음
        </button>
        <button
          onClick={() => fetchPosts(currentPage - 1)}
          disabled={currentPage === 1}
          style={{
            padding: "6px 12px",
            borderRadius: "4px",
            backgroundColor: "#E5E7EB",
            cursor: currentPage === 1 ? "not-allowed" : "pointer",
            opacity: currentPage === 1 ? 0.5 : 1,
          }}
        >
          &lt;
        </button>

        {Array.from({ length: totalPages }, (_, i) => i + 1).map((page) => (
          <button
            key={page}
            onClick={() => fetchPosts(page)}
            style={{
              padding: "6px 12px",
              borderRadius: "4px",
              backgroundColor: currentPage === page ? "#2563EB" : "#E5E7EB",
              color: currentPage === page ? "white" : "black",
              cursor: "pointer",
            }}
          >
            {page}
          </button>
        ))}

        <button
          onClick={() => fetchPosts(currentPage + 1)}
          disabled={currentPage === totalPages}
          style={{
            padding: "6px 12px",
            borderRadius: "4px",
            backgroundColor: "#E5E7EB",
            cursor: currentPage === totalPages ? "not-allowed" : "pointer",
            opacity: currentPage === totalPages ? 0.5 : 1,
          }}
        >
          &gt;
        </button>
        <button
          onClick={() => fetchPosts(totalPages)}
          disabled={currentPage === totalPages}
          style={{
            padding: "6px 12px",
            borderRadius: "4px",
            backgroundColor: "#E5E7EB",
            cursor: currentPage === totalPages ? "not-allowed" : "pointer",
            opacity: currentPage === totalPages ? 0.5 : 1,
          }}
        >
          마지막
        </button>
      </div>

      {/* 게시글 작성 버튼 */}
      <div style={{ display: "flex", justifyContent: "flex-end", marginTop: "24px" }}>
        <Link
          href="/recruitment/new"
          style={{
            display: "inline-flex",
            alignItems: "center",
            justifyContent: "center",
            padding: "8px 16px",
            borderRadius: "4px",
            backgroundColor: "#2563EB",
            color: "white",
            fontSize: "0.875rem",
            textDecoration: "none",
            cursor: "pointer",
          }}
        >
          게시글 작성
        </Link>
      </div>
    </div>
  );
}
