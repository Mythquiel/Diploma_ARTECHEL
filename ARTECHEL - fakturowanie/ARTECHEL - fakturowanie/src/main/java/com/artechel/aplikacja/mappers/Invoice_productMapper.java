package com.artechel.aplikacja.mappers;

import com.artechel.aplikacja.model.Invoice;
import com.artechel.aplikacja.model.Invoice_product;
import org.springframework.jdbc.core.RowMapper;

import javax.persistence.criteria.CriteriaBuilder;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Invoice_productMapper implements RowMapper<Invoice_product> {
    @Override
    public Invoice_product mapRow(ResultSet resultSet, int i) throws SQLException {
        Invoice_product invoice_product = new Invoice_product();
        Invoice invoice = new Invoice();
        invoice.setInvoice_id(resultSet.getInt("invoice_id"));
        invoice_product.setInvoice(invoice);
        invoice_product.setInvoice_product_id(resultSet.getInt("invoice_product_id"));
        invoice_product.setPrice_netto(resultSet.getFloat("price_netto"));
        invoice_product.setProduct_name(resultSet.getString("product_name"));
        invoice_product.setQuantity(resultSet.getFloat("quantity"));
        invoice_product.setUnit(resultSet.getString("unit"));
        invoice_product.setVat(resultSet.getInt("vat"));
        invoice_product.setPrice_brutto(resultSet.getFloat("price_brutto"));
        invoice_product.setValue_netto(resultSet.getFloat("value_netto"));
        invoice_product.setValue_brutto(resultSet.getFloat("value_brutto"));
        invoice_product.setLp(resultSet.getInt("lp"));

        return invoice_product;
    }
}
