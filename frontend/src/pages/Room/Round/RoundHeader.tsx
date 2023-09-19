import { Game } from '../../../api/useGetRoom';

interface Props {
  game: Game;
}

export const RoundHeader = (props: Props) => {
  const { game } = props;

  const round = game.currentRound;

  return <div className={`flex justify-between text-2xl`}>
    <div>Runda: {round.roundNumber} / {game.roundsTotal}</div>
    <div>Punkty: {game.myPoints}</div>
  </div>;
};
