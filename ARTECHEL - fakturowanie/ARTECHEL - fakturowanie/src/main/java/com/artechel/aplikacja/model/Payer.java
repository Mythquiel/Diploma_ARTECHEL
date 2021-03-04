package com.artechel.aplikacja.model;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.HashSet;
import java.util.Set;

//kolumna payer - dane nabywców
public class Payer implements Serializable {

    private int payer_id;
    private String name;
    private String address_line_1;
    private String address_line_2;
    private String address_line_3;
    private String postcode;
    private String nip;
    private BigInteger telephone;
    private BigInteger mobile;
    private String email_address;
    private Set<Invoice> invoiceSet = new HashSet<Invoice>(0);

    public Payer(int payer_id, String name, String address_line_1, String address_line_2, String address_line_3, String postcode, String nip, BigInteger telephone, BigInteger mobile, String email_address, Set<Invoice> invoiceSet) {
        this.payer_id = payer_id;
        this.name = name;
        this.address_line_1 = address_line_1;
        this.address_line_2 = address_line_2;
        this.address_line_3 = address_line_3;
        this.postcode = postcode;
        this.nip = nip;
        this.telephone = telephone;
        this.mobile = mobile;
        this.email_address = email_address;
        this.invoiceSet = invoiceSet;
    }

    public Payer() {

    }

    public int getPayer_id() {
        return payer_id;
    }

    public void setPayer_id(int payer_id) {
        this.payer_id = payer_id;
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

    public Set<Invoice> getInvoiceSet() {
        return invoiceSet;
    }

    public void setInvoiceSet(Set<Invoice> invoiceSet) {
        this.invoiceSet = invoiceSet;
    }
}

