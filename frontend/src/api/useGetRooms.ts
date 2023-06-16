import api from './api';
import { useQuery } from '@tanstack/react-query';

const getRooms = () => api.get<RoomsResponse>('/rooms');

export const useGetRooms = () => useQuery(['getRooms'], getRooms);

interface RoomsResponse {
  rooms: Room[];
}

export interface Room {
  code: string;
  name: string;
  usersCount: number;
}