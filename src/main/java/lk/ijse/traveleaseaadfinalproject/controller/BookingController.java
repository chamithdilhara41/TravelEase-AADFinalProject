package lk.ijse.traveleaseaadfinalproject.controller;

import lk.ijse.traveleaseaadfinalproject.dto.BookingDTO;
import lk.ijse.traveleaseaadfinalproject.dto.ResponseDTO;
import lk.ijse.traveleaseaadfinalproject.service.BookingService;
import lk.ijse.traveleaseaadfinalproject.util.VarList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("api/v1/booking")

public class BookingController {

    @Autowired
    private BookingService bookingService;

    @PostMapping("/save")
    public ResponseEntity<ResponseDTO> saveBooking(@RequestBody BookingDTO bookingDTO) {
        try {
            boolean isSaved = bookingService.saveBooking(bookingDTO);

            if (isSaved) {
                return ResponseEntity.status(HttpStatus.CREATED)
                        .body(new ResponseDTO(VarList.Created, "Booking Saved Successfully", bookingDTO));
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new ResponseDTO(VarList.Not_Acceptable, "Booking Not Saved", null));
            }

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseDTO(VarList.Internal_Server_Error, e.getMessage(), null));
        }
    }

    @GetMapping("/getAll")
    public ResponseEntity<ResponseDTO> getAllBookings() {
        try {
            List<BookingDTO> allBookings = bookingService.getAllBookings();
            return ResponseEntity.ok(new ResponseDTO(
                    VarList.Created, "Bookings Retrieved Successfully", allBookings));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ResponseDTO(VarList.Internal_Server_Error, e.getMessage(), null));
        }
    }

    @GetMapping("/getBookings/{email}")
    public ResponseEntity<List<BookingDTO>> getBookingsByUserEmail(@PathVariable String email) {
        List<BookingDTO> bookings = bookingService.getBookingsByUserEmail(email);
        return ResponseEntity.ok(bookings);
    }

}
