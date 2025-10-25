export async function fetchApi(url: string, options?: RequestInit) {
  // 쿠키 자동 포함
  options = options || {};
  options.credentials = "include";

  if (options?.body) {
    const headers = new Headers(options.headers || {});
    headers.set("Content-Type", "application/json");
    options.headers = headers;
  }

  const fullUrl = `${process.env.NEXT_PUBLIC_API_BASE_URL}${url}`;
  console.log("fetch url:", fullUrl, options);

  let res = await fetch(fullUrl, options);

  if(res.status ==401){
    console.warn("AccessToken 만료. Refresh 실행");
  }

  const refreshRes = await fetch(
    `${process.env.NEXT_PUBLIC_API_BASE_URL}/api/v1/users/refresh`,
    {
      method: "POST",
      credentials: "include",
    }
  );

  if (refreshRes.ok) {
    console.log("AccessToken Refresh 성공");
    // 새 토큰이 쿠키로 설정되었으므로 다시 요청
    res = await fetch(fullUrl, options);
  } else {
    console.error("Refresh 실패. 로그아웃 진행");
    window.location.href = "/auth";
    return;
  }

  const apiResponse = await res.json();

  if (!res.ok) {
    throw new Error(apiResponse.message || "요청 실패");
  }

  // ApiResponse<T> 구조 그대로 반환
  return apiResponse;
}
