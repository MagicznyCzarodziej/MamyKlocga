import api from './api';
import { HelloResponse } from '../types/HelloResponse';
import { useMutation } from '@tanstack/react-query';

const hello = () => api.post<HelloResponse, undefined>('/me/hello', undefined);

export const useHello = () => useMutation(hello);