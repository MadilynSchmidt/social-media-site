package com.maddisportfolio.user;

import com.maddisportfolio.friendrequest.FriendRequest;
import com.maddisportfolio.friendrequest.FriendRequestDao;
import com.maddisportfolio.friendrequest.FriendRequestSerivce;
import com.maddisportfolio.friendrequest.FriendRequestStatus;
import org.apache.commons.validator.routines.EmailValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserDao userDao;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private FriendRequestDao friendRequestDao;

    @Autowired
    private FriendRequestSerivce friendRequestSerivce;

    public User getUser(String loggedInEmailAddress) {
        User userToReturn = userDao.findOneByEmailAddressIgnoreCase(loggedInEmailAddress);
        return userToReturn;
    }

    public void createUser(User user) throws InvalidEmailException, EmailExistsException {

        User existingUser = userDao.findOneByEmailAddressIgnoreCase(user.getEmailAddress());
        if (existingUser != null) {
            throw new EmailExistsException();
        }

        EmailValidator validator = EmailValidator.getInstance();
        if (validator.isValid(user.getEmailAddress())) {
            String encodedPassword = passwordEncoder.encode(user.getPassword());
            user.setPassword(encodedPassword);
            user.setEmailAddress(user.getEmailAddress().toLowerCase());
            user.setFirstName(user.getFirstName().trim());
            user.setLastName(user.getLastName().trim());
            user.setLocation(user.getLocation().trim());
            if(user.getFirstName().length() < 1 || user.getLastName().length() < 1 || user.getLocation().length() < 1 ){
                    throw new RuntimeException();
            }
            userDao.save(user);
        }
        else {
            throw new InvalidEmailException();
        }
    }

    public List<User> searchUsers(String firstName, String lastName, String loggedInUserEmailAddress) {
        List<User> searchResults;
        if(firstName != null && !firstName.equals("") && lastName != null && !lastName.equals("")){
            searchResults = userDao.findByFirstNameIgnoreCaseContainingAndLastNameIgnoreCaseContaining(firstName, lastName);
        }
        else if(firstName != null && !firstName.equals("")){
            searchResults = userDao.findByFirstNameIgnoreCaseContaining(firstName);
        }
        else if(lastName != null && !lastName.equals("")){
            searchResults = userDao.findByLastNameIgnoreCaseContaining(lastName);
        }
        else{
            searchResults = new ArrayList<>();
        }
        User loggedInUser = userDao.findOneByEmailAddressIgnoreCase(loggedInUserEmailAddress);
        List<FriendRequest> correspondingFriendRequests = friendRequestDao.findAllBySenderAndRecipientIn(loggedInUser, searchResults);
        for (FriendRequest friendRequest : correspondingFriendRequests) {
            User correspondingUser = searchResults.stream().filter(x -> x.getId() == friendRequest.getRecipient().getId()).findFirst().get();
            correspondingUser.setHasBeenSentFriendRequestByLoggedInUser(true);
        }
        List<FriendRequest> pendingRequestsToLoggedInUser = friendRequestDao.findAllByRecipientAndFriendRequestStatus(loggedInUser, FriendRequestStatus.PENDING);
        for(FriendRequest friendRequest : pendingRequestsToLoggedInUser){
            Optional<User> correspondingSender = searchResults.stream().filter(x -> x.getId() == friendRequest.getSender().getId()).findFirst();
            if(correspondingSender.isPresent()){
                correspondingSender.get().setHasReceivedRequestFromSearchedUser(true);
            }

        }
        return searchResults;
    }


    public void updateUserProfile(String firstName, String lastName, String location, String loggedInEmailAddress, String timezone) {
        User updatedUser = getUser(loggedInEmailAddress);
        updatedUser.setFirstName(firstName);
        updatedUser.setLastName(lastName);
        updatedUser.setLocation(location);
        updatedUser.setTimezone(timezone);
        userDao.save(updatedUser);
    }


    public User findFriend(long userId, String loggedInUserEmailAddress) {
        List<User> friends = friendRequestSerivce.getFriends(loggedInUserEmailAddress);
        for(User user : friends){
            if(user.getId() == userId){
                return user;
            }
        }
        throw new RuntimeException();

    }


}