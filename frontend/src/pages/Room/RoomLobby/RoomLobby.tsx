import { Button } from '../../../components/Button/Button';
import { RoomResponse } from '../../../api/useGetRoom';
import { useStartGame } from '../../../api/useStartGame';
import { LobbyHeader } from './LobbyHeader';
import { UsersList } from './UsersList';
import { useContext, useEffect } from "react";
import { UsedWordsContext } from "../../../context/UsedWordsContext";

interface Props {
  room: RoomResponse;
}

export const RoomLobby = (props: Props) => {
  const { room } = props;

  const startGameMutation = useStartGame();
  const { resetUsedWords } = useContext(UsedWordsContext);

  useEffect(() => {
    resetUsedWords()
  }, []);

  return (
    <div className={`py-8`}>
      <LobbyHeader room={room} />
      <div className={`text-center text-xl p-6`}>Zaczekaj na rozpoczęcie gry</div>
      <UsersList room={room} />
      <div className={`mt-12`}>
        {room.isRoomOwner &&
          <div className={`px-8`}>
            <Button
              disabled={room.users.length < 2}
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
