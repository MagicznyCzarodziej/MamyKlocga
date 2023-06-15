import api from './api';
import { useMutation } from '@tanstack/react-query';

const setUsername = (username: string) => api.post<SetUsernameResponse, SetUsernameRequest>('/me/username', { username });

export const useSetUsername = () => useMutation(setUsername);

interface SetUsernameRequest {
  username: string,
}

interface SetUsernameResponse {
  username: string,
}