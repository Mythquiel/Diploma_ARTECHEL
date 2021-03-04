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
public class ContactController {

    @Autowired
    EmailService emailService;

    @GetMapping("/contact")
    public String uzytkownicy(ModelMap model) {
        model.getAttribute("username");
        return "contact";
    }


    @RequestMapping(value = "/sendMail", method = RequestMethod.POST)
    public ModelAndView sendMail(ModelAndView model, HttpServletRequest request) throws UnsupportedEncodingException {
        request.setCharacterEncoding("UTF-8");

        String body = request.getParameter("body");
        String header = request.getParameter("header");
        String name = request.getParameter("name");

        emailService.sendContactMail("ma.switala@gmail.com", header, body+"\n-----------\n"+name);

        model.addObject("message","Wiadomość została wysłana");
        model.setViewName("contact");
        return model;
    }
}
