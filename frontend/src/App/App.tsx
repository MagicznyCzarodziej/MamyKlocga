import { Home } from '../pages/Home/Home';
import { Navigate, Route, Routes } from 'react-router-dom';
import React, { useContext, useEffect, useState } from 'react';
import { connect, socket } from '../socket/socket';
import { useHello } from '../api/useHello';
import { Room } from '../pages/Room/Room';
import { UserContext } from '../context/UserContext';
import { Rooms } from '../pages/Rooms/Rooms';

export const App = () => {
  const helloMutation = useHello();
  const [isSocketConnected, setIsSocketConnected] = useState(false);
  const user = useContext(UserContext);

  useEffect(() => {
    helloMutation.mutate();
  }, []);

  useEffect(() => {
    if (!helloMutation.isSuccess) {
      return;
    }

    console.log('Hello:', helloMutation.data);

    user.setUsername(helloMutation.data.username);
    user.setHasUserId(true);

    connect(helloMutation.data.userId);

    socket.on('connect', () => {
      console.log('Socket.io connected');
      setIsSocketConnected(true);
    });

    return () => {
      socket.off('connect');
      setIsSocketConnected(false);
    };
  }, [helloMutation.isSuccess]);

  const AuthenticatedRoute = ({ element }: { element: JSX.Element }) => {
    if (!isSocketConnected) {
      return <Loading />;
    }

    if (user.username !== null) {
      return element;
    }

    return <Navigate to="/" />;
  };

  return (
    <Routes>
      <Route path="/" element={<Home />} />
      <Route
        path="/rooms"
        element={
          <AuthenticatedRoute element={<Rooms />} />
        }
      />
      <Route
        path="/rooms/:roomCode"
        element={
          <AuthenticatedRoute element={<Room />} />
        }
      />
    </Routes>
  );
};

const Loading = () => {
  return <div>Ładowanie</div>;
};
