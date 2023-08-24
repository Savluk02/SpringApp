package com.springapp.order.services;

import com.springapp.order.model.Person;
import com.springapp.order.repository.PersonRepository;
import com.springapp.order.security.PersonDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class PersonService implements UserDetailsService {

    private final PersonRepository personRepository;

    @Autowired
    public PersonService(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    @Override
    public PersonDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        Optional<Person> optionalUser = personRepository.findByUserName(userName);

        if(optionalUser.isEmpty())
            throw new UsernameNotFoundException ("User not found");

        return new PersonDetails(optionalUser.get());
    }

    @Transactional
    public Person getByUserName(String username) {

        Optional<Person> personOptional = personRepository.findByUserName(username);

        if (personOptional.isPresent()) {
            return personOptional.get();
        } else {
            throw new RuntimeException("User not found with username: " + username);
        }
    }
    public void saveUser(Person person) {
        personRepository.save(person);
    }

}
