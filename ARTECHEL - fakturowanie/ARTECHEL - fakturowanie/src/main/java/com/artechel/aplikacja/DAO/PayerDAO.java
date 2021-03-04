package com.artechel.aplikacja.DAO;

import com.artechel.aplikacja.model.Payer;

import java.math.BigInteger;
import java.util.List;

public interface PayerDAO {

    List<Payer> listPayers();
    Payer findPayerbyId(int payer_id);
    Payer findPayerbyNIP(String nip);
    List<Payer> listPayerbyName(String name);
    void insertPayer(String name, String street, String line2, String town, String postcode, String nip, BigInteger telephone, BigInteger mobile, String email);
}
