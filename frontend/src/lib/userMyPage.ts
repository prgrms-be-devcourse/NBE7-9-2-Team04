export interface UserMyPageResponse {
    userId: number;
    email: string;
    password: string;
    name: string;
    nickname: string;
    age: number;
    github: string | null;
    image: string | null;
}