import { useGetRooms } from '../../api/useGetRooms';
import React from 'react';
import { RoomEntry } from './RoomEntry';
import { useWatchRoomsList } from '../../api/useWatchRoomsList';

export const Rooms = () => {
  const roomsQuery = useGetRooms();
  useWatchRoomsList()

  const rooms = roomsQuery.data?.rooms ?? []

  return <div className={`p-8`}>
    <div className={`text-3xl text-center mt-4`}>Wybierz pokój</div>
    <div className={`mt-8`}>
      {rooms.map((room) => <RoomEntry key={room.code} room={room} />)}
    </div>
  </div>
};


