package lk.ijse.traveleaseaadfinalproject.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lk.ijse.traveleaseaadfinalproject.dto.TourPackageDTO;
import lk.ijse.traveleaseaadfinalproject.entity.Destination;
import lk.ijse.traveleaseaadfinalproject.entity.TourPackage;
import lk.ijse.traveleaseaadfinalproject.repo.DestinationRepository;
import lk.ijse.traveleaseaadfinalproject.repo.TourPackageRepository;
import lk.ijse.traveleaseaadfinalproject.service.TourPackageService;
import lk.ijse.traveleaseaadfinalproject.util.VarList;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TourPackageServiceImpl implements TourPackageService {

    @Autowired
    private TourPackageRepository tourPackageRepository;

    @Autowired
    private DestinationRepository destinationRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    @Transactional
    public int saveTourPackage(TourPackageDTO packageDTO) {
        if (tourPackageRepository.existsByName(packageDTO.getName())) {
            return VarList.Not_Acceptable;
        }

        // Map basic fields using ModelMapper
        TourPackage tourPackage = modelMapper.map(packageDTO, TourPackage.class);

        // Manually map destination IDs to Destination entities
        List<Destination> destinationList = packageDTO.getDestinations().stream()
                .map(idStr -> destinationRepository.findById(Long.parseLong(String.valueOf(idStr)))
                        .orElseThrow(() -> new RuntimeException("Destination not found: " + idStr)))
                .collect(Collectors.toList());

        tourPackage.setDestinations(destinationList);

        tourPackageRepository.save(tourPackage);
        return VarList.Created;
    }

    @Override
    @Transactional
    public int updateTourPackage(Long id, TourPackageDTO tourPackageDTO) {
        // Find the existing package by ID
        Optional<TourPackage> existingTourPackageOpt = tourPackageRepository.findById(id);
        if (existingTourPackageOpt.isPresent()) {
            TourPackage existingTourPackage = existingTourPackageOpt.get();

            // Map updated fields from DTO to the existing entity
            modelMapper.map(tourPackageDTO, existingTourPackage);

            // Save the updated tour package
            tourPackageRepository.save(existingTourPackage);
            return VarList.Created;
        }
        return VarList.Not_Found;
    }

    @Override
    @Transactional
    public int deleteTourPackage(Long id) {
        // Find the package by ID
        Optional<TourPackage> tourPackageOpt = tourPackageRepository.findById(id);
        if (tourPackageOpt.isPresent()) {
            // Delete the tour package if found
            tourPackageRepository.deleteById(id);
            return VarList.Created;
        }
        return VarList.Not_Found;
    }

    public TourPackageDTO getTourPackageById(Long id) {
        Optional<TourPackage> tourPackageOpt = tourPackageRepository.findById(id);
        if (tourPackageOpt.isPresent()) {
            // Map the TourPackage entity to DTO using ModelMapper
            return modelMapper.map(tourPackageOpt.get(), TourPackageDTO.class);
        }
        return null;
    }

    @Override
    public List<TourPackageDTO> getAllTourPackages() {
        List<TourPackage> tourPackages = tourPackageRepository.findAll();

        return tourPackages.stream().map(tourPackage -> {
            TourPackageDTO dto = new TourPackageDTO();

            dto.setId(tourPackage.getId());
            dto.setName(tourPackage.getName());
            dto.setPrice(tourPackage.getPrice());
            dto.setEstimatedDays(tourPackage.getEstimatedDays());
            dto.setImageUrl(tourPackage.getImageUrl());

            // ✅ Map destination names instead of IDs
            if (tourPackage.getDestinations() != null) {
                List<String> destinationNames = tourPackage.getDestinations()
                        .stream()
                        .map(Destination::getName)  // <-- get name instead of ID
                        .collect(Collectors.toList());
                dto.setDestinations(destinationNames);
            }

            return dto;
        }).collect(Collectors.toList());
    }



}
