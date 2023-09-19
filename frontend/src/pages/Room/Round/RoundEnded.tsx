import { RoomResponse } from '../../../api/useGetRoom';
import { Button } from '../../../components/Button/Button';
import { useRateGuess } from '../../../api/useRateGuess';
import { useNextRound } from '../../../api/useNextRound';
import { useEndGame } from '../../../api/useEndGame';

interface Props {
  room: RoomResponse;
}

export const RoundEnded = (props: Props) => {
  const { room } = props;

  const game = room.game!;
  const round = game.currentRound;

  const rateGuessMutation = useRateGuess();

  const getRoleDependentContent = () => {
    if (round.role == 'BUILDER') return <div>
      {round.hasRatedGuesserGuess ? <>Czy inny gracz trafił? ...</> : <>Czy zgadujący trafił?<br />
        <span onClick={() => {
          rateGuessMutation.mutate({ roomCode: room.code, hasGuessedCorrectly: true });
        }}>TAK</span>
        /
        <span onClick={() => {
          rateGuessMutation.mutate({ roomCode: room.code, hasGuessedCorrectly: false });
        }}>NIE</span></>}
    </div>;

    return <div>
      {round.users
        .filter((user) => user.role === 'BUILDER')
        .map(user => <div key={user.username}>
          {user.hasPassedChallenge ? 'WYZWANIE ZALICZONE' : 'ZALICZ WYZWANIE'} {user.username}
        </div>)}
    </div>;
  };

  return <div>
    <div>Pokój {room.name} {room.code}</div>
    <div>Runda: {round.roundNumber} / {game.roundsTotal}</div>
    <div>Punkty: {game.myPoints}</div>
    <div>Koniec czasu!</div>
    <div>Wyzwanie: <br />{round.challenge}</div>
    {getRoleDependentContent()}
    {
      room.isRoomOwner
      && game.currentRound.roundNumber < game.roundsTotal
      && game.currentRound.hasEveryoneRated
      && <NextRoundButton roomCode={room.code} />
    }
    {
      room.isRoomOwner
      && game.currentRound.roundNumber >= game.roundsTotal
      && game.currentRound.hasEveryoneRated
      && <EndGameButton roomCode={room.code} />
    }
  </div>;
};

const NextRoundButton = ({ roomCode }: { roomCode: string }) => {
  const nextRoundMutation = useNextRound();

  return <Button onClick={() => {
    nextRoundMutation.mutate(roomCode);
  }}>Następna runda</Button>;
};

const EndGameButton = ({ roomCode }: { roomCode: string }) => {
  const endGameMutation = useEndGame();

  return <Button onClick={() => {
    endGameMutation.mutate(roomCode);
  }}>Podsumowanie</Button>;
};