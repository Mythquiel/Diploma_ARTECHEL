package com.artechel.aplikacja.DAO;

import com.artechel.aplikacja.model.Users;

import java.util.List;

public interface UsersDAO {

    List<Users> listUsers();
    Users findUserbyID();
    Users findUserbyName(String name);
    Users logIn(String name, String password);
    void insert(String username, String password);
}
