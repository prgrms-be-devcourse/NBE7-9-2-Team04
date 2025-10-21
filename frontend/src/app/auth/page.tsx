"use client";

import { useState } from "react";
import { useRouter, useSearchParams } from "next/navigation";
import Link from "next/link";
import { fetchApi } from "@/lib/client";

export default function LoginPage() {
  const router = useRouter();
  const searchParams = useSearchParams();
  const [email, setEmail] = useState({ user: "", admin: "" });
  const [password, setPassword] = useState({ user: "", admin: "" });
  const [isLoading, setIsLoading] = useState(false);
  const [activeTab, setActiveTab] = useState("user");
  const returnUrl = searchParams.get("returnUrl");

  //백 연동 코드 추후 수정 예정. 임시로 넣어둠
  const handleLogin = async (
    e: React.FormEvent<HTMLFormElement>,
    isAdmin = false
  ) => {
    e.preventDefault();
    setIsLoading(true);

    const currentEmail = isAdmin ? email.admin : email.user;
    const currentPassword = isAdmin ? password.admin : password.user;
    const apiPath = isAdmin ? "/api/v1/admin/login" : "/api/v1/users/login";

    if (currentEmail.length < 2 || currentPassword.length < 2) {
      alert("이메일과 비밀번호는 2자 이상 입력해주세요.");
      setIsLoading(false);
      return;
    }

    try {
      const data = await fetchApi(apiPath, {
        method: "POST",
        body: JSON.stringify({
          email: currentEmail,
          password: currentPassword,
        }),
      });

      const resultCode = String(data.resultCode);

      if (resultCode.startsWith("200")) {
        alert(data.msg || "로그인 성공");
        window.location.href = returnUrl || "/";
      } else {
        alert(data.msg || "로그인 실패");
      }
    } catch (err: any) {
      alert(err.message || "로그인 중 오류가 발생했습니다.");
    } finally {
      setIsLoading(false);
    }
  };

  return (
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

          {activeTab === "user" && (
            <div className="p-6">
              <form
                onSubmit={(e) => handleLogin(e, false)}
                className="space-y-4"
              >
                <div className="space-y-2">
                  <h3 className="text-xl font-semibold">일반 사용자</h3>
                </div>

                <div className="space-y-4">
                  <div className="space-y-1">
                    <label
                      htmlFor="user-email"
                      className="text-sm font-medium block"
                    >
                      이메일
                    </label>
                    <input
                      id="user-email"
                      type="email"
                      className="w-full p-2 border border-gray-300 rounded focus:border-blue-500"
                      placeholder="user@example.com"
                      value={email.user}
                      onChange={(e) =>
                        setEmail((prev) => ({ ...prev, user: e.target.value }))
                      }
                      required
                    />
                  </div>
                  <div className="space-y-1">
                    <label
                      htmlFor="user-password"
                      className="text-sm font-medium block"
                    >
                      비밀번호
                    </label>
                    <input
                      id="user-password"
                      type="password"
                      className="w-full p-2 border border-gray-300 rounded focus:border-blue-500"
                      value={password.user}
                      onChange={(e) =>
                        setPassword((prev) => ({
                          ...prev,
                          user: e.target.value,
                        }))
                      }
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
                  <div className="text-sm text-center text-gray-500">
                    계정이 없으신가요?{" "}
                    <Link
                      href="/auth/signup"
                      className="text-blue-600 hover:underline"
                    >
                      회원가입
                    </Link>
                  </div>
                </div>
              </form>
            </div>
          )}

          {activeTab === "admin" && (
            <div className="p-6">
              <form
                onSubmit={(e) => handleLogin(e, true)}
                className="space-y-4"
              >
                <div className="space-y-2">
                  <h3 className="text-xl font-semibold">관리자</h3>
                  <p className="text-sm text-gray-500">
                    관리자 계정으로 로그인하세요
                  </p>
                </div>

                <div className="space-y-4">
                  <div className="space-y-1">
                    <label
                      htmlFor="admin-email"
                      className="text-sm font-medium block"
                    >
                      이메일
                    </label>
                    <input
                      id="admin-email"
                      type="email"
                      className="w-full p-2 border border-gray-300 rounded focus:border-blue-500"
                      placeholder="admin@example.com"
                      value={email.admin}
                      onChange={(e) =>
                        setEmail((prev) => ({ ...prev, admin: e.target.value }))
                      }
                      required
                    />
                  </div>
                  <div className="space-y-1">
                    <label
                      htmlFor="admin-password"
                      className="text-sm font-medium block"
                    >
                      비밀번호
                    </label>
                    <input
                      id="admin-password"
                      type="password"
                      className="w-full p-2 border border-gray-300 rounded focus:border-blue-500"
                      value={password.admin}
                      onChange={(e) =>
                        setPassword((prev) => ({
                          ...prev,
                          admin: e.target.value,
                        }))
                      }
                      required
                    />
                  </div>
                </div>

                <div className="pt-2">
                  <button
                    type="submit"
                    className={`w-full py-2 font-medium rounded transition-colors bg-blue-600 text-white hover:bg-blue-700 ${
                      isLoading ? "opacity-50 cursor-not-allowed" : ""
                    }`}
                    disabled={isLoading}
                  >
                    {isLoading ? "로그인 중..." : "관리자 로그인"}
                  </button>
                </div>
              </form>
            </div>
          )}
        </div>
      </div>
    </div>
  );
}
