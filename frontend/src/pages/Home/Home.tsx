import { Button } from '../../components/Button/Button';
import { useMutation, useQuery } from '@tanstack/react-query';
import { useEffect } from 'react';
import { connect, socket } from '../../socket/socket';
import api from '../../api/api';
import { HelloResponse } from '../../types/HelloResponse';

const hello = () => api.post<HelloResponse, undefined>('/users/hello', undefined);
const test = () => api.get<string>("/test")

export const Home = () => {
  const testQuery = useQuery({ queryKey: ["test"], queryFn: test })
  const helloMutation = useMutation(hello);

  useEffect(() => {
    helloMutation.mutate();
  }, []);

  useEffect(() => {
    if (!helloMutation.isSuccess) return;
    connect(helloMutation.data.userId);

    socket.on('connect', () => {
      console.log('Connected');
    });

    socket.on('GAME_STARTED', () => {
      console.log('game started');
    });

    return () => {
      socket.off('connect');
      socket.off('GAME_STARTED');
    };
  }, [helloMutation]);


  return (
    <div className="grid justify-stretch text-center m-12">
      <div>{testQuery.data ?? ":("}</div>
      <div className="text-4xl">MamyKloc.ga</div>
      <div className="my-12">
        <input placeholder="Imię" className="border-b border-b-black text-center w-full text-2xl py-2" />
      </div>
      <Button onClick={() => {}}>Dołącz do gry</Button>
      <div className="my-3 text-xl font-light text-gray-500">lub</div>
      <Button light onClick={() => {}}>Stwórz grę</Button>
    </div>);
}
