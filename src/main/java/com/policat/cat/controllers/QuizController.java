package com.policat.cat.controllers;

import com.policat.cat.dtos.UserRegistrationDTO;
import com.policat.cat.repositories.QuizRepository;
import com.policat.cat.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.validation.Valid;

@Controller
@RequestMapping("/quiz")
public class QuizController {
    @Autowired
    private UserService userService;

    @Autowired QuizRepository quizRepository;

    @RequestMapping(method = RequestMethod.GET)
    public String showRegistrationForm(Model model) {
        model.addAttribute("quizzes", quizRepository.findAll());
        return "quiz_index";
    }
}
