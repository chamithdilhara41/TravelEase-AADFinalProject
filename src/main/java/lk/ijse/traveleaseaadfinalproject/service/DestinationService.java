package lk.ijse.traveleaseaadfinalproject.service;

import lk.ijse.traveleaseaadfinalproject.dto.DestinationDTO;

import java.util.List;

public interface DestinationService {
    int saveDestination(DestinationDTO destinationDTO);

    int updateDestination(Long id, DestinationDTO destinationDTO);

    int deleteDestination(Long id);

    List<DestinationDTO> getAllDestinations();
}
