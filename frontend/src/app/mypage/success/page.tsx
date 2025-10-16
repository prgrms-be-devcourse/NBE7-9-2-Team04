"use client";

import { useEffect, useState } from "react";
import { useSearchParams } from "next/navigation";
import Link from "next/link";

export default function SuccessPage() {
  const searchParams = useSearchParams();
  const [responseData, setResponseData] = useState<any>(null);

  useEffect(() => {
    async function confirm() {
      const requestData = {
        orderId: searchParams.get("orderId"),
        amount: searchParams.get("amount"),
        paymentKey: searchParams.get("paymentKey"),
      };

      const response = await fetch("http://localhost:8080/api/v1/payments/confirm", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(requestData),
      });

      const json = await response.json();

      if (!response.ok) {
        throw { message: json.message, code: json.code };
      }

      setResponseData(json);
    }

    confirm().catch((error) => {
      window.location.href = `/fail?code=${error.code}&message=${error.message}`;
    });
  }, []);

  return (
    <div className="flex flex-col items-center py-10">
      <img
        src="https://static.toss.im/illusts/check-blue-spot-ending-frame.png"
        width={100}
        alt="success"
        className="mb-4"
      />
      <h2 className="text-2xl font-bold mb-6">결제를 완료했어요 🎉</h2>

      <div className="w-[360px] text-left bg-white p-4 rounded-md shadow">
        <p>
          <b>결제 금액:</b>{" "}
          {Number(searchParams.get("amount")).toLocaleString()}원
        </p>
        <p>
          <b>주문번호:</b> {searchParams.get("orderId")}
        </p>
        <p>
          <b>paymentKey:</b> {searchParams.get("paymentKey")}
        </p>
      </div>

      {responseData && (
        <div className="mt-6 w-[400px] bg-gray-100 p-4 rounded-md text-sm">
          <b>서버 응답:</b>
          <pre>{JSON.stringify(responseData, null, 2)}</pre>
        </div>
      )}

      <Link
        href="/mypage"
        className="mt-6 bg-blue-600 text-white px-4 py-2 rounded-md hover:bg-blue-700"
      >
        마이페이지로 돌아가기
      </Link>
    </div>
  );
}
