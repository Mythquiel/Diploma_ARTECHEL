package com.artechel.aplikacja.mappers;

import com.artechel.aplikacja.model.Users;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class UsersMapper implements RowMapper<Users> {
    @Override
    public Users mapRow(ResultSet resultSet, int i) throws SQLException {
        Users users = new Users();
        users.setUser_id(resultSet.getInt("user_id"));
        users.setName(resultSet.getString("name"));
        users.setPassword(resultSet.getString("password"));
        return users;
    }
}
