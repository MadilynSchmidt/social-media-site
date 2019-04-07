package com.maddisportfolio.user;

import org.apache.commons.validator.routines.EmailValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserDao userDao;

    public void createUser(User user) throws InvalidEmailException, EmailExistsException{

        User existingUser = userDao.findOneByEmailAddressIgnoreCase(user.getEmailAddress());
        if(existingUser != null){
            throw new EmailExistsException();
        }

        EmailValidator validator = EmailValidator.getInstance();
        if (validator.isValid(user.getEmailAddress())) {
            userDao.save(user);
        }
        else{
            throw new InvalidEmailException();
        }
    }

}
