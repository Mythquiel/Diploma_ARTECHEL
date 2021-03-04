package com.artechel.aplikacja.controllers;

import com.artechel.aplikacja.DAO.*;
import com.artechel.aplikacja.model.*;
import com.artechel.aplikacja.methods.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;


@Controller
public class NewInvoiceController {

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


    @RequestMapping(value = "/newInvoice", method = RequestMethod.GET)
    public ModelAndView load(ModelAndView model) {

        List<Product> list = productDAO.listProducts();
        model.addObject("productList", list);
        ArrayList<String> unitList = new ArrayList<>();
        unitList.add("szt.");
        unitList.add("mb.");

        ArrayList<Integer> listVat = new ArrayList<>();
        listVat.add(0);
        listVat.add(8);
        listVat.add(23);

        model.addObject("unit", unitList);
        model.addObject("vat", listVat);
        model.setViewName("newInvoice");
        return model;
    }

    @RequestMapping(value = "/print", method = RequestMethod.GET)
    public ModelAndView print(ModelAndView model, HttpServletRequest request) throws ParseException, UnsupportedEncodingException {
        request.setCharacterEncoding("UTF-8");
        Map<String, String[]> params = request.getParameterMap();

        if (!checkIfDataCorrect(request, params)) {
            model.setViewName("error");
        } else {

            boolean isLast = false;
            CreateInvoice methods = new CreateInvoice();

            String forma = request.getParameter("payment");



            String nettoSum = request.getParameter("nettoSuminv");
            String bruttoSum = request.getParameter("bruttoSuminv");

            String odbiorcaNIP = request.getParameter("nip");
            String odbiorcaNazwa = request.getParameter("odbiorca");
            String odbiorcaUlica = request.getParameter("street");
            String odbiorcaKodPocztowy = request.getParameter("postcode");
            String odbiorcaMiasto = request.getParameter("town");

            String platnikNIP = request.getParameter("nipPlatnik");
            String platnikNazwa = request.getParameter("platnik");
            String platnikUlica = request.getParameter("streetPlatnik");
            String platnikKodPocztowy = request.getParameter("postcodePlatnik");
            String platnikMiasto = request.getParameter("townPlatnik");

            Customer customerObj, customer;
            Payer payerObj, payer;
            Date dateCreated, datePrinted, dueDate;
            String thisInvoiceNumber, path;

            try {
                customer = customerDAO.findCustomerbyNIP(odbiorcaNIP);
            } catch (IndexOutOfBoundsException ie) {
                customer = null;
            }
            if (customer == null) {
                customerDAO.insertCustomer(odbiorcaNazwa, odbiorcaUlica, null, odbiorcaMiasto, odbiorcaKodPocztowy, odbiorcaNIP, null, null, null);
                customerObj = customerDAO.findCustomerbyNIP(odbiorcaNIP);
            } else {
                customerObj = customerDAO.findCustomerbyNIP(odbiorcaNIP);
            }

            dateCreated = methods.getToday();
            datePrinted = methods.getToday();
            List<Invoice> listInvoices = invoiceDAO.listInvoices();
            String lastInvoice;
            if (listInvoices.isEmpty()) {
                thisInvoiceNumber = methods.getCurrentInvoiceNumber("00/0000");
            } else {
                boolean isCorrect = false;
                int listPointer = 1;
                lastInvoice = null;
                while (!isCorrect) {
                    if ((listInvoices.get(listInvoices.size() - listPointer).getInvoice_number()).contains("/zapis") || (listInvoices.get(listInvoices.size() - listPointer).getInvoice_number()).contains("KFV")) {
                        if ((listInvoices.size() - listPointer) == 0) {
                            lastInvoice = "00/0000";
                            isCorrect = true;
                        }
                        listPointer++;
                    } else {
                        lastInvoice = listInvoices.get(listInvoices.size() - listPointer).getInvoice_number();
                        isCorrect = true;
                    }
                }
                thisInvoiceNumber = methods.getCurrentInvoiceNumber(lastInvoice);
            }

            if (!platnikNIP.trim().isEmpty()) {
                try {
                    payer = payerDAO.findPayerbyNIP(platnikNIP);
                } catch (IndexOutOfBoundsException ie) {
                    payer = null;
                }

                if (payer == null) {
                    payerDAO.insertPayer(platnikNazwa, platnikUlica, null, platnikMiasto, platnikKodPocztowy, platnikNIP, null, null, null);
                    payerObj = payerDAO.findPayerbyNIP(platnikNIP);
                } else {
                    payerObj = payerDAO.findPayerbyNIP(platnikNIP);
                    payerObj.getPayer_id();
                }
            } else {
                payerObj = null;
            }

            if (forma.trim().equals("gotówka")) {
                dueDate = methods.getToday();
            } else {
                String chosenDate = request.getParameter("dueDate");
                if (chosenDate.trim().isEmpty()) {
                    dueDate = methods.getTwoWeeksFromToday();
                } else {
                    dueDate = new SimpleDateFormat("yyyy-mm-dd").parse(chosenDate);
                }
            }

            //path = methods.createPrintout(thisInvoiceNumber);
            //printoutDAO.insertPrintout(datePrinted,path);
            //printoutObj = printoutDAO.findPrintoutbyPath(path);

            //!!! chwilowo printout na null!!!
            invoiceDAO.insertInvoice(thisInvoiceNumber, dateCreated, null, datePrinted, customerObj, payerObj, forma, dueDate, Float.valueOf(bruttoSum), Float.valueOf(nettoSum));

            Invoice thisInvoice = invoiceDAO.findInvoicebyNumber(thisInvoiceNumber);
            while (!isLast) {
                for (int i = 0; i < params.size() - 1; i++) {
                    String lp = request.getParameter("inputrow" + i + "0");
                    if (lp == null) {
                        isLast = true;
                    } else {
                        String product = request.getParameter("inputrow" + i + "1");
                        String jm = request.getParameter("inputrow" + i + "2");
                        String quantity = request.getParameter("inputrow" + i + "3");
                        String netto = request.getParameter("inputrow" + i + "4");
                        String VAT = request.getParameter("inputrow" + i + "5");
                        String brutto = request.getParameter("inputrow" + i + "6");
                        String nettoV = request.getParameter("inputrow" + i + "7");
                        String bruttoV = request.getParameter("inputrow" + i + "8");

                        invoice_productDAO.insertProductstoInvoice(thisInvoice, product, Float.valueOf(quantity), jm, Integer.valueOf(VAT), Float.valueOf(netto), Float.valueOf(brutto), Float.valueOf(nettoV), Float.valueOf(bruttoV), Integer.valueOf(lp));

                        //System.out.println(lp + " " + product + " " + jm + " " + quantity + " " + netto + " " + VAT + " " + brutto + " " + nettoV + " " + bruttoV);
                    }
                }
            }
            //System.out.println(forma + " " + nettoSum + " " + bruttoSum);
            //System.out.println(nabywca + " " + odbiorca);
            model.addObject("invoice", thisInvoice);
            model.addObject("listProducts", invoice_productDAO.findInvoice_productbyInvoiceId(thisInvoice.getInvoice_id()));
            model.addObject("customer", customerDAO.findCustomerbyId(thisInvoice.getCustomer().getCustomer_id()));
            model.addObject("payer", payerObj);
            model.setViewName("viewPDF");
        }
        return model;
    }

