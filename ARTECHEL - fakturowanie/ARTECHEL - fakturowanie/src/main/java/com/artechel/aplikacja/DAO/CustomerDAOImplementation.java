package com.artechel.aplikacja.DAO;

import com.artechel.aplikacja.mappers.CustomerMapper;
import com.artechel.aplikacja.model.Customer;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import java.math.BigInteger;
import java.util.List;

public class CustomerDAOImplementation implements CustomerDAO{

    private JdbcTemplate jdbcTemp;

    public CustomerDAOImplementation(DataSource dataSource){
        jdbcTemp = new JdbcTemplate(dataSource);
    }

    @Override
    public List<Customer> listCustomers() {
        String sql = "SELECT * FROM customer";
        return jdbcTemp.query(sql, new CustomerMapper());
    }

    @Override
    public List<Customer> listCustomerbyParameters(String name, String nip) {

        String sql = "SELECT * FROM customer WHERE name LIKE ? AND nip LIKE ?";
        if(name.trim().isEmpty()){
            name="%";
        } else {
            name ="%"+ name.trim()+"%";
        }

        if(nip.trim().isEmpty()){
            nip = "%";
        } else {
            nip = "%"+nip.trim()+"%";
        }

        List<Customer> list = jdbcTemp.query(sql, new Object[]{name,nip}, new CustomerMapper());
        return list;
    }

    @Override
    public Customer findCustomerbyId(int customer_id) {
        String sql = "SELECT * FROM customer WHERE customer_id LIKE ?";
        List<Customer> list = jdbcTemp.query(sql, new Object[]{customer_id},new CustomerMapper());
        return list.get(0);
    }

    @Override
    public Customer findCustomerbyNIP(String nip) {
        String sql = "SELECT * FROM customer WHERE nip LIKE ?";
        List<Customer> list = jdbcTemp.query(sql, new Object[]{nip}, new CustomerMapper());
        return list.get(0);
    }

    @Override
    public void insertCustomer(String name, String street, String line2, String town, String postcode, String nip, BigInteger telephone, BigInteger mobile, String email) {
        String sql = "INSERT INTO customer (name, address_line_1, address_line_2, address_line_3, postcode, nip, telephone, mobile, email_address) VALUES(?,?,?,?,?,?,?,?,?)";
        jdbcTemp.update(sql,name, street, line2, town, postcode, nip, telephone, mobile, email);
    }

}
