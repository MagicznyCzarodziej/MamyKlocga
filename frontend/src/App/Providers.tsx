import { FunctionComponent, ReactNode } from 'react';
import { QueryClient, QueryClientProvider } from '@tanstack/react-query';
import { BrowserRouter } from 'react-router-dom';
import { UserProvider } from '../context/UserContext';

interface Props {
  children: ReactNode;
}

const queryClient = new QueryClient({
  defaultOptions: {
    queries: {
      refetchOnWindowFocus: false,

    }
  }
});

export const Providers: FunctionComponent<Props> = ({ children }) => {
  return (
    <QueryClientProvider client={queryClient}>
      <UserProvider>
        <BrowserRouter>{children}</BrowserRouter>
      </UserProvider>
    </QueryClientProvider>
  );
};
