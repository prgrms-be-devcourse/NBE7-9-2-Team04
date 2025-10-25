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

/* 직접 구현한 미니 UI 컴포넌트들 */
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
  return <div className="p-4 border-b border-gray-100 dark:border-gray-800">{children}</div>;
}

function CardTitle({ children, className = "" }: { children: React.ReactNode; className?: string }) {
  return <h3 className={`font-semibold text-gray-900 dark:text-white ${className}`}>{children}</h3>;
}

function CardContent({ children }: { children: React.ReactNode }) {
  return <div className="p-4">{children}</div>;
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

/* 메인 페이지 */
export default function CsQuestionPage() {
  const router = useRouter();
  const [selectedCategory, setSelectedCategory] = useState<"전체" | QuestionCategoryType>("전체");
  const [currentPage, setCurrentPage] = useState(1);
  const [questions, setQuestions] = useState<QuestionResponse[]>([]);
  const [totalItems, setTotalItems] = useState(0);
  const itemsPerPage = 9;

  // 임시 추천 문제
  const todayQuestion: QuestionResponse = {
    questionId: 0,
    title: "데이터베이스 정규화에 대해 설명하세요",
    content: "데이터 중복을 최소화하고 일관성을 유지하기 위한 정규화 단계에 대해 설명해주세요.",
    isApproved: true,
    score: 10,
    authorId: 1,
    authorNickname: "admin",
    categoryType: "DATABASE",
    createdDate: "",
    modifiedDate: "",
  };

  /* 로그인 여부 확인 후 문제 풀기 */
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

  /* 질문 목록 불러오기 */
  const fetchQuestions = async () => {
    try {
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
    }
  };

  useEffect(() => {
    fetchQuestions();
  }, [selectedCategory, currentPage]);

  const getDifficultyColor = (score: number) => {
    if (score <= 5) return "bg-green-500/10 text-green-700 dark:text-green-400";
    if (score <= 10) return "bg-yellow-500/10 text-yellow-700 dark:text-yellow-400";
    return "bg-red-500/10 text-red-700 dark:text-red-400";
  };

  return (
    <div className="max-w-screen-xl mx-auto px-6 py-10">
      {/* 헤더 */}
      <div className="mb-10">
        <h1 className="text-3xl font-bold mb-2">CS 면접 질문</h1>
        <p className="text-gray-500">컴퓨터 과학 지식을 문제로 학습하세요</p>
      </div>

      {/* 오늘의 추천 문제 */}
      <Card className="mb-8 border-2 border-blue-400 bg-gradient-to-br from-blue-50 to-blue-100 dark:from-blue-900/20 dark:to-blue-800/20">
        <CardHeader>
          <CardTitle className="text-lg font-semibold">오늘의 추천 문제</CardTitle>
          <p className="text-sm text-gray-500 dark:text-gray-400">
            아직 풀지 않은 문제 중 하나를 추천합니다
          </p>
        </CardHeader>
        <CardContent>
          <div className="space-y-3">
            <div className="flex items-center gap-2 text-sm mb-3">
              <Badge variant="outline">
                {
                  QUESTION_CATEGORY_LIST.find(
                    (c) => c.value === todayQuestion.categoryType
                  )?.label ?? todayQuestion.categoryType
                }
              </Badge>
              <Badge className={getDifficultyColor(todayQuestion.score)}>
                {todayQuestion.score}점
              </Badge>
            </div>
            <h3 className="text-xl font-semibold">{todayQuestion.title}</h3>
            <p className="text-gray-600 dark:text-gray-300">{todayQuestion.content}</p>
            <Button asChild href={`/interview/cs/${todayQuestion.questionId}`} className="mt-3">
              문제 풀기
            </Button>
          </div>
        </CardContent>
      </Card>

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
            if (label === "전체") {
              setSelectedCategory("전체");
            } else {
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
        {questions.map((q) => (
          <Card
            key={q.questionId}
            className={`transition-transform hover:-translate-y-1 hover:shadow-lg ${
              q.isApproved ? "opacity-70" : "opacity-100"
            }`}
          >
            <CardHeader>
              <div className="flex items-center justify-between mb-2">
                <Badge variant="outline">
                  {
                    QUESTION_CATEGORY_LIST.find(
                      (c) => c.value === q.categoryType
                    )?.label ?? q.categoryType
                  }
                </Badge>
              </div>
              <CardTitle className="text-lg font-semibold line-clamp-2">{q.title}</CardTitle>
            </CardHeader>
            <CardContent>
              <div className="flex items-center justify-between mt-2">
                <span className={`text-sm font-medium ${getDifficultyColor(q.score)}`}>
                  {q.score}점
                </span>
                <Button
                  variant={q.isApproved ? "outline" : "default"}
                  size="sm"
                  onClick={() => handleSolveClick(q.questionId)}
                >
                  풀기
                </Button>
              </div>
            </CardContent>
          </Card>
        ))}

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
