"use client"
//마이 페이지

import { useRouter } from "next/navigation";
import { useState } from "react";
import { fetchApi } from "@/lib/client";
import Link from "next/link";

//관리자 로그인 페이지
export default function MyPage() {
  const router = useRouter();



  return (
    <>
    <div>
      <h1 className="text-2xl font-bold mb-6">마이페이지</h1>

      <Link
        href="/mypage/subscription"
        className="bg-blue-600 text-white px-5 py-3 rounded-md hover:bg-blue-700 transition"
      >
        구독 서비스
      </Link>
    </div>
    </>
  );
}