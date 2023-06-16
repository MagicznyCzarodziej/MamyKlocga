import { useEffect } from 'react';
import { socket } from '../../socket/socket';
import { useGetRoom } from '../../api/useGetRoom';
import { useParams } from 'react-router-dom';
import { RoomLobby } from './RoomLobby';
import { RoomInGame } from './RoomInGame';

export const Room = () => {
  const { roomCode } = useParams();

  const roomQuery = useGetRoom(roomCode as string);

  useEffect(() => {
    socket.on('GAME_STARTED', () => {
      console.log('Game started');
      roomQuery.refetch().then();
    });

    return () => {
      socket.off('GAME_STARTED');
    };
  }, []);

  switch (roomQuery.data?.state) {
    case 'CREATED': {
      return <RoomLobby room={roomQuery.data} />;
    }

    case 'IN_GAME': {
      return <RoomInGame room={roomQuery.data} />;
    }
  }

  return <div>Ładowanie</div>;
};
