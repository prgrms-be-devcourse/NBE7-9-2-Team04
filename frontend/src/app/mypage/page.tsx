"use client";

import Link from "next/link";

//임시 마이 페이지 - 목업 수정 후 다시 수정 예정
export default function MyPageHome() {
  const menu = [
    {
      title: "활동 내역",
      desc: "내가 작성한 글, 댓글, 해결한 문제를 확인하세요.",
      href: "/mypage/activity",
    },
    {
      title: "이력서 관리",
      desc: "나의 기술 스택과 경력 정보를 정리하고 관리하세요.",
      href: "/mypage/resume",
    },
    {
      title: "개인정보 수정",
      desc: "회원가입 시 입력한 정보를 수정할 수 있습니다.",
      href: "/mypage/settings",
    },
    {
      title: "유료 서비스",
      desc: "프리미엄 멤버십 혜택을 확인하고 구독을 관리하세요.",
      href: "/mypage/premium",
    },
  ];

  return (
    <div className="space-y-8">
      <div>
        <h1 className="text-3xl font-bold">마이페이지</h1>
        <p className="text-gray-500 mt-2">
          내 활동과 정보를 한눈에 확인하고 관리하세요.
        </p>
      </div>

      <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
        {menu.map((item) => (
          <Link
            key={item.href}
            href={item.href}
            className="group border rounded-lg bg-white p-6 shadow-sm hover:shadow-md hover:border-blue-400 transition-all"
          >
            <h2 className="text-lg font-semibold group-hover:text-blue-600">
              {item.title}
            </h2>
            <p className="text-sm text-gray-500 mt-2">{item.desc}</p>
            <p className="mt-4 text-sm text-blue-500 font-medium">
              바로가기 →
            </p>
          </Link>
        ))}
      </div>
    </div>
  );
}
