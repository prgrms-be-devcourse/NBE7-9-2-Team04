"use client";

import { useEffect } from "react";
import { useSearchParams } from "next/navigation";
import { fetchApi } from "@/lib/client";

export default function SuccessPage() {
  const searchParams = useSearchParams();
  const customerKey = searchParams.get("customerKey");
  const authKey = searchParams.get("authKey");

  useEffect(() => {
    if (customerKey && authKey) {
      (async () => {
        try {
          const apiResponse = await fetchApi("/api/v1/billing/issue", {
            method: "POST",
            body: JSON.stringify({
              customerKey,
              authKey,
            }),
          });

          console.log("Billing key issued:", apiResponse);
        } catch (error) {
          console.error("Billing issue failed:", error);
        }
      })();
    }
  }, [customerKey, authKey]);

  return (
    <div>
      <h1>카드 등록이 완료되었습니다 ✅</h1>
      <p>잠시만 기다려주세요, 결제 정보 등록 중입니다.</p>
    </div>
  );
}