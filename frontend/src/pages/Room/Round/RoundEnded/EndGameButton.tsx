import { useEndGame } from '../../../../api/useEndGame';
import { Button } from '../../../../components/Button/Button';

export const EndGameButton = ({ roomCode }: { roomCode: string }) => {
  const endGameMutation = useEndGame();

  return <Button onClick={() => {
    endGameMutation.mutate(roomCode);
  }}>
    Podsumowanie
  </Button>;
};
