import { Home } from '../pages/Home/Home';
import { Route, Routes } from 'react-router-dom';
import { Providers } from './Providers';

function App() {

  return (
    <Providers>
      <Routes>
        <Route path="/" element={<Home />} />
        <Route path="/test" element={<div>Testowanko</div>} />
      </Routes>
    </Providers>
  );
}

export default App;
