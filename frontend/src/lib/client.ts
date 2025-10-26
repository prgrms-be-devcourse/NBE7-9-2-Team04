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
    // refresh 엔드포인트 자체에서 401이 나면 무한루프 방지
    if (url.includes("/refresh")) {
      console.error("Refresh 토큰도 만료됨. 로그인 필요.");
      if (typeof window !== "undefined" && !window.location.pathname.startsWith("/auth")) {
        const currentPath = window.location.pathname + window.location.search;
        window.location.href = `/auth?returnUrl=${encodeURIComponent(
          currentPath
        )}`;
      }
      throw new Error("인증이 만료되었습니다. 다시 로그인해주세요.");
    }

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

      // 토큰 갱신 실패 시 로그인 페이지로 리다이렉트 (이미 /auth에 있으면 제외)
      if (typeof window !== "undefined" && !window.location.pathname.startsWith("/auth")) {
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
        // 실패 시 즉시 state 초기화
        refreshState.isRefreshing = false;
        refreshState.promise = null;
        throw new Error("토큰 갱신 실패");
      }
      return res.json();
    })
    .then((data) => {
      // 성공 시 state 초기화
      refreshState.isRefreshing = false;
      refreshState.promise = null;
      return data;
    })
    .catch((error) => {
      // catch에서도 state 초기화 보장
      refreshState.isRefreshing = false;
      refreshState.promise = null;
      throw error;
    });

  return refreshState.promise;
}