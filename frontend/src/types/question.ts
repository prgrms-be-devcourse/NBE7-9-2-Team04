export type QuestionCategoryType =
  | "NETWORK"
  | "OS"
  | "DATABASE"
  | "DATA_STRUCTURE"
  | "ALGORITHM";

export type QuestionResponse = {
  questionId: number;             // 질문 ID
  title: string;                  // 질문 제목
  content: string;                // 질문 내용
  isApproved: boolean;            // 승인 여부
  score: number;                  // 점수
  authorId: number;               // 작성자 ID
  authorNickname: string;         // 작성자 닉네임
  categoryType: QuestionCategoryType; // 카테고리 타입
  createdDate: string;            // 작성일 (ISO 문자열)
  modifiedDate: string;           // 수정일 (ISO 문자열)
};
