import { RoomResponse } from '../../../api/useGetRoom';
import { Timer } from './Timer';
import { WordsList } from './WordsList';
import { Button } from '../../../components/Button/Button';
import { useStartRound } from '../../../api/useStartRound';

interface Props {
  room: RoomResponse;
}

export const RoundWaitingToStart = (props: Props) => {
  const { room } = props;
  const game = room.game!;
  const round = game.currentRound;

  const getRoleDependentContent = () => {
    if (round.role == 'BUILDER') return <WordsList words={game.words} />;

    return <>
      <div>Zgadujesz!</div>
      {room.isRoomOwner && <StartRoundButton roomCode={room.code} />}
    </>;
  };

  return <div>
    <div>Pokój {room.name} {room.code}</div>
    <div>Runda: {round.roundNumber} / {game.roundsTotal}</div>
    <div>Punkty: {game.myPoints}</div>
    <div>Wyzwanie: <br />{round.challenge}</div>
    {getRoleDependentContent()}
  </div>;
};

const StartRoundButton = ({ roomCode }: { roomCode: string }) => {
  const startRoundMutation = useStartRound();

  return <Button
    onClick={() => {
      startRoundMutation.mutate(roomCode);
    }}
  >
    Start round
  </Button>;
};