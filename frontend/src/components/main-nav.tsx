"use client"

import Link from "next/link";

export default function Navbar() {
  return (
    <>
      <nav className="w-full text-gray-800 px-6 py-3 flex justify-between items-center">
        <Link href="/" className="font-bold text-3xl">
          devstation
        </Link>

        <ul className="flex gap-10 text-lg text-black">
          <li>
            <Link href="/" className="font-bold hover:underline">
              홈
            </Link>
          </li>
          <li>
            <Link href="/mypage" className="font-bold hover:underline">
              마이페이지
            </Link>
          </li>
        </ul>
      </nav>
    </>
  );
}