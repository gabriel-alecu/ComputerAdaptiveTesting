package com.policat.cat.controllers;

import com.policat.cat.dtos.QuestionStatsDTO;
import com.policat.cat.entities.Domain;
import com.policat.cat.entities.Option;
import com.policat.cat.entities.Question;
import com.policat.cat.repositories.OptionRepository;
import com.policat.cat.repositories.QuestionRepository;
import com.policat.cat.repositories.DomainRepository;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/managedb")
public class DbManagementController {
    @Autowired
    DomainRepository domainRepository;

    @Autowired
    QuestionRepository questionRepository;

    @Autowired
    OptionRepository optionRepository;


    @ModelAttribute("domains")
    public List<Domain> getDomains() {
        return (List<Domain>) domainRepository.findAll();
    }

    @ModelAttribute("question")
    public Question newQuestion() {
        return new Question();
    }


    @RequestMapping(method = RequestMethod.GET)
    public String showDomains() {
        return "manage_db_domains";
    }

    @RequestMapping(value="/domain/rename", method = RequestMethod.POST)
    public String saveChangedDomains(@Valid Domain domain, BindingResult bindingResult) {
        if(!bindingResult.hasErrors()) {
            domainRepository.save(domain);
        }
        return "redirect:/managedb";
    }

    @RequestMapping(value="/domain/add", method = RequestMethod.POST)
    public String addDomain(@Valid Domain domain, BindingResult bindingResult) {
        if(!bindingResult.hasErrors()) {
            domainRepository.save(domain);
        }
        return "redirect:/managedb";
    }

    @RequestMapping(value="/domain/{id}/delete", method = RequestMethod.GET)
    public String deleteDomain(@PathVariable Long id) {
        domainRepository.delete(id);
        return "redirect:/managedb";
    }


    @RequestMapping(value="/domain/{id}", method = RequestMethod.GET)
    public String viewQuestions(@PathVariable Long id, Model model) {
        Domain domain = domainRepository.findOne(id);
        List<QuestionStatsDTO> questionsStats = new ArrayList<>();
        List<Object[]> questionsStatsRows = questionRepository.getWithStatsByDomain(domain);
        for(Object[] row : questionsStatsRows) {
            QuestionStatsDTO questionStatsDTO = new QuestionStatsDTO();
            questionStatsDTO.setQuestion((Question) row[0]);
            questionStatsDTO.setNumOptions((Long) row[1]);
            questionStatsDTO.setNumCorrectOptions((Long) row[2]);
            questionsStats.add(questionStatsDTO);
        }
        model.addAttribute(domain);
        model.addAttribute("questionsStats", questionsStats);
        return "manage_db_questions";
    }

    @RequestMapping(value="/domain/{id}/add_question", method = RequestMethod.POST)
    public String addQuestion(@PathVariable Long id, @Valid Question question, BindingResult bindingResult) {
        if(!bindingResult.hasErrors()) {
            Domain domain = domainRepository.findOne(id);
            question.setDomain(domain);
            question = questionRepository.save(question);
            domain.addQuestion(question);
            domainRepository.save(domain);
            return "redirect:/managedb/question/"+question.getId().toString();
        }
        return "redirect:/managedb/domain/"+id.toString();
    }

    @RequestMapping(value="/question/{id}/delete", method = RequestMethod.GET)
    public String deleteQuestion(@PathVariable Long id, Model model) {
        Question question = questionRepository.findOne(id);
        Domain domain = question.getDomain();
        domain.removeQuestion(question);
        domainRepository.save(domain);
        questionRepository.delete(question);
        return "redirect:/managedb/domain/"+domain.getId().toString();
    }


    @RequestMapping(value="/question/{id}", method = RequestMethod.GET)
    public String viewQuestion(@PathVariable Long id, Model model) {
        Question question = questionRepository.findOne(id);
        Hibernate.initialize(question.getOptions());
        model.addAttribute(question);
        return "manage_db_question";
    }

    @RequestMapping(value="/question/change", method = RequestMethod.POST)
    public String updateQuestion(@Valid Question question, BindingResult bindingResult, Model model) {
        Question dbQuestion = questionRepository.findOne(question.getId());
        dbQuestion.setText(question.getText());
        dbQuestion.setScore(question.getScore());
        if(!bindingResult.hasErrors()) {
            questionRepository.save(dbQuestion);
        }
        return "redirect:/managedb/question/"+question.getId().toString();
    }

    @RequestMapping(value="/question/{id}/add_option", method = RequestMethod.POST)
    public String addOption(@PathVariable Long id, @Valid Option option, BindingResult bindingResult) {
        if(!bindingResult.hasErrors()) {
            Question question = questionRepository.findOne(id);
            Option dbOption = new Option();
            dbOption.setText(option.getText());
            dbOption.setCorrect(option.getCorrect());
            dbOption.setQuestion(question);
            dbOption = optionRepository.save(dbOption);
            question.addOption(dbOption);
            questionRepository.save(question);
        }
        return "redirect:/managedb/question/"+id.toString();
    }

    @RequestMapping(value="/option/{id}/delete", method = RequestMethod.GET)
    public String deleteOption(@PathVariable Long id, Model model) {
        Option option = optionRepository.findOne(id);
        Question question = option.getQuestion();
        question.removeOption(option);
        questionRepository.save(question);
        optionRepository.delete(option);
        return "redirect:/managedb/question/"+question.getId().toString();
    }

    @RequestMapping(value="/option/change", method = RequestMethod.POST)
    public String updateOption(@Valid Option option, BindingResult bindingResult, Model model) {
        Option dbOption = optionRepository.findOne(option.getId());
        dbOption.setText(option.getText());
        dbOption.setCorrect(option.getCorrect());
        if(option.getCorrect() == null) {
            dbOption.setCorrect(false);
        }
        if(!bindingResult.hasErrors()) {
            optionRepository.save(dbOption);
        }
        return "redirect:/managedb/question/"+dbOption.getQuestion().getId().toString();
    }
}
