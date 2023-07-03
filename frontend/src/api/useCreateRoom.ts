import api from './api';
import { useMutation } from '@tanstack/react-query';

const createRoom = (roomName: string) => api.post<CreateRoomResponse, CreateRoomRequest>('/rooms', { roomName });

export const userCreateRoom = () => useMutation(createRoom);

interface CreateRoomRequest {
  roomName: string;
}

interface CreateRoomResponse {
  code: string;
  name: string;
}