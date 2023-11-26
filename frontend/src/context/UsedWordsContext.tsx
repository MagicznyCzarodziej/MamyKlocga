import { createContext, FunctionComponent, ReactNode, useEffect, useState } from 'react';

interface UsedWordsContextI {
  usedWords: string[];
  toggleWord: (word: string) => void;
  resetUsedWords: () => void;
}

const defaultValue: UsedWordsContextI = {
  usedWords: [],
  toggleWord: () => void 0,
  resetUsedWords: () => void 0,
};

export const UsedWordsContext = createContext<UsedWordsContextI>(defaultValue);

interface Props {
  children: ReactNode;
}

const getUsedWordsFromLocalStorage = (): string[] => {
  const json = localStorage.getItem('usedWords');
  if (json === null) return [];

  try {
    return JSON.parse(json) as string[];
  } catch (error) {
    return [];
  }
};

export const UsedWordsProvider: FunctionComponent<Props> = ({ children }) => {
  const [usedWords, setUsedWords] = useState(getUsedWordsFromLocalStorage());

  useEffect(() => {
    localStorage.setItem('usedWords', JSON.stringify(usedWords));
  }, [usedWords]);

  const resetUsedWords = () => {
    setUsedWords([])
  }

  const toggleWord = (word: string) => {
    const isWordUsed = usedWords.includes(word);

    if (isWordUsed) {
      setUsedWords((prevWords) => prevWords.filter(it => it !== word));
    } else {
      setUsedWords((prevWords) => [...prevWords, word]);
    }
  };

  return <UsedWordsContext.Provider value={{
    usedWords: usedWords,
    toggleWord: toggleWord,
    resetUsedWords: resetUsedWords,
  }}>
    {children}
  </UsedWordsContext.Provider>;
};