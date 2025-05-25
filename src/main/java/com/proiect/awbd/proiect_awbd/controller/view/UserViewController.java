package com.proiect.awbd.proiect_awbd.controller.view;

import com.proiect.awbd.proiect_awbd.dto.UserDTO;
import com.proiect.awbd.proiect_awbd.dto.UserRequestDTO;
import com.proiect.awbd.proiect_awbd.service.UserService;
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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/users")
public class UserViewController {

    private static final Logger logger = LoggerFactory.getLogger(UserViewController.class);
    private final UserService userService;

    public UserViewController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public String listUsers(@RequestParam(defaultValue = "0") int page,
                            @RequestParam(defaultValue = "5") int size,
                            @RequestParam(defaultValue = "username") String sortBy,
                            @RequestParam(defaultValue = "asc") String direction,
                            Model model) {

        logger.info("Displaying users in view with pagination and sorting: page={}, size={}, sortBy={}, direction={}", page, size, sortBy, direction);

        Sort sort = direction.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<UserDTO> userPage = userService.getAllUsersPaginated(pageable);

        model.addAttribute("userPage", userPage);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", userPage.getTotalPages());
        model.addAttribute("sortBy", sortBy);
        model.addAttribute("direction", direction);
        model.addAttribute("size", size);

        return "users/list";
    }

    @GetMapping("/add")
    public String showAddForm(Model model) {
        logger.info("Displaying user add form");
        model.addAttribute("user", new UserRequestDTO());
        return "users/add";
    }

    @PostMapping("/add")
    public String addUser(@Valid @ModelAttribute("user") UserRequestDTO userRequestDTO,
                          BindingResult result, Model model) {
        if (result.hasErrors()) {
            logger.warn("Validation errors occurred while adding user");
            return "users/add";
        }
        logger.info("Adding user via view: {}", userRequestDTO.getUsername());
        userService.saveUser(userRequestDTO);
        return "redirect:/users";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        logger.info("Displaying edit form for user with id: {}", id);
        var user = userService.getUserById(id);
        UserRequestDTO dto = new UserRequestDTO();
        dto.setUsername(user.getUsername());
        dto.setEmail(user.getEmail());
        dto.setRole(user.getRole());
        model.addAttribute("userId", id);
        model.addAttribute("user", dto);
        return "users/edit";
    }

    @PostMapping("/edit/{id}")
    public String editUser(@PathVariable Long id,
                           @Valid @ModelAttribute("user") UserRequestDTO userRequestDTO,
                           BindingResult result, Model model) {
        if (result.hasErrors()) {
            logger.warn("Validation errors occurred while editing user with id: {}", id);
            model.addAttribute("userId", id);
            return "users/edit";
        }
        logger.info("Updating user via view with id: {}", id);
        userService.updateUser(id, userRequestDTO);
        return "redirect:/users";
    }

    @PostMapping("/delete/{id}")
    public String deleteUser(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        logger.info("Attempting to delete user via view with id: {}", id);
        try {
            userService.deleteUser(id);
            return "redirect:/users";
        } catch (Exception ex) {
            String errorMessage = "Cannot delete user with active bookings";
            logger.error("Error deleting user with id {}: {}", id, ex.getMessage());
            redirectAttributes.addFlashAttribute("errorMessage", errorMessage);
            return "redirect:/users";
        }
    }
}