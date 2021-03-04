package com.artechel.aplikacja.controllers;

import com.artechel.aplikacja.DAO.CustomerDAO;
import com.artechel.aplikacja.model.Customer;
import com.artechel.aplikacja.model.Invoice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
public class CustomerController {

    @Autowired
    CustomerDAO customerDAO;

    @RequestMapping(value = "/customers", method = RequestMethod.GET)
    public ModelAndView uzytkownicy(ModelAndView model) {

        List<Customer> list = customerDAO.listCustomers();
        model.addObject("finalList", list);
        model.setViewName("customers");

        return model;
    }

    @RequestMapping(value = "/searchCustomers", method = RequestMethod.GET)
    public ModelAndView listCustomersByParameters(ModelAndView model, @RequestParam("nazwa") String name, @RequestParam("nip") String nip){
        List<Customer> list = customerDAO.listCustomerbyParameters(name,nip);
        model.addObject("finalList",list);
        model.setViewName("customers");
        return model;
    }
}