import { io, Socket } from 'socket.io-client';

const url = ':48207';

let socket: Socket;

export const connect = (userId: string) => {
  socket = io(url, {
    query: {
      userId
    }
  });
};

export { socket };