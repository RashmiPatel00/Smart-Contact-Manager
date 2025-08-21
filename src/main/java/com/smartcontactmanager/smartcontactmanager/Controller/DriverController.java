package com.smartcontactmanager.smartcontactmanager.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DriverController {

    // Handle driver page
    @GetMapping("/driverPage")
    public String driverPage() {
        return "driver/driverPage"; 
    }

    @GetMapping("/emergencyRides")
    public String showEmergencyRidesPage() {
        return "driver/emergencyRides"; // Path to the Thymeleaf template
    }
    
}