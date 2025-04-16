package lk.ijse.traveleaseaadfinalproject.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class VehicleDTO {
    private String vehicleNumber;
    private String vehicleType;
    private String brand;
    private String model;
    private int year;
    private String color;
    private int seatingCapacity;
    private String fuelType;

    private String insuranceDocument;
    private String registrationDocument;

    private String booked;

    private List<String> vehicleImages;

    private String status;  // New field for status

    private String userEmail; // Foreign key reference to User email
}
