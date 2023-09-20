import api from './api';
import { useMutation } from '@tanstack/react-query';

const rateGuess = ({ roomCode, ratedUserId, hasGuessedCorrectly }: {
  roomCode: string,
  ratedUserId: string | null,
  hasGuessedCorrectly: boolean
}) => {
  return api.post<undefined, Request>(`/rooms/${roomCode}/guess`, {
    ratedUserId: ratedUserId,
    hasGuessedCorrectly: hasGuessedCorrectly,
  });
};

interface Request {
  ratedUserId: string | null,
  hasGuessedCorrectly: boolean,
}

export const useRateGuess = () => useMutation(rateGuess);
