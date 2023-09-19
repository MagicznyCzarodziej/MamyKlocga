import { Button } from '../../../components/Button/Button';
import { RoomResponse } from '../../../api/useGetRoom';
import { useStartGame } from '../../../api/useStartGame';
import { LobbyHeader } from './LobbyHeader';
import { UsersList } from './UsersList';

interface Props {
  room: RoomResponse;
}

export const RoomLobby = (props: Props) => {
  const { room } = props;

  const startGameMutation = useStartGame();

  return (
    <div className={`py-8`}>
      <LobbyHeader room={room} />
      <div className={`text-center text-xl p-6`}>Zaczekaj na rozpoczęcie gry</div>
      <UsersList room={room} />
      <div className={`mt-12`}>
      {room.isRoomOwner &&
          <div className={`px-12`}>
          <Button
              onClick={() => {
                startGameMutation.mutate(room.code);
              }}
          >
              Rozpocznij grę
          </Button>
          </div>
      }
      </div>
    </div>
  );
};
