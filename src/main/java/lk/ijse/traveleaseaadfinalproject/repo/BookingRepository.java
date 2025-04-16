package lk.ijse.traveleaseaadfinalproject.repo;

import lk.ijse.traveleaseaadfinalproject.entity.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, String> {
    List<Booking> findByCheckoutDateLessThanEqualAndActive(LocalDate checkoutDateIsGreaterThan, boolean active);

    List<Booking> findByUserEmail(String email);
}
