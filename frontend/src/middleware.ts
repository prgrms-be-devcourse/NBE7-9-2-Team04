import { NextResponse } from "next/server";
import type { NextRequest } from "next/server";

export function middleware(request: NextRequest) {
  const token = request.cookies.get("accessToken")?.value;

  if (!token) {
    return NextResponse.redirect(new URL("/auth", request.url));
  }

  return NextResponse.next();
}

//로그인 필요한 페이지
export const config = {
    matcher: [
        "/recruitment/new",
        "/portfolio_review/new",
        "/portfolio_review/:path", 
        "/mypage/:path*",
        "/interview/cs/new",
        "/interview/cs/:path",
        "/admin/:path*", 
    ]
}