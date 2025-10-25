"use client";

import { useState } from "react";
import { useRouter, useSearchParams } from "next/navigation";
import Link from "next/link";
import { fetchApi } from "@/lib/client";
import { UserSignupRequest } from "@/types/user";

export default function SignupPage() {
  const router = useRouter();
  const [isLoading, setIsLoading] = useState(false);
  const [confirmPassword, setConfirmPassword] = useState("");
  const [formData, setFormData] = useState<UserSignupRequest>({
    email: "",
    password: "",
    name: "",
    nickname: "",
    age: 0,
    github: ""
  });

  const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const { name, value } = e.target;

    setFormData((prev) => ({
      ...prev,
      [name]: name === "age" ? Number(value) : value, // age는 숫자로 변환
    }));
  };

  const validate = (): boolean => {
    if (!formData.email || !formData.password || !formData.name) {
      alert("필수 항목을 모두 입력해주세요.");
      return false;
    }
    if (formData.password !== confirmPassword) {
      alert("비밀번호가 일치하지 않습니다.");
      return false;
    }
    return true;
  };

  const handleSubmit = async (e: React.FormEvent<HTMLFormElement>) => {
    e.preventDefault();
    if (!validate()) return;

    setIsLoading(true);

    try {
      const apiResponse = await fetchApi(`/api/v1/users/signup`, {
        method: "POST",
        body: JSON.stringify(formData),
      });

      if (apiResponse.status === "OK") {
        alert(apiResponse.message);
        router.replace("/auth"); // 로그인 페이지로 이동
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
      <div className="min-h-screen flex items-center justify-center px-4 py-12 bg-gray-50">
        <div className="w-full max-w-md space-y-8">
          <div className="text-center">
            <h2 className="text-3xl font-bold text-gray-900">회원가입</h2>
            <p className="text-gray-500 mt-2">
              DevStation에 가입하고 다양한 기능을 이용해보세요
            </p>
          </div>

          <form
            onSubmit={handleSubmit}
            className="bg-white rounded-lg shadow-md border border-gray-200 p-6 space-y-4"
          >
            <Input
              label="이메일"
              name="email"
              type="email"
              value={formData.email}
              onChange={handleChange}
              required
            />
            <Input
              label="비밀번호"
              name="password"
              type="password"
              value={formData.password}
              onChange={handleChange}
              required
            />
            <Input
              label="비밀번호 확인"
              name="confirmPassword"
              type="password"
              value={confirmPassword}
              onChange={(e) => setConfirmPassword(e.target.value)}
              required
            />
            <Input
              label="이름"
              name="name"
              value={formData.name}
              onChange={handleChange}
              required
            />
            <Input
              label="닉네임"
              name="nickname"
              value={formData.nickname}
              onChange={handleChange}
              required
            />
            <Input
              label="나이"
              name="age"
              type="number"
              value={formData.age.toString()}
              onChange={handleChange}
              required
            />
            <Input
              label="GitHub 주소"
              name="github"
              value={formData.github}
              onChange={handleChange}
              required
            />

            <button
              type="submit"
              disabled={isLoading}
              className={`w-full py-2 font-medium rounded bg-blue-600 text-white hover:bg-blue-700 ${
                isLoading ? "opacity-50 cursor-not-allowed" : ""
              }`}
            >
              {isLoading ? "가입 중..." : "회원가입"}
            </button>

            <p className="text-sm text-center text-gray-500 mt-3">
              이미 계정이 있으신가요?{" "}
              <Link href="/auth" className="text-blue-600 hover:underline">
                로그인
              </Link>
            </p>
          </form>
        </div>
      </div>
    </>
  );
}

function Input({
  label,
  name,
  type = "text",
  value,
  onChange,
  required,
}: {
  label: string;
  name: string;
  type?: string;
  value: string;
  onChange: React.ChangeEventHandler<HTMLInputElement>;
  required?: boolean;
}) {
  return (
    <div className="space-y-1">
      <label htmlFor={name} className="text-sm font-medium block text-gray-700">
        {label}
      </label>
      <input
        id={name}
        name={name}
        type={type}
        value={value}
        onChange={onChange}
        required={required}
        className="w-full p-2 border border-gray-300 rounded focus:border-blue-500 transition-colors"
      />
    </div>
  );
}
