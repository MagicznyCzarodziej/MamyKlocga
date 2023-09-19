import { FunctionComponent, ReactNode } from 'react';
import { QueryClient, QueryClientProvider } from '@tanstack/react-query';
import { BrowserRouter } from 'react-router-dom';
import { UserProvider } from '../context/UserContext';
import { UsedWordsProvider } from '../context/UsedWordsContext';

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
        <UsedWordsProvider>
          <BrowserRouter>{children}</BrowserRouter>
        </UsedWordsProvider>
      </UserProvider>
    </QueryClientProvider>
  );
};
