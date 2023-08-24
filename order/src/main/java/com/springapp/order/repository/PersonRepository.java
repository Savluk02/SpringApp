package com.springapp.order.repository;

import com.springapp.order.model.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PersonRepository extends JpaRepository<Person, Integer>{
    Optional<Person> findByUserName(String userName);
    Optional<Person> findIdByUserName(String userName);
}
