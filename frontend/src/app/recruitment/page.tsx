"use client"

import { useState, useEffect } from "react"
import Router from "next/navigation"
import Link from "next/link"
import Pagination from "@/components/pagination"
import CategoryTab from "@/components/categoryTab";

//임시 데이터
const premiumPosts = [
  {
    id: "p1",
    title: "시니어 백엔드 개발자 구합니다 - 핀테크 스타트업",
    description: "5년 이상 경력의 백엔드 개발자를 찾습니다. Spring Boot, AWS 경험 필수",
    category: "프로젝트",
    deadline: "2025-11-30",
    members: "3/5",
    isPremium: true,
  },
  {
    id: "p2",
    title: "React 스터디 모집 - 주 2회 온라인",
    description: "React 18 최신 기능을 함께 공부할 스터디원을 모집합니다",
    category: "스터디",
    deadline: "2025-11-15",
    members: "4/6",
    isPremium: true,
  },
  {
    id: "p3",
    title: "AI 챗봇 프로젝트 팀원 모집",
    description: "OpenAI API를 활용한 챗봇 서비스 개발 프로젝트",
    category: "프로젝트",
    deadline: "2025-11-20",
    members: "2/4",
    isPremium: true,
  },
]

//임시 데이터
const regularPosts = [
  {
    id: "1",
    title: "Next.js 14 프로젝트 팀원 모집",
    description:
      "Next.js 14 App Router를 활용한 커머스 사이트 제작 프로젝트입니다. 디자이너 1명, 프론트엔드 개발자 2명을 찾습니다.",
    category: "프로젝트",
    deadline: "2025-11-25",
    members: "2/4",
    author: "김개발",
    createdAt: "2025-10-10",
  },
  {
    id: "2",
    title: "알고리즘 스터디 모집 (백준 골드 이상)",
    description:
      "주 3회 온라인으로 진행되는 알고리즘 스터디입니다. 백준 골드 티어 이상만 지원 가능합니다.",
    category: "스터디",
    deadline: "2025-11-18",
    members: "5/8",
    author: "박알고",
    createdAt: "2025-10-12",
  },
  {
    id: "3",
    title: "사이드 프로젝트 - 독서 기록 앱",
    description:
      "독서 기록 및 리뷰 공유 앱을 만들 팀원을 찾습니다. React Native 경험자 우대",
    category: "프로젝트",
    deadline: "2025-11-22",
    members: "1/3",
    author: "이독서",
    createdAt: "2025-10-13",
  },
  {
    id: "4",
    title: "TypeScript 스터디 - 초급자 환영",
    description:
      "TypeScript 기초부터 고급까지 함께 공부할 스터디원 모집합니다",
    category: "스터디",
    deadline: "2025-11-20",
    members: "3/6",
    author: "최타입",
    createdAt: "2025-10-14",
  },
  {
    id: "5",
    title: "게임 개발 프로젝트",
    description: "Unity를 활용한 2D 플랫포머 게임 제작 프로젝트입니다",
    category: "프로젝트",
    deadline: "2025-12-01",
    members: "2/5",
    author: "정게임",
    createdAt: "2025-10-15",
  },
  {
    id: "6",
    title: "DevOps 스터디 모집",
    description:
      "Docker, Kubernetes, CI/CD를 함께 공부할 스터디원을 찾습니다",
    category: "스터디",
    deadline: "2025-11-28",
    members: "4/6",
    author: "강데브",
    createdAt: "2025-10-14",
  },
  {
    id: "7",
    title: "DevOps 스터디 모집",
    description:
      "Docker, Kubernetes, CI/CD를 함께 공부할 스터디원을 찾습니다",
    category: "스터디",
    deadline: "2025-11-28",
    members: "4/6",
    author: "강데브",
    createdAt: "2025-10-14",
  }
]

