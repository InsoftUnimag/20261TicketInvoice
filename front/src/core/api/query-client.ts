import { QueryClient } from "@tanstack/react-query";
import { ApiError } from "@/core/errors/ApiError";

export const queryClient = new QueryClient({
  defaultOptions: {
    queries: {
      retry: (failureCount, error) => {
        if (error instanceof ApiError && error.status >= 400 && error.status < 500) {
          return false;
        }
        return failureCount < 3;
      },
      refetchOnWindowFocus: false,
      staleTime: 30_000,
      gcTime: 5 * 60_000,
    },
    mutations: {
      onError: (error) => {
        if (error instanceof ApiError) {
          console.error(`[Mutation Error] ${error.code}: ${error.message}`);
        }
      },
    },
  },
});
