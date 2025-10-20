"use client";

import { useState } from "react";

const mockUsers = [
  {
    id: "u1",
    name: "김개발",
    email: "kim@example.com",
    problemsSolved: 75,
    scores: 850,
    status: "active",
    joinedAt: "2025-09-01",
    isPremium: false,
  },
  {
    id: "u2",
    name: "박알고",
    email: "park@example.com",
    problemsSolved: 185,
    scores: 2150,
    status: "active",
    joinedAt: "2025-08-15",
    isPremium: true,
  },
  {
    id: "u3",
    name: "이프론트",
    email: "lee@example.com",
    problemsSolved: 165,
    scores: 1980,
    status: "active",
    joinedAt: "2025-08-20",
    isPremium: true,
  },
  {
    id: "u4",
    name: "최백엔드",
    email: "choi@example.com",
    problemsSolved: 142,
    scores: 1720,
    status: "suspended",
    joinedAt: "2025-07-10",
    isPremium: false,
  },
  {
    id: "u5",
    name: "정풀스택",
    email: "jung@example.com",
    problemsSolved: 128,
    scores: 1580,
    status: "active",
    joinedAt: "2025-09-05",
    isPremium: false,
  },
];

export default function AdminUsersPage() {
  const [users, setUsers] = useState(mockUsers);
  const [searchQuery, setSearchQuery] = useState("");

  const filteredUsers = users.filter(
    (user) =>
      user.name.toLowerCase().includes(searchQuery.toLowerCase()) ||
      user.email.toLowerCase().includes(searchQuery.toLowerCase())
  );

  const updateStatus = (id: string, status: string) => {
    setUsers(users.map((u) => (u.id === id ? { ...u, status } : u)));
    alert(
      status === "active"
        ? "사용자가 다시 활성화되었습니다."
        : status === "suspended"
        ? "사용자의 활동이 정지되었습니다."
        : "사용자가 영구 차단되었습니다."
    );
  };

  const getStatusBadge = (status: string) => {
    if (status === "active")
      return (
        <span className="px-2 py-1 text-sm rounded bg-green-100 text-green-700">
          활성
        </span>
      );
    if (status === "suspended")
      return (
        <span className="px-2 py-1 text-sm rounded bg-yellow-100 text-yellow-700">
          정지
        </span>
      );
    if (status === "banned")
      return (
        <span className="px-2 py-1 text-sm rounded bg-red-100 text-red-700">
          차단
        </span>
      );
  };

  return (
    <div className="max-w-7xl mx-auto p-8 space-y-6">
      <div>
        <h1 className="text-3xl font-bold mb-2">👥 사용자 관리</h1>
        <p className="text-gray-500">
          플랫폼 사용자를 관리하고 활동을 모니터링합니다
        </p>
      </div>

      {/* 검색창 쓸지 논의 필요 */}
      <div className="relative w-full max-w-md">
        <input
          type="text"
          placeholder="이름 또는 이메일로 검색"
          value={searchQuery}
          onChange={(e) => setSearchQuery(e.target.value)}
          className="w-full border border-gray-300 rounded-md px-4 py-2 focus:outline-none focus:border-blue-500"
        />
      </div>

      {/* 사용자 테이블 */}
      <div className="overflow-x-auto bg-white shadow rounded-lg border border-gray-200">
        <table className="w-full text-center">
          <thead className="bg-gray-50 border-b">
            <tr>
              <th className="px-6 py-3 text-sm font-semibold text-gray-600">
                사용자
              </th>
              <th className="px-6 py-3 text-sm font-semibold text-gray-600">
                문제 수
              </th>
              <th className="px-6 py-3 text-sm font-semibold text-gray-600">
                점수
              </th>
              <th className="px-6 py-3 text-sm font-semibold text-gray-600">
                상태
              </th>
              <th className="px-6 py-3 text-sm font-semibold text-gray-600">
                가입일
              </th>
              <th className="px-6 py-3 text-sm font-semibold text-gray-600">
                작업
              </th>
            </tr>
          </thead>
          <tbody>
            {filteredUsers.map((user) => (
              <tr key={user.id} className="hover:bg-gray-100">
                <td className="px-6 py-4 flex items-center gap-3">
                  <div className="w-10 h-10 flex items-center justify-center rounded-full bg-gray-200 font-bold text-gray-700">
                    {user.name[0]}
                  </div>
                  <div>
                    <div className="flex items-center gap-2">
                      <p className="font-medium text-left">{user.name}</p>
                      {user.isPremium && (
                        <span className="text-xs text-blue-600 bg-blue-50 border border-blue-200 rounded px-2 py-0.5">
                          프리미엄
                        </span>
                      )}
                    </div>
                    <p className="text-sm text-gray-500">{user.email}</p>
                  </div>
                </td>
                <td className="px-6 py-4">{user.problemsSolved}</td>
                <td className="px-6 py-4">{user.scores}</td>
                <td className="px-6 py-4">{getStatusBadge(user.status)}</td>
                <td className="px-6 py-4 text-gray-600">{user.joinedAt}</td>
                <td className="px-6 py-4 text-right">
                  {user.status === "active" ? (
                    <div className="flex gap-2 justify-end">
                      <button
                        onClick={() => updateStatus(user.id, "suspended")}
                        className="px-3 py-1 text-sm rounded-md bg-yellow-50 text-yellow-700 hover:bg-yellow-100"
                      >
                        정지
                      </button>
                      <button
                        onClick={() => updateStatus(user.id, "banned")}
                        className="px-3 py-1 text-sm rounded-md bg-red-50 text-red-700 hover:bg-red-100"
                      >
                        차단
                      </button>
                    </div>
                  ) : (
                    <button
                      onClick={() => updateStatus(user.id, "active")}
                      className="px-3 py-1 text-sm rounded-md bg-green-550 text-green-700 hover:bg-green-100"
                    >
                      활성화
                    </button>
                  )}
                </td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>
    </div>
  );
}
