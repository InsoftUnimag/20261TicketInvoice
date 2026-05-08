import { User } from "lucide-react";

export function AppHeader() {
  return (
    <header className="fixed top-0 left-0 right-0 h-16 bg-gradient-to-r from-purple-600 to-purple-700 shadow-lg z-50">
      <div className="h-full px-8 flex items-center justify-between">
        {/* Logo */}
        <div className="flex items-center gap-3">
          <div className="w-8 h-8 bg-purple-200 rounded" />
          <span className="text-white font-semibold text-lg tracking-wide">Ticket Seller</span>
        </div>

        {/* Búsqueda */}
        <div className="flex-1 max-w-md mx-8">
          <input
            type="text"
            placeholder="Buscar evento por ID o nombre..."
            className="w-full px-4 py-2 rounded-lg bg-purple-500/20 text-white placeholder-purple-200 focus:outline-none focus:ring-2 focus:ring-purple-300"
          />
        </div>

        {/* Usuario */}
        <div className="flex items-center gap-3">
          <div className="text-right text-sm">
            <p className="text-purple-100 text-xs">Administrador</p>
            <p className="text-white font-medium">Financiero</p>
          </div>
          <div className="w-10 h-10 rounded-full bg-purple-200 flex items-center justify-center">
            <User className="w-6 h-6 text-purple-700" />
          </div>
        </div>
      </div>
    </header>
  );
}
