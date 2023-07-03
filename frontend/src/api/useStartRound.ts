import api from './api';
import { useMutation } from '@tanstack/react-query';

const startRound = (roomCode: string) => api.post<undefined, undefined>(`/rooms/${roomCode}/startRound`, undefined);

export const useStartRound = () => useMutation(startRound);