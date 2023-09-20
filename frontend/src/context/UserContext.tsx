import { createContext, FunctionComponent, ReactNode, useState } from 'react';

interface UserContextI {
  userId: string | null;
  setUserId: (userId: string) => void;
  username: string | null;
  setUsername: (username: string | null) => void;
}

const defaultValue: UserContextI = {
  userId: null,
  setUserId: () => void 0,
  username: null,
  setUsername: () => void 0
};

export const UserContext = createContext<UserContextI>(defaultValue);

interface Props {
  children: ReactNode;
}

export const UserProvider: FunctionComponent<Props> = ({ children }) => {
  const [userId, setUserId] = useState(localStorage.getItem('userId'));
  const [username, setUsername] = useState<string | null>(localStorage.getItem('username'));

  const saveUsername = (username: string | null) => {
    setUsername(username);
    if (username === null) {
      localStorage.removeItem('username');
    } else {
      localStorage.setItem('username', username);
    }
  };

  const saveUserId = (userId: string | null) => {
    setUserId(userId);
    if (username === null) {
      localStorage.removeItem('userId');
    } else {
      localStorage.setItem('userId', userId!!);
    }
  };

  return <UserContext.Provider value={{
    userId,
    setUserId: saveUserId,
    username,
    setUsername: saveUsername,
  }}>
    {children}
  </UserContext.Provider>;
};
