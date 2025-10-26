"use client";

import { useState, useEffect } from "react";
import Link from "next/link";
import { useRouter } from "next/navigation";
import Pagination from "@/components/pagination";
import CategoryTab from "@/components/categoryTab";
import { fetchApi } from "@/lib/client";
import {
  QuestionResponse,
  QuestionCategoryType,
  QUESTION_CATEGORY_LIST,
} from "@/types/question";

/* UI 컴포넌트들 */
function Card({ children, className = "" }: { children: React.ReactNode; className?: string }) {
  return (
    <div
      className={`bg-white dark:bg-gray-900 border border-gray-200 dark:border-gray-700 rounded-xl shadow-sm transition-transform hover:-translate-y-1 hover:shadow-md ${className}`}
    >
      {children}
    </div>
  );
}

function CardHeader({ children }: { children: React.ReactNode }) {
  return <div className="p-4 border-b border-gray-100 dark:border-gray-800 min-h-[120px] flex flex-col">{children}</div>;
}

function CardTitle({ children, className = "" }: { children: React.ReactNode; className?: string }) {
  return (
    <h3 
      className={`font-semibold text-gray-900 dark:text-white line-clamp-2 ${className}`}
      title={typeof children === 'string' ? children : ''}
    >
      {children}
    </h3>
  );
}

function CardContent({ children }: { children: React.ReactNode }) {
  return <div className="p-4 min-h-[80px] flex items-center">{children}</div>;
}

function Badge({
  children,
  className = "",
  variant = "default",
}: {
  children: React.ReactNode;
  className?: string;
  variant?: "default" | "outline" | "secondary";
}) {
  const base =
    "inline-flex items-center px-2 py-0.5 rounded-full text-xs font-medium whitespace-nowrap";
  const variants = {
    default: "bg-blue-100 text-blue-800 dark:bg-blue-900 dark:text-blue-200",
    outline:
      "border border-gray-300 text-gray-700 dark:border-gray-600 dark:text-gray-300 bg-transparent",
    secondary: "bg-gray-100 text-gray-800 dark:bg-gray-700 dark:text-gray-200",
  };
  return <span className={`${base} ${variants[variant]} ${className}`}>{children}</span>;
}

function Button({
  children,
  onClick,
  asChild = false,
  href,
  variant = "default",
  size = "md",
  className = "",
}: {
  children: React.ReactNode;
  onClick?: () => void;
  asChild?: boolean;
  href?: string;
  variant?: "default" | "outline";
  size?: "sm" | "md";
  className?: string;
}) {
  const sizes = {
    sm: "text-sm px-3 py-1.5",
    md: "text-sm px-4 py-2",
  };
  const variants = {
    default: "bg-blue-500 hover:bg-blue-600 text-white border border-blue-500",
    outline:
      "border border-gray-300 text-gray-700 hover:bg-gray-100 dark:border-gray-700 dark:text-gray-300",
  };
  const classes = `rounded-md font-medium transition ${sizes[size]} ${variants[variant]} ${className}`;

  if (asChild && href) {
    return (
      <Link href={href} className={classes}>
        {children}
      </Link>
    );
  }

  return (
    <button onClick={onClick} className={classes}>
      {children}
    </button>
  );
}

