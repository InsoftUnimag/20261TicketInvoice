import { type ReactNode } from "react";
import { AppHeader } from "./app-header";

interface AppLayoutProps {
  children: ReactNode;
}

export function AppLayout({ children }: AppLayoutProps) {
  return (
    <div className="min-h-screen bg-[#e9e7f0]">
      <AppHeader />
      <main className="pt-28">
        {children}
      </main>
    </div>
  );
}
