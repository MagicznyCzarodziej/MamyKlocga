import { RoomResponse } from '../../../api/useGetRoom';
import { Timer } from './Timer';
import { WordsList } from './WordsList';

interface Props {
  room: RoomResponse;
}

export const RoundInProgress = (props: Props) => {
  const { room } = props;
  const game = room.game!;
  const round = game.currentRound;

  const getRoleDependentContent = () => {
    if (round.role == 'BUILDER') return <WordsList words={game.words} />;

    return <>
      <div>Zgadujesz!</div>
    </>;
  };

  return <div>
    <div>Pokój {room.name} {room.code}</div>
    <div>Runda: {round.roundNumber} / {game.roundsTotal}</div>
    <div>Punkty: {game.myPoints}</div>
    <div>Zgaduje: {round.guesser.username}</div>
    <Timer endDate={round.endsAt!} />
    <div>Wyzwanie: <br />{round.challenge}</div>
    {getRoleDependentContent()}
  </div>;
};
