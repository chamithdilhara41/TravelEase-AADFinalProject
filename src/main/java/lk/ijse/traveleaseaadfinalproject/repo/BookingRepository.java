package lk.ijse.traveleaseaadfinalproject.repo;

import lk.ijse.traveleaseaadfinalproject.entity.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Repository
public interface BookingRepository extends JpaRepository<Booking, String> {
    List<Booking> findByCheckoutDateLessThanEqualAndActive(LocalDate checkoutDateIsGreaterThan, boolean active);

    List<Booking> findByUserEmail(String email);

    @Query(value = "SELECT booking_date AS date, COUNT(*) AS count FROM bookings GROUP BY booking_date", nativeQuery = true)
    List<Map<String, Object>> findBookingsPerDay();

    @Query(value = "SELECT id AS bookingId, total_price AS total FROM bookings", nativeQuery = true)
    List<Map<String, Object>> findTotalPricePerBooking();

}
