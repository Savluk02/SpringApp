package com.springapp.order.until;

import com.springapp.order.model.Person;
import com.springapp.order.security.PersonDetails;
import com.springapp.order.services.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class PersonValidator implements Validator {

    private final PersonService personService;

    @Autowired
    public PersonValidator(PersonService personService) {
        this.personService = personService;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return Person.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        Person person =(Person) target;

        try {
            personService.loadUserByUsername(person.getUserName());
        }catch (UsernameNotFoundException ignored){
            return;
        }
        errors.rejectValue("userName", "", "A person with such a name exists");
    }
}
