package lk.anjula.hotelreservationsystem.controller;

import jakarta.validation.Valid;
import lk.anjula.hotelreservationsystem.controller.request.RoomRequest;
import lk.anjula.hotelreservationsystem.controller.response.RoomResponse;
import lk.anjula.hotelreservationsystem.model.Room;
import lk.anjula.hotelreservationsystem.model.RoomType;
import lk.anjula.hotelreservationsystem.service.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/rooms")
public class RoomController {
    @Autowired
    private RoomService roomService;

    @PostMapping
    public ResponseEntity<RoomResponse> addRoom(@Valid @RequestBody RoomRequest request) {
        return ResponseEntity.ok(roomService.addRoom(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<RoomResponse> updateRoom(@PathVariable Long id, @Valid @RequestBody RoomRequest request) {
        return ResponseEntity.ok(roomService.updateRoom(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRoom(@PathVariable Long id) {
        roomService.deleteRoom(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<RoomResponse> getRoomById(@PathVariable Long id) {
        return ResponseEntity.ok(roomService.getRoomById(id));
    }

    @GetMapping("/available")
    public ResponseEntity<List<Room>> getAvailableRooms(@RequestParam RoomType roomType) {
        return ResponseEntity.ok(roomService.getAvailableRooms(roomType));
    }

    @GetMapping
    public ResponseEntity<List<RoomResponse>> getAllRooms() {
        return ResponseEntity.ok(roomService.getAllRooms());
    }
}
