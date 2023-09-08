import api from './api';
import { useLongPolling } from './useLongPolling';
import { GenericAbortSignal } from 'axios';
import { RoomResponse } from './useGetRoom';

const watchRoom = (roomCode: string) => (signal: GenericAbortSignal) => api.get<RoomResponse>(`/rooms/${roomCode}/watch`, signal);

export const useWatchRoom = (roomCode: string) => useLongPolling(['getRoom', roomCode], watchRoom(roomCode));
