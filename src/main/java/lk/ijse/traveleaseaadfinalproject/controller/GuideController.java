package lk.ijse.traveleaseaadfinalproject.controller;

import lk.ijse.traveleaseaadfinalproject.dto.GuideDTO;
import lk.ijse.traveleaseaadfinalproject.dto.ResponseDTO;
import lk.ijse.traveleaseaadfinalproject.service.GuideService;
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
@RequestMapping("api/v1/guide")

public class GuideController {

    @Autowired
    private GuideService guideService;

    private static final String UPLOAD_DIR = "src/main/resources/templates/uploads/";

    @PostMapping("/save")
    public ResponseEntity<ResponseDTO> saveGuide(
            @RequestParam("fullName") String fullName,
            @RequestParam("description") String description,
            @RequestParam("email") String email,
            @RequestParam(value = "imageUrl", required = false) MultipartFile image,
            @RequestParam("linkedin") String linkedin,
            @RequestParam("paymentPerDay") String paymentPerDay ) {
        try {
            GuideDTO guideDTO = new GuideDTO();
            guideDTO.setFullName(fullName);
            guideDTO.setDescription(description);
            guideDTO.setEmail(email);
            guideDTO.setLinkedin(linkedin);
            guideDTO.setPaymentPerDay(paymentPerDay);
            guideDTO.setStatus("ACTIVE");
            guideDTO.setBooked("NO");

            // Handle Guide Image Upload
            if (image != null && !image.isEmpty()) {
                String imagePath = saveFile(image);
                guideDTO.setImageUrl(imagePath);
            }

            int res = guideService.saveGuide(guideDTO);

            switch (res) {
                case VarList.Created:
                    return ResponseEntity.status(HttpStatus.CREATED)
                            .body(new ResponseDTO(VarList.Created, "Guide Saved Successfully", guideDTO));
                case VarList.Not_Acceptable:
                    return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE)
                            .body(new ResponseDTO(VarList.Not_Acceptable, "Guide Already Exists", null));
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

    @PostMapping("/update/{email}")
    public ResponseEntity<ResponseDTO> updateGuide(
            @PathVariable String email,
            @RequestParam("fullName") String fullName,
            @RequestParam("description") String description,
            @RequestParam(value = "imageUrl", required = false) MultipartFile image,
            @RequestParam("linkedin") String linkedin,
            @RequestParam("paymentPerDay") String paymentPerDay) {

        try {
            GuideDTO guideDTO = new GuideDTO();
            guideDTO.setFullName(fullName);
            guideDTO.setDescription(description);
            guideDTO.setLinkedin(linkedin);
            guideDTO.setPaymentPerDay(paymentPerDay);

            // Handle Guide Image Upload
            if (image != null && !image.isEmpty()) {
                String imagePath = saveFile(image);
                guideDTO.setImageUrl(imagePath);
            }

            int res = guideService.updateGuide(email, guideDTO);

            switch (res) {
                case VarList.Created:
                    return ResponseEntity.status(HttpStatus.OK)
                            .body(new ResponseDTO(VarList.Created, "Guide Updated Successfully", guideDTO));
                case VarList.Not_Found:
                    return ResponseEntity.status(HttpStatus.NOT_FOUND)
                            .body(new ResponseDTO(VarList.Not_Found, "Guide Not Found", null));
                default:
                    return ResponseEntity.status(HttpStatus.BAD_GATEWAY)
                            .body(new ResponseDTO(VarList.Bad_Gateway, "Error", null));
            }

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseDTO(VarList.Internal_Server_Error, e.getMessage(), null));
        }
    }

    @PutMapping("/deactivate/{email}")
    public ResponseEntity<ResponseDTO> deactivateGuide(@PathVariable String email) {
        try {
            int res = guideService.deactivateGuide(email);

            switch (res) {
                case VarList.Created:
                    return ResponseEntity.status(HttpStatus.OK)
                            .body(new ResponseDTO(VarList.Created, "Guide Deactivated Successfully", null));
                case VarList.Not_Found:
                    return ResponseEntity.status(HttpStatus.NOT_FOUND)
                            .body(new ResponseDTO(VarList.Not_Found, "Guide Not Found", null));
                default:
                    return ResponseEntity.status(HttpStatus.BAD_GATEWAY)
                            .body(new ResponseDTO(VarList.Bad_Gateway, "Error", null));
            }

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseDTO(VarList.Internal_Server_Error, e.getMessage(), null));
        }
    }

    @PutMapping("/activate/{email}")
    public ResponseEntity<ResponseDTO> activateGuide(@PathVariable String email) {
        try {
            int res = guideService.activateGuide(email);

            switch (res) {
                case VarList.Created:
                    return ResponseEntity.status(HttpStatus.OK)
                            .body(new ResponseDTO(VarList.Created, "Guide Activated Successfully", null));
                case VarList.Not_Found:
                    return ResponseEntity.status(HttpStatus.NOT_FOUND)
                            .body(new ResponseDTO(VarList.Not_Found, "Guide Not Found", null));
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
    public ResponseEntity<ResponseDTO> getAllGuides() {
        try {
            List<GuideDTO> allGuides = guideService.getAllGuides();
            return ResponseEntity.status(HttpStatus.OK)
                    .body(new ResponseDTO(VarList.Created, "All Guides Retrieved Successfully", allGuides));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseDTO(VarList.Internal_Server_Error, e.getMessage(), null));
        }
    }

}
