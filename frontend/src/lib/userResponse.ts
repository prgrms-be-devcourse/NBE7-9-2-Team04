export interface UserResponse {
    id: number;
    email: string;
    name: string;
    nickname: string;
    age: number | null;
    github: string | null;
    image: string | null;
    role: string;
    createDate: string;
    modifyDate: string;
}