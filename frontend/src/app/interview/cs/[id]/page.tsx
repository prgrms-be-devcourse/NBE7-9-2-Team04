"use client";


import Link from "next/link";
import { useRouter } from "next/navigation";
import { useState } from "react";

// 임시 데이터
const questionData = {
    id: "q1",
    title: "TCP와 UDP의 차이점을 설명하세요",
    category: "네트워크",
    content: `
  TCP(Transmission Control Protocol)와 UDP(User Datagram Protocol)는 모두 전송 계층의 프로토콜입니다.
  두 프로토콜의 주요 차이점을 다음 관점에서 설명해주세요:
  
  1. 연결 방식
  2. 신뢰성
  3. 속도
  4. 사용 사례
  
  각 항목에 대해 구체적으로 설명하고, 실제 어떤 상황에서 어떤 프로토콜을 사용하는 것이 적합한지 예시를 들어주세요.
    `,
    modelAnswer: `
  TCP와 UDP의 주요 차이점:
  
  1. 연결 방식
  - TCP: 연결 지향적 (Connection-oriented). 3-way handshake를 통해 연결을 수립
  - UDP: 비연결형 (Connectionless). 연결 수립 과정 없이 바로 데이터 전송
  
  2. 신뢰성
  - TCP: 신뢰성 보장. 데이터 순서 보장, 오류 검출 및 재전송
  - UDP: 신뢰성 보장 안 함. 데이터 손실 가능, 순서 보장 안 됨
  
  3. 속도
  - TCP: 상대적으로 느림 (연결 수립, 오류 검사 등의 오버헤드)
  - UDP: 빠름 (최소한의 오버헤드)
  
  4. 사용 사례
  - TCP: 웹 브라우징(HTTP/HTTPS), 이메일(SMTP), 파일 전송(FTP)
  - UDP: 실시간 스트리밍, 온라인 게임, DNS, VoIP
    `,
  }

export default function QuestionDetailPage() {
  const [answer, setAnswer] = useState("");
  const [showModelAnswer, setShowModelAnswer] = useState(false);
  const [submitted, setSubmitted] = useState(false);

  const handleSubmit = () => {
    if (!answer.trim()) {
      alert("답변을 입력해주세요.");
      return;
    }

    setSubmitted(true);
    alert(`답변이 제출되었습니다!`);
  }

  return (
    <>
    <div className="max-w-4xl mx-auto px-4 py-8">

      <div className="mb-6">
            <Link
                href="/interview/cs"
                className="inline-flex items-center text-sm text-gray-600 hover:text-blue-600 mb-6"
            >
                ← 목록으로
            </Link>
      </div>


      <div className="border border-gray-200 bg-white rounded-lg shadow-sm mb-6 p-6">
        <div className="flex items-center gap-2 mb-3">
          <span className="px-2 py-0.5 rounded-full border text-xs text-gray-700">
            {questionData.category}
          </span>
        </div>

        <h1 className="text-2xl font-bold mb-3">{questionData.title}</h1>
        <p className="whitespace-pre-wrap text-gray-700 text-sm leading-relaxed">
          {questionData.content}
        </p>
      </div>

      {/* 답변 작성 or 제출 완료 */}
      {!submitted ? (
        <div className="border border-gray-200 bg-white rounded-lg shadow-sm p-6">
          <h2 className="text-xl font-semibold mb-1">답변 작성</h2>
          <p className="text-gray-500 text-sm mb-4">
            문제에 대한 답변을 작성해주세요.
          </p>

          <textarea
            value={answer}
            onChange={(e) => setAnswer(e.target.value)}
            rows={12}
            placeholder="답변을 입력하세요"
            className="w-full border border-gray-300 rounded-md p-3 text-sm font-mono focus:ring-2 focus:ring-blue-500 focus:outline-none"
          ></textarea>

          <div className="flex justify-end gap-3 mt-4">
            <button
              type="button"
              onClick={() => setShowModelAnswer(!showModelAnswer)}
              className="px-4 py-2 border border-gray-300 text-sm rounded-md hover:bg-gray-100"
            >
              {showModelAnswer ? "모범 답안 숨기기" : "모범 답안 보기"}
            </button>
            <button
              onClick={handleSubmit}
              className="px-4 py-2 bg-blue-600 text-white text-sm rounded-md hover:bg-blue-700"
            >
              답변 제출
            </button>
          </div>
        </div>
      ) : (
        <div className="border border-green-400 bg-green-50 rounded-lg shadow-sm p-6">
          <h2 className="text-xl font-semibold text-green-700 mb-2">
            ✅ 답변이 제출되었습니다!
          </h2>

          <div className="mb-4">
            <h3 className="font-semibold mb-2">내 답변:</h3>
            <div className="p-4 bg-white border rounded-md whitespace-pre-wrap text-sm text-gray-800">
              {answer}
            </div>
          </div>

          <button
            type="button"
            onClick={() => setShowModelAnswer(!showModelAnswer)}
            className="px-4 py-2 border border-gray-300 text-sm rounded-md hover:bg-gray-100"
          >
            {showModelAnswer ? "모범 답안 숨기기" : "모범 답안 보기"}
          </button>
        </div>
      )}


      {showModelAnswer && (
        <div className="border border-blue-300 bg-blue-50 rounded-lg shadow-sm p-6 mt-6">
          <h3 className="text-lg font-semibold mb-3">모범 답안</h3>
          <div className="whitespace-pre-wrap text-sm text-gray-700">
            {questionData.modelAnswer}
          </div>
        </div>
      )}
    </div>
    </>
  )
}