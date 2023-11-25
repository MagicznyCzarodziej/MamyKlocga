import { Button } from '../../components/Button/Button';
import { useContext, useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { useSetUsername } from '../../api/useSetUsername';
import { UserContext } from '../../context/UserContext';
import { userCreateRoom } from '../../api/useCreateRoom';

export const Home = () => {

  const setUsernameMutation = useSetUsername();
  const createRoomMutation = userCreateRoom();

  const navigate = useNavigate();

  const user = useContext(UserContext);
  const [username, setUsername] = useState(user.username);

  useEffect(() => {
    setUsername(user.username)
  }, [user.username])

  return (
    <div className="grid justify-stretch text-center p-12">
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
          onSuccess: (data) => {
            user.setUsername(data.username);
            navigate('/rooms');
          }
        });

      }}>Dołącz do gry</Button>
      <div className="my-3 text-xl font-light text-gray-500">lub</div>
      <Button light onClick={() => {
        if (username == null) return;
        setUsernameMutation.mutate(username, {
          onSuccess: (data) => {
            user.setUsername(data.username);
            createRoomMutation.mutate(data.username.toUpperCase(), {
              onSuccess: (room) => {
                navigate(`/rooms/${room.code}`)
              }
            })
          }})

      }}>Stwórz grę</Button>
    </div>);
};
