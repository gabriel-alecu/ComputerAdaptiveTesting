package com.policat.cat.controllers;

import com.policat.cat.dtos.UserRegistrationDTO;
import com.policat.cat.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.context.request.WebRequest;

import javax.validation.Valid;

@Controller
@RequestMapping("/register")
public class RegistrationController {
    @Autowired
    private UserService userService;

    @RequestMapping(method = RequestMethod.GET)
    public String showRegistrationForm() {
        return "register";
    }

    @RequestMapping(method=RequestMethod.POST)
    public String checkPersonInfo(@Valid UserRegistrationDTO userRegistrationDTO, BindingResult bindingResult) {
        if (!bindingResult.hasErrors()) {
            userService.registerUserAccount(userRegistrationDTO);
        }

        if (bindingResult.hasErrors()) {
            return "register";
        }

        return "redirect:/register/success";
    }

    @RequestMapping(value="/success", method=RequestMethod.GET)
    public String checkPersonInfo() {
        return "register_success";
    }
}
