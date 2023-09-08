import api from './api';
import { useLongPolling } from './useLongPolling';
import { GenericAbortSignal } from 'axios';

const watchRoomsList = (signal: GenericAbortSignal) => api.get<WatchRoomsListResponse>('/rooms/watch', signal);

export const useWatchRoomsList = () => useLongPolling(['getRooms'], watchRoomsList);

export interface WatchRoomsListResponse {
  roomCode: string;
}
