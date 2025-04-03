package lk.ijse.traveleaseaadfinalproject.dto;

import lombok.*;
import org.springframework.stereotype.Component;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Component

public class DestinationDTO {

    private Long id;
    private String name;
    private String description;
    private String imageUrl;
    private String location;
    private String category;
    private String costPerDay;
}
