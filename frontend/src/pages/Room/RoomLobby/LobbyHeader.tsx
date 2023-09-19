import { RoomResponse } from '../../../api/useGetRoom';

interface Props {
  room: RoomResponse;
}

export const LobbyHeader = (props: Props) => {
  const { room } = props;

  return <div className={`bg-gray-100 flex justify-between mx-8 p-4`}>
    <div>#{room.code}</div>
    <div className={`text-right`}>Udostępnij link do pokoju</div>
  </div>;
};
