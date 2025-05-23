package lk.anjula.hotelreservationsystem.service;

import lk.anjula.hotelreservationsystem.controller.response.ReportResponse;

import java.time.LocalDate;

public interface ReportService {
    ReportResponse generateOccupancyReport(LocalDate date);
    ReportResponse generateRevenueReport(LocalDate date);
}
