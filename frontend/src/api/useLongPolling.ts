import { useEffect } from 'react';
import { QueryKey, useQueryClient } from '@tanstack/react-query';
import { GenericAbortSignal } from 'axios';

export const useLongPolling = <T>(queryKey: QueryKey, subscribeFn: (signal: GenericAbortSignal) => Promise<T>) => {
  const queryClient = useQueryClient();

  useEffect(() => {
    let isMounted = true;
    const abortController = new AbortController();
    const signal = abortController.signal;

    const subscribe = async () => {
      let data;

      try {
        data = await subscribeFn(signal);
      } finally {
        if (isMounted) {
          subscribe();
        }
      }

      queryClient.setQueryData(queryKey, data);
    };

    subscribe();

    return () => {
      isMounted = false;
      abortController.abort('Component unmounted');
    };
  }, [queryClient, queryKey, subscribeFn]);
};