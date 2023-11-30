import { Home } from '../pages/Home/Home';
import {
  createBrowserRouter,
  createRoutesFromElements,
  Navigate,
  Route,
  RouterProvider,
} from 'react-router-dom';
import React, { useContext, useEffect } from 'react';
import { useHello } from '../api/useHello';
import { Room } from '../pages/Room/Room';
import { UserContext } from '../context/UserContext';
import { Rooms } from '../pages/Rooms/Rooms';

export const App = () => {
  const helloMutation = useHello();
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
    user.setUserId(helloMutation.data.userId);
  }, [helloMutation.isSuccess]);

  const AuthenticatedRoute = ({ element }: { element: JSX.Element }) => {
    if (user.username !== null) {
      return element;
    }

    return <Navigate to="/" />;
  };

  const router = createBrowserRouter(createRoutesFromElements(
    <>
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
    </>
  ))

  return (
    <RouterProvider router={router} />
  );
};
