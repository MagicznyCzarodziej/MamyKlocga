import { createContext, FunctionComponent, ReactNode, useState } from 'react';

interface UserContextI {
  isLoggedIn: boolean;
  username: string | null;
  setUsername: (username: string | null) => void;
}

const defaultValue: UserContextI = {
  isLoggedIn: false,
  username: null,
  setUsername: () => void 0
};

export const UserContext = createContext<UserContextI>(defaultValue);

interface Props {
  children: ReactNode;
}

export const UserProvider: FunctionComponent<Props> = ({ children }) => {
  const [isLoggedIn, setIsLoggedIn] = useState(Boolean(localStorage.getItem('isLoggedIn')));
  const [username, setUsername] = useState<string | null>(localStorage.getItem('username') || null);

  const logIn = (username: string | null) => {
    console.log('login', username);
    setUsername(username);
    setIsLoggedIn(true);
    localStorage.setItem('isLoggedIn', 'true');
    if (username === null) {
      localStorage.removeItem('username');
    } else {
      localStorage.setItem('username', username);
    }
  };

  return <UserContext.Provider value={{
    isLoggedIn,
    username,
    setUsername: logIn,
  }}>
    {children}
  </UserContext.Provider>;
};
