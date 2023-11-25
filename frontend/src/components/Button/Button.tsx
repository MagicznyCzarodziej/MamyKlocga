import React, { FunctionComponent, MouseEventHandler, ReactNode } from 'react';

interface Props {
  children: ReactNode;
  light?: boolean;
  disabled?: boolean;
  onClick: MouseEventHandler<HTMLDivElement>;
}

export const Button: FunctionComponent<Props> = (props: Props) => {
  const { children, light, disabled, onClick } = props;

  return <div
    onClick={(event) => {
      if (!disabled) onClick(event);
    }}
    className={`text-2xl py-3 ${disabled ? 'bg-gray-100' : 'bg-gray-200' } w-full text-center ${disabled ? 'text-gray-300' : light ? 'font-extralight' : ''}`}
  >
    {children}
  </div>;
};
