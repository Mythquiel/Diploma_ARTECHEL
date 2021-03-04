package com.artechel.aplikacja.mappers;

import com.artechel.aplikacja.model.Payer;
import org.springframework.jdbc.core.RowMapper;

import java.math.BigInteger;
import java.sql.ResultSet;
import java.sql.SQLException;

public class PayerMapper implements RowMapper<Payer> {
    @Override
    public Payer mapRow(ResultSet resultSet, int i) throws SQLException {
        Payer payer = new Payer();
        payer.setPayer_id(resultSet.getInt("payer_id"));
        payer.setAddress_line_1(resultSet.getString("address_line_1"));
        payer.setAddress_line_2(resultSet.getString("address_line_2"));
        payer.setAddress_line_3(resultSet.getString("address_line_3"));
        payer.setEmail_address(resultSet.getString("email_address"));
        payer.setMobile(BigInteger.valueOf(resultSet.getLong("mobile")));
        payer.setName(resultSet.getString("name"));
        payer.setNip(resultSet.getString("nip"));
        payer.setPostcode(resultSet.getString("postcode"));
        payer.setTelephone(BigInteger.valueOf(resultSet.getLong("telephone")));
        return payer;
    }
}
