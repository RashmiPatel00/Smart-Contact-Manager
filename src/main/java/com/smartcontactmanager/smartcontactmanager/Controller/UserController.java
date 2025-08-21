package com.smartcontactmanager.smartcontactmanager.Controller;

// import java.io.File;
// import java.nio.file.Files;
// import java.nio.file.Path;
// import java.nio.file.Paths;
// import java.nio.file.StandardCopyOption;
import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
// import org.springframework.web.bind.annotation.RequestParam;
// import org.springframework.web.multipart.MultipartFile;

import com.smartcontactmanager.smartcontactmanager.Entities.Contact;
import com.smartcontactmanager.smartcontactmanager.Entities.User;
import com.smartcontactmanager.smartcontactmanager.Helper.Message;
import com.smartcontactmanager.smartcontactmanager.dao.UserRepository;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserRepository userRepository;

    // This will run all the time along with user
    @ModelAttribute
    public void addCommonData(Model model, Principal principal) {
        String userName = principal.getName();
        System.out.println("UserName" + userName);

        // GET the user Details using User NAME(EMAIL)
        User user = userRepository.getUserByUserName(userName);
        System.out.println("USER" + user);
        model.addAttribute("user", user);

    }

    // DAshbord Home
    @RequestMapping("/index")
    public String dashboard(Model model, Principal principal) {

        return "/normal/user_dashboard";
    }
    @RequestMapping("/ai")
    public String dashboard2(Model model, Principal principal) {

        return "/normal/ai";
    }
    @RequestMapping("/index1")
    public String dashboard1(Model model, Principal principal) {

        return "/normal/user_dashboard1";
    }
    // open add form handler
    @GetMapping("/add-contact")
    public String openAddContactForm(Model model) {
        model.addAttribute("title", "Add Contact");
        model.addAttribute("contact", new Contact());
        return "normal/add_contact_form";
    }

    // Processing and adding contact form
   @PostMapping("/process-contact")
public String processContact(@ModelAttribute Contact contact,
                        
                             Principal principal,HttpSession session) {
    try {
        String name = principal.getName();
        User user = this.userRepository.getUserByUserName(name);

        // Handle file upload
        // if (file.isEmpty()) {
        //     contact.setImage("default.png"); // Fallback image
        // } else {
        //     String filename = file.getOriginalFilename();
        //     contact.setImage(filename);

        //     File saveFile = new ClassPathResource("static/image").getFile();
        //     Path path = Paths.get(saveFile.getAbsolutePath() + File.separator + filename);
        //     Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
        // }

        // Set user relationship
        contact.setUser(user);
        user.getContacts().add(contact);

        // Save the contact
        this.userRepository.save(user);

        System.out.println("DATA:" + contact);
        System.out.println("Added to database");

        //message successfully
        session.setAttribute("messsage",new Message("Your Contact is Added!! Add more", "success"));
         

    } catch (Exception e) {
        e.printStackTrace();
        //error message
        session.setAttribute("messsage",new Message("Something went wrong...!! TRY AGAIN!!", "danger"));
        return "normal/add_contact_form";
    }
    return "normal/add_contact_form";
}


}