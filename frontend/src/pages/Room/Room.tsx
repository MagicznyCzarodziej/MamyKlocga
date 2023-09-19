import { useEffect } from 'react';
import { useGetRoom } from '../../api/useGetRoom';
import { useParams } from 'react-router-dom';
import { RoomLobby } from './RoomLobby/RoomLobby';
import { RoomInGame } from './RoomInGame';
import { useWatchRoom } from '../../api/useWatchRoom';
import { RoomPunctuation } from './RoomPunctuation';

export const Room = () => {
  const { roomCode } = useParams();

  const roomQuery = useGetRoom(roomCode as string);
  useWatchRoom(roomCode as string)

  switch (roomQuery.data?.state) {
    case 'CREATED': {
      return <RoomLobby room={roomQuery.data} />;
    }

    case 'IN_GAME': {
      return <RoomInGame room={roomQuery.data} />;
    }

    case 'GAME_ENDED': {
      return <RoomPunctuation room={roomQuery.data} />
    }
  }

  return <div>Ładowanie</div>;
};
