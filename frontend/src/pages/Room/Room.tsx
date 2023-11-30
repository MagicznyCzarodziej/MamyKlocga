import { useGetRoom } from '../../api/useGetRoom';
import { useParams, unstable_usePrompt } from 'react-router-dom';
import { RoomLobby } from './RoomLobby/RoomLobby';
import { RoomInGame } from './RoomInGame';
import { useWatchRoom } from '../../api/useWatchRoom';
import { RoomScoring } from './RoomScoring';
import { useEffect } from 'react';

export const Room = () => {
  const { roomCode } = useParams();

  const roomQuery = useGetRoom(roomCode as string);
  useWatchRoom(roomCode as string)

  unstable_usePrompt({
    message: "Na pewno chcesz opuścić pokój?",
    when: ({ currentLocation, nextLocation }) =>
      currentLocation.pathname !== nextLocation.pathname,
  });

  useEffect(() => {
    if (navigator.wakeLock){
      navigator.wakeLock.request("screen").then()
    }
  }, []);

  switch (roomQuery.data?.state) {
    case 'CREATED': {
      return <RoomLobby room={roomQuery.data} />;
    }

    case 'IN_GAME': {
      return <RoomInGame room={roomQuery.data} />;
    }

    case 'GAME_ENDED': {
      return <RoomScoring room={roomQuery.data} />
    }
  }

  return <div
    className={`text-2xl text-center mt-12`}
  >
    Ładowanie...
  </div>;
};
