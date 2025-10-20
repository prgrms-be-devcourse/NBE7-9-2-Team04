export function fetchApi(url: string, options?: RequestInit) {

    // 쿠키 자동 포함 
    // 백엔드 ApiResponse로 수정 필요
    options = options || {};
    options.credentials = "include"
  
    if (options?.body) {
      const headers = new Headers(options.headers || {});
      headers.set("Content-Type", "application/json");
      options.headers = headers;
    }
  
    console.log("fetch url:", `${process.env.NEXT_PUBLIC_API_BASE_URL}${url}`, options);
  
    return fetch(`${process.env.NEXT_PUBLIC_API_BASE_URL}${url}`, options).then(
      async (res) => {
        if (!res.ok) {
          const rsData = await res.json();
          throw new Error(rsData.msg || "요청 실패");
        }
        return res.json();
      }
    );
  }