package lk.anjula.hotelreservationsystem.service.impl;

import lk.anjula.hotelreservationsystem.controller.request.BlockBookingRequest;
import lk.anjula.hotelreservationsystem.controller.request.CheckInRequest;
import lk.anjula.hotelreservationsystem.controller.request.CheckOutRequest;
import lk.anjula.hotelreservationsystem.controller.request.ReservationRequest;
import lk.anjula.hotelreservationsystem.controller.response.BillingResponse;
import lk.anjula.hotelreservationsystem.controller.response.ReservationResponse;
import lk.anjula.hotelreservationsystem.exception.ReservationException;
import lk.anjula.hotelreservationsystem.exception.ResourceNotFoundException;
import lk.anjula.hotelreservationsystem.model.*;
import lk.anjula.hotelreservationsystem.repository.*;
import lk.anjula.hotelreservationsystem.service.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
public class ReservationServiceImpl implements ReservationService {
    @Autowired
    private ReservationRepository reservationRepository;
    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private RoomRepository roomRepository;
    @Autowired
    private BillingRepository billingRepository;
    @Autowired
    private BlockBookingRepository blockBookingRepository;

    @Override
    public ReservationResponse createReservation(ReservationRequest request) {
        Customer customer = customerRepository.findById(request.getCustomerId())
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found with id: " + request.getCustomerId()));
        Room room = roomRepository.findById(request.getRoomId())
                .orElseThrow(() -> new ResourceNotFoundException("Room not found with id: " + request.getRoomId()));
        if (!room.getIsAvailable()) {
            throw new ReservationException("Room is not available");
        }
        if (request.getOccupants() > room.getMaxOccupants()) {
            throw new ReservationException("Exceeds room occupant limit");
        }

        Reservation reservation = new Reservation();
        reservation.setCustomer(customer);
        reservation.setRoom(room);
        reservation.setCheckInDate(request.getCheckInDate());
        reservation.setCheckOutDate(request.getCheckOutDate());
        reservation.setOccupants(request.getOccupants());
        reservation.setReservationStatus(StringUtils.hasText(request.getCreditCardDetails()) ? ReservationStatus.CONFIRMED : ReservationStatus.PENDING);
        reservation.setCreditCardDetails(request.getCreditCardDetails());
        room.setIsAvailable(false);
        roomRepository.save(room);
        Reservation saved = reservationRepository.save(reservation);

        return mapToReservationResponse(saved);
    }

    @Override
    public ReservationResponse updateReservation(Long id, ReservationRequest request) {
        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Reservation not found with id: " + id));
        Room room = roomRepository.findById(request.getRoomId())
                .orElseThrow(() -> new ResourceNotFoundException("Room not found with id: " + request.getRoomId()));
        if (!room.getIsAvailable() && !room.getId().equals(reservation.getRoom().getId())) {
            throw new ReservationException("Room is not available");
        }
        if (request.getOccupants() > room.getMaxOccupants()) {
            throw new ReservationException("Exceeds room occupant limit");
        }

        reservation.getRoom().setIsAvailable(true);
        roomRepository.save(reservation.getRoom());
        reservation.setRoom(room);
        reservation.setCheckInDate(request.getCheckInDate());
        reservation.setCheckOutDate(request.getCheckOutDate());
        reservation.setOccupants(request.getOccupants());
        reservation.setCreditCardDetails(request.getCreditCardDetails());
        reservation.setReservationStatus(StringUtils.hasText(request.getCreditCardDetails()) ? ReservationStatus.CONFIRMED : ReservationStatus.PENDING);
        room.setIsAvailable(false);
        roomRepository.save(room);
        Reservation updated = reservationRepository.save(reservation);

        return mapToReservationResponse(updated);
    }

    @Override
    public void cancelReservation(Long id) {
        Reservation reservation = reservationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Reservation not found with id: " + id));
        reservation.setReservationStatus(ReservationStatus.CANCELLED);
        reservation.getRoom().setIsAvailable(true);
        roomRepository.save(reservation.getRoom());
        reservationRepository.save(reservation);
    }

    @Override
    public ReservationResponse checkIn(CheckInRequest request) {
        Reservation reservation = reservationRepository.findById(request.getReservationId())
                .orElseThrow(() -> new ResourceNotFoundException("Reservation not found with id: " + request.getReservationId()));
        if (reservation.getReservationStatus() != ReservationStatus.PENDING && reservation.getReservationStatus() != ReservationStatus.CONFIRMED) {
            throw new ReservationException("Invalid reservation status for check-in: " + reservation.getReservationStatus());
        }
        reservation.setReservationStatus(ReservationStatus.CHECKED_IN);
        Reservation updated = reservationRepository.save(reservation);
        return mapToReservationResponse(updated);
    }

