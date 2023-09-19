import api from './api';
import { useMutation } from '@tanstack/react-query';

const endGame = (roomCode: string) => api.post<undefined, undefined>(`/rooms/${roomCode}/end-game`, undefined);

export const useEndGame = () => useMutation(endGame);