package lk.ijse.traveleaseaadfinalproject.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;


@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "bookings")
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // === Relationships ===

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "package_id", nullable = false)
    private TourPackage tourPackage;

    @ManyToOne
    @JoinColumn(name = "vehicle_id", nullable = false)
    private Vehicle vehicle;

    @ManyToOne
    @JoinColumn(name = "guide_id", referencedColumnName = "gid", nullable = false)
    private Guide guide;

    // === Booking Details ===
    private int estimatedDays;

    private LocalDate bookingDate;
    private LocalDate checkoutDate;

    private int numberOfPeople;

    private double basePrice;
    private double guideFee;
    private double totalPrice;

    @Column(nullable = false)
    private boolean active = true;

}
