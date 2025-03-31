package lk.ijse.traveleaseaadfinalproject.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "vehicles")
public class Vehicle {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
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

    @ElementCollection(fetch = FetchType.EAGER) // This enables storing multiple image paths
    private List<String> vehicleImages;

    @Column(nullable = false)
    private String status = "PENDING";// Default status

    @ManyToOne
    @JoinColumn(name = "user_email", referencedColumnName = "email", nullable = false)
    private User user;
}
