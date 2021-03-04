package com.artechel.aplikacja.DAO;

import com.artechel.aplikacja.model.Customer;

import java.math.BigInteger;
import java.util.List;

public interface CustomerDAO {

    List<Customer> listCustomers();
    List<Customer> listCustomerbyParameters(String name, String nip);
    Customer findCustomerbyId(int customer_id);
    Customer findCustomerbyNIP(String nip);
    void insertCustomer(String name, String street, String line2, String town, String postcode, String nip, BigInteger telephone, BigInteger mobile, String email);
}
