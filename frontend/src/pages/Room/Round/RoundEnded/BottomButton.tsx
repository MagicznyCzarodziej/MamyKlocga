import { RoomResponse } from '../../../../api/useGetRoom';
import { NextRoundButton } from './NextRoundButton';
import { EndGameButton } from './EndGameButton';

interface Props {
  room: RoomResponse;
}

export const BottomButton = (props: Props) => {
  const { room } = props;
  const game = room.game!;

  if (!room.isRoomOwner) return null;
  if (!game.currentRound.hasEveryoneRated) return null;

  if (game.currentRound.roundNumber < game.roundsTotal) {
    return <NextRoundButton roomCode={room.code} />;
  }

  return <EndGameButton roomCode={room.code} />;
};
