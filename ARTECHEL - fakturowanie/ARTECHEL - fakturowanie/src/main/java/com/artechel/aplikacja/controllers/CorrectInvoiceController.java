package com.artechel.aplikacja.controllers;

import com.artechel.aplikacja.DAO.*;
import com.artechel.aplikacja.methods.CreateInvoice;
import com.artechel.aplikacja.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Controller
public class CorrectInvoiceController {

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


    @RequestMapping(value = "/correctEdit", method = RequestMethod.GET)
    public ModelAndView printEdit(ModelAndView model, HttpServletRequest request) throws UnsupportedEncodingException, ParseException {
        request.setCharacterEncoding("UTF-8");
        Map<String, String[]> params = request.getParameterMap();

        if (checkIfDataCorrect(request, params)) {
            model.setViewName("error");
        } else {

            boolean isLast = false;
            CreateInvoice methods = new CreateInvoice();

            String invNo = request.getParameter("invoiceNo");
            String nettoSum = request.getParameter("nettoSuminv");
            String bruttoSum = request.getParameter("bruttoSuminv");

            Invoice oldInvoice = invoiceDAO.findInvoicebyNumber(invNo.trim());
            int invoiceId = oldInvoice.getInvoice_id();
            model.addObject("oldInvoice", oldInvoice);
            List<Invoice_product> oldInvoiceProducts = invoice_productDAO.findInvoice_productbyInvoiceId(invoiceId);
            model.addObject("oldProducts", oldInvoiceProducts);

            invoiceDAO.updateInvoice(oldInvoice.getInvoice_number(), oldInvoice.getDate_created(), methods.getToday(), oldInvoice.getDate_printed(), oldInvoice.getCustomer(), oldInvoice.getPayer(), oldInvoice.getPayment(), oldInvoice.getPayment_due(), Float.valueOf(bruttoSum), Float.valueOf(nettoSum), oldInvoice.getInvoice_number());
            Invoice newInvoice = invoiceDAO.findInvoicebyNumber(oldInvoice.getInvoice_number());
            model.addObject("newInvoice", newInvoice);

            Float nettoCorrect = newInvoice.getValue_netto() - oldInvoice.getValue_netto();
            Float bruttoCorrect = newInvoice.getValue_brutto() - oldInvoice.getValue_brutto();


            String thisInvoiceNumber = findInvoiceNo(oldInvoice.getInvoice_number());

            invoiceDAO.insertInvoice(thisInvoiceNumber, methods.getToday(), null, methods.getToday(), oldInvoice.getCustomer(), oldInvoice.getPayer(), oldInvoice.getPayment(), methods.getToday(), bruttoCorrect, nettoCorrect);
            Invoice thisInvoice = invoiceDAO.findInvoicebyNumber(thisInvoiceNumber);
            model.addObject("thisInvoice", thisInvoice);

            invoice_productDAO.deleteProductsfromInvoice(invoiceId);

            while (!isLast) {
                for (int i = 0; i < params.size() - 1; i++) {
                    String lp = request.getParameter("inputrow" + i + "0");
                    if (lp == null) {
                        isLast = true;
                    } else {
                        String product = request.getParameter("inputrow" + i + "1");
                        String jm = request.getParameter("inputrow" + i + "2");
                        String quantity = request.getParameter("inputrow" + i + "3");
                        Float correctQuantity = Float.valueOf(quantity) - oldInvoiceProducts.get(i).getQuantity();
                        String netto = request.getParameter("inputrow" + i + "4");
                        String VAT = request.getParameter("inputrow" + i + "5");
                        String brutto = request.getParameter("inputrow" + i + "6");
                        String nettoV = request.getParameter("inputrow" + i + "7");
                        Float correctNetto = Float.valueOf(nettoV) - oldInvoiceProducts.get(i).getValue_netto();
                        String bruttoV = request.getParameter("inputrow" + i + "8");
                        Float correctBrutto = Float.valueOf(bruttoV) - oldInvoiceProducts.get(i).getValue_brutto();

                        invoice_productDAO.insertProductstoInvoice(newInvoice, product, Float.valueOf(quantity), jm, Integer.valueOf(VAT), Float.valueOf(netto), Float.valueOf(brutto), Float.valueOf(nettoV), Float.valueOf(bruttoV), Integer.valueOf(lp));
                        if (correctQuantity < 0) {
                            invoice_productDAO.insertProductstoInvoice(thisInvoice, product, correctQuantity, jm, Integer.valueOf(VAT), Float.valueOf(netto), Float.valueOf(brutto), correctNetto, correctBrutto, Integer.valueOf(lp));
                        }
                        //System.out.println(lp + " " + product + " " + jm + " " + quantity + " " + netto + " " + VAT + " " + brutto + " " + nettoV + " " + bruttoV);
                    }
                }
            }

            List<Invoice_product> newInvoiceProducts = invoice_productDAO.findInvoice_productbyInvoiceId(newInvoice.getInvoice_id());
            model.addObject("newProducts", newInvoiceProducts);

            List<Invoice_product> thisInvoiceProducts = invoice_productDAO.findInvoice_productbyInvoiceId(thisInvoice.getInvoice_id());
            model.addObject("thisProducts", thisInvoiceProducts);


            model.addObject("customer", customerDAO.findCustomerbyId(oldInvoice.getCustomer().getCustomer_id()));
            Payer payerObj;
            try {
                payerObj = payerDAO.findPayerbyId(oldInvoice.getPayer().getPayer_id());
            } catch (NullPointerException | IndexOutOfBoundsException exc) {
                payerObj = null;
            }
            model.addObject("payer", payerObj);
            model.setViewName("correctPDF");
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

        if (forma.trim().isEmpty() || odbiorcaNIP.trim().isEmpty() || odbiorcaKodPocztowy.trim().isEmpty() || odbiorcaMiasto.trim().isEmpty() || odbiorcaNazwa.trim().isEmpty() || odbiorcaUlica.trim().isEmpty()) {
            state = false;
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

                    if (product.trim().isEmpty() || jm.trim().isEmpty() || quantity.trim().isEmpty() || VAT.trim().isEmpty() || brutto.trim().isEmpty()) {
                        state = false;
                    }
                }
            }
        }

        return !state;
    }

    private String findInvoiceNo(String invoice_number) {
        List<Invoice> listInvoices = invoiceDAO.listInvoices();
        CreateInvoice methods = new CreateInvoice();
        String lastInvoice;
        String thisInvoiceNumber;
        if (listInvoices.isEmpty()) {
            thisInvoiceNumber = methods.getCurrentCorrectNumber("00/0000", invoice_number);
        } else {
            boolean isCorrect = false;
            int listPointer = 1;
            lastInvoice = null;
            while (!isCorrect) {
                if ((listInvoices.get(listInvoices.size() - listPointer).getInvoice_number()).contains("/zapis") || ((listInvoices.get(listInvoices.size() - listPointer).getInvoice_number()).contains("/20") && !(listInvoices.get(listInvoices.size() - listPointer).getInvoice_number()).contains("KFV"))) {
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
            thisInvoiceNumber = methods.getCurrentCorrectNumber(lastInvoice,invoice_number);
        }
        return thisInvoiceNumber;
    }

}
