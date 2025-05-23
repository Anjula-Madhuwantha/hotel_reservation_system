package lk.anjula.hotelreservationsystem.controller;

import lk.anjula.hotelreservationsystem.controller.response.ReportResponse;
import lk.anjula.hotelreservationsystem.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/reports")
public class ReportController {
    @Autowired
    private ReportService reportService;

    @GetMapping("/occupancy")
    public ResponseEntity<ReportResponse> getOccupancyReport(@RequestParam String date) {
        return ResponseEntity.ok(reportService.generateOccupancyReport(LocalDate.parse(date)));
    }

    @GetMapping("/revenue")
    public ResponseEntity<ReportResponse> getRevenueReport(@RequestParam String date) {
        return ResponseEntity.ok(reportService.generateRevenueReport(LocalDate.parse(date)));
    }
}
