package lk.ijse.traveleaseaadfinalproject.controller;


import lk.ijse.traveleaseaadfinalproject.dto.ResponseDTO;
import lk.ijse.traveleaseaadfinalproject.dto.TourPackageDTO;
import lk.ijse.traveleaseaadfinalproject.service.TourPackageService;
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
import java.util.List;

@CrossOrigin(origins = "http://localhost:63342")
@RestController
@RequestMapping("api/v1/package")

public class TourPackageController {
    @Autowired
    private TourPackageService tourPackageService;

    private static final String UPLOAD_DIR = "src/main/resources/templates/uploads/";

    @PostMapping("/save")
    public ResponseEntity<ResponseDTO> saveTourPackage(
            @RequestParam("name") String name,
            @RequestParam("price") Double price,
            @RequestParam("estimatedDays") Integer estimatedDays,
            @RequestParam("destinations") List<String> destinationIds, // expects multiple 'destinations' values
            @RequestParam(value = "imageUrl", required = false) MultipartFile image
    ) {
        try {
            // Prepare DTO
            TourPackageDTO packageDTO = new TourPackageDTO();
            packageDTO.setName(name);
            packageDTO.setPrice(price);
            packageDTO.setEstimatedDays(estimatedDays);
            packageDTO.setDestinations(destinationIds); // Just pass the IDs (simplified DTO)

            // Handle Guide Image Upload
            if (image != null && !image.isEmpty()) {
                String imagePath = saveFile(image);
                packageDTO.setImageUrl(imagePath);
            }

            // Save the tour package using the service
            int res = tourPackageService.saveTourPackage(packageDTO);

            switch (res) {
                case VarList.Created:
                    return ResponseEntity.status(HttpStatus.CREATED)
                            .body(new ResponseDTO(VarList.Created, "Tour Package Saved Successfully", packageDTO));
                case VarList.Not_Acceptable:
                    return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE)
                            .body(new ResponseDTO(VarList.Not_Acceptable, "Package Already Exists", null));
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
        File directory = new File(UPLOAD_DIR);
        if (!directory.exists()) {
            directory.mkdirs();
        }

        String uniqueFileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
        Path path = Paths.get(UPLOAD_DIR + uniqueFileName);
        Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);

        return uniqueFileName;
    }

    @GetMapping("/getAll")
    public ResponseEntity<ResponseDTO> getAllTourPackages() {
        try {
            List<TourPackageDTO> allPackages = tourPackageService.getAllTourPackages();
            return ResponseEntity.ok(new ResponseDTO(VarList.Created, "Packages retrieved successfully", allPackages));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseDTO(VarList.Internal_Server_Error, e.getMessage(), null));
        }
    }

}
