import { RoomResponse } from '../../../../api/useGetRoom';
import { useRateGuess } from '../../../../api/useRateGuess';
import { Button } from '../../../../components/Button/Button';
import { useContext } from 'react';
import { UserContext } from '../../../../context/UserContext';

interface Props {
  room: RoomResponse;
}

export const RateGuess = (props: Props) => {
  const { room } = props;

  const me = useContext(UserContext)

  const round = room.game!.currentRound!;

  const rateGuessMutation = useRateGuess();

  const rateGuess = (ratedUserId: string | null, hasGuessedCorrectly: boolean) => rateGuessMutation.mutate({
    roomCode: room.code,
    ratedUserId: ratedUserId,
    hasGuessedCorrectly: hasGuessedCorrectly,
  });

  if (!round.hasRatedGuesserGuess) {
    return <div>
      <div>Czy zgadujący trafił?</div>
      <div className={`flex gap-2`}>
        <div
          className={`bg-gray-200`}
          onClick={() => {
            rateGuess(round.guesser.userId, true);
          }}
        >
          TAK
        </div>
        <div onClick={() => {
          rateGuess(round.guesser.userId, false);
        }}
        >
          NIE
        </div>
      </div>
    </div>;
  }

  if (!round.hasRatedStolenGuess) {
    return <div>
      <div>Czy inny gracz zgadł?</div>
      <div className={`flex gap-2`}>
        {round.users
          .filter(user => user.userId !== me.userId)
          .filter(user => user.role === 'BUILDER')
          .map((user) => <div
          key={user.userId}>
          <div>{user.username}</div>
          <div
            className={`bg-gray-200`}
            onClick={() => {
              rateGuess(user.userId, true);
            }}
          >
            TAK
          </div>
        </div>)}
        <Button onClick={() => {
          rateGuess(null, false);
        }}>Nikt nie zgadł</Button>
      </div>
    </div>;
  }

  return null;
};
