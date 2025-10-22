"use client";

import { useEffect, useState } from "react";
import { useRouter, useSearchParams } from "next/navigation";
import { fetchApi } from "@/lib/client";

export default function SuccessPage() {
  const searchParams = useSearchParams();
  const customerKey = searchParams.get("customerKey");
  const authKey = searchParams.get("authKey");
  const router = useRouter();
  const [message, setMessage] = useState("카드 등록이 완료되었습니다 ✅");

  useEffect(() => {
    if (customerKey && authKey) {
      (async () => {
        try {
          const apiResponse = await fetchApi(`/api/v1/billing/confirm`, {
            method: "POST",
            body: JSON.stringify({
              customerKey,
              authKey,
            }),
          });

          setMessage(
            "카드 등록 및 결제가 완료되었습니다.<br/> 다음 결제일은 오늘로부터 30일 뒤에 자동으로 진행됩니다."
          );

          // 3초 후 자동 이동
          setTimeout(() => {
            router.push("/mypage/premium");
          }, 3000);
        } catch (error) {
          console.error("Billing issue failed:", error);
          setMessage(
            "결제 정보 등록 중 오류가 발생했습니다. 다시 시도해주세요."
          );
        }
      })();
    }
  }, [customerKey, authKey, router]);

  return (
    <>
      <div className="flex flex-col items-center justify-center min-h-screen text-center px-6">
        <h1
          className="text-2xl font-bold mb-4"
          dangerouslySetInnerHTML={{ __html: message }}
        />
        <p className="text-gray-500">잠시만 기다려주세요...</p>
      </div>
    </>
  );
}
