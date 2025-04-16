package lk.ijse.traveleaseaadfinalproject.service;

import lk.ijse.traveleaseaadfinalproject.dto.TourPackageDTO;

import java.util.List;

public interface TourPackageService {
    int saveTourPackage(TourPackageDTO packageDTO);

    List<TourPackageDTO> getAllTourPackages();

    int deleteTourPackage(Long id);

    int updateTourPackage(Long id, TourPackageDTO tourPackageDTO);


}
