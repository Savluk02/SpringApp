package com.springapp.order.repository;

import com.springapp.order.model.Orders;
import com.springapp.order.model.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface OrderRepository extends JpaRepository<Orders, String > {

    Orders findById(int id);
    List<Orders> findByUserId(Person userId);
    void deleteOrdersById(int id);
}
