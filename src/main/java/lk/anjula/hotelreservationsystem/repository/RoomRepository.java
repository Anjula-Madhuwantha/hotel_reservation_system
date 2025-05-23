package lk.anjula.hotelreservationsystem.repository;

import lk.anjula.hotelreservationsystem.model.Room;
import lk.anjula.hotelreservationsystem.model.RoomType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RoomRepository extends JpaRepository<Room, Long> {
    List<Room> findByIsAvailableTrueAndRoomType(RoomType roomType);
}
