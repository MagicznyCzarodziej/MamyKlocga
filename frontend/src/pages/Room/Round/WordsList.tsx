import {
  CheckBoxOutlineBlankOutlined,
  CheckBoxOutlined,
  Visibility,
  VisibilityOffOutlined
} from '@mui/icons-material';
import { useContext, useState } from 'react';
import { UsedWordsContext } from '../../../context/UsedWordsContext';
import { clsx } from 'clsx';

interface Props {
  words: string[];
}

export const WordsList = (props: Props) => {
  const { words } = props;

  const { usedWords, toggleWord } = useContext(UsedWordsContext);
  const [showWords, setShowWords] = useState(true);

  return <>
    <div className={`flex items-center`}>
      <div
        className={`text-center w-full text-xl underline underline-offset-8 text-gray-500 mb-8`}
        onClick={() => {
          setShowWords(prev => !prev);
        }}
      >
        {showWords
          ? null
          : <div>
            <div>Pokaż słowa</div>
            <VisibilityOffOutlined className={`text-gray-200 mt-24`} sx={{ fontSize: 150 }} />
          </div>
        }
      </div>

    </div>
    {showWords && words.map((word) => {
      const isWordUsed = usedWords.includes(word);

      return (
        <div
          key={word}
          className={`flex items-center text-xl py-1 cursor-pointer`}
          onClick={() => {
            toggleWord(word);
          }}
        >
          {isWordUsed
            ? <CheckBoxOutlined />
            : <CheckBoxOutlineBlankOutlined />
          }
          <span className={clsx({ 'line-through': isWordUsed }, ['ml-3'])}>
            {word}
          </span>
        </div>
      );
    })
    }
  </>;
};