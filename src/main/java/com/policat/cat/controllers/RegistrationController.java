package com.policat.cat.controllers;

import com.policat.cat.dtos.UserRegistrationDTO;
import com.policat.cat.entities.User;
import com.policat.cat.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.validation.Valid;

@Controller
@RequestMapping("/register")
public class RegistrationController {
    @Autowired
    private UserService userService;

    @RequestMapping(method = RequestMethod.GET)
    public String showRegistrationForm(UserRegistrationDTO userRegistrationDTO) {
        return "register";
    }

    @RequestMapping(method = RequestMethod.POST)
    public String registerUser(@Valid UserRegistrationDTO userRegistrationDTO, BindingResult bindingResult) {
        if (!bindingResult.hasErrors()) {
            User newUser = userService.registerUserAccount(userRegistrationDTO);
            if (newUser == null) {
                bindingResult.rejectValue("username", "message.regError");
            }
        }

        if (bindingResult.hasErrors()) {
            return "register";
        }

        return "register_success";
    }
}
