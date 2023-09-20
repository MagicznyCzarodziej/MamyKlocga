import { useNextRound } from '../../../../api/useNextRound';
import { Button } from '../../../../components/Button/Button';

export const NextRoundButton = ({ roomCode }: { roomCode: string }) => {
  const nextRoundMutation = useNextRound();

  return <Button onClick={() => {
    nextRoundMutation.mutate(roomCode);
  }}>
    Następna runda
  </Button>;
};
