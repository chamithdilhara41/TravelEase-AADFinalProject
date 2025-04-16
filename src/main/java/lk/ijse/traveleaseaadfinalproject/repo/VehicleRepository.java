package lk.ijse.traveleaseaadfinalproject.repo;

import lk.ijse.traveleaseaadfinalproject.entity.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface VehicleRepository extends JpaRepository<Vehicle, Long> {
    boolean existsByVehicleNumber(String vehicleNumber);

    List<Vehicle> findByStatus(String status);

    Optional<Vehicle> findByVehicleNumber(String vehicleNumber);

    List<Vehicle> findAllByBookedAndStatus(String booked, String status);

    List<Vehicle> findByUserEmail(String email);

    String findOwnerEmailByVehicleNumber(String vehicleNumber);
}
