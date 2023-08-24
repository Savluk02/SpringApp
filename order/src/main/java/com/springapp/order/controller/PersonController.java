package com.springapp.order.controller;

import com.springapp.order.model.Person;
import com.springapp.order.repository.PersonRepository;
import com.springapp.order.services.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Optional;

@Controller
public class PersonController {

    private final PersonService personService;
    private final PersonRepository personRepository;


    @Autowired
    public PersonController(PersonService personService, PersonRepository personRepository) {
        this.personService = personService;
        this.personRepository = personRepository;
    }

    @GetMapping("/person")
    public String personPage(){
        return "person/index";
    }

    @GetMapping("/change-role")
    public String changeRole() {

        Person currentUser = getCurrentUser();

        currentUser.setUserRole("ROLE_PERSONAL");

        personService.saveUser(currentUser);

        return "redirect:/";
    }

    public Person getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        String userName = authentication.getName();
        System.out.println("Username = " + userName);

        Optional<Person> optionalUser = personRepository.findByUserName(userName);

        return optionalUser.orElse(null);
    }
}