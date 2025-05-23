package lk.anjula.hotelreservationsystem.service;

import lk.anjula.hotelreservationsystem.controller.request.RoomRequest;
import lk.anjula.hotelreservationsystem.controller.response.RoomResponse;
import lk.anjula.hotelreservationsystem.model.Room;
import lk.anjula.hotelreservationsystem.model.RoomType;

import java.util.List;

public interface RoomService {
    RoomResponse addRoom(RoomRequest request);
    RoomResponse updateRoom(Long id, RoomRequest request);
    void deleteRoom(Long id);
    RoomResponse getRoomById(Long id);
    List<Room> getAvailableRooms(RoomType roomType);
    List<RoomResponse> getAllRooms();
}
