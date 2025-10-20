"use client";

import { useState, useEffect } from "react";

export default function ResumePage() {
  const [isLoading, setIsLoading] = useState(true);

  const [resumeData, setResumeData] = useState({
    content: "",
    techStack: "",
    portfolioUrl: "",
  });

  const [activities, setActivities] = useState<any[]>([]);
  const [certifications, setCertifications] = useState<any[]>([]);
  const [experiences, setExperiences] = useState<any[]>([]);

  // ✅ 임시 유저 가정
  useEffect(() => {
    const timer = setTimeout(() => setIsLoading(false), 200);
    return () => clearTimeout(timer);
  }, []);

  // ✅ 통합 handle 함수 (백엔드 연동 대비)
  const handleAdd = (type: string) => {
    const id = Date.now();
    const newItem =
      type === "activity"
        ? { id, date: "", name: "", description: "" }
        : type === "certification"
        ? { id, date: "", name: "" }
        : { id, company: "", position: "", startDate: "", endDate: "" };

    switch (type) {
      case "activity":
        setActivities((prev) => [...prev, newItem]);
        break;
      case "certification":
        setCertifications((prev) => [...prev, newItem]);
        break;
      case "experience":
        setExperiences((prev) => [...prev, newItem]);
        break;
    }

    // 나중에 fetch(`/api/${type}`, { method: "POST", body: JSON.stringify(newItem) })
  };

  const handleUpdate = (type: string, id: number, field: string, value: string) => {
    const updateList = (list: any[]) => list.map((item) => (item.id === id ? { ...item, [field]: value } : item));

    switch (type) {
      case "activity":
        setActivities(updateList(activities));
        break;
      case "certification":
        setCertifications(updateList(certifications));
        break;
      case "experience":
        setExperiences(updateList(experiences));
        break;
    }

    // 나중에 fetch(`/api/${type}/${id}`, { method: "PUT", body: JSON.stringify({ [field]: value }) })
  };

  const handleRemove = (type: string, id: number) => {
    const filterList = (list: any[]) => list.filter((item) => item.id !== id);

    switch (type) {
      case "activity":
        setActivities(filterList(activities));
        break;
      case "certification":
        setCertifications(filterList(certifications));
        break;
      case "experience":
        setExperiences(filterList(experiences));
        break;
    }

    // 나중에 fetch(`/api/${type}/${id}`, { method: "DELETE" })
  };
  if (isLoading) {
    return (
      <div className="flex items-center justify-center min-h-[60vh] text-gray-500">
        로딩 중...
      </div>
    );
  }

  return (
    <>
    <div className="bg-white p-8 rounded-lg shadow-md border border-gray-200">
      <h2 className="text-2xl font-bold mb-2 flex items-center gap-2">💼 이력서 관리</h2>
      <p className="text-gray-500 mb-6">이력서 정보를 등록하고 관리하세요.</p>

      {/* 자기소개 */}
      <div className="mb-6">
        <label className="block font-semibold mb-1">이력서 내용</label>
        <textarea
          className="w-full border border-gray-300 rounded-md p-3 focus:outline-blue-500"
          placeholder="자기소개 및 경력 요약을 작성하세요"
          rows={6}
          value={resumeData.content}
          onChange={(e) => setResumeData({ ...resumeData, content: e.target.value })}
        />
      </div>

      {/* 기술 스택 */}
      <div className="mb-6">
        <label className="block font-semibold mb-1">기술 스택</label>
        <textarea
          className="w-full border border-gray-300 rounded-md p-3 focus:outline-blue-500"
          placeholder="예: React, Next.js, TypeScript, Node.js, PostgreSQL"
          rows={3}
          value={resumeData.techStack}
          onChange={(e) => setResumeData({ ...resumeData, techStack: e.target.value })}
        />
      </div>

      {/* 대외 활동 */}
      <Section
        title="대외 활동"
        type="activity"
        list={activities}
        handleAdd={handleAdd}
        handleUpdate={handleUpdate}
        handleRemove={handleRemove}
      />

      {/* 자격증 */}
      <Section
        title="자격증"
        type="certification"
        list={certifications}
        handleAdd={handleAdd}
        handleUpdate={handleUpdate}
        handleRemove={handleRemove}
      />

      {/* 경력 사항 */}
      <Section
        title="경력 사항"
        type="experience"
        list={experiences}
        handleAdd={handleAdd}
        handleUpdate={handleUpdate}
        handleRemove={handleRemove}
      />

      {/* 포트폴리오 URL */}
      <div className="mb-6">
        <label className="block font-semibold mb-1">포트폴리오 URL</label>
        <div className="flex items-center gap-2">
          <span className="text-xl">💻</span>
          <input
            type="text"
            className="flex-1 border border-gray-300 rounded-md p-2"
            placeholder="https://github.com/username 또는 포트폴리오 사이트"
            value={resumeData.portfolioUrl}
            onChange={(e) => setResumeData({ ...resumeData, portfolioUrl: e.target.value })}
          />
        </div>
      </div>

      <button className="w-full bg-blue-600 text-white py-2 rounded-md hover:bg-blue-700 font-semibold">
        저장
      </button>
    </div>
    </>
  );
}
function Section({
    title,
    type,
    list,
    handleAdd,
    handleUpdate,
    handleRemove,
  }: {
    title: string;
    type: string;
    list: any[];
    handleAdd: Function;
    handleUpdate: Function;
    handleRemove: Function;
  }) {
    const fieldMap: Record<string, any> = {
      activity: [
        { label: "날짜", field: "date", type: "date" },
        { label: "활동명 (대회 이름, 수상 내역)", field: "name", type: "text", placeholder: "예: 해커톤 대회 1등" },
        { label: "활동 정보", field: "description", type: "textarea", placeholder: "활동 내용이나 수상 내역을 작성하세요" },
      ],
      certification: [
        { label: "취득 날짜", field: "date", type: "date" },
        { label: "자격증명", field: "name", type: "text", placeholder: "예: 정보처리기사" },
      ],
      experience: [
        { label: "회사명", field: "company", type: "text", placeholder: "예: ABC 주식회사" },
        { label: "직무", field: "position", type: "text", placeholder: "예: 프론트엔드 개발자" },
        { label: "재직 시작일", field: "startDate", type: "date" },
        { label: "재직 종료일", field: "endDate", type: "date" },
      ],
    };
  
    const fields = fieldMap[type];
  
    return (
      <div className="mb-8">
        <div className="flex justify-between items-center mb-3">
          <label className="font-semibold">{title}</label>
          <button
            onClick={() => handleAdd(type)}
            className="px-3 py-1 text-sm bg-gray-100 rounded-md hover:bg-gray-200"
          >
            추가
          </button>
        </div>
  
        {list.length === 0 ? (
          <p className="text-sm text-gray-500">{title}을(를) 추가해주세요</p>
        ) : (
          <div className="space-y-4">
            {list.map((item) => (
              <div
                key={item.id}
                className="border border-gray-200 rounded-lg p-4 bg-gray-50 space-y-3"
              >
                <div className="flex justify-end">
                  <button
                    onClick={() => handleRemove(type, item.id)}
                    className="px-3 py-1 text-sm bg-gray-100 rounded-md hover:bg-gray-200"
                  >
                    삭제
                  </button>
                </div>
  
                <div className="grid md:grid-cols-2 gap-3">
                  {fields.map((f: any, index: number) => (
                    <div key={index}>
                      <label className="block text-sm font-medium mb-1">{f.label}</label>
                      {f.type === "textarea" ? (
                        <textarea
                          rows={2}
                          className="w-full border border-gray-300 rounded-md p-2"
                          placeholder={f.placeholder}
                          value={item[f.field] || ""}
                          onChange={(e) => handleUpdate(type, item.id, f.field, e.target.value)}
                        />
                      ) : (
                        <input
                          type={f.type}
                          className="w-full border border-gray-300 rounded-md p-2"
                          placeholder={f.placeholder}
                          value={item[f.field] || ""}
                          onChange={(e) => handleUpdate(type, item.id, f.field, e.target.value)}
                        />
                      )}
                    </div>
                  ))}
                </div>
              </div>
            ))}
          </div>
        )}
      </div>
    );
  }