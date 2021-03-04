package com.artechel.aplikacja.model;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.HashSet;
import java.util.Set;

//tabela customer - dane nabywców
public class Customer implements Serializable {

    private int customer_id;
    private String name;
    private String address_line_1;
    private String address_line_2;
    private String address_line_3;
    private String postcode;
    private String nip;
    private BigInteger telephone;
    private BigInteger mobile;
    private String email_address;
    private Set<Invoice> invoiceList = new HashSet<>(0);

    public Customer() {
    }

    public Customer(int customer_id, String name, String address_line_1, String address_line_2, String address_line_3, String postcode, String nip, BigInteger telephone, BigInteger mobile, String email_address, Set<Invoice> invoiceList) {
        this.customer_id = customer_id;
        this.name = name;
        this.address_line_1 = address_line_1;
        this.address_line_2 = address_line_2;
        this.address_line_3 = address_line_3;
        this.postcode = postcode;
        this.nip = nip;
        this.telephone = telephone;
        this.mobile = mobile;
        this.email_address = email_address;
        this.invoiceList = invoiceList;
    }

    public int getCustomer_id() {
        return customer_id;
    }

    public void setCustomer_id(int customer_id) {
        this.customer_id = customer_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress_line_1() {
        return address_line_1;
    }

    public void setAddress_line_1(String address_line_1) {
        this.address_line_1 = address_line_1;
    }

    public String getAddress_line_2() {
        return address_line_2;
    }

    public void setAddress_line_2(String address_line_2) {
        this.address_line_2 = address_line_2;
    }

    public String getAddress_line_3() {
        return address_line_3;
    }

    public void setAddress_line_3(String address_line_3) {
        this.address_line_3 = address_line_3;
    }

    public String getPostcode() {
        return postcode;
    }

    public void setPostcode(String postcode) {
        this.postcode = postcode;
    }

    public String getNip() {
        return nip;
    }

    public void setNip(String nip) {
        this.nip = nip;
    }

    public BigInteger getTelephone() {
        return telephone;
    }

    public void setTelephone(BigInteger telephone) {
        this.telephone = telephone;
    }

    public BigInteger getMobile() {
        return mobile;
    }

    public void setMobile(BigInteger mobile) {
        this.mobile = mobile;
    }

    public String getEmail_address() {
        return email_address;
    }

    public void setEmail_address(String email_address) {
        this.email_address = email_address;
    }

    public Set<Invoice> getInvoiceList() {
        return invoiceList;
    }

    public void setInvoiceList(Set<Invoice> invoiceList) {
        this.invoiceList = invoiceList;
    }
}


