import React from 'react';
import ReactDOM from 'react-dom/client';
import './index.css';
import { Providers } from './App/Providers';
import { App } from './App/App';

const root = ReactDOM.createRoot(document.getElementById('root') as HTMLElement);

root.render(
  <React.StrictMode>
    <Providers>
      <App />
    </Providers>
  </React.StrictMode>,
);
