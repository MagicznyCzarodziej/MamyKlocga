import api from './api';
import { useMutation } from '@tanstack/react-query';

const startGame = (roomCode: string) => api.post<undefined, undefined>(`/rooms/${roomCode}/start`, undefined);

export const useStartGame = () => useMutation(startGame);