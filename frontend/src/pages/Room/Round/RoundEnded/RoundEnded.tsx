import { RoomResponse } from '../../../../api/useGetRoom';
import { RoundHeader } from '../RoundHeader';
import { Challenge } from '../Challenge';
import { BottomButton } from './BottomButton';
import { UsersChallenge } from './UsersChallenge';
import { RateGuess } from './RateGuess';

interface Props {
  room: RoomResponse;
}

export const RoundEnded = (props: Props) => {
  const { room } = props;

  const game = room.game!;
  const round = game.currentRound;

  const getRoleDependentContent = () => {
    if (round.role == 'BUILDER') return <RateGuess room={room} />

    return <UsersChallenge round={round} roomCode={room.code} />;
  };

  return <div className={`p-12 flex flex-col h-full`}>
    <RoundHeader game={game} />
    <div className={`text-center text-2xl mt-8`}>Koniec rundy!</div>
    <Challenge challenge={round.challenge} />
    {getRoleDependentContent()}
    <div className={`mt-auto`}><BottomButton room={room} /></div>
  </div>;
};

