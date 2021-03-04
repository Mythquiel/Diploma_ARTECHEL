package com.artechel.aplikacja.mappers;

import com.artechel.aplikacja.model.Customer;
import com.artechel.aplikacja.model.Invoice;
import com.artechel.aplikacja.model.Payer;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class AllInvoicesMapper implements RowMapper<Invoice> {
    @Override
    public Invoice mapRow(ResultSet resultSet, int i) throws SQLException {
        Invoice invoice = new Invoice();
        Payer payer = new Payer();
        Customer customer = new Customer();

        customer.setCustomer_id(resultSet.getInt(1));
        invoice.setDate_created(resultSet.getDate(2));
        invoice.setDate_modified(resultSet.getDate(3));
        invoice.setDate_printed(resultSet.getDate(4));
        invoice.setInvoice_id(resultSet.getInt(5));
        invoice.setInvoice_number(resultSet.getString(6));
        payer.setPayer_id(resultSet.getInt(7));
        invoice.setPayment(resultSet.getString(8));
        invoice.setPayment_due(resultSet.getDate(9));
        invoice.setValue_brutto(resultSet.getFloat(10));
        invoice.setValue_netto(resultSet.getFloat(11));
        customer.setName(resultSet.getString(12));
        invoice.setCustomer(customer);
        payer.setName(resultSet.getString(13));
        invoice.setPayer(payer);
        return invoice;
    }
}
