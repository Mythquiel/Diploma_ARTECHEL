package com.artechel.aplikacja.controllers;

import com.artechel.aplikacja.DAO.InvoiceDAO;
import com.artechel.aplikacja.DAO.UsersDAO;
import com.artechel.aplikacja.model.Invoice;
import com.artechel.aplikacja.model.Users;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;
import java.util.List;

@Controller
public class LoginController {

    @Autowired
    InvoiceDAO invDAO;

    @Autowired
    UsersDAO usersDAO;

    @RequestMapping(value = "/logout", method = RequestMethod.GET)
    public String login(Model model) {
        return "login";
    }

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public ModelAndView loginToMain(ModelAndView model) throws IOException {
                List<Invoice> listInv = invDAO.listInvoices();
                model.addObject("finalList", listInv);
                model.setViewName("main");

        return model;
    }
}