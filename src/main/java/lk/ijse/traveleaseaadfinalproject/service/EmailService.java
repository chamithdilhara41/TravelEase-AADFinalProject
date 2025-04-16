package lk.ijse.traveleaseaadfinalproject.service;

import lk.ijse.traveleaseaadfinalproject.dto.BookingDTO;

public interface EmailService {
    void sendGuideRegistrationEmail(String email, String fullName);

    void sendBookingConfirmationEmails(BookingDTO dto);
}
