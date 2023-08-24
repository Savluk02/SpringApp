package com.springapp.order.controller;

import com.springapp.order.dto.OrderDto;
import com.springapp.order.model.Orders;
import com.springapp.order.model.Person;
import com.springapp.order.repository.OrderRepository;
import com.springapp.order.repository.PersonRepository;
import com.springapp.order.services.OrderService;
import com.springapp.order.services.PersonService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

@Controller
public class OrdersController {

    private final OrderService orderService;
    private final PersonService personService;
    private final OrderRepository orderRepository;

    private final SimpMessagingTemplate messagingTemplate;

    private static final Logger log = LoggerFactory.getLogger(MainController.class);

    @Autowired
    public OrdersController(OrderService orderService, PersonService personService, OrderRepository orderRepository, SimpMessagingTemplate messagingTemplate) {
        this.orderService = orderService;
        this.personService = personService;
        this.orderRepository = orderRepository;
        this.messagingTemplate = messagingTemplate;
    }

    @GetMapping("/order/{orderId}")
    public String showOrder(@PathVariable("orderId") int orderId, Model model, Principal principal) {
        Orders orders = orderService.getOrderById(orderId);
        model.addAttribute("order", orders);
        return "order/index";
    }

    @PostMapping("/order/{orderId}/take")
    @PreAuthorize("hasRole('ROLE_PERSONAL')")
    public String takeOrder(@PathVariable int orderId, Principal principal) {

        Person assignedTo = personService.getByUserName(principal.getName());

        Orders order = orderService.getOrderById(orderId);

        order.setAssignedTo(assignedTo);
        order.setStatus("Executed");

        orderRepository.save(order);
        OrderDto updateObject = new OrderDto(order.getStatus());

        log.info("UpdateStatus" + updateObject);
        messagingTemplate.convertAndSend("/topic/some-topic", updateObject);

        return "redirect:/order/{orderId}";
    }

    @GetMapping("/order/ownOrder")
    public String ownOrder(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            String username = authentication.getName();

            Person user = personService.getByUserName(username);

            List<Orders> userOrders = orderService.getOrdersCreatedByUser(user);
            model.addAttribute("userOrders", userOrders);
        }

        return "order/ownOrder";
    }

    @PostMapping("/order/ownOrder/{id}")
    public String deleteOrder(@PathVariable("id") int id){
        orderService.deleteOrder(id);
        return "redirect:/";
    }


}