/* ✅ 메인 페이지 */
export default function CsQuestionPage() {
  const router = useRouter();
  const [selectedCategory, setSelectedCategory] = useState<"전체" | QuestionCategoryType>("전체");
  const [currentPage, setCurrentPage] = useState(1);
  const [questions, setQuestions] = useState<QuestionResponse[]>([]);
  const [totalItems, setTotalItems] = useState(0);
  const [answeredIds, setAnsweredIds] = useState<number[]>([]); // ✅ 사용자가 푼 문제 ID 저장
  const [loading, setLoading] = useState(false);
  const itemsPerPage = 9;

  // ✅ 내 답변 목록 불러오기 (각 질문별로 확인)
  const fetchMyAnswers = async () => {
    try {
      // 먼저 모든 질문 목록을 가져옴
      const allQuestionsRes = await fetchApi("/api/v1/questions?page=1&size=1000");
      const allQuestions = allQuestionsRes?.data?.questions ?? [];
      
      if (allQuestions.length === 0) {
        setAnsweredIds([]);
        return;
      }

      // 각 질문에 대해 내 답변이 있는지 확인
      const answeredPromises = allQuestions.map(async (q: QuestionResponse) => {
        try {
          const res = await fetchApi(`/api/v1/questions/${q.questionId}/answers/mine`);
          if (res.status === "OK" && res.data) {
            return q.questionId;
          }
          return null;
        } catch {
          return null;
        }
      });

      const results = await Promise.all(answeredPromises);
      const ids = results.filter((id): id is number => id !== null);
      console.log("추출된 questionIds:", ids); // 디버깅용
      setAnsweredIds(ids);
    } catch (err) {
      console.warn("내 답변 조회 실패:", err);
      setAnsweredIds([]);
    }
  };

  // ✅ 질문 목록 불러오기
  const fetchQuestions = async () => {
    try {
      setLoading(true);
      const endpoint =
        selectedCategory === "전체"
          ? `/api/v1/questions?page=${currentPage}`
          : `/api/v1/questions/category/${selectedCategory}?page=${currentPage}`;

      const res = await fetchApi(endpoint);
      const items = res?.data?.questions ?? [];
      const totalCount = res?.data?.totalCount ?? 0;

      setQuestions(items);
      setTotalItems(totalCount);
    } catch (err: any) {
      console.warn("질문 조회 결과 없음:", err.message);
      setQuestions([]);
      setTotalItems(0);
    } finally {
      setLoading(false);
    }
  };

  // ✅ 로그인 확인 후 문제 풀이로 이동
  const handleSolveClick = async (id: number) => {
    try {
      const res = await fetchApi("/api/v1/users/check", { method: "GET" });
      if (res.status === "OK") {
        router.push(`/interview/cs/${id}`);
      } else {
        alert("로그인 후 이용해주세요.");
        router.push("/login");
      }
    } catch (err) {
      console.error("로그인 확인 실패:", err);
      alert("로그인 후 이용해주세요.");
      router.push("/login");
    }
  };

  // ✅ 초기 렌더링 시 질문 목록을 먼저 로드한 후 내 답변 확인
  useEffect(() => {
    fetchQuestions();
  }, [selectedCategory, currentPage]);

  // ✅ 질문 목록이 로드되면 답변 상태 확인
  useEffect(() => {
    if (questions.length > 0) {
      checkAnsweredQuestions();
    }
  }, [questions]);

  // ✅ 현재 페이지의 질문들에 대해서만 답변 여부 확인
  const checkAnsweredQuestions = async () => {
    const answeredPromises = questions.map(async (q) => {
      try {
        const res = await fetchApi(`/api/v1/questions/${q.questionId}/answers/mine`);
        if (res.status === "OK" && res.data) {
          return q.questionId;
        }
        return null;
      } catch {
        return null;
      }
    });

    const results = await Promise.all(answeredPromises);
    const ids = results.filter((id): id is number => id !== null);
    setAnsweredIds(ids);
  };

  const getDifficultyColor = (score: number) => {
    if (score <= 5) return "rounded-full px-3 bg-green-500/10 text-green-700 dark:text-green-400";
    if (score <= 10) return "rounded-full px-3 bg-yellow-500/10 text-yellow-700 dark:text-yellow-400";
    return "bg-red-500/10 rounded-full px-3 text-red-700 dark:text-red-400";
  };

  if (loading) {
    return <div className="text-center py-10 text-gray-500">문제 목록을 불러오는 중...</div>;
  }

  return (
    <div className="max-w-screen-xl mx-auto px-6 py-10">
      {/* 헤더 */}
      <div className="mb-10">
        <h1 className="text-3xl font-bold mb-2">CS 면접 질문</h1>
        <p className="text-gray-500">컴퓨터 과학 지식을 문제로 학습하세요</p>
      </div>

      {/* 카테고리 탭 */}
      <div className="flex justify-between items-center mb-6 border-b border-gray-200 pb-2">
        <CategoryTab
          categories={["전체", ...QUESTION_CATEGORY_LIST.map((c) => c.label)]}
          selected={
            selectedCategory === "전체"
              ? "전체"
              : QUESTION_CATEGORY_LIST.find((c) => c.value === selectedCategory)?.label ?? "전체"
          }
          onSelect={(label) => {
            if (label === "전체") setSelectedCategory("전체");
            else {
              const category = QUESTION_CATEGORY_LIST.find((c) => c.label === label);
              if (category) setSelectedCategory(category.value);
            }
            setCurrentPage(1);
          }}
        />

        <Button
          onClick={() => router.push("/interview/cs/new")}
          className="bg-blue-500 hover:bg-blue-600 text-white text-sm px-4 py-2 rounded-md transition"
        >
          + 문제 등록
        </Button>
      </div>

      {/* 질문 카드 리스트 */}
      <div className="grid md:grid-cols-2 lg:grid-cols-3 gap-6">
        {questions.map((q) => {
          const isSolved = answeredIds.includes(q.questionId); // ✅ 푼 문제 여부 확인

          return (
            <Card key={q.questionId}>
              <CardHeader>
                <div className="flex items-center justify-between mb-2">
                  <Badge variant="outline">
                    {
                      QUESTION_CATEGORY_LIST.find((c) => c.value === q.categoryType)?.label ??
                      q.categoryType
                    }
                  </Badge>
                  {isSolved && (
                    <span className="text-xs font-medium text-green-600 bg-green-50 px-2 py-0.5 rounded-full">
                      ✓ 완료
                    </span>
                  )}
                </div>
                <CardTitle className="text-lg font-semibold flex-1">{q.title}</CardTitle>
              </CardHeader>
              <CardContent>
                <div className="flex items-center justify-between w-full">
                  <span className={`text-sm font-medium ${getDifficultyColor(q.score)}`}>
                    {q.score}점
                  </span>
                  <Button
                    variant="default"
                    size="sm"
                    onClick={() => handleSolveClick(q.questionId)}
                    className={isSolved ? "bg-gray-500 hover:bg-gray-600" : ""}
                  >
                    {isSolved ? "다시 풀기" : "문제 풀기"}
                  </Button>
                </div>
              </CardContent>
            </Card>
          );
        })}

        {/* 질문이 없는 경우 */}
        {questions.length === 0 && (
          <div className="col-span-full text-center text-gray-400 py-10">
            등록된 질문이 없습니다.
          </div>
        )}
      </div>

      {/* 페이지네이션 */}
      <Pagination
        currentPage={currentPage}
        totalItems={totalItems}
        itemsPerPage={itemsPerPage}
        onPageChange={setCurrentPage}
      />
    </div>
  );
}