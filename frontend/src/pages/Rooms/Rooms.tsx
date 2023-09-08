import { useGetRooms } from '../../api/useGetRooms';
import React, { useEffect, useState } from 'react';
import { RoomEntry } from './RoomEntry';
import { useWatchRoomsList } from '../../api/useWatchRoomsList';

export const Rooms = () => {
  const roomsQuery = useGetRooms();
  useWatchRoomsList()

  const rooms = roomsQuery.data?.rooms ?? []

  // const [rooms, setRooms] = useState(roomsQuery.data?.rooms ?? []);

  // useEffect(() => {
  //   if (!roomsQuery.isSuccess) return;
  //   setRooms(roomsQuery.data.rooms);
  // }, [roomsQuery.is]);

  return <div>
    <div>Wpisz kod pokoju</div>
    <div>lub wybierz z listy</div>
    <div>
      {rooms.map((room) => <RoomEntry key={room.code} room={room} />)}
    </div>
  </div>
};


