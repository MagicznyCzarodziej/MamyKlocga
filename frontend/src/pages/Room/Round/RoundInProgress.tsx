import { RoomResponse } from '../../../api/useGetRoom';
import { Timer } from './Timer';
import { WordsList } from './WordsList';
import { Challenge } from './Challenge';
import { RoundHeader } from './RoundHeader';

interface Props {
  room: RoomResponse;
}

export const RoundInProgress = (props: Props) => {
  const { room } = props;
  const game = room.game!;
  const round = game.currentRound;

  const getRoleDependentContent = () => {
    if (round.role == 'BUILDER') return <>
      <WordsList words={game.words} />
    </>;

    return <div className={`text-center text-4xl mt-24`}>Zgadujesz!</div>;
  };

  return <div className={`flex flex-col h-full`}>
    <RoundHeader game={game} />
    <div className={`px-12 pb-6`}>
      <Timer endDate={round.endsAt!} />
      {round.role === 'BUILDER' &&
        <div className={`text-xl text-center mt-4`}>Zgaduje: {round.guesser.username}</div>}
      <Challenge challenge={round.challenge} />
      {getRoleDependentContent()}
    </div>
  </div>;
};
