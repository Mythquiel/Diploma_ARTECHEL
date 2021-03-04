package com.artechel.aplikacja.model;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

//tabela invoice - dane faktur
public class Invoice implements Serializable {

    private int invoice_id;
    private Date date_created;
    private Date date_modified;
    private Date date_printed;
    private Customer customer;
    private String invoice_number;
    private Payer payer;
    private String payment;
    private Date payment_due;
    private float value_netto;
    private float value_brutto;
    private Set<Invoice_product> invoice_productSet = new HashSet<>(0);

    public Invoice() {

    }

    public Invoice(int invoice_id, Date date_created, Date date_modified, Date date_printed, Customer customer, String invoice_number, Payer payer, String payment, Date payment_due, float value_netto, float value_brutto, Set<Invoice_product> invoice_productSet) {
        this.invoice_id = invoice_id;
        this.date_created = date_created;
        this.date_modified = date_modified;
        this.date_printed = date_printed;
        this.customer = customer;
        this.invoice_number = invoice_number;
        this.payer = payer;
        this.payment = payment;
        this.payment_due = payment_due;
        this.value_netto = value_netto;
        this.value_brutto = value_brutto;
        this.invoice_productSet = invoice_productSet;
    }

    public int getInvoice_id() {
        return invoice_id;
    }

    public void setInvoice_id(int invoice_id) {
        this.invoice_id = invoice_id;
    }

    public Date getDate_created() {
        return date_created;
    }

    public void setDate_created(Date date_created) {
        this.date_created = date_created;
    }

    public Date getDate_modified() {
        return date_modified;
    }

    public void setDate_modified(Date date_modified) {
        this.date_modified = date_modified;
    }

    public Date getDate_printed() {
        return date_printed;
    }

    public void setDate_printed(Date date_printed) {
        this.date_printed = date_printed;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public String getInvoice_number() {
        return invoice_number;
    }

    public void setInvoice_number(String invoice_number) {
        this.invoice_number = invoice_number;
    }

    public Payer getPayer() {
        return payer;
    }

    public void setPayer(Payer payer) {
        this.payer = payer;
    }

    public String getPayment() {
        return payment;
    }

    public void setPayment(String payment) {
        this.payment = payment;
    }

    public Date getPayment_due() {
        return payment_due;
    }

    public void setPayment_due(Date payment_due) {
        this.payment_due = payment_due;
    }

    public float getValue_netto() {
        return value_netto;
    }

    public void setValue_netto(float value_netto) {
        this.value_netto = value_netto;
    }

    public float getValue_brutto() {
        return value_brutto;
    }

    public void setValue_brutto(float value_brutto) {
        this.value_brutto = value_brutto;
    }

    public Set<Invoice_product> getInvoice_productSet() {
        return invoice_productSet;
    }

    public void setInvoice_productSet(Set<Invoice_product> invoice_productSet) {
        this.invoice_productSet = invoice_productSet;
    }
}