    @RequestMapping(value = "/delete", method = RequestMethod.GET)
    public ModelAndView delete(ModelAndView model) {
        System.out.println("jestsem");
        model.setViewName("newInvoice");
        return model;
    }

    @RequestMapping(value = "/save", method = RequestMethod.GET)
    public ModelAndView save(ModelAndView model, HttpServletRequest request) throws ParseException, UnsupportedEncodingException {
        request.setCharacterEncoding("UTF-8");
        Map<String, String[]> params = request.getParameterMap();

        if (!checkIfDataCorrect(request, params)) {
            model.setViewName("error");
        } else {
            boolean isLast = false;
            CreateInvoice methods = new CreateInvoice();

            String forma = request.getParameter("payment");
            System.out.println(forma);

            String nettoSum = request.getParameter("nettoSuminv");
            String bruttoSum = request.getParameter("bruttoSuminv");

            String odbiorcaNIP = request.getParameter("nip");
            String odbiorcaNazwa = request.getParameter("odbiorca");
            String odbiorcaUlica = request.getParameter("street");
            String odbiorcaKodPocztowy = request.getParameter("postcode");
            String odbiorcaMiasto = request.getParameter("town");

            String platnikNIP = request.getParameter("nipPlatnik");
            String platnikNazwa = request.getParameter("platnik");
            String platnikUlica = request.getParameter("streetPlatnik");
            String platnikKodPocztowy = request.getParameter("postcodePlatnik");
            String platnikMiasto = request.getParameter("townPlatnik");

            Customer customerObj, customer;
            Payer payerObj, payer;
            Date dateCreated, dueDate;
            String thisInvoiceNumber, path;

            try {
                customer = customerDAO.findCustomerbyNIP(odbiorcaNIP);
            } catch (IndexOutOfBoundsException ie) {
                customer = null;
            }
            if (customer == null) {
                customerDAO.insertCustomer(odbiorcaNazwa, odbiorcaUlica, null, odbiorcaMiasto, odbiorcaKodPocztowy, odbiorcaNIP, null, null, null);
                customerObj = customerDAO.findCustomerbyNIP(odbiorcaNIP);
            } else {
                customerObj = customerDAO.findCustomerbyNIP(odbiorcaNIP);
            }

            dateCreated = methods.getToday();
            List<Invoice> listInvoices = invoiceDAO.listInvoices();
            String lastInvoice = null;
            if (listInvoices.isEmpty()) {
                lastInvoice = "00/zapis";
            } else {
                boolean isCorrect = false;
                int listPointer = 1;
                lastInvoice = null;
                while (!isCorrect) {
                    if ((listInvoices.get(listInvoices.size() - listPointer).getInvoice_number()).contains("/20") || (listInvoices.get(listInvoices.size() - listPointer).getInvoice_number()).contains("KFV")) {
                        if ((listInvoices.size() - listPointer) == 0) {
                            lastInvoice = "00/zapis";
                            isCorrect = true;
                        }
                        listPointer++;
                    } else {
                        lastInvoice = listInvoices.get(listInvoices.size() - listPointer).getInvoice_number();
                        isCorrect = true;
                    }
                }
            }
            thisInvoiceNumber = methods.getCurrentSavedInvoiceNumber(lastInvoice);


            if (!platnikNIP.trim().isEmpty()) {
                try {
                    payer = payerDAO.findPayerbyNIP(platnikNIP);
                } catch (IndexOutOfBoundsException ie) {
                    payer = null;
                }

                if (payer == null) {
                    payerDAO.insertPayer(platnikNazwa, platnikUlica, null, platnikMiasto, platnikKodPocztowy, platnikNIP, null, null, null);
                    payerObj = payerDAO.findPayerbyNIP(platnikNIP);
                } else {
                    payerObj = payerDAO.findPayerbyNIP(platnikNIP);
                    payerObj.getPayer_id();
                }
            } else {
                payerObj = null;
            }

            if (forma.trim().equals("gotówka")) {
                dueDate = methods.getToday();
            } else {
                String chosenDate = request.getParameter("dueDate");
                if (chosenDate.trim().isEmpty()) {
                    dueDate = methods.getTwoWeeksFromToday();
                } else {
                    dueDate = new SimpleDateFormat("yyyy-mm-dd").parse(chosenDate);
                }
            }

            invoiceDAO.insertInvoice(thisInvoiceNumber, dateCreated, null, null, customerObj, payerObj, forma, dueDate, Float.valueOf(bruttoSum), Float.valueOf(nettoSum));

            Invoice thisInvoice = invoiceDAO.findInvoicebyNumber(thisInvoiceNumber);
            while (!isLast) {
                for (int i = 0; i < params.size() - 1; i++) {
                    String lp = request.getParameter("inputrow" + i + "0");
                    if (lp == null) {
                        isLast = true;
                    } else {
                        String product = request.getParameter("inputrow" + i + "1");
                        String jm = request.getParameter("inputrow" + i + "2");
                        String quantity = request.getParameter("inputrow" + i + "3");
                        String netto = request.getParameter("inputrow" + i + "4");
                        String VAT = request.getParameter("inputrow" + i + "5");
                        String brutto = request.getParameter("inputrow" + i + "6");
                        String nettoV = request.getParameter("inputrow" + i + "7");
                        String bruttoV = request.getParameter("inputrow" + i + "8");

                        invoice_productDAO.insertProductstoInvoice(thisInvoice, product, Float.valueOf(quantity), jm, Integer.valueOf(VAT), Float.valueOf(netto), Float.valueOf(brutto), Float.valueOf(nettoV), Float.valueOf(bruttoV), Integer.valueOf(lp));

                        //System.out.println(lp + " " + product + " " + jm + " " + quantity + " " + netto + " " + VAT + " " + brutto + " " + nettoV + " " + bruttoV);
                    }
                }
            }

            List<Product> list = productDAO.listProducts();
            model.addObject("productList", list);
            model.addObject("invoice", thisInvoice);
            model.addObject("listProducts", invoice_productDAO.findInvoice_productbyInvoiceId(thisInvoice.getInvoice_id()));
            model.addObject("customer", customerDAO.findCustomerbyId(thisInvoice.getCustomer().getCustomer_id()));
            model.addObject("payer", payerObj);
            model.setViewName("editInvoice");
        }
        return model;
    }

