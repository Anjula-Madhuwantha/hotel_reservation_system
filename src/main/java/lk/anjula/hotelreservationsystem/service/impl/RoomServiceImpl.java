package lk.anjula.hotelreservationsystem.service.impl;

import lk.anjula.hotelreservationsystem.controller.request.RoomRequest;
import lk.anjula.hotelreservationsystem.controller.response.RoomResponse;
import lk.anjula.hotelreservationsystem.exception.ResourceNotFoundException;
import lk.anjula.hotelreservationsystem.exception.RoomException;
import lk.anjula.hotelreservationsystem.model.Room;
import lk.anjula.hotelreservationsystem.model.RoomType;
import lk.anjula.hotelreservationsystem.repository.ReservationRepository;
import lk.anjula.hotelreservationsystem.repository.RoomRepository;
import lk.anjula.hotelreservationsystem.service.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RoomServiceImpl implements RoomService {
    @Autowired
    private RoomRepository roomRepository;
    @Autowired
    private ReservationRepository reservationRepository;

    @Override
    public RoomResponse addRoom(RoomRequest request) {
        if (roomRepository.findAll().stream().anyMatch(r -> r.getRoomNumber().equals(request.getRoomNumber()))) {
            throw new RoomException("Room number " + request.getRoomNumber() + " already exists");
        }

        Room room = new Room();
        room.setRoomNumber(request.getRoomNumber());
        room.setRoomType(request.getRoomType());
        room.setPricePerNight(request.getPricePerNight());
        room.setMaxOccupants(request.getMaxOccupants());
        room.setIsAvailable(request.getIsAvailable());
        Room saved = roomRepository.save(room);
        return mapToRoomResponse(saved);
    }

    @Override
    public RoomResponse updateRoom(Long id, RoomRequest request) {
        Room room = roomRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Room not found with id: " + id));
        if (roomRepository.findAll().stream()
                .anyMatch(r -> r.getRoomNumber().equals(request.getRoomNumber()) && !r.getId().equals(id))) {
            throw new RoomException("Room number " + request.getRoomNumber() + " already exists");
        }

        room.setRoomNumber(request.getRoomNumber());
        room.setRoomType(request.getRoomType());
        room.setPricePerNight(request.getPricePerNight());
        room.setMaxOccupants(request.getMaxOccupants());
        room.setIsAvailable(request.getIsAvailable());
        Room updated = roomRepository.save(room);
        return mapToRoomResponse(updated);
    }

    @Override
    public void deleteRoom(Long id) {
        Room room = roomRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Room not found with id: " + id));
        if (reservationRepository.findAll().stream()
                .anyMatch(r -> r.getRoom().getId().equals(id) && r.getReservationStatus() != lk.anjula.hotelreservationsystem.model.ReservationStatus.CANCELLED && r.getReservationStatus() != lk.anjula.hotelreservationsystem.model.ReservationStatus.CHECKED_OUT)) {
            throw new RoomException("Cannot delete room with active reservations");
        }
        roomRepository.delete(room);
    }

    @Override
    public RoomResponse getRoomById(Long id) {
        Room room = roomRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Room not found with id: " + id));
        return mapToRoomResponse(room);
    }

    @Override
    public List<Room> getAvailableRooms(RoomType roomType) {
        return roomRepository.findByIsAvailableTrueAndRoomType(roomType);
    }

    @Override
    public List<RoomResponse> getAllRooms() {
        return roomRepository.findAll().stream()
                .map(this::mapToRoomResponse)
                .collect(Collectors.toList());
    }

    private RoomResponse mapToRoomResponse(Room room) {
        RoomResponse response = new RoomResponse();
        response.setId(room.getId());
        response.setRoomNumber(room.getRoomNumber());
        response.setRoomType(room.getRoomType());
        response.setPricePerNight(room.getPricePerNight());
        response.setMaxOccupants(room.getMaxOccupants());
        response.setIsAvailable(room.getIsAvailable());
        return response;
    }
}
