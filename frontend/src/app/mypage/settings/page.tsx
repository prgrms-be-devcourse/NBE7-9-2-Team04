"use client";

import { useState, useEffect } from "react";

export default function SettingsPage() {
  const [isLoading, setIsLoading] = useState(true);
  const [showPasswordModal, setShowPasswordModal] = useState(true);
  const [passwordInput, setPasswordInput] = useState("");
  const [passwordError, setPasswordError] = useState("");
  const [canEdit, setCanEdit] = useState(false);


  const [formData, setFormData] = useState({
    email: "kim@example.com",
    password: "",
    name: "김개발",
    nickname: "devkim",
    age: "25",
    githubUrl: "https://github.com/devkim",
  });

  useEffect(() => {
    const timer = setTimeout(() => setIsLoading(false), 200);
    return () => clearTimeout(timer);
  }, []);


  const handleVerifyPassword = () => {
    const dummyPassword = "user1234"; // 임시 패스워드
    if (passwordInput === dummyPassword) {
      setCanEdit(true);
      setShowPasswordModal(false);
      setPasswordError("");
      setPasswordInput("");
    } else {
      setPasswordError("비밀번호가 일치하지 않습니다.");
    }
  };


  const handleSave = () => {
    console.log("저장할 데이터:", formData);
    alert("정보가 저장되었습니다. (임시)");
  };

  if (isLoading) {
    return (
      <div className="flex items-center justify-center min-h-[60vh] text-gray-500">
        로딩 중...
      </div>
    );
  }

  return (
    <div className="relative">
      {/* 비밀번호 확인  */}
      {showPasswordModal && (
        <div className="fixed inset-0 bg-black/30 flex items-center justify-center z-50">
          <div className="bg-white p-6 rounded-lg shadow-lg w-96">
            <h2 className="text-xl font-semibold mb-2">비밀번호 확인</h2>
            <p className="text-sm text-gray-600 mb-4">
              개인정보 수정을 위해 비밀번호를 입력해주세요.
            </p>
            <input
              type="password"
              placeholder="비밀번호 입력"
              className="w-full border border-gray-300 rounded-md p-2 mb-2 focus:outline-blue-500"
              value={passwordInput}
              onChange={(e) => {
                setPasswordInput(e.target.value);
                setPasswordError("");
              }}
              onKeyDown={(e) => e.key === "Enter" && handleVerifyPassword()}
            />
            {passwordError && (
              <p className="text-sm text-red-500 mb-3">{passwordError}</p>
            )}
            <div className="flex justify-end gap-2">
              <button
                className="px-3 py-1 border rounded-md hover:bg-gray-100"
                onClick={() => (window.location.href = "/profile")}
              >
                취소
              </button>
              <button
                className="px-3 py-1 bg-blue-600 text-white rounded-md hover:bg-blue-700"
                onClick={handleVerifyPassword}
              >
                확인
              </button>
            </div>
          </div>
        </div>
      )}


      {canEdit ? (
        <div className="bg-white border border-gray-200 rounded-lg p-8 shadow-sm max-w-2xl mx-auto">
          <h2 className="text-2xl font-bold mb-2">개인정보 수정</h2>
          <p className="text-gray-500 mb-6">
            회원 정보를 수정하고 저장하세요.
          </p>

          <form
            className="space-y-5"
            onSubmit={(e) => {
              e.preventDefault();
              handleSave();
            }}
          >
            <div>
              <label className="block text-sm font-medium mb-1">이메일</label>
              <input
                type="email"
                className="w-full border border-gray-300 rounded-md p-2"
                value={formData.email}
                onChange={(e) =>
                  setFormData({ ...formData, email: e.target.value })
                }
              />
            </div>

            <div>
              <label className="block text-sm font-medium mb-1">비밀번호</label>
              <input
                type="password"
                className="w-full border border-gray-300 rounded-md p-2"
                placeholder="새 비밀번호 입력 (선택)"
                value={formData.password}
                onChange={(e) =>
                  setFormData({ ...formData, password: e.target.value })
                }
              />
            </div>

            <div className="grid grid-cols-2 gap-4">
              <div>
                <label className="block text-sm font-medium mb-1">이름</label>
                <input
                  type="text"
                  className="w-full border border-gray-300 rounded-md p-2"
                  value={formData.name}
                  onChange={(e) =>
                    setFormData({ ...formData, name: e.target.value })
                  }
                />
              </div>

              <div>
                <label className="block text-sm font-medium mb-1">닉네임</label>
                <input
                  type="text"
                  className="w-full border border-gray-300 rounded-md p-2"
                  value={formData.nickname}
                  onChange={(e) =>
                    setFormData({ ...formData, nickname: e.target.value })
                  }
                />
              </div>
            </div>

            <div>
              <label className="block text-sm font-medium mb-1">나이</label>
              <input
                type="number"
                min="0"
                className="w-full border border-gray-300 rounded-md p-2"
                value={formData.age}
                onChange={(e) =>
                  setFormData({ ...formData, age: e.target.value })
                }
              />
            </div>

            <div>
              <label className="block text-sm font-medium mb-1">
                GitHub URL
              </label>
              <input
                type="url"
                className="w-full border border-gray-300 rounded-md p-2"
                placeholder="https://github.com/username"
                value={formData.githubUrl}
                onChange={(e) =>
                  setFormData({ ...formData, githubUrl: e.target.value })
                }
              />
            </div>

            <div className="flex gap-3 mt-6">
              <button
                type="submit"
                className="flex-1 bg-blue-600 text-white py-2 rounded-md hover:bg-blue-700"
              >
                저장
              </button>
              <button
                type="button"
                onClick={() => {
                  setCanEdit(false);
                  setShowPasswordModal(true);
                }}
                className="flex-1 border border-gray-300 py-2 rounded-md hover:bg-gray-100"
              >
                취소
              </button>
            </div>
          </form>
        </div>
      ) : (
        <div className="text-center py-16 text-gray-500">
          개인정보 수정을 위해 비밀번호 확인이 필요합니다.
          <div className="mt-4">
            <button
              className="px-4 py-2 bg-blue-600 text-white rounded-md hover:bg-blue-700"
              onClick={() => setShowPasswordModal(true)}
            >
              비밀번호 확인
            </button>
          </div>
        </div>
      )}
    </div>
  );
}
