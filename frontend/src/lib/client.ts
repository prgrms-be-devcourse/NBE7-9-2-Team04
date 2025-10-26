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

  if (res.status === 401 && apiResponse.status === "UNAUTHORIZED") {
    try {
      console.log("Access token 만료, 갱신 시도...");

      // 토큰 갱신
      await refreshAccessToken();

      console.log("토큰 갱신 성공, 원래 요청 재시도");

      // 원래 요청 재시도
      const retryRes = await fetch(fullUrl, options);
      const retryApiResponse = await retryRes.json();

      if (!retryRes.ok) {
        throw new Error(retryApiResponse.message || "요청 실패");
      }

      return retryApiResponse;
    } catch (refreshError) {
      console.error("토큰 갱신 실패:", refreshError);

      // 토큰 갱신 실패 시 로그인 페이지로 리다이렉트
      if (typeof window !== "undefined") {
        const currentPath = window.location.pathname + window.location.search;
        window.location.href = `/auth?returnUrl=${encodeURIComponent(
          currentPath
        )}`;
      }
      throw new Error("인증이 만료되었습니다. 다시 로그인해주세요.");
    }
  }

  if (!res.ok) {
    throw new Error(apiResponse.message || "요청 실패");
  }

  // ApiResponse<T> 구조 그대로 반환
  return apiResponse;
}

const refreshState = {
  isRefreshing: false,
  promise: null as Promise<any> | null,
};

async function refreshAccessToken() {
  if (refreshState.isRefreshing && refreshState.promise) {
    return refreshState.promise;
  }

  refreshState.isRefreshing = true;
  refreshState.promise = fetch(
    `${process.env.NEXT_PUBLIC_API_BASE_URL}/api/v1/users/refresh`,
    {
      method: "POST",
      credentials: "include",
    }
  )
    .then(async (res) => {
      if (!res.ok) {
        throw new Error("토큰 갱신 실패");
      }
      return res.json();
    })
    .finally(() => {
      refreshState.isRefreshing = false;
      refreshState.promise = null;
    });

  return refreshState.promise;
}