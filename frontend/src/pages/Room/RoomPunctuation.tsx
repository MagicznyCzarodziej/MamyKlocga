import { RoomResponse } from '../../api/useGetRoom';
import { useGetPoints } from '../../api/useGetPoints';
import { clsx } from 'clsx';

interface Props {
  room: RoomResponse;
}

export const RoomPunctuation = (props: Props) => {
  const { room } = props;
  const punctuation = useGetPoints(room.code);

  return <div className={`p-12`}>
    <div className={`text-4xl`}>Punktacja:</div>
    <div className={`mt-8`}>
      {
        punctuation.isSuccess && punctuation.data.pointsPerUser
          .sort((a, b) => a.points - b.points)
          .map((userPoints, index) =>
            <div
              className={`text-2xl py-1 font-light flex justify-between`}
              key={userPoints.username}
            >
              <div className={clsx({ 'text-3xl': index === 0 })}>{userPoints.username}</div>
              <div>{userPoints.points}</div>
            </div>
          )
      }
    </div>
  </div>;

};
