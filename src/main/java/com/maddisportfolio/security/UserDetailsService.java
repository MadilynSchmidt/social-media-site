package com.maddisportfolio.security;

import com.maddisportfolio.user.User;
import com.maddisportfolio.user.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import java.util.ArrayList;

@Service
public class UserDetailsService implements org.springframework.security.core.userdetails.UserDetailsService {

    @Autowired
    private UserDao userDao;

    @Override
    //method that takes in a username puts it in a loadUserByUsername and throws exception if not found
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //in dao findOneByEmailAddressIgnoreCase taking in a username putting it in excistingUser
        User existingUser = userDao.findOneByEmailAddressIgnoreCase(username);
        //if existingUser is null
        if(existingUser == null){
            //display "The specified user cannot be found."
            throw new UsernameNotFoundException("The specified user cannot be found.");
        }
        // ????????????
        return new org.springframework.security.core.userdetails.User(username, existingUser.getPassword(), new ArrayList<>());
    }
}
