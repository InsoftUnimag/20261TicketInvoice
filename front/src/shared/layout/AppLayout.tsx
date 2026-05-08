import { type ReactNode } from "react";
import { AppHeader } from "./app-header";

interface AppLayoutProps {
  children: ReactNode;
}

export function AppLayout({ children }: AppLayoutProps) {
  return (
    <div className="min-h-screen bg-gray-50">
      <AppHeader />
      <main className="pt-20">
        {children}
      </main>
    </div>
  );
}
