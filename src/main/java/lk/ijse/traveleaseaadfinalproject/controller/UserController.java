package lk.ijse.traveleaseaadfinalproject.controller;

import jakarta.validation.Valid;
import lk.ijse.traveleaseaadfinalproject.dto.AuthDTO;
import lk.ijse.traveleaseaadfinalproject.dto.ResponseDTO;
import lk.ijse.traveleaseaadfinalproject.dto.UserDTO;
import lk.ijse.traveleaseaadfinalproject.service.UserService;
import lk.ijse.traveleaseaadfinalproject.util.JwtUtil;
import lk.ijse.traveleaseaadfinalproject.util.VarList;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "http://localhost:63342")
@RestController
@RequestMapping("api/v1/user")
public class UserController {
    private final UserService userService;
    private final JwtUtil jwtUtil;

    //constructor injection
    public UserController(UserService userService, JwtUtil jwtUtil) {
        this.userService = userService;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping(value = "/register")
    public ResponseEntity<ResponseDTO> registerUser(@RequestBody @Valid UserDTO userDTO) {
        try {
            int res = userService.saveUser(userDTO);
            switch (res) {
                case VarList.Created -> {
                    String token = jwtUtil.generateToken(userDTO);
                    AuthDTO authDTO = new AuthDTO();
                    authDTO.setEmail(userDTO.getEmail());
                    authDTO.setToken(token);
                    return ResponseEntity.status(HttpStatus.CREATED)
                            .body(new ResponseDTO(VarList.Created, "Success", authDTO));
                }
                case VarList.Not_Acceptable -> {
                    return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE)
                            .body(new ResponseDTO(VarList.Not_Acceptable, "Email Already Used", null));
                }
                default -> {
                    return ResponseEntity.status(HttpStatus.BAD_GATEWAY)
                            .body(new ResponseDTO(VarList.Bad_Gateway, "Error", null));
                }
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseDTO(VarList.Internal_Server_Error, e.getMessage(), null));
        }
    }
    @GetMapping(value = "/getUserByEmail")
    public ResponseEntity<ResponseDTO> getUserDetail(@RequestParam String email) {

        UserDTO user = userService.getUserByEmail(email);
        if (user != null) {
            return ResponseEntity.ok(new ResponseDTO(VarList.Created, "Success", user));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ResponseDTO(VarList.Not_Acceptable, "User not found", null));
        }
    }

    @PostMapping("/update-role")
    public ResponseEntity<ResponseDTO> updateUserRole(@RequestParam String email, @RequestParam String role) {
        boolean updated = userService.updateUserRole(email, role);

        if (updated) {
            return ResponseEntity.ok(new ResponseDTO(VarList.Created, "User role updated successfully", updated));
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ResponseDTO(VarList.Not_Acceptable, "User not found", null));
    }


}
