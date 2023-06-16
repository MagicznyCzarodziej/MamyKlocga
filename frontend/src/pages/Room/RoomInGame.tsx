import { Button } from '../../components/Button/Button';
import { RoomResponse } from '../../api/useGetRoom';
import { useStartGame } from '../../api/useStartGame';

interface Props {
  room: RoomResponse;
}

export const RoomInGame = (props: Props) => {
  const { room } = props;

  return (
    <div>
      <div>pokój {room.name} {room.code}</div>
      <div>{room.game!.currentRound.challenge}</div>
      <div>{room.game!.words.join(', ')}</div>
    </div>
  );
};
