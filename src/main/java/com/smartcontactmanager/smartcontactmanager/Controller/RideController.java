package com.smartcontactmanager.smartcontactmanager.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.smartcontactmanager.smartcontactmanager.Entities.Ride;
import com.smartcontactmanager.smartcontactmanager.Entities.User;
import com.smartcontactmanager.smartcontactmanager.dao.RideRepository;
import com.smartcontactmanager.smartcontactmanager.dao.UserRepository;

@Controller
public class RideController {

    @Autowired
    private RideRepository rideRepository;

    @Autowired
    private UserRepository userRepository;

    @PostMapping("/schedule")
    public String scheduleRide(@ModelAttribute Ride ride, Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = authentication.getName(); // Get the logged-in user's email
    
        User user = userRepository.getUserByUserName(userEmail);
    
        if (user != null) {
            ride.setUser(user); // Set the user for the ride
            ride.setStatus("Pending"); // Set the status to Pending
            rideRepository.save(ride); // Save the ride to the database
    
            // Redirect to the ride details page with the ride's ID
            return "normal/success"; // Redirect to view rides after booking
        } else {
            model.addAttribute("error", "User not found");
            return "signup"; // Error page if user is not found
        }
    }
    @PostMapping("/user/searchResults")
    public String handleRideForm(@RequestParam String fromLocation,
                                 @RequestParam String toLocation,
                                 @RequestParam String date,
                                 @RequestParam String time,
                                 Model model) {
        // Add form data to the model
        model.addAttribute("fromLocation", fromLocation);
        model.addAttribute("toLocation", toLocation);
        model.addAttribute("date", date);
        model.addAttribute("time", time);

        // Redirect to search results page
        return "normal/searchResults";  // This matches the Thymeleaf template "searchResults.html"
    }
    
@GetMapping("/user/viewRides")
public String viewUserRides(Model model) {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    String userEmail = authentication.getName(); // Get logged-in user's email

    User user = userRepository.getUserByUserName(userEmail);

    if (user != null) {
        // Fetch rides for the logged-in user only
        List<Ride> userRides = rideRepository.findByUser(user); 
        model.addAttribute("rides", userRides);
        return "normal/view_ride"; // Points to templates/normal/view_ride.html
    } else {
        model.addAttribute("error", "User not found");
        return "error"; // Redirect to an error page if user is not found
    }
}

  

}
