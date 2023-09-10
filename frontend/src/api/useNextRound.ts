import api from './api';
import { useMutation } from '@tanstack/react-query';

const nextRound = (roomCode: string) => api.post<undefined, undefined>(`/rooms/${roomCode}/nextRound`, undefined);

export const useNextRound = () => useMutation(nextRound);