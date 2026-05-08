import { useState } from "react";
import { QueryClientProvider } from "@tanstack/react-query";
import { queryClient } from "@/core/api/query-client";
import { ErrorBoundary } from "@/core/errors/error-boundary";
import { AppLayout } from "@/shared/layout/AppLayout";
import { EventosListPage } from "@/features/eventos/pages/eventos-list-page";
import { EventoDetailPage } from "@/features/eventos/pages/evento-detail-page";
import type { EventoListItem } from "@/features/eventos/api/eventos.api";

type Page = "list" | "detail";

export default function App() {
  const [currentPage, setCurrentPage] = useState<Page>("list");
  const [selectedEvento, setSelectedEvento] = useState<EventoListItem | null>(null);

  const handleSelectEvento = (evento: EventoListItem) => {
    setSelectedEvento(evento);
    setCurrentPage("detail");
  };

  const handleBackToList = () => {
    setCurrentPage("list");
    setSelectedEvento(null);
  };

  return (
    <QueryClientProvider client={queryClient}>
      <ErrorBoundary>
        <AppLayout>
          {currentPage === "list" && (
            <EventosListPage onSelectEvento={handleSelectEvento} />
          )}
          {currentPage === "detail" && selectedEvento && (
            <EventoDetailPage evento={selectedEvento} onBack={handleBackToList} />
          )}
        </AppLayout>
      </ErrorBoundary>
    </QueryClientProvider>
  );
}
