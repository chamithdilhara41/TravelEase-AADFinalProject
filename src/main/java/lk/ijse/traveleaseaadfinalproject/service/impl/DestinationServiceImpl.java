package lk.ijse.traveleaseaadfinalproject.service.impl;

import lk.ijse.traveleaseaadfinalproject.dto.DestinationDTO;
import lk.ijse.traveleaseaadfinalproject.entity.Destination;
import lk.ijse.traveleaseaadfinalproject.repo.DestinationRepository;
import lk.ijse.traveleaseaadfinalproject.service.DestinationService;
import lk.ijse.traveleaseaadfinalproject.util.VarList;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class DestinationServiceImpl implements DestinationService {

    @Autowired
    private DestinationRepository destinationRepository;

    @Autowired
    private ModelMapper modelMapper;


    @Override
    public int saveDestination(DestinationDTO destinationDTO) {
        if (destinationRepository.existsByName(destinationDTO.getName())) {
            return VarList.Not_Acceptable; // Destination already exists
        }
        Destination destination = modelMapper.map(destinationDTO, Destination.class);
        destinationRepository.save(destination);
        return VarList.Created; // Successfully saved
    }

    @Override
    public int updateDestination(Long id, DestinationDTO destinationDTO) {
        Optional<Destination> optionalDestination = destinationRepository.findById(id);
        if (optionalDestination.isPresent()) {
            Destination existingDestination = optionalDestination.get();
            existingDestination.setName(destinationDTO.getName());
            existingDestination.setDescription(destinationDTO.getDescription());
            existingDestination.setImageUrl(destinationDTO.getImageUrl());
            existingDestination.setCostPerDay(destinationDTO.getCostPerDay());
            existingDestination.setLocation(destinationDTO.getLocation());
            existingDestination.setCategory(destinationDTO.getCategory());

            destinationRepository.save(existingDestination);
            return VarList.Created; // Successfully updated
        }
        return VarList.Not_Found; // Destination not found
    }

    @Override
    public int deleteDestination(Long id) {
        if (destinationRepository.existsById(id)) {
            destinationRepository.deleteById(id);
            return VarList.Created; // Successfully deleted
        }
        return VarList.Not_Found; // Destination not found
    }

    @Override
    public List<DestinationDTO> getAllDestinations() {
        List<Destination> destinations = destinationRepository.findAll();
        return destinations.stream()
                .map(destination -> modelMapper.map(destination, DestinationDTO.class))
                .collect(Collectors.toList());
    }

}
