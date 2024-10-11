import { QueryClient } from '@tanstack/react-query';

const queryClient = new QueryClient({
  defaultOptions: {
    queries: {
      staleTime: 1_000 * 60 * 10,
      gcTime: 1_000 * 60 * 10,
    },
  },
});

export default queryClient;
