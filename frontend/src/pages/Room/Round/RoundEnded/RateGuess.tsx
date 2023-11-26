import { RoomResponse } from '../../../../api/useGetRoom';
import { useRateGuess } from '../../../../api/useRateGuess';
import { useContext } from 'react';
import { UserContext } from '../../../../context/UserContext';

interface Props {
  room: RoomResponse;
}

export const RateGuess = (props: Props) => {
  const { room } = props;

  const me = useContext(UserContext);

  const game = room.game!
  const round = game.currentRound!;

  const rateGuessMutation = useRateGuess();

  const rateGuess = (ratedUserId: string | null, hasGuessedCorrectly: boolean) => rateGuessMutation.mutate({
    roomCode: room.code,
    ratedUserId: ratedUserId,
    hasGuessedCorrectly: hasGuessedCorrectly,
  });

  if (!round.hasRatedGuesserGuess) {
    return <div>
      <div
        className={`text-2xl text-center mt-4 mb-4 pt-12 border-t-2`}
      >
        Czy zgadujący trafił?
      </div>
      <div className={`flex gap-4`}>
        <div
          className={`bg-green-400 text-white text-2xl py-3 w-full text-center`}
          onClick={() => {
            rateGuess(round.guesser.userId, true);
          }}
        >
          TAK
        </div>
        <div
          className={`bg-red-400 text-white text-2xl py-3 w-full text-center`}
          onClick={() => {
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
      <div
        className={`text-2xl text-center mt-4 mb-4 pt-12 border-t-2`}
      >
        Czy inny gracz zgadł?
      </div>
      <div>
        {round.users
          .filter(user => user.userId !== me.userId)
          .filter(user => user.role === 'BUILDER')
          .map((user) => <div
            key={user.userId}
            className={`w-full grid grid-cols-2 items-center`}
          >
            <div
              className={`text-xl`}
            >
              {user.username}
            </div>
            <div
              className={`bg-green-400 text-white text-2xl py-3 w-full text-center`}
              onClick={() => {
                rateGuess(user.userId, true);
              }}
            >
              TAK
            </div>
          </div>)}
        <div
          className={`bg-red-400 text-white text-2xl py-3 mt-4 w-full text-center`}
          onClick={() => {
            rateGuess(null, false);
          }}
        >
          Nikt nie zgadł
        </div>
      </div>
    </div>;
  }

  if (game.currentRound.roundNumber < game.roundsTotal) {
    return <div
      className={`mt-auto text-2xl py-3 w-full text-center`}
    >
      Zaczekaj na następną rundę
    </div>;
  }

  return null
};
