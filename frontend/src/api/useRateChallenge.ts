import api from './api';
import { useMutation } from '@tanstack/react-query';

const rateChallenge = ({ roomCode, userId, hasPassed }: {
  roomCode: string,
  userId: string,
  hasPassed: boolean
}) => {
  const rate = hasPassed ? 'yes' : 'no';
  return api.post<undefined, undefined>(`/rooms/${roomCode}/challenge/${userId}/${rate}`, undefined);
};

export const useRateChallenge = () => useMutation(rateChallenge);
