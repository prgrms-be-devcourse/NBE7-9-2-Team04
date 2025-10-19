"use client"

import { useState } from "react"
import { useRouter, useSearchParams } from "next/navigation"
import Link from "next/link"
import { fetchApi } from "@/lib/client"; 


// 폼 데이터 타입 정의(수정 예정)
interface FormData {
  email: string
  password: string
  confirmPassword: string
  name: string
  nickname: string
  age: string
  githubUrl: string
}

// 에러 메시지 타입 정의(수정 예정)
interface Errors {
  email: string
  password: string
}

export default function SignupPage() {

    const router = useRouter();
  const [isLoading, setIsLoading] = useState(false)
  const [formData, setFormData] = useState<FormData>({
    email: "",
    password: "",
    confirmPassword: "",
    name: "",
    nickname: "",
    age: "",
    githubUrl: "",
  })
  const [errors, setErrors] = useState<Errors>({
    email: "",
    password: "",
  })

  const handleInputChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const { name, value } = e.target
    setFormData((prev) => ({ ...prev, [name]: value }))

    // 사용자가 입력할 때 관련 에러 메시지 초기화
    if (name === "email" && errors.email) {
      setErrors((prev) => ({ ...prev, email: "" }))
    }
    if ((name === "password" || name === "confirmPassword") && errors.password) {
      setErrors((prev) => ({ ...prev, password: "" }))
    }
  }

  const validateForm = (): boolean => {
    let isValid = true
    const newErrors: Errors = { email: "", password: "" }
    
    if (!formData.email) {
      newErrors.email = "이메일을 입력해주세요";
      isValid = false;
    }
    if (!formData.password || !formData.confirmPassword) {
      newErrors.password = "비밀번호를 입력해주세요";
      isValid = false;
    }
    
    if (formData.password && formData.password.length < 6) {
      newErrors.password = "비밀번호는 최소 6자 이상이어야 합니다"
      isValid = false
    }


    if (formData.password && formData.confirmPassword && formData.password !== formData.confirmPassword) {
      newErrors.password = "비밀번호가 일치하지 않습니다"
      isValid = false
    }
    if (!formData.name || !formData.nickname || !formData.age) {
        alert("모든 필수 항목(*)을 입력해주세요.");
        isValid = false;
    }

    setErrors(newErrors)
    return isValid
  }

  const handleSubmit = async (e: React.FormEvent<HTMLFormElement>) => {
    e.preventDefault()

    if (!validateForm()) {
      return
    }

    setIsLoading(true)

    try {
      const data: any = await fetchApi("/api/v1/users/signup", {
        method: "POST",
        body: JSON.stringify({
          email: formData.email,
          password: formData.password,
          name: formData.name,
          nickname: formData.nickname,
          age: parseInt(formData.age), // 숫자 형식으로 변환
          githubUrl: formData.githubUrl,
        }),
      });

      const resultCode = String(data.resultCode);

      if (resultCode.startsWith("20")) {
        alert(data.msg || "회원가입 성공! 로그인 페이지로 이동합니다.");
        router.replace("/login");
      } else if (resultCode === "409") {
        setErrors(prev => ({ ...prev, email: data.msg || "이미 사용 중인 이메일입니다" }));
      } else {
        // 기타 서버 에러
        alert(data.msg || "회원가입 중 오류가 발생했습니다.");
      }
    } catch (err: any) {
      alert(err.message || "네트워크 오류 또는 예상치 못한 문제가 발생했습니다.");
    } finally {
      setIsLoading(false);
    }
  }

  return (
    <div className="min-h-screen flex items-center justify-center px-4 py-12 bg-gray-50">
      <div className="w-full max-w-md space-y-8">
        <div className="text-center">
          <h2 className="text-3xl font-bold text-gray-900">회원가입</h2>
          <p className="text-gray-500 mt-2">DevStation에 가입하고 다양한 기능을 이용해보세요</p>
        </div>

        <div className="bg-white rounded-lg shadow-xl border border-gray-200">
          <div className="p-6">
            <p className="text-sm text-gray-500 mb-6">아래 정보를 입력하여 회원가입을 완료하세요</p>
            
            <form onSubmit={handleSubmit} className="space-y-4">

              <div className="space-y-1">
                <label htmlFor="email" className="text-sm font-medium block text-gray-700">이메일 *</label>
                <input
                  id="email"
                  name="email"
                  type="email"
                  className={`w-full p-2 border ${errors.email ? "border-red-500" : "border-gray-300 focus:border-blue-500"} rounded transition-colors`}
                  placeholder="example@email.com"
                  value={formData.email}
                  onChange={handleInputChange}
                  required
                />
                {errors.email && (
                  <div className="flex items-center gap-1 text-sm text-red-500 mt-1">
                    <span>{errors.email}</span>
                  </div>
                )}
              </div>

              <div className="space-y-1">
                <label htmlFor="password" className="text-sm font-medium block text-gray-700">비밀번호 *</label>
                <input
                  id="password"
                  name="password"
                  type="password"
                  className={`w-full p-2 border ${errors.password ? "border-red-500" : "border-gray-300 focus:border-blue-500"} rounded transition-colors`}
                  placeholder="최소 6자 이상"
                  value={formData.password}
                  onChange={handleInputChange}
                  required
                />
              </div>

              <div className="space-y-1">
                <label htmlFor="confirmPassword" className="text-sm font-medium block text-gray-700">비밀번호 확인 *</label>
                <input
                  id="confirmPassword"
                  name="confirmPassword"
                  type="password"
                  className={`w-full p-2 border ${errors.password ? "border-red-500" : "border-gray-300 focus:border-blue-500"} rounded transition-colors`}
                  placeholder="비밀번호를 다시 입력하세요"
                  value={formData.confirmPassword}
                  onChange={handleInputChange}
                  required
                />
                {errors.password && (
                  <div className="flex items-center gap-1 text-sm text-red-500 mt-1">
                    <span>{errors.password}</span>
                  </div>
                )}
              </div>

              <div className="space-y-1">
                <label htmlFor="name" className="text-sm font-medium block text-gray-700">이름 *</label>
                <input
                  id="name"
                  name="name"
                  type="text"
                  className="w-full p-2 border border-gray-300 rounded focus:border-blue-500 transition-colors"
                  placeholder="이름"
                  value={formData.name}
                  onChange={handleInputChange}
                  required
                />
              </div>

              <div className="space-y-1">
                <label htmlFor="nickname" className="text-sm font-medium block text-gray-700">닉네임 *</label>
                <input
                  id="nickname"
                  name="nickname"
                  type="text"
                  className="w-full p-2 border border-gray-300 rounded focus:border-blue-500 transition-colors"
                  placeholder="닉네임"
                  value={formData.nickname}
                  onChange={handleInputChange}
                  required
                />
              </div>

              <div className="space-y-1">
                <label htmlFor="age" className="text-sm font-medium block text-gray-700">나이 *</label>
                <input
                  id="age"
                  name="age"
                  type="number"
                  className="w-full p-2 border border-gray-300 rounded focus:border-blue-500 transition-colors"
                  placeholder="나이"
                  min="1"
                  max="100"
                  value={formData.age}
                  onChange={handleInputChange}
                  required
                />
              </div>

              <div className="space-y-1">
                <label htmlFor="githubUrl" className="text-sm font-medium block text-gray-700">깃허브 링크</label>
                <input
                  id="githubUrl"
                  name="githubUrl"
                  type="url"
                  className="w-full p-2 border border-gray-300 rounded focus:border-blue-500 transition-colors"
                  placeholder="https://github.com/username"
                  value={formData.githubUrl}
                  onChange={handleInputChange}
                />
              </div>

              <div className="flex flex-col gap-4 pt-2">
                <button 
                  type="submit" 
                  className={`w-full py-2 font-medium rounded transition-colors bg-blue-600 text-white hover:bg-blue-700 ${isLoading ? 'opacity-50 cursor-not-allowed' : ''}`}
                  disabled={isLoading}
                >
                  {isLoading ? "가입 중..." : "회원가입"}
                </button>
                <div className="text-sm text-center text-gray-500">
                  이미 계정이 있으신가요?{" "}
                  <Link href="/auth" className="text-blue-600 hover:underline">
                    로그인
                  </Link>
                </div>
              </div>
            </form>
          </div>
        </div>
      </div>
    </div>
  )
}