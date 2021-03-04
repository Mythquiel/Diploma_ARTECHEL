package com.artechel.aplikacja.controllers;

import com.artechel.aplikacja.config.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;

@Controller
public class ForgottenPasswordController {

    @Autowired
    EmailService emailService;

    @GetMapping("/forgottenPassword")
    public String uzytkownicy(ModelMap model) {
        model.getAttribute("username");
        return "forgottenPassword";
    }


    @RequestMapping(value = "remindPassword", method = RequestMethod.POST)
    public ModelAndView remind(ModelAndView model, HttpServletRequest request) throws UnsupportedEncodingException {
        request.setCharacterEncoding("UTF-8");
        String username = request.getParameter("username");
        String email = request.getParameter("email");

        emailService.sendRemindPasswordEmail("Użytkownik "+username+" ("+email+") wysłał prośbę o zresetowanie hasła.");
        model.addObject("message","Prośba została wysłana");
        model.setViewName("forgottenPassword");
        return model;
    }
}
