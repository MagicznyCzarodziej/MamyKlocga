import React, { FunctionComponent, MouseEventHandler, ReactNode } from 'react';
import { clsx } from 'clsx';

interface Props {
  children: ReactNode;
  variant?: 'primary' | 'secondary' | 'text'
  disabled?: boolean;
  onClick: MouseEventHandler<HTMLDivElement>;
}

export const Button: FunctionComponent<Props> = (props: Props) => {
  const { children, variant = 'primary', disabled, onClick } = props;

  return <div
    onClick={(event) => {
      if (!disabled) onClick(event);
    }}
    className={clsx('text-2xl py-3 w-full text-center', {
      'bg-gray-200': variant == 'primary',
      'bg-gray-100': disabled,
      'text-gray-300': disabled,
      'font-extralight': variant != 'primary'
    })}
  >
    {children}
  </div>;
};
