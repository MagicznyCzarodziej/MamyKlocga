import { RoomResponse } from '../../../api/useGetRoom';
import { Timer } from './Timer';
import { WordsList } from './WordsList';
import { Button } from '../../../components/Button/Button';
import { useStartRound } from '../../../api/useStartRound';
import { Challenge } from './Challenge';
import { RoundHeader } from './RoundHeader';

interface Props {
  room: RoomResponse;
}

export const RoundWaitingToStart = (props: Props) => {
  const { room } = props;
  const game = room.game!;
  const round = game.currentRound;

  const getRoleDependentContent = () => {

    return <div className={`mt-8`}>
      {round.role == 'BUILDER'
        ? <WordsList words={game.words} />
        : <div className={`text-center text-4xl mb-12`}>Zgadujesz!</div>}
      <div className={`mt-8`}>
        {room.isRoomOwner
          ? <StartRoundButton roomCode={room.code} />
          : <div className={`text-center text-xl`}>Zaczekaj na rozpoczęcie rundy</div>
        }
      </div>
    </div>;
  };

  return <div className={`flex flex-col h-full`}>
    <RoundHeader game={game} />
    <div className={`p-12`}>
      <Challenge challenge={round.challenge} />
      {getRoleDependentContent()}
    </div>
  </div>;
};

const StartRoundButton = ({ roomCode }: { roomCode: string }) => {
  const startRoundMutation = useStartRound();

  return <Button
    onClick={() => {
      startRoundMutation.mutate(roomCode);
    }}
  >
    Czas start!
  </Button>;
};