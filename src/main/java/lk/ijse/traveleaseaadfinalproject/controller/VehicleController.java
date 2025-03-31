package lk.ijse.traveleaseaadfinalproject.controller;

import lk.ijse.traveleaseaadfinalproject.dto.ResponseDTO;
import lk.ijse.traveleaseaadfinalproject.dto.VehicleDTO;
import lk.ijse.traveleaseaadfinalproject.entity.User;
import lk.ijse.traveleaseaadfinalproject.service.UserService;
import lk.ijse.traveleaseaadfinalproject.service.VehicleService;
import lk.ijse.traveleaseaadfinalproject.util.VarList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "http://localhost:63342")
@RestController
@RequestMapping("api/v1/vehicle")
public class VehicleController {
    @Autowired
    private VehicleService vehicleService;

    @Autowired
    private UserService userService;


    @PostMapping("/register")
    public ResponseEntity<ResponseDTO> registerVehicle(
            @RequestParam("vehicleNumber") String vehicleNumber,
            @RequestParam("vehicleType") String vehicleType,
            @RequestParam("brand") String brand,
            @RequestParam("model") String model,
            @RequestParam("year") int year,
            @RequestParam("color") String color,
            @RequestParam("seatingCapacity") int seatingCapacity,
            @RequestParam("fuelType") String fuelType,
            @RequestParam("userEmail") String userEmail,  // ✅ Add user email
            @RequestParam(value = "insuranceDocument", required = false) MultipartFile insuranceDocument,
            @RequestParam(value = "registrationDocument", required = false) MultipartFile registrationDocument,
            @RequestParam(value = "vehicleImages", required = false) MultipartFile[] vehicleImages) {

        try {

            VehicleDTO vehicleDTO = new VehicleDTO();
            vehicleDTO.setVehicleNumber(vehicleNumber);
            vehicleDTO.setVehicleType(vehicleType);
            vehicleDTO.setBrand(brand);
            vehicleDTO.setModel(model);
            vehicleDTO.setYear(year);
            vehicleDTO.setColor(color);
            vehicleDTO.setSeatingCapacity(seatingCapacity);
            vehicleDTO.setFuelType(fuelType);
            vehicleDTO.setStatus("PENDING");
            vehicleDTO.setUserEmail(userEmail);

            // Handle the insurance document
            if (insuranceDocument != null && !insuranceDocument.isEmpty()) {
                String insuranceDocPath = saveFile(insuranceDocument);
                vehicleDTO.setInsuranceDocument(insuranceDocPath); // Store path instead of MultipartFile
            }

            // Handle the registration document
            if (registrationDocument != null && !registrationDocument.isEmpty()) {
                String registrationDocPath = saveFile(registrationDocument);
                vehicleDTO.setRegistrationDocument(registrationDocPath); // Store path instead of MultipartFile
            }

            // Handle vehicle images
            List<String> imagePaths = new ArrayList<>();
            if (vehicleImages != null && vehicleImages.length > 0) {
                for (MultipartFile file : vehicleImages) {
                    String imagePath = saveFile(file);
                    imagePaths.add(imagePath);
                }
            }
            vehicleDTO.setVehicleImages(imagePaths); // Store list of paths

            int res = vehicleService.saveVehicle(vehicleDTO);

            switch (res) {
                case VarList.Created:
                    return ResponseEntity.status(HttpStatus.CREATED)
                            .body(new ResponseDTO(VarList.Created, "Vehicle Registered Successfully", vehicleDTO));
                case VarList.Not_Acceptable:
                    return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE)
                            .body(new ResponseDTO(VarList.Not_Acceptable, "Vehicle Number Already Exists", null));
                default:
                    return ResponseEntity.status(HttpStatus.BAD_GATEWAY)
                            .body(new ResponseDTO(VarList.Bad_Gateway, "Error", null));
            }

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseDTO(VarList.Internal_Server_Error, e.getMessage(), null));
        }
    }

    // Save the file and return its path
    private String saveFile(MultipartFile file) throws IOException {
        String uploadDir = "src/main/resources/templates/uploads/";
        File directory = new File(uploadDir);
        if (!directory.exists()) {
            directory.mkdirs();
        }

        String uniqueFileName = System.currentTimeMillis() + "_" +file.getOriginalFilename();
        Path path = Paths.get(uploadDir + uniqueFileName);
        Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);

        return uniqueFileName;
    }



    @GetMapping("/getAll")
    public ResponseEntity<ResponseDTO> getAllVehicles() {
        try {
            List<VehicleDTO> vehicles = vehicleService.getAllVehicles();
            return ResponseEntity.ok(new ResponseDTO(VarList.Created, "Fetched All Vehicles", vehicles));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseDTO(VarList.Internal_Server_Error, e.getMessage(), null));
        }
    }


    @PatchMapping("/{id}/status")
    public ResponseEntity<ResponseDTO> updateVehicleStatus(@PathVariable Long id, @RequestParam String status) {
        try {
            boolean isUpdated = vehicleService.updateVehicleStatus(id, status);
            if (isUpdated) {
                return ResponseEntity.ok(new ResponseDTO(VarList.Created, "Vehicle Status Updated Successfully", null));
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ResponseDTO(VarList.Not_Found, "Vehicle Not Found", null));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseDTO(VarList.Internal_Server_Error, e.getMessage(), null));
        }
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<ResponseDTO> getVehiclesByStatus(@PathVariable String status) {
        try {
            List<VehicleDTO> vehicles = vehicleService.getVehiclesByStatus(status);
            return ResponseEntity.ok(new ResponseDTO(VarList.Created, "Fetched Vehicles by Status", vehicles));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseDTO(VarList.Internal_Server_Error, e.getMessage(), null));
        }
    }
}
