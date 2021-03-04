package com.artechel.aplikacja.DAO;

import com.artechel.aplikacja.mappers.UsersMapper;
import com.artechel.aplikacja.model.Users;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import java.util.List;

public class UsersDAOImplementation implements UsersDAO {

    JdbcTemplate jdbcTemp;

    public UsersDAOImplementation(DataSource dataSource) {
        jdbcTemp = new JdbcTemplate(dataSource);
    }

    @Override
    public List<Users> listUsers() {
        String sql = "SELECT * FROM users";
        return jdbcTemp.query(sql, new UsersMapper());
    }

    @Override
    public Users findUserbyID() {
        String sql = "SELECT * FROM users WHERE user_id LIKE ?";
        List<Users> list = jdbcTemp.query(sql, new UsersMapper());
        return list.get(0);
    }

    @Override
    public Users findUserbyName(String name) {
        String sql = "SELECT * FROM users WHERE name LIKE ?";

        return jdbcTemp.query(sql, new Object[]{name}, new UsersMapper()).get(0);
    }

    @Override
    public Users logIn(String name, String password) {
        String sql = "SELECT * FROM users WHERE name LIKE ? AND password like ?";
        List<Users> list = jdbcTemp.query(sql, new Object[]{name, password}, new UsersMapper());
        if (list.isEmpty()) {
            return null;
        } else {
            return list.get(0);
        }
    }

    @Override
    public void insert(String username, String password) {
        String sql = "INSERT INTO users (name, password) VALUES (?,?)";
        jdbcTemp.update(sql, username,password);
    }
}
