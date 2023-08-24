package com.springapp.order;

import static org.junit.jupiter.api.Assertions.assertEquals;
import com.springapp.order.controller.OrdersController;
import com.springapp.order.model.Orders;
import com.springapp.order.model.Person;
import com.springapp.order.repository.PersonRepository;
import com.springapp.order.services.OrderService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class OrdersControllerTest {

    @InjectMocks
    private OrdersController ordersController;

    @Mock
    private PersonRepository personRepository;

    @Mock
    private OrderService orderService;

    @Mock
    private Model model;

    @Mock
    private BindingResult bindingResult;

    @Test
    public void testAddOrder() throws Exception {

        MockitoAnnotations.initMocks(this);
        MockMvc mockMvc = MockMvcBuilders.standaloneSetup(ordersController).build();


        SecurityContext securityContext = mock(SecurityContext.class);
        SecurityContextHolder.setContext(securityContext);


        Authentication authentication = mock(Authentication.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn("testUser");


        when(personRepository.findIdByUserName("testUser")).thenReturn(Optional.of(new Person()));


        MvcResult result = mockMvc.perform(post("/create-order")
                .param("specialization", "Test Specialization")
                .param("description", "Test Description"))
                .andExpect(status().is3xxRedirection())
                .andReturn();


        verify(personRepository, times(1)).findIdByUserName("testUser");
        verify(orderService, times(1)).addOrder(any(Orders.class));
        verify(model, times(1)).addAttribute(eq("order"), any(Orders.class));

        MockHttpServletResponse response = result.getResponse();
        assertEquals(302, response.getStatus());
        assertEquals("/", response.getRedirectedUrl());
    }
}