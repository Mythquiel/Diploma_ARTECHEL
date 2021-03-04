package com.artechel.aplikacja.config;

import com.artechel.aplikacja.DAO.UsersDAO;
import com.artechel.aplikacja.model.Users;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User.UserBuilder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service("userDetailsService")
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private UsersDAO usersDao;

    @Transactional(readOnly = true)
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //dodawanie-szukanie uzytkownika
        Users user = new Users();
        try {
            user = usersDao.findUserbyName(username);
        } catch (Exception e){
            e.printStackTrace();
        }
        UserBuilder builder = null;
        if (user != null) {
            builder = org.springframework.security.core.userdetails.User.withUsername(username);
            builder.password(user.getPassword());
            builder.roles("ADMIN");
        } else {
            throw new UsernameNotFoundException("User not found.");
        }
        return builder.build();
    }
}