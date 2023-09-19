import React, { FunctionComponent, MouseEventHandler, ReactNode } from 'react';

interface Props {
  children: ReactNode;
  light?: boolean;
  onClick: MouseEventHandler<HTMLDivElement>;
}

export const Button: FunctionComponent<Props> = (props: Props) => {
    const {children, light, onClick} = props

    return <div
      onClick={onClick}
      className={`text-2xl py-3 bg-gray-200 w-full text-center ${light ? "font-extralight" : ""}`}
    >
      {children}
    </div>
}
