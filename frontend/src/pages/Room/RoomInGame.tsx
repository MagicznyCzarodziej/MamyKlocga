import { Button } from '../../components/Button/Button';
import { RoomResponse } from '../../api/useGetRoom';
import { useStartRound } from '../../api/useStartRound';
import { RoundInProgress } from './Round/RoundInProgress';
import { RoundWaitingToStart } from './Round/RoundWaitingToStart';
import { RoundEnded } from './Round/RoundEnded';

interface Props {
  room: RoomResponse;
}

export const RoomInGame = (props: Props) => {
  const { room } = props;

  if (room.game?.currentRound.state === 'WAITING_TO_START') {
    return <RoundWaitingToStart room={room} />
  }

  if (room.game?.currentRound.state === 'ENDED') {
    return <RoundEnded room={room} />;
  }
  // if (room.game?.currentRound.state === 'IN_PROGRESS') {
  if (true) {
    return <RoundInProgress room={room} />;
  }

  return (
    <div>
      <div>pokój {room.name} {room.code}</div>
      <div>{room.game!.currentRound.challenge}</div>
      <div>{room.game!.words.join(', ')}</div>
      <div>{room.game?.currentRound.state}</div>

    </div>
  );
};
