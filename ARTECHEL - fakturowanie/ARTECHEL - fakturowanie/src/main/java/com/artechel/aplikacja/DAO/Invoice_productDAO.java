package com.artechel.aplikacja.DAO;

import com.artechel.aplikacja.model.Invoice;
import com.artechel.aplikacja.model.Invoice_product;

import java.util.List;

public interface Invoice_productDAO {

    List<Invoice_product> listInvoice_products();
    List<Invoice_product> findInvoice_productbyInvoiceId(int id);
    void insertProductstoInvoice(Invoice invoice, String name, float quantity, String unit, int vat, float priceNetto, float priceBrutto, float valueNetto, float valueBrutto, int lp);
    void deleteProductsfromInvoice(int invoiceId);
}

