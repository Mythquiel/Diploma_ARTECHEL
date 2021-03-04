package com.artechel.aplikacja.model;

import java.io.Serializable;

//table invoice_product - produkty na fakturach
public class Invoice_product implements Serializable {

    private int invoice_product_id;
    private Invoice invoice;
    private String product_name;
    private float quantity;
    private String unit;
    private int vat;
    private float price_netto;
    private float price_brutto;
    private float value_netto;
    private float value_brutto;
    private int lp;

    public Invoice_product() {

    }

    public Invoice_product(int invoice_product_id, Invoice invoice, String product_name, float quantity, String unit, int vat, float price_netto, float price_brutto, float value_netto, float value_brutto, int lp) {
        this.invoice_product_id = invoice_product_id;
        this.invoice = invoice;
        this.product_name = product_name;
        this.quantity = quantity;
        this.unit = unit;
        this.vat = vat;
        this.price_netto = price_netto;
        this.price_brutto = price_brutto;
        this.value_netto = value_netto;
        this.value_brutto = value_brutto;
        this.lp = lp;
    }

    public int getInvoice_product_id() {
        return invoice_product_id;
    }

    public void setInvoice_product_id(int invoice_product_id) {
        this.invoice_product_id = invoice_product_id;
    }

    public Invoice getInvoice() {
        return invoice;
    }

    public void setInvoice(Invoice invoice) {
        this.invoice = invoice;
    }

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public float getQuantity() {
        return quantity;
    }

    public void setQuantity(float quantity) {
        this.quantity = quantity;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public int getVat() {
        return vat;
    }

    public void setVat(int vat) {
        this.vat = vat;
    }

    public float getPrice_netto() {
        return price_netto;
    }

    public void setPrice_netto(float price_netto) {
        this.price_netto = price_netto;
    }

    public float getPrice_brutto() {
        return price_brutto;
    }

    public void setPrice_brutto(float price_brutto) {
        this.price_brutto = price_brutto;
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

    public int getLp() {
        return lp;
    }

    public void setLp(int lp) {
        this.lp = lp;
    }
}