package com.artechel.aplikacja.controllers;

import com.artechel.aplikacja.DAO.*;
import com.artechel.aplikacja.model.Invoice;
import com.artechel.aplikacja.model.Payer;
import com.artechel.aplikacja.model.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Controller
public class MainController {

    @Autowired
    ProductDAO productDAO;

    @Autowired
    InvoiceDAO invoiceDAO;

    @Autowired
    Invoice_productDAO invoice_productDAO;

    @Autowired
    CustomerDAO customerDAO;

    @Autowired
    PayerDAO payerDAO;


    @RequestMapping(value = "/main", method = RequestMethod.GET)
    public ModelAndView loadMain(ModelAndView model) {
        List<Invoice> listInv = invoiceDAO.listInvoices();
        model.addObject("finalList", listInv);
        model.setViewName("main");

        return model;
    }

    @RequestMapping(value = "/searchInvoices", method = RequestMethod.GET)
    public ModelAndView szukaj(ModelAndView model, @RequestParam(name = "numer") String number, @RequestParam(name = "dataAfter") String dateAfter, @RequestParam("dataB4") String dateBefore, @RequestParam("kontrahent") String customer) throws IOException {
        List<Invoice> list = invoiceDAO.listByAllparameters(number, dateAfter, dateBefore, customer);
        System.out.println(list);
        model.addObject("finalList", list);
        model.setViewName("main");
        return model;
    }

    @RequestMapping(value = "/openInEdit", method = RequestMethod.POST)
    public ModelAndView openInEdit(ModelAndView model, @RequestParam("invoiceNumber") String invoiceNo) throws UnsupportedEncodingException {
        Payer payerObj;
        Invoice thisInvoice = invoiceDAO.findInvoicebyNumber(invoiceNo.trim());
        String p = thisInvoice.getPayer().getName();
        if(p==null){
            payerObj = null;
        } else {
            payerObj=payerDAO.findPayerbyId(thisInvoice.getPayer().getPayer_id());
        }

        List<Product> list = productDAO.listProducts();
        model.addObject("productList", list);
        model.addObject("invoice", thisInvoice);
        model.addObject("listProducts", invoice_productDAO.findInvoice_productbyInvoiceId(thisInvoice.getInvoice_id()));
        model.addObject("customer", customerDAO.findCustomerbyId(thisInvoice.getCustomer().getCustomer_id()));
        model.addObject("payer", payerObj);
        model.setViewName("editInvoice");

        return model;
    }

    @RequestMapping(value = "/openInCorrect", method = RequestMethod.POST)
    public ModelAndView openInCorrect(ModelAndView model, HttpServletRequest request) throws UnsupportedEncodingException {
        request.setCharacterEncoding("UTF-8");
        Payer payerObj;
        String invoiceNo = request.getParameter("number");
        Invoice thisInvoice = invoiceDAO.findInvoicebyNumber(invoiceNo.trim());
        String p = thisInvoice.getPayer().getName();
        if(p==null){
            payerObj = null;
        } else {
            payerObj=payerDAO.findPayerbyId(thisInvoice.getPayer().getPayer_id());
        }

        List<Product> list = productDAO.listProducts();
        model.addObject("productList", list);
        model.addObject("invoice", thisInvoice);
        model.addObject("listProducts", invoice_productDAO.findInvoice_productbyInvoiceId(thisInvoice.getInvoice_id()));
        model.addObject("customer", customerDAO.findCustomerbyId(thisInvoice.getCustomer().getCustomer_id()));
        model.addObject("payer", payerObj);
        model.setViewName("correctInvoice");

        return model;
    }

    @RequestMapping(value = "/generateReport", method = RequestMethod.GET)
    public ModelAndView report(ModelAndView model, @RequestParam(name = "numer") String number, @RequestParam(name = "dataAfter") String dateAfter, @RequestParam("dataB4") String dateBefore, @RequestParam("kontrahent") String customer){
        List<Invoice> list = invoiceDAO.listByAllparameters(number, dateAfter, dateBefore, customer);
        System.out.println(list);

        model.addObject("invoicesList", list);
        model.setViewName("reportPDF");
        return model;
    }

}