package lk.ijse.traveleaseaadfinalproject.service.impl;

import lk.ijse.traveleaseaadfinalproject.dto.BookingDTO;
import lk.ijse.traveleaseaadfinalproject.entity.*;
import lk.ijse.traveleaseaadfinalproject.repo.*;
import lk.ijse.traveleaseaadfinalproject.service.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Service
public class BookingServiceImpl implements BookingService {

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private GuideRepository guideRepository;

    @Autowired
    private VehicleRepository vehicleRepository;

    @Autowired
    private TourPackageRepository packageRepository;

    @Override
    @Transactional
    public boolean saveBooking(BookingDTO dto) {
        try {
            TourPackage tourPackage = packageRepository.findByName(dto.getPackageName())
                    .orElseThrow(() -> new RuntimeException("Package not found"));

            Guide guide = guideRepository.findByEmail(dto.getGuideEmail())
                    .orElseThrow(() -> new RuntimeException("Guide not found"));

            Vehicle vehicle = vehicleRepository.findByVehicleNumber(dto.getVehicleNumber())
                    .orElseThrow(() -> new RuntimeException("Vehicle not found"));

            User user = userRepository.findByEmail(dto.getUserEmail());

            Booking booking = new Booking();
            booking.setUser(user);
            booking.setGuide(guide);
            booking.setVehicle(vehicle);
            booking.setTourPackage(tourPackage);
            booking.setEstimatedDays(dto.getEstimatedDays());
            booking.setBookingDate(LocalDate.parse(dto.getBookingDate()));
            booking.setCheckoutDate(LocalDate.parse(dto.getCheckoutDate()));
            booking.setNumberOfPeople(dto.getNumberOfPeople());
            booking.setBasePrice(dto.getBasePrice());
            booking.setGuideFee(dto.getGuideFee());
            booking.setTotalPrice(dto.getTotalPrice());
            booking.setActive(true);

            bookingRepository.save(booking);

            vehicle.setBooked("YES");
            guide.setBooked("YES");

            vehicleRepository.save(vehicle);
            guideRepository.save(guide);

            return true;

        } catch (Exception e) {
            e.printStackTrace(); // or use a logger
            return false;
        }
    }

    @Override
    public int getTotalBookings() {
        return (int) bookingRepository.count();
    }

    @Override
    public List<BookingDTO> getAllBookings() {
        List<Booking> bookings = bookingRepository.findAll();
        return bookings.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    public List<BookingDTO> getBookingsByUserEmail(String email) {
        List<Booking> bookings = bookingRepository.findByUserEmail(email);
        return bookings.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    private BookingDTO convertToDTO(Booking booking) {
        BookingDTO dto = new BookingDTO();
        dto.setPackageName(booking.getTourPackage().getName());
        dto.setGuideEmail(booking.getGuide().getEmail());
        dto.setVehicleNumber(booking.getVehicle().getVehicleNumber());
        dto.setUserEmail(booking.getUser().getEmail());
        dto.setBookingDate(booking.getBookingDate().toString());
        dto.setCheckoutDate(booking.getCheckoutDate().toString());
        dto.setEstimatedDays(booking.getEstimatedDays());
        dto.setNumberOfPeople(booking.getNumberOfPeople());
        dto.setBasePrice(booking.getBasePrice());
        dto.setGuideFee(booking.getGuideFee());
        dto.setTotalPrice(booking.getTotalPrice());
        return dto;
    }
    @Override
    public List<Map<String, Object>> getBookingsPerDay() {
        return bookingRepository.findBookingsPerDay();
    }
    @Override
    public List<Map<String, Object>> getTotalPricePerBooking() {
        return bookingRepository.findTotalPricePerBooking();
    }


}
