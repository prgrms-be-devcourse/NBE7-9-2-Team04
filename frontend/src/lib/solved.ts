export interface SolvedProblem {
    id: string;
    title: string;
    category: string;
    solvedAt: string;
}

// 더미 데이터 예시
export const solvedProblems: SolvedProblem[] = [
    {
        id: "1",
        title: "TCP와 UDP의 차이점",
        category: "네트워크",
        solvedAt: "2025-10-15",
    },
    {
        id: "2",
        title: "프로세스와 스레드",
        category: "운영체제",
        solvedAt: "2025-10-14",
    },

];