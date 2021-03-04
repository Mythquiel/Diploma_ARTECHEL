package com.artechel.aplikacja.DAO;

import com.artechel.aplikacja.model.Customer;
import com.artechel.aplikacja.model.Invoice;
import com.artechel.aplikacja.model.Payer;

import java.util.Date;
import java.util.List;

public interface InvoiceDAO {

    List<Invoice> listInvoices();
    Invoice findInvoicebyID(int invoice_id);
    Invoice findInvoicebyNumber(String number);
    List<Invoice> listByAllparameters(String number, String dataAfter, String dataBefore, String customer_name);
    void insertInvoice(String number,Date created, Date modified, Date printed, Customer customer, Payer payer, String payment, Date due, Float brutto, Float netto);
    void updateInvoice(String number,Date created, Date modified, Date printed, Customer customer, Payer payer, String payment, Date due, Float brutto, Float netto, String invNo);
    void deleteInvoice(String number);
}
