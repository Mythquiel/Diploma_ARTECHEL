package com.artechel.aplikacja.model;

import java.io.Serializable;

//tabla product - lista produktów - pomoc przy tworzeniu faktur
public class Product implements Serializable {

    private int product_id;
    private String name;
    private String unit;
    private int vat;

    public Product() {

    }

    public Product(int product_id, String name, String unit, int vat) {
        this.product_id = product_id;
        this.name = name;
        this.unit = unit;
        this.vat = vat;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setVat(int vat) {
        this.vat = vat;
    }

    public int getProduct_id() {
        return product_id;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public void setProduct_id(int product_id) {
        this.product_id = product_id;
    }

    public String getUnit() {
        return unit;
    }

    public int getVat() {
        return vat;
    }
}
