package com.artechel.aplikacja.mappers;

import com.artechel.aplikacja.model.Product;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ProductMapper implements RowMapper<Product> {
    @Override
    public Product mapRow(ResultSet resultSet, int i) throws SQLException {
        Product product = new Product();
        product.setName(resultSet.getString("name"));
        product.setProduct_id(resultSet.getInt("product_id"));
        product.setUnit(resultSet.getString("unit"));
        product.setVat(resultSet.getInt("vat"));
        return product;
    }
}
