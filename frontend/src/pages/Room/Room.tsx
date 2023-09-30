import React, { useEffect } from 'react';
import { useGetRoom } from '../../api/useGetRoom';
import { useParams } from 'react-router-dom';
import { RoomLobby } from './RoomLobby/RoomLobby';
import { RoomInGame } from './RoomInGame';
import { useWatchRoom } from '../../api/useWatchRoom';
import { RoomScoring } from './RoomScoring';

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
      return <RoomScoring room={roomQuery.data} />
    }
  }

  return <div
    className={`text-2xl text-center mt-12`}
  >
    Ładowanie...
  </div>;
};