    private boolean checkIfDataCorrect(HttpServletRequest request, Map<String, String[]> params) {

        boolean state = true;
        boolean isLast = false;
        String forma = request.getParameter("payment");

        String odbiorcaNIP = request.getParameter("nip");
        String odbiorcaNazwa = request.getParameter("odbiorca");
        String odbiorcaUlica = request.getParameter("street");
        String odbiorcaKodPocztowy = request.getParameter("postcode");
        String odbiorcaMiasto = request.getParameter("town");

        if(forma.trim().isEmpty() || odbiorcaNIP.trim().isEmpty() || odbiorcaKodPocztowy.trim().isEmpty() || odbiorcaMiasto.trim().isEmpty() || odbiorcaNazwa.trim().isEmpty() || odbiorcaUlica.trim().isEmpty()){
            state=false;
        }
        while (!isLast) {
            for (int i = 0; i < params.size() - 1; i++) {
                String lp = request.getParameter("inputrow" + i + "0");
                if (lp == null) {
                    isLast = true;
                } else {
                    String product = request.getParameter("inputrow" + i + "1");
                    String jm = request.getParameter("inputrow" + i + "2");
                    String quantity = request.getParameter("inputrow" + i + "3");
                    String VAT = request.getParameter("inputrow" + i + "5");
                    String brutto = request.getParameter("inputrow" + i + "6");

                    if(product.trim().isEmpty() || jm.trim().isEmpty() || quantity.trim().isEmpty() ||  VAT.trim().isEmpty() || brutto.trim().isEmpty()){
                        state=false;
                    }
                }
            }
        }

        return state;
    }

}
