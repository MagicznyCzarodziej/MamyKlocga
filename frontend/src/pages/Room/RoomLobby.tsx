import { Button } from '../../components/Button/Button';
import { RoomResponse } from '../../api/useGetRoom';
import { useStartGame } from '../../api/useStartGame';
import { useEffect, useState } from 'react';

interface Props {
  room: RoomResponse;
}

export const RoomLobby = (props: Props) => {
  const { room } = props;

  const startGameMutation = useStartGame();

  const [users, setUsers] = useState(room.users)

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
