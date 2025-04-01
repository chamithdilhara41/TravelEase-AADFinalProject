package lk.ijse.traveleaseaadfinalproject.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Table(name = "guides")

public class Guide {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int gid;

    private String fullName;
    private String description;
    @Column(unique = true, nullable = false)
    private String email;
    private String imageUrl;
    private String linkedin;
    private String paymentPerDay;
    private String status;
    private String booked;


}
