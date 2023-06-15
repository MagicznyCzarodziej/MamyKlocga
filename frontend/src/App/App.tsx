import { Home } from '../pages/Home/Home';
import { Navigate, Route, Routes } from 'react-router-dom';
import { useContext, useEffect, useState } from 'react';
import { connect, socket } from '../socket/socket';
import { useHello } from '../api/useHello';
import { Room } from '../pages/Room/Room';
import { UserContext } from '../context/UserContext';

export const App = () => {
  const helloMutation = useHello();
  const [isConnected, setIsConnected] = useState(false);
  const user = useContext(UserContext);

  useEffect(() => {
    helloMutation.mutate();
  }, []);

  useEffect(() => {
    if (!helloMutation.isSuccess) {
      return;
    }

    user.setUsername(helloMutation.data.username)
    connect(helloMutation.data.userId);

    socket.on('connect', () => {
      console.log('Connected');
    });

    setIsConnected(true);

    return () => {
      socket.off('connect');
      setIsConnected(false);
    };
  }, [helloMutation]);

  const AuthenticatedRoute = ({ element }: { element: JSX.Element }) => {
    if (!isConnected) {
      return <div>Ładowanie</div>
    }

    if (user.username !== null) {
      return element
    }

    return <Navigate to="/" />
  };

  return (
    <Routes>
      <Route path="/" element={<Home />} />
      <Route path="/rooms" element={<div>Lista</div>} />
      <Route path="/rooms/:roomId"
             element={<AuthenticatedRoute element={<Room />} />} />
    </Routes>
  );
};