export default function RecruitmentPage() {
  const [currentSlide, setCurrentSlide] = useState(0);
  const [selectedCategory, setSelectedCategory] = useState("전체");
  const [currentPage, setCurrentPage] = useState(1);
  const postsPerPage = 9;
  const categories = ["전체", "프로젝트", "스터디"];

  useEffect(() => {
    const timer = setInterval(() => {
      setCurrentSlide((prev) => (prev + 1) % premiumPosts.length)
    }, 5000)
    return () => clearInterval(timer)
  }, []);

  const nextSlide = () => setCurrentSlide((prev) => (prev + 1) % premiumPosts.length);
  const prevSlide = () => setCurrentSlide((prev) => (prev - 1 + premiumPosts.length) % premiumPosts.length);

  const filteredPosts =
    selectedCategory === "전체"
      ? regularPosts
      : regularPosts.filter((post) => post.category === selectedCategory);

  const indexOfLastPost = currentPage * postsPerPage;
  const indexOfFirstPost = indexOfLastPost - postsPerPage;
  const currentPosts = filteredPosts.slice(indexOfFirstPost, indexOfLastPost);

  return (
    <>
    <div className="max-w-screen-xl mx-auto px-6 py-10">

      <div className="mb-10">
        <h1 className="text-3xl font-bold mb-2">팀 프로젝트 & 스터디 모집</h1>
        <p className="text-gray-500">함께 성장할 팀원을 찾아보세요</p>
      </div>

      {/* 유료 서비스 */}
      <div className="mb-10">
        <div className="flex items-center justify-between mb-4">
          <h2 className="text-xl font-semibold">프리미엄 모집글</h2>
          <div className="flex gap-2">
            <button onClick={prevSlide} className="h-8 w-8 rounded-md border border-gray-300 hover:bg-gray-100 text-gray-600">&lt;</button>
            <button onClick={nextSlide} className="h-8 w-8 rounded-md border border-gray-300 hover:bg-gray-100 text-gray-600">&gt;</button>
          </div>
        </div>

        <div className="relative overflow-hidden rounded-lg">
          <div className="flex transition-transform duration-500 ease-in-out" style={{ transform: `translateX(-${currentSlide * 100}%)` }}>
            {premiumPosts.map((post) => (
              <div key={post.id} className="w-full flex-shrink-0">
                <div className="border border-blue-500 bg-gradient-to-br from-blue-50 to-blue-100 p-8 rounded-lg flex justify-between min-h-[160px]">
                  <div className="flex flex-col justify-between flex-1 pr-4">
                    <div className="space-y-2">
                      <div className="flex items-center gap-3">
                        <span className="bg-blue-500 text-white text-xs font-semibold rounded-full px-2.5 py-0.5">프리미엄</span>
                        <span className="bg-gray-100 text-gray-700 text-xs font-medium rounded-full px-2.5 py-0.5">{post.category}</span>
                      </div>
                      <h3 className="text-lg font-bold line-clamp-1">{post.title}</h3>
                      <p className="text-gray-700 text-sm line-clamp-2">{post.description}</p>
                    </div>
                    <div className="flex items-center gap-1 text-gray-700 text-sm mt-3">
                      🧑‍🤝‍🧑 <span>{post.members}</span>
                    </div>
                  </div>

                  <div className="flex flex-col justify-between items-end">
                    <div className="text-sm text-gray-500">⏰ 마감: {post.deadline}</div>
                    <Link href={`/recruitment/${post.id}`} className="bg-blue-500 text-white hover:bg-blue-600 text-sm px-4 py-2 rounded-md">
                      자세히 보기
                    </Link>
                  </div>
                </div>
              </div>
            ))}
          </div>

          {/* 하단 슬라이드 */}
          <div className="flex justify-center gap-2 mt-3">
            {premiumPosts.map((_, i) => (
              <button
                key={i}
                onClick={() => setCurrentSlide(i)}
                className={`h-2 rounded-full transition-all ${i === currentSlide ? "w-8 bg-blue-600" : "w-2 bg-gray-300"}`}
              />
            ))}
          </div>
        </div>
      </div>

      {/* 카테고리*/}
        <CategoryTab categories={categories} selected={selectedCategory} onSelect={(c) => {
          setSelectedCategory(c);
          setCurrentPage(1);
        }}/>
        
      {/* 모집글 목록 */}
      <div>
        <div className="flex items-center justify-between mb-6">
          <h2 className="text-xl font-bold">모집글 목록</h2>
          <Link href="/recruitment/new" className="bg-blue-500 text-white hover:bg-blue-600 text-sm px-4 py-2 rounded-md shadow">
            모집글 작성
          </Link>
        </div>

        <div className="grid md:grid-cols-2 lg:grid-cols-3 gap-6">
          {currentPosts.map((post) => (
            <div key={post.id} className="bg-white border border-gray-200 rounded-lg p-4 shadow-sm hover:shadow-md transition">
              <div className="flex items-center justify-between mb-2 text-sm">
                <span
                  className={`px-2 py-0.5 rounded-full text-xs font-medium ${
                    post.category === "프로젝트"
                      ? "bg-indigo-50 text-indigo-700"
                      : post.category === "스터디"
                      ? "bg-green-50 text-green-700"
                      : "bg-gray-50 text-gray-700" 
                  }`}
                >
                  {post.category}
                </span>
                <span className="text-gray-500 text-xs">마감일 {post.deadline}</span>
              </div>
              <h3 className="text-lg font-semibold mb-1 line-clamp-2">{post.title}</h3>
              <p className="text-gray-600 text-sm line-clamp-3 mb-4">{post.description}</p>

              <div className="flex items-center justify-between text-sm text-gray-500 mb-3">
                <span>🧑‍🤝‍🧑 {post.members}</span>
              </div>

              <Link
                href={`/recruitment/${post.id}`}
                className="block text-center border border-gray-300 rounded-md py-2 text-sm font-medium text-gray-700 hover:bg-gray-100"
              >
                자세히 보기
              </Link>
            </div>
          ))}
        </div>
      </div>

      <Pagination 
        currentPage={currentPage}
        totalItems={filteredPosts.length}
        itemsPerPage={postsPerPage}
        onPageChange={setCurrentPage}
        />
    </div>
    </>
  )
}

