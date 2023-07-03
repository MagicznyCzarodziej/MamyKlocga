import { Button } from '../../components/Button/Button';
import { RoomResponse } from '../../api/useGetRoom';
import { useStartGame } from '../../api/useStartGame';
import { useEffect, useState } from 'react';
import { socket } from '../../socket/socket';
import { useSetUsername } from '../../api/useSetUsername';

interface Props {
  room: RoomResponse;
}

export const RoomLobby = (props: Props) => {
  const { room } = props;

  const startGameMutation = useStartGame();

  const [users, setUsers] = useState(room.users)

  useEffect(() => {
    socket.on('USER_JOINED_ROOM', (data) => {
      console.log('User joined');
      setUsers(
        users => [...users, {username: data.username}]
      )
    });

    socket.on('USER_LEFT_ROOM', (data) => {
      console.log('User left');
      setUsers(
        users => users.filter(user => user.username !== data.username)
      )
    });

    return () => {
      socket.off('USER_JOINED_ROOM');
      socket.off('USER_LEFT_ROOM');
    };
  }, []);

  return (
    <div>
      <div>pokój {room.name} {room.code}</div>
      <div>Zaczekaj na rozpoczęcie gry</div>
      <div>Users:
        {users.map((user) => <div key={user.username}>{user.username}</div>)}
      </div>
      {room.isRoomOwner &&
          <Button
              onClick={() => {
                startGameMutation.mutate(room.code);
              }}
          >
              Rozpocznij grę
          </Button>
      }
    </div>
  );
};
