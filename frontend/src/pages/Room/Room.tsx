import { useEffect } from 'react';
import { socket } from '../../socket/socket';

export const Room = () => {
    useEffect(() => {
      console.log(socket);
      socket.on('GAME_STARTED', () => {
        console.log('game started');
      });

      return () => {
        socket.off('GAME_STARTED');
      };
    }, [])

    return <div>pokój</div>
}
