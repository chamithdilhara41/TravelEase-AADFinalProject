package lk.ijse.traveleaseaadfinalproject.service;

import jakarta.validation.Valid;
import lk.ijse.traveleaseaadfinalproject.dto.VehicleDTO;
import lk.ijse.traveleaseaadfinalproject.entity.Vehicle;

import java.io.IOException;
import java.util.List;

public interface VehicleService {
    List<VehicleDTO> getAllVehicles();

    VehicleDTO getVehicleById(Long id);

    boolean updateVehicleStatus(Long id, String status);

    List<VehicleDTO> getVehiclesByStatus(String status);

    int saveVehicle(@Valid VehicleDTO vehicleDTO);

   /* VehicleDTO getVehicleByNumber(String vehicleNumber);

    int updateVehicle(String vehicleNumber, VehicleDTO updatedVehicle);

    boolean deleteVehicle(String vehicleNumber);*/
}
