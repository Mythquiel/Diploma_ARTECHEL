package com.artechel.aplikacja.DAO;

import com.artechel.aplikacja.mappers.PayerMapper;
import com.artechel.aplikacja.model.Payer;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import java.math.BigInteger;
import java.util.List;

public class PayerDAOImplementation implements PayerDAO {

    private JdbcTemplate jdbcTemp;

    public PayerDAOImplementation(DataSource dataSource){
        jdbcTemp = new JdbcTemplate(dataSource);
    }

    @Override
    public List<Payer> listPayers() {
        String sql = "SELECT * FROM payer";
        return jdbcTemp.query(sql, new PayerMapper());
    }

    @Override
    public Payer findPayerbyId(int payer_id) {
        String sql = "SELECT * FROM payer WHERE payer_id LIKE ?";
        List<Payer> list = jdbcTemp.query(sql, new Object[]{payer_id},new PayerMapper());
        return list.get(0);
    }

    @Override
    public Payer findPayerbyNIP(String nip) {
        String sql = "SELECT * FROM payer WHERE nip LIKE ?";
        List<Payer> list = jdbcTemp.query(sql, new Object[]{nip}, new PayerMapper());
        return list.get(0);
    }

    @Override
    public List<Payer> listPayerbyName(String name) {
        String sql = "SELECT * FROM payer WHERE name LIKE '%?%'";
        return jdbcTemp.query(sql, new PayerMapper(), name);
    }

    @Override
    public void insertPayer(String name, String street, String line2, String town, String postcode, String nip, BigInteger telephone, BigInteger mobile, String email) {
        String sql = "INSERT INTO payer (name, address_line_1, address_line_2, address_line_3, postcode, nip, telephone, mobile, email_address) VALUES(?,?,?,?,?,?,?,?,?)";
        jdbcTemp.update(sql,name, street, line2, town, postcode, nip, telephone, mobile, email);
    }

}
