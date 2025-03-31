package lk.ijse.traveleaseaadfinalproject.repo;


import lk.ijse.traveleaseaadfinalproject.dto.UserDTO;
import lk.ijse.traveleaseaadfinalproject.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;


public interface UserRepository extends JpaRepository<User,String> {

    User findByEmail(String userName);

    boolean existsByEmail(String userName);



}