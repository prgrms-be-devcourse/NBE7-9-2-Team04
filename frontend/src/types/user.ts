/** 권한 / 계정 상태 Enum */
export type Role = "USER" | "ADMIN";
export type AccountStatus = "ACTIVE" | "SUSPENDED" | "DEACTIVATED" | "BANNED";

/** 로그인 요청 DTO */
export type UserLoginRequest = {
  email: string;
  password: string;
};

/** 회원가입 요청 DTO */
export type UserSignupRequest = {
  email: string;
  password: string;
  name: string;
  nickname: string;
  age: number;
  github: string;
  image?: string | null;
};

/** 관리자: 사용자 상태 변경 요청 DTO */
export type AdminUserStatusUpdateRequest = {
  status: AccountStatus;
};

/** 관리자용 사용자 응답 DTO */
export type AdminUserResponse = {
  id: number;
  email: string;
  name: string;
  nickname: string;
  age: number;
  github: string;
  image?: string | null;
  role: Role;
  accountStatus: AccountStatus;
};

/** 일반 사용자 응답 DTO */
export type UserResponse = {
  id: number;
  email: string;
  name: string;
  nickname: string;
  age: number;
  github: string;
  image?: string | null;
  role: Role;
  createDate: string;
  modifyDate: string;
};

/** 사용자 목록 페이지 응답 DTO */
export type UserPageResponse<T = AdminUserResponse> = {
  users: T[];
  currentPage: number;
  totalPages: number;
  totalCount: number;
  pageSize: number;
};

/** 한글 라벨 매핑 (UI용) */
export const ACCOUNT_STATUS_LABELS: Record<AccountStatus, string> = {
  ACTIVE: "활성",
  SUSPENDED: "일시정지",
  DEACTIVATED: "탈퇴",
  BANNED: "영구정지",
};

export const ROLE_LABELS: Record<Role, string> = {
  USER: "사용자",
  ADMIN: "관리자",
};