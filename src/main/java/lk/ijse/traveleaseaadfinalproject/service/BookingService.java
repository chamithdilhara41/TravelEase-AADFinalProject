package lk.ijse.traveleaseaadfinalproject.service;

import lk.ijse.traveleaseaadfinalproject.dto.BookingDTO;

import java.util.List;

public interface BookingService {
    boolean saveBooking(BookingDTO dto);

    List<BookingDTO> getAllBookings();

    List<BookingDTO> getBookingsByUserEmail(String email);
}
