import { Calculator, Building2, LayoutDashboard, DollarSign } from "lucide-react";
import { cn } from "@/shared/lib/utils";

const navItems = [
  { label: "Dashboard", href: "/", icon: LayoutDashboard },
  { label: "Eventos", href: "/eventos", icon: Calculator },
  { label: "Recintos", href: "/recintos", icon: Building2 },
  { label: "Liquidación", href: "/liquidacion", icon: DollarSign },
] as const;

export function Sidebar() {
  return (
    <aside className="fixed left-0 top-0 z-40 h-screen w-64 border-r bg-background">
      <div className="flex h-14 items-center border-b px-6 font-semibold">
        Liquidación y Dispersión
      </div>
      <nav className="flex flex-col gap-1 p-4">
        {navItems.map((item) => (
          <a
            key={item.href}
            href={item.href}
            className={cn(
              "flex items-center gap-3 rounded-lg px-3 py-2 text-sm font-medium transition-colors",
              "hover:bg-secondary hover:text-secondary-foreground",
              "text-muted-foreground",
            )}
          >
            <item.icon className="h-4 w-4" />
            {item.label}
          </a>
        ))}
      </nav>
    </aside>
  );
}
