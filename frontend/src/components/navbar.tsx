"use client"

import Link from "next/link"
import { usePathname } from "next/navigation"
import { useEffect, useState } from "react"
import { useRouter } from "next/navigation"

export default function Navbar() {
  const pathname = usePathname()
  const router = useRouter()
  const [isLoggedIn, setIsLoggedIn] = useState(false)
  const [isAdmin, setIsAdmin] = useState(false)
  const [userName, setUserName] = useState("")

  // 상단바 메뉴 링크의 기본/활성 스타일
  const linkBaseClasses = "inline-flex h-10 w-max items-center justify-center rounded-md bg-white px-4 py-2 text-sm font-medium hover:bg-gray-100 transition-colors"
  const activeLinkClasses = "bg-blue-100 text-blue-800"

  useEffect(() => {
    // window/localStorage 접근을 클라이언트 환경에서만 수행
    if (typeof localStorage !== 'undefined') {
      const loggedIn = localStorage.getItem("isLoggedIn") === "true"
      const role = localStorage.getItem("userRole")
      const name = localStorage.getItem("userName") || ""

      setIsLoggedIn(loggedIn)
      setIsAdmin(role === "admin")
      setUserName(name)

    }
  }, [pathname])

  const handleLogout = () => {
    if (typeof localStorage !== 'undefined') {
      localStorage.removeItem("isLoggedIn")
      localStorage.removeItem("userRole")
      localStorage.removeItem("userName")
    }
    setIsLoggedIn(false)
    setIsAdmin(false)
    setUserName("")

    if (pathname === "/profile") {
      window.location.href = "/"
    } else {
      window.location.reload()
    }
  }

  // 드롭다운 메뉴 아이템 컴포넌트 (HTML로 단순화)
  const DropdownItem = ({ href, title, description }: { href: string, title: string, description: string }) => (
    <li>
      <Link
        href={href}
        className="block p-3 rounded-md hover:bg-gray-100 transition-colors outline-none focus:bg-gray-100"
      >
        <div className="text-sm font-medium">{title}</div>
        <p className="text-xs text-gray-500 mt-1">{description}</p>
      </Link>
    </li>
  )

  return (
    <>
    <header className="sticky top-0 z-50 w-full border-b border-gray-200 bg-white/95 backdrop-blur supports-[backdrop-filter]:bg-white/60 text-gray-900">
      <div className="container mx-auto flex h-16 items-center justify-between px-4">
        
        <div className="flex items-center gap-6">
          <Link href="/" className="flex items-center space-x-2">
            <span className="font-bold text-2xl">DevStation</span>
          </Link>

          <nav className="hidden md:flex">
            <ul className="flex items-center gap-1">
              
              <li>
                <Link 
                  href="/recruitment" 
                  className={`${linkBaseClasses} ${pathname === "/recruitment" ? activeLinkClasses : ""}`}
                >
                  👥 모집
                </Link>
              </li>

              <li className="relative group">
                <div className="inline-flex h-10 px-4 items-center justify-center text-sm font-medium cursor-pointer hover:bg-gray-100 rounded-md">
                  💡 면접 준비
                </div>
                <div className="absolute top-full left-0 mt-0 w-[400px] hidden group-hover:block bg-white border border-gray-200 rounded-lg shadow-lg">
                  <ul className="p-4 space-y-2">
                    <DropdownItem
                      href="/interview/cs"
                      title="CS 면접 질문"
                      description="네트워크, 운영체제 등 CS 지식 문제 풀이"
                    />
                    <DropdownItem
                      href="/interview/portfolio"
                      title="포트폴리오 면접"
                      description="AI 기반 포트폴리오 분석 및 예상 질문"
                    />
                  </ul>
                </div>
              </li>

              <li>
                <Link 
                  href="/qna" 
                  className={`${linkBaseClasses} ${pathname === "/qna" ? activeLinkClasses : ""}`}
                >
                  💬 QnA
                </Link>
              </li>

              <li>
                <Link 
                  href="/ranking" 
                  className={`${linkBaseClasses} ${pathname === "/ranking" ? activeLinkClasses : ""}`}
                >
                  🏆 랭킹
                </Link>
              </li>

              {isLoggedIn && isAdmin && (
                <li>
                  <Link 
                    href="/admin" 
                    className={`${linkBaseClasses} ${pathname?.startsWith("/admin") ? activeLinkClasses : ""}`}
                  >
                    <span className="w-4 h-4 mr-2">🛡️</span>
                    관리자
                  </Link>
                </li>
              )}
            </ul>
          </nav>
        </div>

        <div className="flex items-center gap-2">
          {isLoggedIn ? (
            <>
              <Link href="/profile" className="inline-flex items-center justify-center h-10 w-10 rounded-md hover:bg-gray-100 p-2">
                <span className="h-5 w-5 text-lg">👤</span>
              </Link>
              
              <button 
                onClick={handleLogout}
                className="inline-flex h-10 px-4 bg-white hover:bg-gray-100 text-gray-800 font-medium rounded-md items-center justify-center border border-gray-300"
              >
                <span className="w-4 h-4 mr-2">➡️</span>
                로그아웃
              </button>
            </>
          ) : (
            <>
              <Link href="/profile" className="inline-flex items-center justify-center h-10 w-10 rounded-md hover:bg-gray-100 p-2">
                <span className="h-5 w-5 text-lg">👤</span>
              </Link>
              
              <Link href="/auth" 
                className="inline-flex h-10 px-4 bg-blue-600 text-white font-medium rounded-md hover:bg-blue-700 items-center justify-center"
              >
                로그인
              </Link>
            </>
          )}
        </div>
      </div>
    </header>
    </>
  );
}