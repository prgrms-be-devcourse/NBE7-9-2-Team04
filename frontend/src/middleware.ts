import { NextResponse } from "next/server";
import type { NextRequest } from "next/server";

export async function middleware(request: NextRequest) {
  const accessToken = request.cookies.get("accessToken")?.value;
  const refreshToken = request.cookies.get("refreshToken")?.value;

  // accessToken이 없는 경우
  if (!accessToken) {
    // refreshToken이 있으면 갱신 시도
    if (refreshToken) {
      try {
        const refreshResponse = await fetch(
          `${process.env.NEXT_PUBLIC_API_BASE_URL}/api/v1/users/refresh`,
          {
            method: "POST",
            credentials: "include",
            headers: {
              Cookie: `refreshToken=${refreshToken}`,
            },
          }
        );

        if (refreshResponse.ok) {
          // 갱신 성공 - 새로운 쿠키를 받아서 설정
          const response = NextResponse.next();
          
          const setCookieHeaders = refreshResponse.headers.getSetCookie();
          setCookieHeaders.forEach((cookie) => {
            response.headers.append("Set-Cookie", cookie);
          });
          
          return response;
        }
      } catch (error) {
        console.error("Middleware 토큰 갱신 실패:", error);
      }
    }

    // refreshToken도 없거나 갱신 실패 시 로그인 페이지로
    const redirectUrl = new URL("/auth", request.url);
    redirectUrl.searchParams.set(
      "returnUrl",
      request.nextUrl.pathname + request.nextUrl.search
    );
    return NextResponse.redirect(redirectUrl);
  }

  // accessToken이 있는 경우 정상 진행
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
        "/ranking"
    ]
}