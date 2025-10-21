"use client";

import { useState } from "react";
import Link from "next/link";
import Pagination from "@/components/pagination";
import CategoryTab from "@/components/categoryTab";

//임시 데이터
const questions = [
  {
    id: "1",
    title: "TCP와 UDP의 차이점을 설명하세요",
    category: "네트워크",
    points: 10,
    solved: false,
  },
  {
    id: "2",
    title: "프로세스와 스레드의 차이는 무엇인가요?",
    category: "운영체제",
    points: 5,
    solved: true,
  },
  {
    id: "3",
    title: "데이터베이스 정규화에 대해 설명하세요",
    category: "데이터베이스",
    points: 10,
    solved: false,
  },
  {
    id: "4",
    title: "HTTP와 HTTPS의 차이점은?",
    category: "네트워크",
    points: 5,
    solved: true,
  },
  {
    id: "5",
    title: "가상 메모리란 무엇인가요?",
    category: "운영체제",
    points: 10,
    solved: false,
  },
  {
    id: "6",
    title: "REST API의 특징을 설명하세요",
    category: "네트워크",
    points: 10,
    solved: false,
  },
  {
    id: "7",
    title: "트랜잭션의 ACID 속성이란?",
    category: "데이터베이스",
    points: 15,
    solved: false,
  },
  {
    id: "8",
    title: "교착상태(Deadlock)란 무엇인가요?",
    category: "운영체제",
    points: 15,
    solved: false,
  },
  {
    id: "9",
    title: "교착상태(Deadlock)란 무엇인가요?",
    category: "운영체제",
    points: 15,
    solved: false,
  },
  {
    id: "10",
    title: "교착상태(Deadlock)란 무엇인가요?",
    category: "운영체제",
    points: 15,
    solved: false,
  },
  {
    id: "11",
    title: "교착상태(Deadlock)란 무엇인가요?",
    category: "운영체제",
    points: 15,
    solved: false,
  },
  {
    id: "12",
    title: "교착상태(Deadlock)란 무엇인가요?",
    category: "운영체제",
    points: 15,
    solved: false,
  },
];

//임시 데이터
const todayQuestion = {
  id: "3",
  title: "데이터베이스 정규화에 대해 설명하세요",
  category: "데이터베이스",
  points: 10,
  description:
    "데이터베이스 정규화의 개념과 1NF, 2NF, 3NF에 대해 설명해주세요.",
};

export default function CsQuestionPage() {
  const [selectedCategory, setSelectedCategory] = useState("전체");
  const [currentPage, setCurrentPage] = useState(1);
  const itemsPerPage = 9;
  const categories = [
    "전체",
    "네트워크",
    "운영체제",
    "데이터베이스",
    "자료구조",
    "알고리즘",
  ];

  const filteredQuestions =
    selectedCategory === "전체"
      ? questions
      : questions.filter((question) => question.category === selectedCategory);

  const getPagedData = (data: any[], page: number) => {
    const start = (page - 1) * itemsPerPage;
    return data.slice(start, start + itemsPerPage);
  };

  const pagedQuestions = getPagedData(filteredQuestions, currentPage);

  return (
    <>
      <div className="max-w-screen-xl mx-auto px-6 py-10">
        <div className="mb-10">
          <h1 className="text-3xl font-bold mb-2">CS 면접 질문</h1>
          <p className="text-gray-500">컴퓨터 과학 지식을 문제로 학습하세요</p>
        </div>

        {/* 오늘의 추천 문제 */}
        <div className="border bg-blue-50 border-blue-200 rounded-lg p-6 mb-10 shadow-sm">
          <div className="flex items-center gap-2 mb-2">
            <h2 className="text-sm font-semibold">오늘의 추천 문제</h2>
          </div>
          <p className="text-gray-500 text-sm mb-4">
            아직 풀지 않은 문제 중 하나를 추천합니다
          </p>

          <div className="space-y-3">
            <div className="flex items-center gap-2 text-sm mb-3">
              <span className="px-2 py-0.5 border border-gray-300 rounded-full">
                {todayQuestion.category}
              </span>
              <span className="px-2 py-0.5 bg-gray-100 text-gray-700 rounded-full">
                {todayQuestion.points}점
              </span>
            </div>
            <h3 className="text-xl font-semibold">{todayQuestion.title}</h3>
            <p className="text-gray-600">{todayQuestion.description}</p>
            <Link
              href={`/interview/cs/${todayQuestion.id}`}
              className="inline-block bg-blue-500 hover:bg-blue-600 text-white text-sm px-4 py-2 rounded-md transition"
            >
              문제 풀기
            </Link>
          </div>
        </div>

        {/* 카테고리 */}
        <div className="flex justify-between items-center mb-6 border-b border-gray-200 pb-2">
          <CategoryTab
            categories={categories}
            selected={selectedCategory}
            onSelect={(c) => {
              setSelectedCategory(c);
              setCurrentPage(1);
            }}
          />

          <Link
            href="/interview/cs/submit"
            className="bg-blue-500 hover:bg-blue-600 text-white text-sm px-4 py-2 rounded-md transition"
          >
            + 문제 등록
          </Link>
        </div>

        {/* 질문 목록 */}
        <div className="grid md:grid-cols-2 lg:grid-cols-3 gap-6">
          {pagedQuestions.map((q) => (
            <div
              key={q.id}
              className={`border border-gray-200 rounded-lg p-4 shadow-sm hover:shadow-md transition ${
                q.solved ? "opacity-60" : ""
              }`}
            >
              <div className="flex items-center justify-between mb-2">
                <span className="text-xs px-2 py-0.5 border border-gray-300 rounded-full">
                  {q.category}
                </span>
                {q.solved && (
                  <span className="text-xs px-2 py-0.5 bg-gray-100 text-gray-700 rounded-full">
                    완료
                  </span>
                )}
              </div>

              <h3 className="text-lg font-semibold mb-3 line-clamp-2">
                {q.title}
              </h3>

              <div className="flex items-center justify-between text-sm">
                <div className="flex items-center gap-2">
                  <span className="text-gray-500">{q.points}점</span>
                </div>

                <Link
                  href={`/interview/cs/${q.id}`}
                  className={`px-3 py-1 rounded-md text-sm font-medium border transition ${
                    q.solved
                      ? "border-gray-300 text-gray-700 hover:bg-gray-100"
                      : "bg-blue-500 text-white hover:bg-blue-600 border-blue-500"
                  }`}
                >
                  {q.solved ? "다시 풀기" : "풀기"}
                </Link>
              </div>
            </div>
          ))}
        </div>

        {/* 페이지네이션 */}
        <Pagination
          currentPage={currentPage}
          totalItems={filteredQuestions.length}
          itemsPerPage={itemsPerPage}
          onPageChange={setCurrentPage}
        />
      </div>
    </>
  );
}
