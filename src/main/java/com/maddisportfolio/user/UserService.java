package com.maddisportfolio.user;

import com.maddisportfolio.friendrequest.FriendRequest;
import com.maddisportfolio.friendrequest.FriendRequestDao;
import com.maddisportfolio.friendrequest.FriendRequestSerivce;
import com.maddisportfolio.friendrequest.FriendRequestStatus;
import org.apache.commons.validator.routines.EmailValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
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


    //method takes in a loggedInEmailAddress called getUser
    public User getUser(String loggedInEmailAddress) {
        //in Dao finds findOneByEmailAddressIgnoreCase taking in a loggedInEmailAddress keeps it in usersToReturn in User
        User userToReturn = userDao.findOneByEmailAddressIgnoreCase(loggedInEmailAddress);
        //return userToReturn
        return userToReturn;
    }

    //method that doesn't return anything takes in a user and makes an exception for an invalidEmailException or if an email already exists
    public void createUser(User user) throws InvalidEmailException, EmailExistsException {

        //in Dao finds findOneByEmailAddressIgnoreCase takes in users emailAddress and keeps it in existingUser
        User existingUser = userDao.findOneByEmailAddressIgnoreCase(user.getEmailAddress());
        //if the existingUser is not null
        if (existingUser != null) {
            //throws exception if not null
            throw new EmailExistsException();
        }

        //validates emails on one instance
        EmailValidator validator = EmailValidator.getInstance();
        //if users emailaddress is valid
        if (validator.isValid(user.getEmailAddress())) {
            //gets users password and encodes it
            String encodedPassword = passwordEncoder.encode(user.getPassword());
            //sets users password to encoded
            user.setPassword(encodedPassword);
            //sets users email address to all lower case
            user.setEmailAddress(user.getEmailAddress().toLowerCase());
            //trims users first name of any extra spacing or white space
            user.setFirstName(user.getFirstName().trim());
            //trims users last name of any extra spacing or white space
            user.setLastName(user.getLastName().trim());
            //trims users location of any extra spacing or white space
            user.setLocation(user.getLocation().trim());
            //users first name, last name, and location length has to be less than 1
            if(user.getFirstName().length() < 1 || user.getLastName().length() < 1 || user.getLocation().length() < 1 ){
                //if not less than one than throws exception
                    throw new RuntimeException();
            }
            //saves user in dao
            userDao.save(user);
        }
        //otherwise throw exception
        else {
            throw new InvalidEmailException();
        }
    }

    //method that takes in a list of users called searchUsers, the List takes in emailAddressToSearchFor and loggedInUserEmailAddress
    public List<User> searchUsers(String emailAddressToSearchFor, String loggedInUserEmailAddress) {
        //in Dao findByEmailAddressIgnoreCaseContaining takes in an emailAddressToSearchFor and puts it in searchResults
        List<User> searchResults = userDao.findByEmailAddressIgnoreCaseContaining(emailAddressToSearchFor);
        //in Dao findOneByEmailAddressIgnoreCase takes in loggedInUserEmailAddress and puts it in loggedInUser
        User loggedInUser = userDao.findOneByEmailAddressIgnoreCase(loggedInUserEmailAddress);
        //in Dao findAllBySenderAndRecipientIn takes in loggedInUser and searchResults and puts it in a list of correspondingFriendRequests
        List<FriendRequest> correspondingFriendRequests = friendRequestDao.findAllBySenderAndRecipientIn(loggedInUser, searchResults);
        // for every friendRequests in the correspondingFriendRequests
        for (FriendRequest friendRequest : correspondingFriendRequests) {
            //loops for every friendRequest that the recipient with id with optional filters out the ids in search results and puts it in correspondingUsers
            User correspondingUser = searchResults.stream().filter(x -> x.getId() == friendRequest.getRecipient().getId()).findFirst().get();
            //friend request that is sent by the logged in user if it is true then go ahead
            correspondingUser.setHasBeenSentFriendRequestByLoggedInUser(true);
        }
        //in dao finds friend request status of pending takes in a logged in user and the pending friend request and puts it in pendingRequestsToLoggedInUser
        List<FriendRequest> pendingRequestsToLoggedInUser = friendRequestDao.findAllByRecipientAndFriendRequestStatus(loggedInUser, FriendRequestStatus.PENDING);
        //for loop for every friendRequest that is a pendingRequestToLoggedInUser
        for(FriendRequest friendRequest : pendingRequestsToLoggedInUser){
            //loops in for every friendRequest that the sender sent filter searchResults by id saves in on optional of list of correspondingSender
            Optional<User> correspondingSender = searchResults.stream().filter(x -> x.getId() == friendRequest.getSender().getId()).findFirst();
            //if the correspondingSender is there
            if(correspondingSender.isPresent()){
                //it is set to be true
                correspondingSender.get().setHasReceivedRequestFromSearchedUser(true);
            }

        }
        //return searchResults
        return searchResults;
    }

    //method that doesn't return anything takes in a firstName, lastName, location and loggedInEmailAddress and puts it in updateUserProfile
    public void updateUserProfile(String firstName, String lastName, String location, String loggedInEmailAddress) {
        //gets loggedInEmailAddress on User and puts it in updatedUser
        User updatedUser = getUser(loggedInEmailAddress);
        //sets the users first name and updates updatedUser
        updatedUser.setFirstName(firstName);
        //sets the users last name and updates updatedUser
        updatedUser.setLastName(lastName);
        //sets the users location and updates updatedUser
        updatedUser.setLocation(location);
        //saves the updatedUser in dao
        userDao.save(updatedUser);
    }

    //method that takes in a userId, and loggedInUserEmailAddress and puts it in findFriend
    public User findFriend(long userId, String loggedInUserEmailAddress) {
        //in the friendRequestService you get friends of the loggedInUserEmailAddress and put in in a list of users called friends
        List<User> friends = friendRequestSerivce.getFriends(loggedInUserEmailAddress);
        //loops for every user of friends
        for(User user : friends){
            //in loop if users id is the same as users id
            if(user.getId() == userId){
                //return user
                return user;
            }
        }
        //if not throw RuntimeException
        throw new RuntimeException();

    }


}