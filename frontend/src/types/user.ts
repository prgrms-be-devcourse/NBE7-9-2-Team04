export type UserSignupRequest = {
    email: string;
    password: string;
    name: string;
    nickname: string;
    age: number;
    github: string;
    image?: string;
  };