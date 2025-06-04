package lk.anjula.hotelreservationsystem.controller;

import jakarta.annotation.security.RolesAllowed;
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
@CrossOrigin(origins = "http://localhost:3000")
public class RoomController {
    @Autowired
    private RoomService roomService;

    @RolesAllowed({"ADMIN","CUSTOMER"})
    @PostMapping
    public ResponseEntity<RoomResponse> addRoom(@Valid @RequestBody RoomRequest request) {
        return ResponseEntity.ok(roomService.addRoom(request));
    }

    @RolesAllowed({"ADMIN","CUSTOMER"})
    @PutMapping("/{id}")
    public ResponseEntity<RoomResponse> updateRoom(@PathVariable Long id, @Valid @RequestBody RoomRequest request) {
        return ResponseEntity.ok(roomService.updateRoom(id, request));
    }

    @RolesAllowed({"ADMIN","CUSTOMER"})
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRoom(@PathVariable Long id) {
        roomService.deleteRoom(id);
        return ResponseEntity.ok().build();
    }

    @RolesAllowed({"ADMIN","CUSTOMER"})
    @GetMapping("/{id}")
    public ResponseEntity<RoomResponse> getRoomById(@PathVariable Long id) {
        return ResponseEntity.ok(roomService.getRoomById(id));
    }

    @RolesAllowed({"ADMIN","CUSTOMER"})
    @GetMapping("/available")
    public ResponseEntity<List<Room>> getAvailableRooms(@RequestParam RoomType roomType) {
        return ResponseEntity.ok(roomService.getAvailableRooms(roomType));
    }

    @RolesAllowed({"ADMIN","CUSTOMER"})
    @GetMapping
    public ResponseEntity<List<RoomResponse>> getAllRooms() {
        return ResponseEntity.ok(roomService.getAllRooms());
    }
}
