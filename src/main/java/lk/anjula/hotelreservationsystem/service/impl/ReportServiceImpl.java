package lk.anjula.hotelreservationsystem.service.impl;

import lk.anjula.hotelreservationsystem.controller.response.ReportResponse;
import lk.anjula.hotelreservationsystem.model.Reservation;
import lk.anjula.hotelreservationsystem.repository.ReservationRepository;
import lk.anjula.hotelreservationsystem.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class ReportServiceImpl implements ReportService {
    @Autowired
    private ReservationRepository reservationRepository;

    @Override
    public ReportResponse generateOccupancyReport(LocalDate date) {
        List<Reservation> reservations = reservationRepository.findByCheckInDateBetween(date, date);
        ReportResponse response = new ReportResponse();
        response.setDate(date.toString());
        response.setTotalOccupancy(reservations.size());
        response.setTotalRevenue(0.0); // Occupancy report doesn't include revenue
        return response;
    }

    @Override
    public ReportResponse generateRevenueReport(LocalDate date) {
        List<Reservation> reservations = reservationRepository.findByCheckInDateBetween(date, date);
        double totalRevenue = reservations.stream()
                .mapToDouble(res -> res.getRoom().getPricePerNight())
                .sum();
        ReportResponse response = new ReportResponse();
        response.setDate(date.toString());
        response.setTotalOccupancy(reservations.size());
        response.setTotalRevenue(totalRevenue);
        return response;
    }
}
