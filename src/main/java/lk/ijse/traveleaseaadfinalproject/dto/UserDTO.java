package lk.ijse.traveleaseaadfinalproject.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserDTO {
    private String name;
    private String email;
    private String contact;
    private String password;
    private String role;

    /*private List<VehicleDTO> vehicles; // List of associated vehicles*/
}
