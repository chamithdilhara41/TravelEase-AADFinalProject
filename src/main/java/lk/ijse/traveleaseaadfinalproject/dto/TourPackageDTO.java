package lk.ijse.traveleaseaadfinalproject.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Component

public class TourPackageDTO {
    private Long id;
    private String name;
    private Double price;
    private Integer estimatedDays;
    private List<String> destinations;  // This will store the destination IDs instead of entities
    private String imageUrl;

}
