package com.maddisportfolio.user;

import org.apache.commons.validator.routines.EmailValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserDao userDao;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public void createUser(User user) throws InvalidEmailException, EmailExistsException{

        User existingUser = userDao.findOneByEmailAddressIgnoreCase(user.getEmailAddress());
        if(existingUser != null){
            throw new EmailExistsException();
        }

        EmailValidator validator = EmailValidator.getInstance();
        if (validator.isValid(user.getEmailAddress())) {
            String encodedPassword = passwordEncoder.encode(user.getPassword());
            user.setPassword(encodedPassword);
            userDao.save(user);
        }
        else{
            throw new InvalidEmailException();
        }
    }

    public List<User> searchUsers(String emailAddress){
        List<User> searchList = userDao.findByEmailAddressIgnoreCaseContaining(emailAddress);
        return searchList;

    }



}
