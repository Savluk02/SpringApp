package com.springapp.order.services;

import com.springapp.order.model.Orders;
import com.springapp.order.model.Person;
import com.springapp.order.repository.OrderRepository;
import com.springapp.order.repository.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class OrderService {

    private final OrderRepository orderRepository;

    @Autowired
    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Transactional
    public List<Orders> showAllOrders() {
        return orderRepository.findAll();
    }

    @Transactional
    public void addOrder(Orders orders) {
        orderRepository.save(orders);
    }


    public Orders getOrderById(int orderId) {
        return orderRepository.findById(orderId);

    }

    @Transactional
    public void deleteOrder(int id){
        orderRepository.deleteOrdersById(id);
    }



    public List<Orders> getOrdersCreatedByUser(Person user) {
        return orderRepository.findByUserId(user);
    }


}