    @Override
    public BillingResponse checkOut(CheckOutRequest request) {
        Reservation reservation = reservationRepository.findById(request.getReservationId())
                .orElseThrow(() -> new ResourceNotFoundException("Reservation not found with id: " + request.getReservationId()));
        if (reservation.getReservationStatus() != ReservationStatus.CHECKED_IN) {
            throw new ReservationException("Reservation not checked in");
        }
        Room room = reservation.getRoom();
        long nights = ChronoUnit.DAYS.between(reservation.getCheckInDate(), reservation.getCheckOutDate());
        double amount = nights * room.getPricePerNight();
        if (reservation.getCheckOutDate().isBefore(LocalDate.now())) {
            amount += room.getPricePerNight(); // Additional night charge for late checkout
        }
        amount += request.getOptionalCharges();

        Billing billing = new Billing();
        billing.setReservation(reservation);
        billing.setAmount(amount);
        billing.setPaymentMethod(PaymentMethod.valueOf(String.valueOf(request.getPaymentMethod())));
        billing.setBillingDate(LocalDate.now());
        billing.setBillingStatus(BillingStatus.PAID);
        billing.setOptionalCharges(request.getOptionalCharges());
        Billing savedBilling = billingRepository.save(billing);

        reservation.setReservationStatus(ReservationStatus.CHECKED_OUT);
        room.setIsAvailable(true);
        roomRepository.save(room);
        reservationRepository.save(reservation);

        return mapToBillingResponse(savedBilling);
    }

    @Scheduled(cron = "0 0 19 * * ?") // 7 PM daily
    public void handleNoShowsAndBilling() {
        LocalDate today = LocalDate.now();
        List<Reservation> pendingReservations = reservationRepository.findByReservationStatusAndCheckInDate(ReservationStatus.PENDING, today);
        for (Reservation reservation : pendingReservations) {
            reservation.setReservationStatus(ReservationStatus.CANCELLED);
            reservation.getRoom().setIsAvailable(true);
            roomRepository.save(reservation.getRoom());
            reservationRepository.save(reservation);
        }

        List<Reservation> noShowReservations = reservationRepository.findByReservationStatusAndCheckInDate(ReservationStatus.CONFIRMED, today);
        for (Reservation reservation : noShowReservations) {
            Billing billing = new Billing();
            billing.setReservation(reservation);
            billing.setAmount(reservation.getRoom().getPricePerNight());
            billing.setPaymentMethod(PaymentMethod.CREDIT_CARD);
            billing.setBillingDate(today);
            billing.setBillingStatus(BillingStatus.PENDING);
            billing.setOptionalCharges(0.0);
            billingRepository.save(billing);
            reservation.setReservationStatus(ReservationStatus.CANCELLED);
            reservation.getRoom().setIsAvailable(true);
            roomRepository.save(reservation.getRoom());
            reservationRepository.save(reservation);
        }
    }

    @Override
    public void createBlockBooking(BlockBookingRequest request) {
        List<Room> availableRooms = roomRepository.findByIsAvailableTrueAndRoomType(RoomType.STANDARD);
        if (availableRooms.size() < request.getNumberOfRooms()) {
            throw new ReservationException("Not enough available rooms for block booking");
        }
        BlockBooking blockBooking = new BlockBooking();
        blockBooking.setTravelCompanyName(request.getTravelCompanyName());
        blockBooking.setNumberOfRooms(request.getNumberOfRooms());
        blockBooking.setDiscountedRate(request.getDiscountedRate());
        blockBooking.setStartDate(request.getStartDate());
        blockBooking.setEndDate(request.getEndDate());
        blockBookingRepository.save(blockBooking);

        for (int i = 0; i < request.getNumberOfRooms(); i++) {
            Room room = availableRooms.get(i);
            room.setIsAvailable(false);
            roomRepository.save(room);
        }
    }

    @Override
    public Page<ReservationResponse> getAllReservations(String status, Long customerId, LocalDate startDate, LocalDate endDate, Pageable pageable) {
        ReservationStatus reservationStatus = null;
        if (StringUtils.hasText(status)) {
            try {
                reservationStatus = ReservationStatus.valueOf(status.toUpperCase());
            } catch (IllegalArgumentException e) {
                throw new ReservationException("Invalid reservation status: " + status);
            }
        }
        Page<Reservation> reservations = reservationRepository.findByFilters(reservationStatus, customerId, startDate, endDate, pageable);
        return reservations.map(this::mapToReservationResponse);
    }

    private ReservationResponse mapToReservationResponse(Reservation reservation) {
        ReservationResponse response = new ReservationResponse();
        response.setId(reservation.getId());
        response.setCustomerId(reservation.getCustomer().getId());
        response.setRoomId(reservation.getRoom().getId());
        response.setCheckInDate(reservation.getCheckInDate());
        response.setCheckOutDate(reservation.getCheckOutDate());
        response.setOccupants(reservation.getOccupants());
        response.setReservationStatus(reservation.getReservationStatus());
        return response;
    }

    private BillingResponse mapToBillingResponse(Billing billing) {
        BillingResponse response = new BillingResponse();
        response.setId(billing.getId());
        response.setReservationId(billing.getReservation().getId());
        response.setAmount(billing.getAmount());
        response.setPaymentMethod(String.valueOf(billing.getPaymentMethod()));
        response.setBillingDate(billing.getBillingDate());
        response.setStatus(String.valueOf(billing.getBillingStatus()));
        response.setOptionalCharges(billing.getOptionalCharges());
        return response;
    }
}
