import { RoomResponse } from '../../../api/useGetRoom';
import { useEffect, useState } from 'react';

interface Props {
  room: RoomResponse;
}

export const LobbyHeader = (props: Props) => {
  const { room } = props;

  const [copied, setCopied] = useState(false);

  useEffect(() => {
    if (copied)
      window.setTimeout(() => {
        setCopied(false);
      }, 3000);
  }, [copied]);

  return <div className={`bg-gray-100 flex justify-between mx-8 p-4`}>
    <div>#{room.code}</div>
    <div
      className={`text-right`}
      onClick={() => {
        const link = window.document.location.href;
        const shareData = {
          title: "Dołącz do pokoju w MamyKlocga",
          url: link
        }

        if (!navigator.share || !navigator.canShare(shareData)) {
          window.navigator.clipboard.writeText(link).then(() => {
            setCopied(true);
          });
          return;
        }

        navigator.share(shareData).then() // Only over HTTPS
      }}
    >
      {copied ? "Skopiowano link do pokoju!" : "Udostępnij link do pokoju"}
    </div>
  </div>;
};
