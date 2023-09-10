import { RoomResponse } from '../../api/useGetRoom';
import { useGetPoints } from '../../api/useGetPoints';

interface Props {
  room: RoomResponse;
}

export const RoomPunctuation = (props: Props) => {
  const { room } = props;
  const punctuation = useGetPoints(room.code)

  return (
    <div>
      <div>pokój {room.name} {room.code}</div>
      <div>Punktacja:
        {punctuation.isSuccess && punctuation.data.pointsPerUser.map((userPoints) => <div key={userPoints.username}>{userPoints.username} {userPoints.points}</div>)}
      </div>
    </div>
  );
};
