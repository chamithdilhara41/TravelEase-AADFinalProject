package lk.ijse.traveleaseaadfinalproject.service.impl;

import lk.ijse.traveleaseaadfinalproject.dto.VehicleDTO;
import lk.ijse.traveleaseaadfinalproject.entity.User;
import lk.ijse.traveleaseaadfinalproject.entity.Vehicle;
import lk.ijse.traveleaseaadfinalproject.repo.UserRepository;
import lk.ijse.traveleaseaadfinalproject.repo.VehicleRepository;
import lk.ijse.traveleaseaadfinalproject.service.VehicleService;
import lk.ijse.traveleaseaadfinalproject.util.VarList;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class VehicleServiceImpl implements VehicleService {

    @Autowired
    private VehicleRepository vehicleRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ModelMapper modelMapper;


    @Override
    public int saveVehicle(VehicleDTO vehicleDTO) {
        if (vehicleRepository.existsByVehicleNumber(vehicleDTO.getVehicleNumber())) {
            return VarList.Not_Acceptable;
        }

        // ✅ Fetch the User entity using the email
        Optional<User> userOptional = Optional.ofNullable(userRepository.findByEmail(vehicleDTO.getUserEmail()));
        if (userOptional.isEmpty()) {
            return VarList.Not_Found; // Return error if user does not exist
        }
        User user = userOptional.get(); // ✅ Extract the User object safely

        // ✅ Map DTO to Entity
        Vehicle vehicle = modelMapper.map(vehicleDTO, Vehicle.class);
        vehicle.setUser(user); // ✅ Assign managed User entity
        vehicle.setStatus("PENDING"); // Default status
        vehicle.setBooked("NO"); // Default status

        vehicleRepository.save(vehicle);
        return VarList.Created;
    }

    @Override
    public List<VehicleDTO> getAllVehicles() {
        List<Vehicle> vehicles = vehicleRepository.findAll();

        return vehicles.stream().map(vehicle -> {
            VehicleDTO dto = modelMapper.map(vehicle, VehicleDTO.class);

            // ✅ Convert Hibernate PersistentBag to a List<String>
            if (vehicle.getVehicleImages() != null) {
                List<String> imagePaths = vehicle.getVehicleImages()
                        .stream()
                        .map(String::valueOf) // Ensure proper conversion to String
                        .collect(Collectors.toList());
                dto.setVehicleImages(imagePaths);
            }

            return dto;
        }).collect(Collectors.toList());
    }

    @Override
    public VehicleDTO getVehicleById(Long id) {
        Optional<Vehicle> vehicle = vehicleRepository.findById(id);
        return vehicle.map(value -> modelMapper.map(value, VehicleDTO.class)).orElse(null);
    }

    @Override
    public boolean updateVehicleStatus(Long id, String status) {
        Optional<Vehicle> optionalVehicle = vehicleRepository.findById(id);
        if (optionalVehicle.isPresent()) {
            Vehicle vehicle = optionalVehicle.get();
            vehicle.setStatus(status);
            vehicleRepository.save(vehicle);
            return true;
        }
        return false;
    }

    @Override
    public int deactivateVehicle(String vehicleNumber) {
        Optional<Vehicle> existingVehicleOpt = vehicleRepository.findByVehicleNumber(vehicleNumber);
        if (existingVehicleOpt.isPresent()) {
            Vehicle existingVehicle = existingVehicleOpt.get();
            existingVehicle.setStatus("PENDING");
            vehicleRepository.save(existingVehicle);
            return VarList.Created;
        }
        return VarList.Not_Found;
    }

    @Override
    public int activateVehicle(String vehicleNumber) {
        Optional<Vehicle> existingVehicleOpt = vehicleRepository.findByVehicleNumber(vehicleNumber);
        if (existingVehicleOpt.isPresent()) {
            Vehicle existingVehicle = existingVehicleOpt.get();
            existingVehicle.setStatus("ACTIVE");
            vehicleRepository.save(existingVehicle);
            return VarList.Created;
        }
        return VarList.Not_Found;
    }


    @Override
    public List<VehicleDTO> getVehiclesByStatus(String status) {
        List<Vehicle> vehicles = vehicleRepository.findByStatus(status);
        return vehicles.stream()
                .map(vehicle -> modelMapper.map(vehicle, VehicleDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<VehicleDTO> getAvailableVehicles() {
        List<Vehicle> vehicles = vehicleRepository.findAllByBookedAndStatus("NO", "ACTIVE");
        return vehicles.stream()
                .map(vehicle -> modelMapper.map(vehicle, VehicleDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<VehicleDTO> getVehiclesByUserEmail(String email) {
        List<Vehicle> vehicles = vehicleRepository.findByUserEmail(email);
        return vehicles.stream()
                .map(vehicle -> modelMapper.map(vehicle, VehicleDTO.class))
                .collect(Collectors.toList());
    }
    @Override
    public int getTotalVehicleCount() {
        return (int) vehicleRepository.count();
    }

}
