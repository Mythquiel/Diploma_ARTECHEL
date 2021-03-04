package com.artechel.aplikacja.mappers;

import com.artechel.aplikacja.model.Customer;
import org.springframework.jdbc.core.RowMapper;

import java.math.BigInteger;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CustomerMapper implements RowMapper<Customer> {
    @Override
    public Customer mapRow(ResultSet resultSet, int i) throws SQLException {
        Customer customer = new Customer();
        customer.setCustomer_id(resultSet.getInt("customer_id"));
        customer.setAddress_line_1(resultSet.getString("address_line_1"));
        customer.setAddress_line_2(resultSet.getString("address_line_2"));
        customer.setAddress_line_3(resultSet.getString("address_line_3"));
        customer.setEmail_address(resultSet.getString("email_address"));
        customer.setMobile(BigInteger.valueOf(resultSet.getLong("mobile")));
        customer.setName(resultSet.getString("name"));
        customer.setNip(resultSet.getString("nip"));
        customer.setPostcode(resultSet.getString("postcode"));
        customer.setTelephone(BigInteger.valueOf(resultSet.getLong("telephone")));
        return customer;
    }

}
