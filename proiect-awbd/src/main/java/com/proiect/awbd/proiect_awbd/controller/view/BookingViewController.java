package com.proiect.awbd.proiect_awbd.controller.view;

import com.proiect.awbd.proiect_awbd.dto.BookingRequestDTO;
import com.proiect.awbd.proiect_awbd.dto.BookingResponseDTO;
import com.proiect.awbd.proiect_awbd.service.BookingService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/bookings")
public class BookingViewController {

    private final BookingService bookingService;

    private static final Logger logger = LoggerFactory.getLogger(BookingViewController.class);

    public BookingViewController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @GetMapping
    public String listBookings(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(defaultValue = "bookingId") String sortBy,
            @RequestParam(defaultValue = "asc") String direction,
            Model model) {

        Page<BookingResponseDTO> bookingPage = bookingService.getAllBookingsPaginated(page, size, sortBy, direction);

        model.addAttribute("bookings", bookingPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", bookingPage.getTotalPages());
        model.addAttribute("size", size);
        model.addAttribute("sortBy", sortBy);
        model.addAttribute("direction", direction);

        return "bookings/list";
    }

    @GetMapping("/create")
    public String showCreateForm(Model model) {
        logger.info("Showing booking creation form");
        model.addAttribute("booking", new BookingRequestDTO());
        return "bookings/form";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        logger.info("Showing edit form for booking with id: {}", id);
        BookingResponseDTO dto = bookingService.getBookingById(id);
        BookingRequestDTO form = new BookingRequestDTO();

        form.setBookingId(dto.getBookingId());
        form.setDate(dto.getDate());
        form.setStartTime(dto.getStartTime());
        form.setEndTime(dto.getEndTime());

        form.setUserId(dto.getUserId());
        form.setRoomId(dto.getRoomId());
        form.setEventTypeId(dto.getEventTypeId());
        form.setEquipmentIds(dto.getEquipmentIds());

        model.addAttribute("booking", form);
        return "bookings/form";
    }


    @PostMapping("/save")
    public String saveBooking(@Valid @ModelAttribute("booking") BookingRequestDTO booking,
                              BindingResult result,
                              RedirectAttributes redirectAttributes,
                              Model model) {
        logger.info("Saving booking: {}", booking);
        if (result.hasErrors()) {
            logger.warn("Validation errors while saving booking: {}", result.getAllErrors());
            return "bookings/form";
        }

        try {
            if (booking.getBookingId() == null) {
                logger.info("Creating new booking");
                bookingService.createBooking(booking);
            } else {
                logger.info("Updating booking with id: {}", booking.getBookingId());
                bookingService.updateBooking(booking.getBookingId(), booking);
            }
            return "redirect:/bookings";
        } catch (RuntimeException ex) {
            logger.error("Error while saving booking: {}", ex.getMessage(), ex);
            model.addAttribute("errorMessage", ex.getMessage());
            return "bookings/form";
        }
    }

    @GetMapping("/delete/{id}")
    public String deleteBooking(@PathVariable Long id) {
        logger.info("Deleting booking with id: {}", id);
        bookingService.deleteBooking(id);
        return "redirect:/bookings";
    }
}