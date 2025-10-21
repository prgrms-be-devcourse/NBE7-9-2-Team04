"use client";

import { useEffect } from "react";
import { useRouter, useSearchParams } from "next/navigation";

export default function SuccessPage() {
  const router = useRouter();
  const searchParams = useSearchParams();
  const customerKey = searchParams.get("customerKey");
  const authKey = searchParams.get("authKey");

  useEffect(() => {
    if (customerKey && authKey) {
      // 서버로 전송해서 billingKey 발급 요청
      fetch("/api/billing/issue-key", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ customerKey, authKey }),
      })
        .then((res) => res.json())
        .then((data) => {
          console.log("billingKey 발급 완료:", data);
          router.push("/billing/complete");
        })
        .catch((err) => console.error("billingKey 발급 실패:", err));
    }
  }, [customerKey, authKey]);

  return <p>카드 인증이 완료되었습니다. 잠시만 기다려주세요...</p>;
}