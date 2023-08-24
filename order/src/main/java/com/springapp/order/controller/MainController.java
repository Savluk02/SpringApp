package com.springapp.order.controller;

import com.springapp.order.model.Orders;
import com.springapp.order.model.Person;
import com.springapp.order.repository.PersonRepository;
import com.springapp.order.services.OrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

@Controller
public class MainController {

    private final PersonRepository personRepository;
    private final OrderService orderService;
    private final SimpMessagingTemplate messagingTemplate;

    private static final Logger log = LoggerFactory.getLogger(MainController.class);


    @Autowired
    public MainController(PersonRepository personRepository, OrderService orderService, SimpMessagingTemplate messagingTemplate) {
        this.personRepository = personRepository;
        this.orderService = orderService;
        this.messagingTemplate = messagingTemplate;
    }


    @GetMapping("/")
    public String showOrder(@ModelAttribute("order")Orders orders, Model model){
        log.info("Request to the main page.");
        model.addAttribute("order", orderService.showAllOrders());
        return "index";
    }
    @MessageMapping("/update-orders")
    public void updateOrders() {
        List<Orders> orders = orderService.showAllOrders();
        messagingTemplate.convertAndSend("/topic/orders", orders);
    }

    @PostMapping(value = "/create-order")
    public String addOrder(@RequestParam("specialization") String specialization, @RequestParam("description") String description,
                         @ModelAttribute("order") Orders orders){

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        String currentUserId = authentication.getName();

        personRepository.findIdByUserName(currentUserId).ifPresent(orders::setUserId);

        log.info("New order added. But no updates");
        Date currentTime = new Date();
        Timestamp timestamp = new Timestamp(currentTime.getTime());
        orders.setCreationTime(timestamp);
        orders.setStatus("Waiting");
        orders.setDescription(description);
        orders.setSpecialization(specialization);
        orderService.addOrder(orders);

        log.info("Sending order update to /topic/orders");
        log.info("New order added. Id:{}, Specialization: {}, Description: {}, User: {}", orders.getId(), specialization, description, currentUserId);
        messagingTemplate.convertAndSend("/topic/orders", orders);

        log.info("New order added.Id:{}, Specialization: {}, Description: {}, User: {}", orders.getId(), specialization, description, currentUserId);
        return "redirect:/";

    }

}
