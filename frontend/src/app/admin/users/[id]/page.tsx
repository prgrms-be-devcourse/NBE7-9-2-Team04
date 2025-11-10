"use client";

import { useEffect, useState } from "react";
import { useParams, useRouter } from "next/navigation";
import { fetchApi } from "@/lib/client";
import {
  AdminUserResponse,
  AdminUserStatusUpdateRequest,
  AccountStatus,
  ACCOUNT_STATUS_LABELS,
} from "@/types/user";

export default function AdminUserDetailPage() {
  const { id } = useParams();
  const router = useRouter();

  const [user, setUser] = useState<AdminUserResponse | null>(null);
  const [isLoading, setIsLoading] = useState(true);
  const [isProcessing, setIsProcessing] = useState(false);

  /** ✅ 상태 뱃지 (내부 정의) */
  const getStatusBadge = (status: AccountStatus) => {
    const base = "px-2 py-1 text-sm rounded font-medium";
    switch (status) {
      case "ACTIVE":
        return <span className={`${base} bg-green-100 text-green-700`}>활성</span>;
      case "SUSPENDED":
        return <span className={`${base} bg-yellow-100 text-yellow-700`}>일시정지</span>;
      case "BANNED":
        return <span className={`${base} bg-red-100 text-red-700`}>영구정지</span>;
      default:
        return <span className={`${base} bg-gray-100 text-gray-700`}>알 수 없음</span>;
    }
  };

  /** ✅ 사용자 정보 조회 */
  const fetchUser = async () => {
    try {
      const res = await fetchApi(`/api/v1/admin/users/${id}`, {
        method: "GET",
      });

      if (res.status === "OK") {
        setUser(res.data);
      } else {
        alert(res.message || "사용자 정보를 불러오지 못했습니다.");
      }
    } catch (e) {
      console.error(e);
      alert("서버 오류가 발생했습니다.");
    } finally {
      setIsLoading(false);
    }
  };

  useEffect(() => {
    if (id) fetchUser();
  }, [id]);

  /** ✅ 상태 변경 */
  const handleStatusChange = async (newStatus: AccountStatus) => {
    if (!user) return;
    if (!confirm(`정말로 ${ACCOUNT_STATUS_LABELS[newStatus]} 상태로 변경하시겠습니까?`)) return;

    setIsProcessing(true);
    try {
      const body: AdminUserStatusUpdateRequest = { status: newStatus };
      const res = await fetchApi(`/api/v1/admin/users/${user.id}/status`, {
        method: "PATCH",
        body: JSON.stringify(body),
      });

      if (res.status === "OK") {
        alert(`사용자 상태가 "${ACCOUNT_STATUS_LABELS[newStatus]}"로 변경되었습니다.`);
        // 최신 데이터 갱신
        await fetchUser();
      } else {
        alert(res.message || "상태 변경 실패");
      }
    } catch (e) {
      console.error(e);
      alert("서버 오류가 발생했습니다.");
    } finally {
      setIsProcessing(false);
    }
  };

  /** ✅ 로딩 중 */
  if (isLoading)
    return (
      <div className="flex justify-center items-center py-20 text-gray-500">
        로딩 중...
      </div>
    );

  /** ✅ 사용자 없음 */
  if (!user)
    return (
      <div className="text-center py-20 text-red-600">
        사용자를 찾을 수 없습니다.
      </div>
    );

  return (
    <div className="max-w-3xl mx-auto p-8 space-y-8 bg-white border border-gray-200 shadow-sm rounded-lg">
      {/* 헤더 */}
      <div className="flex justify-between items-center">
        <h1 className="text-2xl font-bold">👤 사용자 상세 정보</h1>
        <button
          onClick={() => router.push("/admin")}
          className="px-3 py-2 border border-gray-300 rounded text-sm hover:bg-gray-100 transition"
        >
          ← 목록으로
        </button>
      </div>

      {/* 사용자 프로필 */}
      <div className="flex items-center gap-4">
        <div className="w-16 h-16 flex items-center justify-center rounded-full bg-gray-200 text-2xl font-semibold text-gray-600">
          {user.nickname?.[0] || "?"}
        </div>
        <div>
          <p className="text-lg font-semibold">{user.name}</p>
          <p className="text-gray-500">{user.email}</p>
        </div>
      </div>

      {/* 정보 테이블 */}
      <div className="space-y-3 text-sm">
        <p>
          <span className="font-semibold w-32 inline-block">닉네임:</span>
          {user.nickname}
        </p>
        <p>
          <span className="font-semibold w-32 inline-block">나이:</span>
          {user.age}세
        </p>
        <p>
          <span className="font-semibold w-32 inline-block">GitHub:</span>
          {user.github ? (
            <a
              href={user.github}
              target="_blank"
              className="text-blue-600 hover:underline"
            >
              {user.github}
            </a>
          ) : (
            <span className="text-gray-400">없음</span>
          )}
        </p>
        <p>
          <span className="font-semibold w-32 inline-block">역할:</span>
          {user.role === "ADMIN" ? "관리자" : "일반 사용자"}
        </p>
        <p>
          <span className="font-semibold w-32 inline-block">계정 상태:</span>
          {getStatusBadge(user.accountStatus)}
        </p>
      </div>

      {/* 상태 변경 버튼 */}
      <div className="flex gap-3 pt-4 border-t">
        {user.accountStatus === "ACTIVE" ? (
          <>
            <button
              disabled={isProcessing}
              onClick={() => handleStatusChange("SUSPENDED")}
              className={`px-4 py-2 rounded border border-yellow-200 text-yellow-700 hover:bg-yellow-50 ${
                isProcessing ? "opacity-60 cursor-not-allowed" : ""
              }`}
            >
              일시정지
            </button>
            <button
              disabled={isProcessing}
              onClick={() => handleStatusChange("BANNED")}
              className={`px-4 py-2 rounded border border-red-200 bg-red-100 text-red-700 hover:bg-red-200 ${
                isProcessing ? "opacity-60 cursor-not-allowed" : ""
              }`}
            >
              영구정지
            </button>
          </>
        ) : (
          <button
            disabled={isProcessing}
            onClick={() => handleStatusChange("ACTIVE")}
            className={`px-4 py-2 rounded bg-green-600 text-white hover:bg-green-700 ${
              isProcessing ? "opacity-60 cursor-not-allowed" : ""
            }`}
          >
            활성화
          </button>
        )}
      </div>
    </div>
  );
}
