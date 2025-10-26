export async function fetchApi(url: string, options?: RequestInit) {
  options = options || {};
  options.credentials = "include";


  const headers = new Headers(options.headers || {});
  if (!headers.has("Content-Type")) {
    headers.set("Content-Type", "application/json");
  }
  options.headers = headers;

  const fullUrl = `${process.env.NEXT_PUBLIC_API_BASE_URL}${url}`;
  console.log("fetch url:", fullUrl, options);

  const res = await fetch(fullUrl, options);
  let apiResponse: any = {};

  try {
    apiResponse = await res.json();
  } catch {
    // JSON이 아닐 수도 있으니 안전하게 처리
    apiResponse = {};
  }

  // 로그인 체크용 요청은 401이 나도 refresh 시도하지 않음
  const isLoginCheckRequest = url.includes("/api/v1/users/check");

  if (res.status === 401) {
    if (isLoginCheckRequest) {
      console.warn("로그인되지 않은 상태입니다. (refresh 시도 안 함)");
      return apiResponse; // 단순히 비로그인 상태로 처리
    }

    // refresh 자체가 실패한 경우 (무한루프 방지)
    if (url.includes("/refresh")) {
      console.error("Refresh 토큰도 만료됨. 로그인 필요.");
      redirectToLogin();
      throw new Error("인증이 만료되었습니다. 다시 로그인해주세요.");
    }

    try {
      console.log("Access token 만료, 갱신 시도...");

      await refreshAccessToken();

      console.log("토큰 갱신 성공, 원래 요청 재시도");

      //refresh 성공 시 원래 요청을 다시 fetchApi로
      return await fetchApi(url, options);
    } catch (refreshError) {
      console.error("토큰 갱신 실패:", refreshError);
      redirectToLogin();
      throw new Error("인증이 만료되었습니다. 다시 로그인해주세요.");
    }
  }

  if (!res.ok) {
    throw new Error(apiResponse.message || "요청 실패");
  }

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
        refreshState.isRefreshing = false;
        refreshState.promise = null;
        throw new Error("토큰 갱신 실패");
      }
      return res.json();
    })
    .then((data) => {
      refreshState.isRefreshing = false;
      refreshState.promise = null;
      return data;
    })
    .catch((error) => {
      refreshState.isRefreshing = false;
      refreshState.promise = null;
      throw error;
    });

  return refreshState.promise;
}

function redirectToLogin() {
  if (typeof window !== "undefined" && !window.location.pathname.startsWith("/auth")) {
    const currentPath = window.location.pathname + window.location.search;
    window.location.href = `/auth?returnUrl=${encodeURIComponent(currentPath)}`;
  }
}