package lk.ijse.traveleaseaadfinalproject.service;


import lk.ijse.traveleaseaadfinalproject.dto.UserDTO;
import lk.ijse.traveleaseaadfinalproject.entity.User;

import java.util.Optional;

public interface UserService {
    int saveUser(UserDTO userDTO);
    UserDTO searchUser(String username);

    UserDTO getUserByEmail(String email);

    boolean updateUserRole(String email, String role);
}