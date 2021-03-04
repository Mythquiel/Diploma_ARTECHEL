package com.artechel.aplikacja.DAO;

import com.artechel.aplikacja.mappers.AllInvoicesMapper;
import com.artechel.aplikacja.model.Customer;
import com.artechel.aplikacja.model.Invoice;
import com.artechel.aplikacja.model.Payer;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;

public class InvoiceDAOImplementation implements InvoiceDAO {

    private JdbcTemplate jdbcTemp;

    public InvoiceDAOImplementation(DataSource dataSource) {
        jdbcTemp = new JdbcTemplate(dataSource);
    }

    @Override
    public List<Invoice> listInvoices() {
        String sql = "select i.*, c.name, p.name from invoice i join customer c on i.customer_id=c.customer_id left join payer p on p.payer_id=i.payer_id order by i.invoice_number";
        List<Invoice> list = jdbcTemp.query(sql, new AllInvoicesMapper());
        return list;
    }

    @Override
    public Invoice findInvoicebyID(int id) {
        String sql = "select i.*, c.name, p.name from invoice i join customer c on i.customer_id=c.customer_id left join payer p on p.payer_id=i.payer_id where i.invoice_id like ?";
        List<Invoice> list = jdbcTemp.query(sql, new AllInvoicesMapper(), id);
        return list.get(0);
    }

    @Override
    public Invoice findInvoicebyNumber(String number) {
        String sql = "select i.*, c.name, p.name from invoice i join customer c on i.customer_id=c.customer_id left join payer p on p.payer_id=i.payer_id where i.invoice_number like ?";
        List<Invoice> list = jdbcTemp.query(sql, new Object[]{number}, new AllInvoicesMapper());
        return list.get(0);
    }

    @Override
    public List<Invoice> listByAllparameters(String number, String dataAfter, String dataBefore, String customer_name) {
        if (number.trim().isEmpty()) {
            number = "%";
            System.out.println(number);
        } else {
            number = "%" + number.trim() + "%";
        }

        if (dataAfter.trim().isEmpty()) {
            dataAfter = "2019-01-01";
        } else {
            dataAfter = dataAfter.trim() ;
        }

        if (dataBefore.trim().isEmpty()) {
            long millis=System.currentTimeMillis();
            Date date=new java.sql.Date(millis);
            dataBefore = date.toString().trim();
        } else {
            dataBefore = dataBefore.trim() ;
        }

        if(customer_name.trim().isEmpty()){
            customer_name="%";
        } else {
            customer_name = "%" + customer_name.trim() + "%";
        }

        String sql = "select i.*, c.name, p.name from invoice i join customer c on i.customer_id=c.customer_id left join payer p on p.payer_id=i.payer_id where i.date_created between ? and ? and i.invoice_number like ? and (c.name like ? or p.name like ? )";
        List<Invoice> list = jdbcTemp.query(sql, new Object[]{dataAfter, dataBefore, number, customer_name, customer_name}, new AllInvoicesMapper());
        System.out.println(dataAfter+" "+dataBefore+" "+number+" "+customer_name);
        System.out.println(list);
        return list;
    }

    @Override
    public void insertInvoice(String number, Date created, Date modified, Date printed, Customer customer, Payer payer, String payment, Date due, Float brutto, Float netto) {
        String sql = "insert into invoice (customer_id, date_created, date_modified, date_printed, invoice_number, payer_id, payment, payment_due, value_brutto, value_netto) values(?,?,?,?,?,?,?,?,?,?)";
        //change printout to printout.getPrintout_id();
        if(payer==null){
            jdbcTemp.update(sql,customer.getCustomer_id(),created,modified,printed,number, null,payment,due,brutto,netto);
        } else {
            jdbcTemp.update(sql,customer.getCustomer_id(),created,modified,printed,number,payer.getPayer_id(),payment,due,brutto,netto);
        }

    }

    @Override
    public void updateInvoice(String number, Date created, Date modified, Date printed, Customer customer, Payer payer, String payment, Date due, Float brutto, Float netto, String invNo) {
        String sql = "update invoice set customer_id = ?, date_created = ?, date_modified = ?, date_printed = ?, invoice_number = ?, payer_id = ?, payment = ?, payment_due = ?, value_brutto = ?, value_netto = ? where invoice_number like ?";
        if(payer==null){
            jdbcTemp.update(sql,customer.getCustomer_id(),created,modified,printed,number, null,payment,due,brutto,netto,invNo);
        } else {
            jdbcTemp.update(sql,customer.getCustomer_id(),created,modified,printed,number,payer.getPayer_id(),payment,due,brutto,netto,invNo);
        }
    }

    @Override
    public void deleteInvoice(String number) {
        String sql = "delete from invoice where invoice_number like ?";
        jdbcTemp.update(sql,number);
    }

}
