package com.artechel.aplikacja.DAO;

import com.artechel.aplikacja.mappers.ProductMapper;
import com.artechel.aplikacja.model.Product;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import java.util.List;

public class ProductDAOImplementation implements ProductDAO {

    JdbcTemplate jdbcTemp;

    public ProductDAOImplementation(DataSource dataSource){
        jdbcTemp = new JdbcTemplate(dataSource);
    }

    @Override
    public List<Product> listProducts() {
        String sql = "SELECT * FROM product ORDER BY name";
        return jdbcTemp.query(sql,new ProductMapper());
    }
}
