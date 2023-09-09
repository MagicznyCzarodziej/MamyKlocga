import { Button } from '../../components/Button/Button';
import { RoomResponse } from '../../api/useGetRoom';
import { useStartGame } from '../../api/useStartGame';

interface Props {
  room: RoomResponse;
}

export const RoomLobby = (props: Props) => {
  const { room } = props;

  const startGameMutation = useStartGame();

  return (
    <div>
      <div>pokój {room.name} {room.code}</div>
      <div>Zaczekaj na rozpoczęcie gry</div>
      <div>Users:
        {room.users.map((user) => <div key={user.username}>{user.username}</div>)}
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
