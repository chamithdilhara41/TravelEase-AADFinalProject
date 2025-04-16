package lk.ijse.traveleaseaadfinalproject.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookingDTO {

    private String packageName;       // From TourPackage.name
    private String guideEmail;        // From Guide.email
    private String vehicleNumber;     // From Vehicle.vehicleNumber
    private String userEmail;         // From User.email

    private int estimatedDays;
    private String bookingDate;       // You'll parse this to LocalDate
    private String checkoutDate;      // You'll parse this to LocalDate

    private int numberOfPeople;
    private double basePrice;
    private double guideFee;
    private double totalPrice;
}
