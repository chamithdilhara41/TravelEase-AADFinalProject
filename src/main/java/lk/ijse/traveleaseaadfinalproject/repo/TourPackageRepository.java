package lk.ijse.traveleaseaadfinalproject.repo;

import lk.ijse.traveleaseaadfinalproject.entity.TourPackage;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TourPackageRepository extends JpaRepository<TourPackage, Long> {
    boolean existsByName(String name);

    Optional<TourPackage> findByName(String name);
}
