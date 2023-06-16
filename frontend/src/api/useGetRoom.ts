import api from './api';
import { useQuery } from '@tanstack/react-query';

const getRoom = (roomCode: string) => () => api.get<RoomResponse>(`/rooms/${roomCode}`);

export const useGetRoom = (roomCode: string) => useQuery(['getRooms'], getRoom(roomCode));

export interface RoomResponse {
  code: string;
  name: string;
  users: User[];
  state: 'CREATED' | 'IN_GAME' | 'GAME_ENDED' | 'ABORTED';
  game: Game | null;
}

interface User {
  username: string;
}

interface Game {
  roundsTotal: number;
  currentRound: Round;
  myPoints: number;
  words: string[];
}

interface Round {
  roundNumber: number;
  role: 'GUESSER' | 'BUILDER';
  guesser: User;
  challenge: string;
  endsAt: string | null;
}