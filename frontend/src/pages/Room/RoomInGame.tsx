import { RoomResponse } from '../../api/useGetRoom';
import { RoundInProgress } from './Round/RoundInProgress';
import { RoundWaitingToStart } from './Round/RoundWaitingToStart';
import { RoundEnded } from './Round/RoundEnded/RoundEnded';

interface Props {
  room: RoomResponse;
}

export const RoomInGame = (props: Props) => {
  const { room } = props;

  if (room.game?.currentRound.state === 'WAITING_TO_START') {
    return <RoundWaitingToStart room={room}/>
  }

  if (room.game?.currentRound.state === 'ENDED') {
    return <RoundEnded room={room}/>;
  }

  return <RoundInProgress room={room}/>;
};
