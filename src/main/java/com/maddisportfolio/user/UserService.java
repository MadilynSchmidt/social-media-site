package com.maddisportfolio.user;

import com.maddisportfolio.friendrequest.FriendRequest;
import com.maddisportfolio.friendrequest.FriendRequestDao;
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

    @Autowired
    private FriendRequestDao friendRequestDao;

    public User returnUser(String loggedInEmailAddress){
        User userToReturn = userDao.findOneByEmailAddressIgnoreCase(loggedInEmailAddress);
        return userToReturn;
    }

    public void createUser(User user) throws InvalidEmailException, EmailExistsException{

        User existingUser = userDao.findOneByEmailAddressIgnoreCase(user.getEmailAddress());
        if(existingUser != null){
            throw new EmailExistsException();
        }

        EmailValidator validator = EmailValidator.getInstance();
        if (validator.isValid(user.getEmailAddress())) {
            String encodedPassword = passwordEncoder.encode(user.getPassword());
            user.setPassword(encodedPassword);
            user.setEmailAddress(user.getEmailAddress().toLowerCase());
            userDao.save(user);
        }
        else{
            throw new InvalidEmailException();
        }
    }

    public List<User> searchUsers(String emailAddressToSearchFor, String loggedInUserEmailAddress){
        List<User> searchResults = userDao.findByEmailAddressIgnoreCaseContaining(emailAddressToSearchFor);
        User loggedInUser = userDao.findOneByEmailAddressIgnoreCase(loggedInUserEmailAddress);
        List<FriendRequest> correspondingFriendRequests = friendRequestDao.findAllBySenderAndRecipientIn(loggedInUser, searchResults);
        for(FriendRequest friendRequest : correspondingFriendRequests){
            User correspondingUser = searchResults.stream().filter(x -> x.getId() == friendRequest.getRecipient().getId()).findFirst().get();
            correspondingUser.setHasBeenSentFriendRequestByLoggedInUser(true);
        }
        return searchResults;
    }


    public void updateUserProfile(String firstName, String lastName, String location, String loggedInEmailAddress){
        User updatedUser = returnUser(loggedInEmailAddress);
        updatedUser.setFirstName(firstName);
        updatedUser.setLastName(lastName);
        updatedUser.setLocation(location);
        userDao.save(updatedUser);
        }
    }




