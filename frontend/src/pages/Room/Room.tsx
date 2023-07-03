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

    socket.on('ROUND_STARTED', (data) => {
      console.log('Round started', data);
      roomQuery.refetch().then();
    });

    socket.on('ROUND_ENDED', (data) => {
      console.log('Round ended', data);
      roomQuery.refetch().then();
    });

    return () => {
      socket.off('GAME_STARTED');
      socket.off('ROUND_ENDED');
      socket.off('ROUND_STARTED');
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
