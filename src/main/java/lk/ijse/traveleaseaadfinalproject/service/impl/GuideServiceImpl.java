package lk.ijse.traveleaseaadfinalproject.service.impl;

import lk.ijse.traveleaseaadfinalproject.dto.GuideDTO;
import lk.ijse.traveleaseaadfinalproject.entity.Guide;
import lk.ijse.traveleaseaadfinalproject.repo.GuideRepository;
import lk.ijse.traveleaseaadfinalproject.service.GuideService;
import lk.ijse.traveleaseaadfinalproject.util.VarList;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
public class GuideServiceImpl implements GuideService {

    @Autowired
    private GuideRepository guideRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public int saveGuide(GuideDTO guideDTO) {

        if (guideRepository.existsByEmail(guideDTO.getEmail())) {
            return VarList.Not_Acceptable;
        }

        Guide guide = modelMapper.map(guideDTO, Guide.class);

        guideRepository.save(guide);
        return VarList.Created;
    }

    @Override
    public int updateGuide(String email, GuideDTO guideDTO) {
        Optional<Guide> existingGuideOpt = guideRepository.findByEmail(email);
        if (existingGuideOpt.isPresent()) {
            Guide existingGuide = existingGuideOpt.get();

            existingGuide.setFullName(guideDTO.getFullName());
            existingGuide.setDescription(guideDTO.getDescription());
            existingGuide.setLinkedin(guideDTO.getLinkedin());
            existingGuide.setPaymentPerDay(guideDTO.getPaymentPerDay());

            if (guideDTO.getImageUrl() != null) {
                existingGuide.setImageUrl(guideDTO.getImageUrl());
            }

            guideRepository.save(existingGuide);
            return VarList.Created;
        }
        return VarList.Not_Found;
    }

    @Override
    public int deactivateGuide(String email) {
        Optional<Guide> existingGuideOpt = guideRepository.findByEmail(email);
        if (existingGuideOpt.isPresent()) {
            Guide existingGuide = existingGuideOpt.get();
            existingGuide.setStatus("INACTIVE");
            guideRepository.save(existingGuide);
            return VarList.Created;
        }
        return VarList.Not_Found;
    }

    @Override
    public int activateGuide(String email) {
        Optional<Guide> existingGuideOpt = guideRepository.findByEmail(email);
        if (existingGuideOpt.isPresent()) {
            Guide existingGuide = existingGuideOpt.get();
            existingGuide.setStatus("ACTIVE");  // Change status to ACTIVE
            guideRepository.save(existingGuide);
            return VarList.Created;
        }
        return VarList.Not_Found;
    }

    @Override
    public List<GuideDTO> getAllGuides() {
        List<Guide> allGuides = guideRepository.findAll();
        return allGuides.stream()
                .map(guide -> modelMapper.map(guide, GuideDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<GuideDTO> getAvailableGuides() {
        List<Guide> guides = guideRepository.findAllByBookedAndStatus("NO", "ACTIVE");
        return guides.stream()
                .map(guide -> modelMapper.map(guide, GuideDTO.class))
                .collect(Collectors.toList());
    }

}
