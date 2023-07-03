import { RoomResponse } from '../../../api/useGetRoom';
import { Button } from '../../../components/Button/Button';

interface Props {
  room: RoomResponse;
}

export const RoundEnded = (props: Props) => {
  const { room } = props;

  const game = room.game!;
  const round = game.currentRound;

  const getRoleDependentContent = () => {
    if (round.role == 'BUILDER') return <>
      <div>Czy zgadujący trafił?<br /> TAK / NIE</div>
    </>;

    return <div>
      {room.users.map(user => <div key={user.username}>
        ZALICZ WYZWANIE {user.username} ZGADŁEŚ/NIE ZGADŁEŚ
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
    {room.isRoomOwner && <NextRoundButton />}
  </div>;
};

const NextRoundButton = () => {
  return <Button onClick={() => {

  }}>Następna runda</Button>;
};