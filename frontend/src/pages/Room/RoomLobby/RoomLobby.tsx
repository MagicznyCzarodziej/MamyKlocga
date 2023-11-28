import { Button } from '../../../components/Button/Button';
import { RoomResponse } from '../../../api/useGetRoom';
import { useStartGame } from '../../../api/useStartGame';
import { LobbyHeader } from './LobbyHeader';
import { UsersList } from './UsersList';
import { useContext, useEffect } from "react";
import { UsedWordsContext } from "../../../context/UsedWordsContext";
import { useShareRoom } from '../../../hooks/useShareRoom';

interface Props {
  room: RoomResponse;
}

export const RoomLobby = (props: Props) => {
  const { room } = props;

  const startGameMutation = useStartGame();
  const { resetUsedWords } = useContext(UsedWordsContext);
  const { share, copied } = useShareRoom()

  useEffect(() => {
    resetUsedWords()
  }, []);

  return (
    <div>
      <LobbyHeader room={room} />
      {room.isRoomOwner
        ? <div className={`h-6`} />
        : <div className={`text-center text-xl p-6`}>Zaczekaj na rozpoczęcie gry</div>
      }
      <UsersList room={room} />
      <div className={`mt-12`}>
        {room.isRoomOwner &&
          <div className={`px-8`}>
            {room.users.length < 2
              ? <Button
                onClick={() => {
                  share(window.document.location.href)
                }}
              >
                {copied ? 'Skopiowano!' : 'Zaproś graczy'}
              </Button>
              : <Button
                onClick={() => {
                  startGameMutation.mutate(room.code);
                }}
              >
                Rozpocznij grę
              </Button>
            }
          </div>
        }
      </div>
    </div>
  );
};
