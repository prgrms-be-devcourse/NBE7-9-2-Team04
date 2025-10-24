"use client"

import Link from "next/link"
import { useRouter, useParams } from "next/navigation"
import { useState, useEffect } from "react"
import { fetchApi } from "@/lib/client"
import {
  AnswerCreateRequest,
  AnswerCreateResponse,
  MyAnswerReadResponse,
} from "@/types/answer"
import { QuestionResponse } from "@/types/question"
import { FeedbackReadResponse } from "@/types/feedback"

export default function QuestionDetailPage() {
  const router = useRouter()
  const params = useParams()
  const questionId = params.id as string

  const [question, setQuestion] = useState<QuestionResponse | null>(null)
  const [answer, setAnswer] = useState("")
  const [submitted, setSubmitted] = useState(false)
  const [isJustSubmitted, setIsJustSubmitted] = useState(false)
  const [submittedAnswer, setSubmittedAnswer] = useState<AnswerCreateResponse | null>(null)
  const [feedback, setFeedback] = useState<FeedbackReadResponse>(null)
  const [loading, setLoading] = useState(true)
  const [isPublic, setIsPublic] = useState(true) // 공개 여부 상태

  // 질문 + 내 답변 + 피드백 불러오기
  useEffect(() => {
    const fetchData = async () => {
      try {
        setLoading(true)

        // 질문 조회
        const res = (await fetchApi(`/api/v1/questions/${questionId}`)) as {
          status: string
          data: QuestionResponse
          message?: string
        }
        if (res.status === "OK") setQuestion(res.data)
        else alert(`질문 조회 실패: ${res.message}`)

        // 내 답변 조회
        const myAnswerRes = (await fetchApi(
          `/api/v1/questions/${questionId}/answers/mine`
        )) as {
          status: string
          data: MyAnswerReadResponse
          message?: string
        }
        if (myAnswerRes.status === "OK" && myAnswerRes.data) {
          setSubmitted(true)
          setSubmittedAnswer(myAnswerRes.data)
          setAnswer(myAnswerRes.data.content)
          setIsPublic(myAnswerRes.data.isPublic) // 기존 답변 공개 여부 반영
        }

        // 피드백 조회
        const feedbackRes = (await fetchApi(`/api/v1/feedback/${questionId}`)) as {
          status: string
          data: FeedbackReadResponse
          message?: string
        }
        if (feedbackRes.status === "OK") setFeedback(feedbackRes.data)
        else setFeedback(null)
      } catch (err) {
        console.error(err)
        alert("데이터를 불러오는 중 오류가 발생했습니다.")
      } finally {
        setLoading(false)
      }
    }

    fetchData()
  }, [questionId])

  // 답변 제출
  const handleSubmit = async () => {
    if (!answer.trim()) {
      alert("답변을 입력해주세요.")
      return
    }

    try {
      const payload: AnswerCreateRequest = {
        content: answer,
        isPublic: isPublic,
      }

      const res = (await fetchApi(`/api/v1/questions/${questionId}/answers`, {
        method: "POST",
        body: JSON.stringify(payload),
      })) as { status: string; data: AnswerCreateResponse; message?: string }

      if (res.status === "CREATED") {
        setSubmittedAnswer(res.data)
        setSubmitted(true)
        setIsJustSubmitted(true)
        alert("답변이 제출되었습니다!")
      } else {
        alert(`답변 제출 실패: ${res.message}`)
      }
    } catch (err) {
      console.error(err)
      alert("답변 제출 중 오류가 발생했습니다.")
    }
  }

  // 공개/비공개 전환 PATCH
  const handleToggleVisibility = async () => {
    if (!submittedAnswer) return

    try {
      const updatedIsPublic = !isPublic

      const res = (await fetchApi(
        `/api/v1/questions/${questionId}/answers/${submittedAnswer.id}`,
        {
          method: "PATCH",
          body: JSON.stringify({ isPublic: updatedIsPublic }),
        }
      )) as { status: string; data: AnswerCreateResponse; message?: string }

      if (res.status === "OK") {
        setIsPublic(updatedIsPublic)
        setSubmittedAnswer(res.data)
      } else {
        alert(`공개/비공개 전환 실패: ${res.message}`)
      }
    } catch (err) {
      console.error(err)
      alert("공개/비공개 전환 중 오류가 발생했습니다.")
    }
  }

  if (loading || !question) {
    return <div className="text-center py-12">로딩 중...</div>
  }

  return (
    <div className="max-w-4xl mx-auto px-4 py-8">
      <div className="mb-6">
        <Link
          href="/interview/cs"
          className="inline-flex items-center text-sm text-gray-600 hover:text-blue-600 mb-6"
        >
          ← 목록으로
        </Link>
      </div>

      {/* 질문 박스 */}
      <div className="border border-gray-200 bg-white rounded-lg shadow-sm mb-6 p-6">
        <div className="flex items-center gap-2 mb-3">
          <span className="px-2 py-0.5 rounded-full border text-xs text-gray-700">
            {question.categoryType}
          </span>
        </div>
        <h1 className="text-2xl font-bold mb-3">{question.title}</h1>
        <p className="whitespace-pre-wrap text-gray-700 text-sm leading-relaxed">{question.content}</p>
      </div>

      {/* 답변 상태별 렌더링 */}
      {!submitted ? (
        // 작성 전
        <div className="border border-gray-200 bg-white rounded-lg shadow-sm p-6">
          <h2 className="text-xl font-semibold mb-1">답변 작성</h2>
          <p className="text-gray-500 text-sm mb-4">문제에 대한 답변을 작성해주세요.</p>

          <textarea
            value={answer}
            onChange={(e) => setAnswer(e.target.value)}
            rows={12}
            placeholder="답변을 입력하세요"
            className="w-full border border-gray-300 rounded-md p-3 text-sm font-mono focus:ring-2 focus:ring-blue-500 focus:outline-none"
          />

          {/* ✅ 작성 시 비공개 체크박스 */}
          <div className="flex items-center mt-2">
            <input
              type="checkbox"
              id="privateCheck"
              checked={!isPublic}
              onChange={(e) => setIsPublic(!e.target.checked ? true : false)}
              className="mr-2"
            />
            <label htmlFor="privateCheck" className="text-sm text-gray-700">
              비공개로 작성
            </label>
          </div>

          <div className="flex justify-end gap-3 mt-4">
            <button
              onClick={handleSubmit}
              disabled={loading}
              className={`px-4 py-2 rounded-md text-sm text-white ${
                loading ? "bg-gray-400 cursor-not-allowed" : "bg-blue-600 hover:bg-blue-700 cursor-pointer"
              }`}
            >
              {loading ? "제출 중..." : "답변 제출"}
            </button>
          </div>
        </div>
      ) : (
        // 제출 후
        <div className="border border-green-400 bg-green-50 rounded-lg shadow-sm p-6">
          {isJustSubmitted && (
            <h2 className="text-xl font-semibold text-green-700 mb-2">✅ 답변이 제출되었습니다!</h2>
          )}

          <div className="mb-4">
            <h3 className="font-semibold mb-2">내 답변:</h3>
            <div className="p-4 bg-white border rounded-md whitespace-pre-wrap text-sm text-gray-800">
              {submittedAnswer?.content || answer}
            </div>

            <p className="text-gray-600 text-xs mt-1">
              공개 여부: {isPublic ? "공개" : "비공개"}
            </p>

            {/* ✅ 제출 후 공개/비공개 전환 버튼 */}
            <div className="mt-2">
              <button
                onClick={handleToggleVisibility}
                className="px-2 py-1 text-xs rounded-md bg-teal-600 text-white hover:bg-teal-700 cursor-pointer"
              >
                {isPublic ? "비공개로 전환" : "공개로 전환"}
              </button>
            </div>
          </div>

          {/* 피드백 박스 */}
          <div className="mb-4">
            <h3 className="font-semibold mb-2">AI 피드백:</h3>
            <div className="p-4 bg-sky-100 border rounded-md text-sm text-gray-800">
              {feedback ? (
                <>
                  <p className="mb-2">{feedback.content}</p>
                  <p className="text-gray-600 text-xs">AI 점수: {feedback.score}</p>
                </>
              ) : (
                <p className="text-gray-500 italic">아직 피드백이 존재하지 않습니다.</p>
              )}
            </div>
          </div>

          <div className="flex gap-3">
            <button
              type="button"
              onClick={() => router.push(`/interview/cs/${questionId}/answers`)}
              className="px-4 py-2 bg-blue-600 text-white text-sm rounded-md hover:bg-blue-700 cursor-pointer"
            >
              다른 사람들의 답변 보기
            </button>

            <button
              type="button"
              className="px-4 py-2 bg-gray-200 text-gray-800 text-sm rounded-md hover:bg-gray-300 cursor-pointer"
            >
              다시 풀기
            </button>
          </div>
        </div>
      )}
    </div>
  )
}
