import { createContext, FunctionComponent, ReactNode, useState } from 'react';

interface UserContextI {
  hasUserId: boolean;
  setHasUserId: (hasUserId: boolean) => void;
  username: string | null;
  setUsername: (username: string | null) => void;
}

const defaultValue: UserContextI = {
  hasUserId: false,
  setHasUserId: () => void 0,
  username: null,
  setUsername: () => void 0
};

export const UserContext = createContext<UserContextI>(defaultValue);

interface Props {
  children: ReactNode;
}

export const UserProvider: FunctionComponent<Props> = ({ children }) => {
  const [hasUserId, setHasUserId] = useState(Boolean(localStorage.getItem('hasUserId')));
  const [username, setUsername] = useState<string | null>(localStorage.getItem('username'));

  const saveUsername = (username: string | null) => {
    setUsername(username);
    if (username === null) {
      localStorage.removeItem('username');
    } else {
      localStorage.setItem('username', username);
    }
  };

  return <UserContext.Provider value={{
    hasUserId,
    setHasUserId,
    username,
    setUsername: saveUsername,
  }}>
    {children}
  </UserContext.Provider>;
};
