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

  const res = await fetch(fullUrl, options);
  const apiResponse = await res.json();

  if (!res.ok) {
    throw new Error(apiResponse.message || "요청 실패");
  }

  // ApiResponse<T> 구조 그대로 반환
  return apiResponse;
}

