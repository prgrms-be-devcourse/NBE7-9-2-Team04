// 사용자가 작성한 게시글 정보
export interface UserPostDto {
    id: string;         // 게시글 고유 ID
    title: string;      // 게시글 제목
    status: string;     // 모집 상태 (예: "모집중", "마감")
    date: string;       // 작성일 (ISO 형식 문자열)
}

// 사용자가 작성한 댓글 정보
export interface UserCommentDto {
    id: string;         // 댓글 ID
    postId: string;     // 댓글이 달린 게시글 ID
    content: string;    // 댓글 내용
    postTitle: string;  // 댓글이 달린 게시글 제목
    date: string;       // 댓글 작성일
}

// 사용자가 등록한 질문 정보
export interface UserQuestionDto {
    id: string;         // 질문 ID
    title: string;      // 질문 제목
    category: string;   // 질문 카테고리 (예: 네트워크, 운영체제 등)
    score: number;      // 질문 점수 (혹은 채점 결과)
    isApproved: boolean; // 승인 여부
}

export const userPosts: UserPostDto[] = [
    {
        id: "1",
        title: "Next.js 14 프로젝트 팀원 모집",
        status: "모집중",
        date: "2025-10-10",
    },
    {
        id: "2",
        title: "React 스터디 모집",
        status: "마감",
        date: "2025-10-08",
    },
];

export const userComments: UserCommentDto[] = [
    {
        id: "1",
        postId: "1",
        content: "좋은 프로젝트네요! 참여하고 싶습니다.",
        postTitle: "Next.js 14 프로젝트 팀원 모집",
        date: "2025-10-12",
    },
    {
        id: "2",
        postId: "2",
        content: "저도 같은 문제를 겪었는데...",
        postTitle: "React 스터디 모집",
        date: "2025-10-11",
    },
];

export const userQuestions: UserQuestionDto[] = [
    {
        id: "1",
        title: "TCP와 UDP의 차이점을 설명하세요",
        category: "네트워크",
        score: 10,
        isApproved: false,
    },
    {
        id: "2",
        title: "프로세스와 스레드의 차이는 무엇인가요?",
        category: "운영체제",
        score: 5,
        isApproved: true,
    },
    {
        id: "3",
        title: "데이터베이스 정규화에 대해 설명하세요",
        category: "데이터베이스",
        score: 10,
        isApproved: false,
    },
];