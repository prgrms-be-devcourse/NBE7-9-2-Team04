"use client"

import React, { useState, useRef, useEffect } from "react"

interface DropdownMenuProps {
  children: React.ReactNode
}

export function DropdownMenu({ children }: DropdownMenuProps) {
  return <div className="relative inline-block text-left">{children}</div>
}

export function DropdownMenuTrigger({
  children,
}: {
  children: React.ReactNode
}) {
  return <>{children}</>
}

export function DropdownMenuContent({
  children,
  open,
  align = "end",
}: {
  children: React.ReactNode
  open: boolean
  align?: "start" | "end"
}) {
  if (!open) return null
  return (
    <div
      className={`absolute mt-2 w-40 bg-white border border-gray-200 rounded-md shadow-lg z-50 ${
        align === "end" ? "right-0" : "left-0"
      }`}
    >
      {children}
    </div>
  )
}

export function DropdownMenuItem({
  children,
  onClick,
  className = "",
}: {
  children: React.ReactNode
  onClick?: () => void
  className?: string
}) {
  return (
    <button
      onClick={onClick}
      className={`block w-full text-left px-4 py-2 text-sm hover:bg-gray-100 ${className}`}
    >
      {children}
    </button>
  )
}

// ✅ 드롭다운 메뉴 토글 훅
export function useDropdown() {
  const [open, setOpen] = useState(false)
  const ref = useRef<HTMLDivElement>(null)

  useEffect(() => {
    const handleClickOutside = (e: MouseEvent) => {
      if (ref.current && !ref.current.contains(e.target as Node)) {
        setOpen(false)
      }
    }
    document.addEventListener("mousedown", handleClickOutside)
    return () => document.removeEventListener("mousedown", handleClickOutside)
  }, [])

  return { ref, open, setOpen }
}
