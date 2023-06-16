import { useGetRooms } from '../../api/useGetRooms';
import React, { useEffect, useState } from 'react';
import { RoomEntry } from './RoomEntry';
import { socket } from '../../socket/socket';

export const Rooms = () => {
  const roomsQuery = useGetRooms();

  const [rooms, setRooms] = useState(roomsQuery.data?.rooms ?? []);

  useEffect(() => {
    if (!roomsQuery.isSuccess) return;
    setRooms(roomsQuery.data.rooms);
  }, [roomsQuery.isSuccess]);

  useEffect(() => {
    socket.on('NEW_ROOM', (data) => {
      const isRoomAlreadyOnList = rooms.some((room) => room.code === data.code);
      if (isRoomAlreadyOnList) return;

      setRooms((prevRooms) => [...prevRooms, {
        code: data.code,
        name: data.name,
        usersCount: data.usersCount,
      }]);
    });

    return () => {
      socket.off('NEW_ROOM');
    };
  }, []);

  return <div>
    <div>Wpisz kod pokoju</div>
    <div>lub wybierz z listy</div>
    <div>
      {rooms.map((room) => <RoomEntry key={room.code} room={room} />)}
    </div>
  </div>;
};


