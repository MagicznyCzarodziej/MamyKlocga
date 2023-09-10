import api from './api';
import { useQuery } from '@tanstack/react-query';
import { RoomResponse } from './useGetRoom';

const getPoints = (roomCode: string) => () => api.get<PointsResponse>(`/rooms/${roomCode}/points`);

export const useGetPoints = (roomCode: string) => useQuery(['getPoints', roomCode], getPoints(roomCode));

export interface PointsResponse {
  pointsPerUser: UserPoints[];
}

interface UserPoints {
  userId: string;
  username: string;
  points: number;
}