package lk.ijse.traveleaseaadfinalproject.service.impl;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lk.ijse.traveleaseaadfinalproject.dto.BookingDTO;
import lk.ijse.traveleaseaadfinalproject.repo.VehicleRepository;
import lk.ijse.traveleaseaadfinalproject.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class EmailServiceImpl implements EmailService {

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private VehicleRepository vehicleRepository;

    @Override
    public void sendGuideRegistrationEmail(String toEmail, String guideName) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmail);
        message.setSubject("Guide Registration Successful");
        message.setText("Dear " + guideName + ",\n\nYour registration as a guide is successful!\n\nBest Regards,\nTravelEase Team");
        mailSender.send(message);
    }

    @Override
    public void sendBookingConfirmationEmails(BookingDTO dto) {
        String vehicleOwnerEmail = vehicleRepository.findOwnerEmailByVehicleNumber(dto.getVehicleNumber());

        String subject = "TravelEase - Booking Confirmation";
        String htmlContent = generateHtmlBookingTable(dto);

        try {
            // Send to user
            sendHtmlEmail(dto.getUserEmail(), subject, "Dear User,<br><br>Your booking has been confirmed!<br><br>" + htmlContent);

            // Send to guide
            sendHtmlEmail(dto.getGuideEmail(), subject, "Dear Guide,<br><br>You have been assigned to a new booking.<br><br>" + htmlContent);

            // Send to vehicle owner
            if (vehicleOwnerEmail != null) {
                sendHtmlEmail(vehicleOwnerEmail, subject, "Dear Vehicle Owner,<br><br>Your vehicle has been assigned to a new booking.<br><br>" + htmlContent);
            }

        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    private void sendHtmlEmail(String to, String subject, String htmlBody) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(htmlBody, true); // true = HTML

        mailSender.send(message);
    }

    private String generateHtmlBookingTable(BookingDTO dto) {
        return """
                <table border="1" cellpadding="10" cellspacing="0" style="border-collapse: collapse;">
                    <thead>
                        <tr style="background-color: #f2f2f2;">
                            <th>Package</th>
                            <th>Booking Date</th>
                            <th>Checkout Date</th>
                            <th>People</th>
                            <th>Guide</th>
                            <th>Vehicle</th>
                            <th>Total (LKR)</th>
                        </tr>
                    </thead>
                    <tbody>
                        <tr>
                            <td>%s</td>
                            <td>%s</td>
                            <td>%s</td>
                            <td>%d</td>
                            <td>%s</td>
                            <td>%s</td>
                            <td>%.2f</td>
                        </tr>
                    </tbody>
                </table>
                """.formatted(
                dto.getPackageName(),
                dto.getBookingDate(),
                dto.getCheckoutDate(),
                dto.getNumberOfPeople(),
                dto.getGuideEmail(),
                dto.getVehicleNumber(),
                dto.getTotalPrice()
        );
    }
}
