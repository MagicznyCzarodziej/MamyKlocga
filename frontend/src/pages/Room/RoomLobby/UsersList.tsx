import { RoomResponse } from '../../../api/useGetRoom';

interface Props {
  room: RoomResponse;
}

export const UsersList = (props: Props) => {
  const { room } = props;

  return <div className={`text-center bg-gray-100 py-4`}>
    <div className={`text-2xl mb-2`}>Gracze:</div>
    <div className={`mx-4`}>
      {room.users.map((user) => (
        <div className={`text-xl py-1 font-light`} key={user.username}>{user.username}</div>
      ))}
    </div>
  </div>;
};
