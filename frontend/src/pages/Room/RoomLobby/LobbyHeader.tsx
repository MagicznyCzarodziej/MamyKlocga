import { RoomResponse } from '../../../api/useGetRoom';
import { ShareOutlined, TagOutlined } from '@mui/icons-material';
import { useShareRoom } from '../../../hooks/useShareRoom';

interface Props {
  room: RoomResponse;
}

export const LobbyHeader = (props: Props) => {
  const { room } = props;

  const { share, copied } = useShareRoom()

  return <div className={`bg-gray-100 flex justify-between p-4`}>
    <div className={`text-xl`}><TagOutlined className={`align-middle`} /><span
      className={`align-middle`}>{room.code}</span></div>
    <div
      className={`text-right`}
      onClick={() => {
        share(window.document.location.href)
      }}
    >
      {copied ? "Skopiowano!" : <>Zaproś <ShareOutlined /></>}
    </div>
  </div>;
};
