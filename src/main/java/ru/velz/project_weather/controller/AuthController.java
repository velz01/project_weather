package ru.velz.project_weather.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.velz.project_weather.dao.RegistrationDao;
import ru.velz.project_weather.model.User;

@Controller
public class AuthController {

    private final RegistrationDao registrationDao;

    @Autowired
    public AuthController(RegistrationDao registrationDao) {
        this.registrationDao = registrationDao;
    }

    @GetMapping("/registration")
    public String showRegistrationPage(Model model) {
        model.addAttribute("user", new User());

        return "sign-up";
    }

    @PostMapping("/registration")
    public String registerUser(@ModelAttribute("user") @Valid User user, BindingResult bindingResult,
                               @RequestParam("repeatPassword") String repeatedPassword) {
        if (!user.getPassword().equals(repeatedPassword)) {
           bindingResult.addError(new ObjectError("password","Password should be not different"));
        }
        System.out.println(user);
        if (bindingResult.hasErrors()) {
            return "sign-up";
        }

        registrationDao.createUser(user);

        return "redirect:/index";
    }
}
