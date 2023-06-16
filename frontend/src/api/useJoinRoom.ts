import api from './api';
import { useMutation } from '@tanstack/react-query';

const joinRoom = (roomCode: string) => api.post<undefined, undefined>(`/rooms/${roomCode}/join`, undefined);

export const useJoinRoom = () => useMutation(joinRoom);
