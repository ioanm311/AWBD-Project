package com.proiect.awbd.proiect_awbd.controller.view;

import com.proiect.awbd.proiect_awbd.dto.FeedbackDTO;
import com.proiect.awbd.proiect_awbd.exception.ResourceNotFoundException;
import com.proiect.awbd.proiect_awbd.service.FeedbackService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/feedback")
public class FeedbackViewController {

    private static final Logger logger = LoggerFactory.getLogger(FeedbackViewController.class);
    private final FeedbackService feedbackService;

    public FeedbackViewController(FeedbackService feedbackService) {
        this.feedbackService = feedbackService;
    }

    @GetMapping
    public String getAllFeedbacks(@RequestParam(defaultValue = "0") int page,
                                  @RequestParam(defaultValue = "5") int size,
                                  @RequestParam(defaultValue = "bookingId") String sortBy,
                                  @RequestParam(defaultValue = "asc") String direction,
                                  Model model) {

        logger.info("Loading paginated feedbacks: page {}, size {}, sorted by {}, direction {}", page, size, sortBy, direction);

        Sort sort = direction.equalsIgnoreCase("asc") ?
                Sort.by(sortBy).ascending() :
                Sort.by(sortBy).descending();

        Pageable pageable = PageRequest.of(page, size, sort);

        Page<FeedbackDTO> feedbackPage = feedbackService.getAllFeedbacksPaginated(pageable);

        model.addAttribute("feedbacks", feedbackPage.getContent());
        model.addAttribute("feedbackPage", feedbackPage);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", feedbackPage.getTotalPages());
        model.addAttribute("sortBy", sortBy);
        model.addAttribute("direction", direction);
        model.addAttribute("size", size);

        return "feedback/list";
    }

    @GetMapping("/add")
    public String showAddForm(Model model) {
        logger.info("Showing add feedback form");
        model.addAttribute("feedback", new FeedbackDTO());
        return "feedback/form";
    }

    @PostMapping("/save")
    public String saveFeedback(@ModelAttribute("feedback") @Valid FeedbackDTO dto,
                               @RequestParam(required = false, defaultValue = "false") boolean isEdit,
                               BindingResult result,
                               Model model) {
        logger.info("Saving feedback from view (edit: {}) for bookingId: {}", isEdit, dto.getBookingId());

        if (result.hasErrors()) {
            logger.warn("Validation errors occurred when saving feedback");
            return "feedback/form";
        }

        try {
            if (isEdit) {
                feedbackService.updateFeedback(dto.getBookingId(), dto);
            } else {
                feedbackService.saveFeedback(dto);
            }
            return "redirect:/feedback";
        } catch (ResourceNotFoundException | IllegalStateException ex) {
            logger.error("Error while saving feedback: {}", ex.getMessage());
            model.addAttribute("errorMessage", ex.getMessage());
            model.addAttribute("feedback", dto);
            return "feedback/form";
        }
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        logger.info("Showing edit form for feedback with id: {}", id);
        FeedbackDTO feedback = feedbackService.getFeedbackById(id);
        model.addAttribute("feedback", feedback);
        return "feedback/form";
    }

    @GetMapping("/details/{id}")
    public String viewDetails(@PathVariable Long id, Model model) {
        logger.info("Showing feedback details for id: {}", id);
        FeedbackDTO feedback = feedbackService.getFeedbackById(id);
        model.addAttribute("feedback", feedback);
        return "feedback/details";
    }
}