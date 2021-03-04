package com.artechel.aplikacja.DAO;

import com.artechel.aplikacja.mappers.Invoice_productMapper;
import com.artechel.aplikacja.model.Invoice;
import com.artechel.aplikacja.model.Invoice_product;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import java.util.List;

public class Invoice_productDAOImplementation implements Invoice_productDAO {

    private JdbcTemplate jdbcTemp;

    public Invoice_productDAOImplementation(DataSource dataSource){
        jdbcTemp = new JdbcTemplate(dataSource);
    }

    @Override
    public List<Invoice_product> listInvoice_products() {
        List<Invoice_product> list = jdbcTemp.query("SELECT * FROM invoice_product", new Invoice_productMapper());
        return list;
    }

    @Override
    public List<Invoice_product> findInvoice_productbyInvoiceId(int id) {
        String sql = "SELECT * FROM invoice_product WHERE invoice_id LIKE ?";
        List<Invoice_product> list = jdbcTemp.query(sql, new Object[]{id},new Invoice_productMapper());
        return list;
    }

    @Override
    public void insertProductstoInvoice(Invoice invoice, String name, float quantity, String unit, int vat, float priceNetto, float priceBrutto, float valueNetto, float valueBrutto,int lp) {
        String sql ="INSERT INTO invoice_product (invoice_id, product_name, quantity, unit, vat, price_netto, price_brutto, value_netto, value_brutto, lp) VALUES (?,?,?,?,?,?,?,?,?,?)";
        jdbcTemp.update(sql,invoice.getInvoice_id(),name,quantity,unit,vat,priceNetto,priceBrutto,valueNetto,valueBrutto,lp);
    }

    @Override
    public void deleteProductsfromInvoice(int invoiceId) {

        String sql = "DELETE FROM invoice_product WHERE invoice_id LIKE ?";
        jdbcTemp.update(sql,invoiceId);
    }


}
