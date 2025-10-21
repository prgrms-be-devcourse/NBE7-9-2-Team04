"use client";

import { useState } from "react";
import { useRouter, useSearchParams } from "next/navigation";
import Link from "next/link";
import { fetchApi } from "@/lib/client";

export default function LoginPage() {
  const router = useRouter();
  const searchParams = useSearchParams();
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [isLoading, setIsLoading] = useState(false);
  const [activeTab, setActiveTab] = useState("user");
  const returnUrl = searchParams.get("returnUrl");

  const handleLogin = async (e: React.FormEvent<HTMLFormElement>) => {
    e.preventDefault();
    setIsLoading(true);

    try {
      const apiResponse = await fetchApi(`/api/v1/users/login`, {
        method: "POST",
        body: JSON.stringify({ email, password }),
      });

      if (apiResponse.status === "OK") {
        alert(apiResponse.message);

        window.dispatchEvent(new Event("loginSuccess")); //로그인 시 상단바 바로 바뀌도록 함

        const user = apiResponse.data;

        if (user.role === "ADMIN") {
          router.push("/admin");
        } else {
          router.push(returnUrl || "/");
        }
        router.refresh();
      } else {
        alert(apiResponse.message);
      }
    } catch (err: any) {
      alert(err.message);
    } finally {
      setIsLoading(false);
    }
  };

  return (
    <>
      <div className="min-h-screen flex mt-20 justify-center p-4">
        <div className="w-full max-w-md space-y-8">
          <div className="text-center">
            <h2 className="text-3xl font-bold text-gray-900">로그인</h2>
            <p className="text-gray-500 mt-2">
              DevStation에 오신 것을 환영합니다
            </p>
          </div>

          <div className="w-full bg-white rounded-lg shadow-lg border border-gray-200">
            <div className="flex w-full border-b border-gray-200">
              <div
                className={`flex-1 py-2 text-center cursor-pointer border-b-2 ${
                  activeTab === "user"
                    ? "border-blue-600 text-blue-600"
                    : "border-gray-200 text-gray-500"
                }`}
                onClick={() => setActiveTab("user")}
              >
                일반 로그인
              </div>
              <div
                className={`flex-1 py-2 text-center cursor-pointer border-b-2 ${
                  activeTab === "admin"
                    ? "border-blue-600 text-blue-600"
                    : "border-gray-200 text-gray-500"
                }`}
                onClick={() => setActiveTab("admin")}
              >
                관리자 로그인
              </div>
            </div>

            <div className="p-6">
              <form onSubmit={handleLogin} className="space-y-4">
                <div className="space-y-2">
                  <h3 className="text-xl font-semibold">
                    {activeTab === "admin"
                      ? "관리자 로그인"
                      : "일반 사용자 로그인"}
                  </h3>
                  {activeTab === "admin" && (
                    <p className="text-sm text-gray-500">
                      관리자 계정으로 로그인하세요
                    </p>
                  )}
                </div>

                <div className="space-y-4">
                  <div className="space-y-1">
                    <label
                      htmlFor="email"
                      className="text-sm font-medium block"
                    >
                      이메일
                    </label>
                    <input
                      id="email"
                      type="email"
                      className="w-full p-2 border border-gray-300 rounded focus:border-blue-500"
                      placeholder={
                        activeTab === "admin"
                          ? "admin@example.com"
                          : "user@example.com"
                      }
                      value={email}
                      onChange={(e) => setEmail(e.target.value)}
                      required
                    />
                  </div>

                  <div className="space-y-1">
                    <label
                      htmlFor="password"
                      className="text-sm font-medium block"
                    >
                      비밀번호
                    </label>
                    <input
                      id="password"
                      type="password"
                      className="w-full p-2 border border-gray-300 rounded focus:border-blue-500"
                      value={password}
                      onChange={(e) => setPassword(e.target.value)}
                      required
                    />
                  </div>
                </div>

                <div className="flex flex-col gap-3 pt-2">
                  <button
                    type="submit"
                    className={`w-full py-2 font-medium rounded transition-colors bg-blue-600 text-white hover:bg-blue-700 ${
                      isLoading ? "opacity-50 cursor-not-allowed" : ""
                    }`}
                    disabled={isLoading}
                  >
                    {isLoading ? "로그인 중..." : "로그인"}
                  </button>

                  {activeTab === "user" && (
                    <div className="text-sm text-center text-gray-500">
                      계정이 없으신가요?{" "}
                      <Link
                        href="/auth/signup"
                        className="text-blue-600 hover:underline"
                      >
                        회원가입
                      </Link>
                    </div>
                  )}
                </div>
              </form>
            </div>
          </div>
        </div>
      </div>
    </>
  );
}
