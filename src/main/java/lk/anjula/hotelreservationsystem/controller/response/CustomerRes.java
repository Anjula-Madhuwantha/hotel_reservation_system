package lk.anjula.hotelreservationsystem.controller.response;

import lk.anjula.hotelreservationsystem.model.Role;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CustomerRes {
    private Long id;
    private String name;
    private String email;
    private String phone;
    private String username;
    private String token;
    private Role role;
}
