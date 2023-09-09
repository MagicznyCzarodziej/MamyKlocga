import api from './api';
import { useMutation } from '@tanstack/react-query';

const rateGuess = ({ roomCode, hasGuessedCorrectly }: {
  roomCode: string,
  hasGuessedCorrectly: boolean
}) => {
  const rate = hasGuessedCorrectly ? 'yes' : 'no';
  return api.post<undefined, undefined>(`/rooms/${roomCode}/rate-guess/${rate}`, undefined);
};

export const useRateGuess = () => useMutation(rateGuess);
