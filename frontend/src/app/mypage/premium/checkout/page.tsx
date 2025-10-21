"use client"

//결제 성공
import { loadTossPayments, ANONYMOUS } from "@tosspayments/tosspayments-sdk";
import { useEffect, useState } from "react";

const clientKey = process.env.NEXT_PUBLIC_TOSS_CLIENT_KEY!;
const customerKey = generateRandomString();

//결제창 띄우기
export default function PaymentCheckoutPage() {
  const [payment, setPayment] = useState<any>(null);

  useEffect(() => {
    async function fetchPayment() {
      try {
        // ------  SDK 초기화 ------
        const tossPayments = await loadTossPayments(clientKey);

        // 회원 결제
        const payment = tossPayments.payment({customerKey,});

        setPayment(payment);
      } catch (error) {
        console.error("Error fetching payment:", error);
      }
    }

    fetchPayment();
  }, [clientKey, customerKey]);

  //카드 등록하기 버튼 누르면 결제창 띄우기

  async function requestBillingAuth() {
    await payment.requestBillingAuth({
      method: "CARD",
      successUrl: window.location.origin + "/mypage/premium/success", // 요청이 성공하면 리다이렉트되는 URL
      failUrl: window.location.origin + "/mypage/premium//fail", // 요청이 실패하면 리다이렉트되는 URL
      customerKey: "customer123@gmail.com",

    });
  }

  return(
    <button className="button" onClick={()=> requestBillingAuth()}>
      카드 등록하기
    </button>
  );

}

function generateRandomString() {
  return window.btoa(Math.random().toString()).slice(0, 20);
}
