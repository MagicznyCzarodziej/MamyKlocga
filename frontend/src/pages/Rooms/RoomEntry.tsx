import { Room } from '../../api/useGetRooms';
import { useJoinRoom } from '../../api/useJoinRoom';
import { useNavigate } from 'react-router-dom';

interface Props {
  room: Room;
}

export const RoomEntry = (props: Props) => {
  const { room } = props;

  const joinRoomMutation = useJoinRoom();
  const navigate = useNavigate();

  return <div
    className={`text-2xl py-3 bg-gray-200 w-full`}
    onClick={() => {
      joinRoomMutation.mutate(room.code, {
        onSuccess: () => {
          navigate(`/rooms/${room.code}`);
        }
      });
    }}
  >
    {room.name} #{room.code}
  </div>;
};