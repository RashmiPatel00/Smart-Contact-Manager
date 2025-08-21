package com.smartcontactmanager.smartcontactmanager.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.smartcontactmanager.smartcontactmanager.Entities.Role;
import com.smartcontactmanager.smartcontactmanager.Entities.User;
import com.smartcontactmanager.smartcontactmanager.Helper.Message;
import com.smartcontactmanager.smartcontactmanager.dao.UserRepository;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

@Controller
public class HomeController {

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;
    
    @Autowired
    private UserRepository userRepository;

    // Home page
    @RequestMapping("/")
    public String home(Model model) {
        model.addAttribute("title", "Home-Smart Contact Manager");
        return "home";
    }

    // About page
    @RequestMapping("/about")
    public String about(Model model) {
        model.addAttribute("title", "About-Smart Contact Manager");
        return "about";
    }

    // Signup page
    @RequestMapping("/signup")
    public String signup(Model model) {
        model.addAttribute("title", "Signup-Smart Contact Manager");
        model.addAttribute("user", new User());
        return "signup";
    }

    // Registration handler
    @RequestMapping(value = "/do_register", method = RequestMethod.POST)
    public String registerUser(@Valid @ModelAttribute("user") User user,
                               BindingResult result1,
                               @RequestParam(value = "agreement", defaultValue = "false") boolean agreement,
                               @RequestParam(value = "role", required = true) String role, // Required role parameter
                               Model model,
                               HttpSession session) {

        try {
            // Agreement validation
            if (!agreement) {
                throw new Exception("You have not agreed to the terms and conditions.");
            }

            // Validation result check
            if (result1.hasErrors()) {
                model.addAttribute("user", user);
                return "signup";
            }

            // Validate the role
            try {
                Role userRole = Role.valueOf(role);  // Convert string role to Role enum
                user.setRole(userRole);
            } catch (IllegalArgumentException e) {
                session.setAttribute("message", new Message("Invalid role selected", "alert-danger"));
                model.addAttribute("user", user);
                return "signup";
            }

            // Set default user properties
            user.setEnabled(true);
            user.setImageUrl("default.png");
            user.setPassword(passwordEncoder.encode(user.getPassword())); // Encode password

            // Save the user to the database
            userRepository.save(user);

            // Clear the user form
            model.addAttribute("user", new User());

            // Set success message in session
            session.setAttribute("message", new Message("Successfully registered", "alert-success"));
            return "signup"; // Redirect back to signup page with success message

        } catch (Exception e) {
            // Exception handling
            e.printStackTrace();
            model.addAttribute("user", user);
            session.setAttribute("message", new Message("Something went wrong: " + e.getMessage(), "alert-danger"));
            return "signup";
        }
    }

    // Handler for custom Login
    @GetMapping("/signin")
    public String customLogin(Model model) {
        model.addAttribute("title", "Login Page");
        return "login";
    }
}
