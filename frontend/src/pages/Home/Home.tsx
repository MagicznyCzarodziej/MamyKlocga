import { Button } from '../../components/Button/Button';
import { useMutation, useQuery } from '@tanstack/react-query';
import { useContext, useEffect, useState } from 'react';
import { connect, socket } from '../../socket/socket';
import api from '../../api/api';
import { HelloResponse } from '../../types/HelloResponse';
import { useNavigate } from 'react-router-dom';
import { useSetUsername } from '../../api/useSetUsername';
import { UserContext } from '../../context/UserContext';

const test = () => api.get<string>('/test');

export const Home = () => {

  const setUsernameMutation = useSetUsername();

  const navigate = useNavigate();

  const user = useContext(UserContext);
  const [username, setUsername] = useState(user.username);

  return (
    <div className="grid justify-stretch text-center m-12">
      <div className="text-4xl">MamyKloc.ga</div>
      <div className="my-12">
        <input placeholder="Imię"
               className="border-b border-b-black text-center w-full text-2xl py-2"
               value={username ?? ''}
               onChange={(event) => {
                 setUsername(event.target.value);
               }} />
      </div>
      <Button onClick={() => {
        if (username == null) return;
        setUsernameMutation.mutate(username, {
          onSuccess: () => {
            user.setUsername(username);
            navigate('/rooms/1');
          }
        });

      }}>Dołącz do gry</Button>
      <div className="my-3 text-xl font-light text-gray-500">lub</div>
      <Button light onClick={() => {
      }}>Stwórz grę</Button>
    </div>);
};
