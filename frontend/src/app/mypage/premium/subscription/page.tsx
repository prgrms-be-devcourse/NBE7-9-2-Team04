"use client"

import Link from "next/link";

export default function SubscriptionPage() {
    return (
      <div>
        <h1 className="text-2xl font-bold mb-4">구독 서비스</h1>
          <Link
            href="/mypage/checkout"
            className="bg-green-600 text-white px-5 py-3 rounded-md hover:bg-green-700 transition"
          >
            결제하기
          </Link>
      </div>
    );
  }