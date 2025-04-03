package lk.ijse.traveleaseaadfinalproject.controller;

import lk.ijse.traveleaseaadfinalproject.dto.DestinationDTO;
import lk.ijse.traveleaseaadfinalproject.dto.ResponseDTO;
import lk.ijse.traveleaseaadfinalproject.service.DestinationService;
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
@RequestMapping("api/v1/destination")
public class DestinationController {

    @Autowired
    private DestinationService destinationService;

    private static final String UPLOAD_DIR = "src/main/resources/templates/uploads/";

    @PostMapping("/save")
    public ResponseEntity<ResponseDTO> saveDestination(
            @RequestParam("name") String name,
            @RequestParam("description") String description,
            @RequestParam("location") String location,
            @RequestParam("category") String category,
            @RequestParam("costPerDay") String costPerDay,
            @RequestParam(value = "imageUrl", required = false) MultipartFile image) {
        try {
            DestinationDTO destinationDTO = new DestinationDTO();
            destinationDTO.setName(name);
            destinationDTO.setDescription(description);
            destinationDTO.setLocation(location);
            destinationDTO.setCategory(category);
            destinationDTO.setCostPerDay(costPerDay);

            if (image != null && !image.isEmpty()) {
                String imagePath = saveFile(image);
                destinationDTO.setImageUrl(imagePath);
            }

            int res = destinationService.saveDestination(destinationDTO);

            switch (res) {
                case VarList.Created:
                    return ResponseEntity.status(HttpStatus.CREATED)
                            .body(new ResponseDTO(VarList.Created, "Destination Saved Successfully", destinationDTO));
                case VarList.Not_Acceptable:
                    return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE)
                            .body(new ResponseDTO(VarList.Not_Acceptable, "Destination Already Exists", null));
                default:
                    return ResponseEntity.status(HttpStatus.BAD_GATEWAY)
                            .body(new ResponseDTO(VarList.Bad_Gateway, "Error", null));
            }

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseDTO(VarList.Internal_Server_Error, e.getMessage(), null));
        }
    }

    // Save the image file and return its path
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


    @PostMapping("/update/{id}")
    public ResponseEntity<ResponseDTO> updateDestination(
            @PathVariable Long id,
            @RequestParam("editDestinationName") String name,
            @RequestParam("editDestinationDescription") String description,
            @RequestParam("editDestinationLocation") String location,
            @RequestParam("editDestinationCategory") String category,
            @RequestParam("editDestinationCostPerDay") String costPerDay,
            @RequestParam(value = "editDestinationImage", required = false) MultipartFile image) {
        System.out.println(id);
        try {

            DestinationDTO destinationDTO = new DestinationDTO();
            destinationDTO.setName(name);
            destinationDTO.setDescription(description);
            destinationDTO.setLocation(location);
            destinationDTO.setCategory(category);
            destinationDTO.setCostPerDay(costPerDay);

            // Handle Image Upload
            if (image != null && !image.isEmpty()) {
                String imagePath = saveFile(image);
                destinationDTO.setImageUrl(imagePath);
            }

            int res = destinationService.updateDestination(id, destinationDTO);

            switch (res) {
                case VarList.Created:
                    return ResponseEntity.status(HttpStatus.OK)
                            .body(new ResponseDTO(VarList.Created, "Destination Updated Successfully", destinationDTO));
                case VarList.Not_Found:
                    return ResponseEntity.status(HttpStatus.NOT_FOUND)
                            .body(new ResponseDTO(VarList.Not_Found, "Destination Not Found", null));
                default:
                    return ResponseEntity.status(HttpStatus.BAD_GATEWAY)
                            .body(new ResponseDTO(VarList.Bad_Gateway, "Error", null));
            }

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseDTO(VarList.Internal_Server_Error, e.getMessage(), null));
        }
    }


    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ResponseDTO> deleteDestination(@PathVariable Long id) {
        try {
            int res = destinationService.deleteDestination(id);
            switch (res) {
                case VarList.Created:
                    return ResponseEntity.status(HttpStatus.OK)
                            .body(new ResponseDTO(VarList.Created, "Destination Deleted Successfully", null));
                case VarList.Not_Found:
                    return ResponseEntity.status(HttpStatus.NOT_FOUND)
                            .body(new ResponseDTO(VarList.Not_Found, "Destination Not Found", null));
                default:
                    return ResponseEntity.status(HttpStatus.BAD_GATEWAY)
                            .body(new ResponseDTO(VarList.Bad_Gateway, "Error", null));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseDTO(VarList.Internal_Server_Error, e.getMessage(), null));
        }
    }

    @GetMapping("/getAll")
    public ResponseEntity<ResponseDTO> getAllDestinations() {
        try {
            List<DestinationDTO> allDestinations = destinationService.getAllDestinations();
            return ResponseEntity.status(HttpStatus.OK)
                    .body(new ResponseDTO(VarList.Created, "All Destinations Retrieved Successfully", allDestinations));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseDTO(VarList.Internal_Server_Error, e.getMessage(), null));
        }
    }
}
