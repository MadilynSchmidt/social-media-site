package com.maddisportfolio.friendrequest;

import com.maddisportfolio.user.User;
import com.maddisportfolio.user.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;

@Service
public class FriendRequestSerivce {
    @Autowired
    private UserDao userDao;

    @Autowired
    private FriendRequestDao friendRequestDao;

    //method that doesn't return anything takes in a senderEmailAddress and recipientId and puts it in createFriendRequest
    public void createFriendRequest(String senderEmailAddress, long recipientId){
        //in dao findsOneByEMailAddressIgnoreCase taking in the senderEmailAddress and putting it in sender
        User sender = userDao.findOneByEmailAddressIgnoreCase(senderEmailAddress);
        //in dao finds id takes in recipientId gets it, and puts it in recipient
        User recipient = userDao.findById(recipientId).get();
        //if the sender and recipient are the same person, return
        if(usersAreSamePerson(sender, recipient)){
            return;
        }
        //in dao findOneBySenderAndRecipient taking in sender and recipient and putting it in existingFriendRequest
        FriendRequest existingFriendRequest = friendRequestDao.findOneBySenderAndRecipient(sender, recipient);
        //if the existingFriendReuqest is not null, return
        if(existingFriendRequest != null){
            return;
        }
        //in dao findOneBySenderAndRecipient taking in recipient and sender and putting it in alreadyPendingRequest
        FriendRequest alreadyPendingRequest = friendRequestDao.findOneBySenderAndRecipient(recipient, sender);
        //if the alreadyPendingRequest is not null, return
        if(alreadyPendingRequest != null){
            return;
        }
        //makes a new friendRequest
        FriendRequest friendRequest = new FriendRequest();
        //sets sender in friendRequest
        friendRequest.setSender(sender);
        //sets recipient in friendRequest
        friendRequest.setRecipient(recipient);
        //sets the friendRequest status to pending in friendRequest
        friendRequest.setFriendRequestStatus(FriendRequestStatus.PENDING);
        //in dao saves friendRequest
        friendRequestDao.save(friendRequest);
    }

    //method that takes in loggedInUserEmailAddress puts it in a list of getPendingFriendRequestsForUser
    public List<FriendRequest> getPendingFriendRequestsForUser(String loggedInUserEmailAddress){
        // in dao findOneByEmailAddressIgnoreCase taking in the loggedInUserEmailAddress and putting it in loggedInUser
        User loggedInUser = userDao.findOneByEmailAddressIgnoreCase(loggedInUserEmailAddress);
        // in dao findAllByRecipientAndFriendRequestStatus taking in a loggedInUser, and a pending request and putting it in a list of friendRequests
        List<FriendRequest> friendRequests = friendRequestDao.findAllByRecipientAndFriendRequestStatus(loggedInUser, FriendRequestStatus.PENDING);
        //returns friendRequests
        return friendRequests;
    }

    // a method that doesn't return anything takes in loggedInUserEmailAddress and friendRequestId and puts it in acceptFriendRequest
    public void acceptFriendRequest(String loggedInUserEmailAddress, long friendRequestId){
        // in dao findOneByEmailAddressIgnoreCase takes in loggedInUserEmailAddress and puts it in loggedInUser
        User loggedInUser = userDao.findOneByEmailAddressIgnoreCase(loggedInUserEmailAddress);
        // in dao, gets one and takes in friendRequestId and puts it in friendRequest
        FriendRequest friendRequest = friendRequestDao.getOne(friendRequestId);
        //if not usersAreSamePerson taking in the loggedInUser and the recipient of the friend request, return
        if(!usersAreSamePerson(loggedInUser, friendRequest.getRecipient())){
            return;
        }
        // taking in the accepted friend request in friend request
        friendRequest.setFriendRequestStatus(FriendRequestStatus.ACCEPTED);
        //saves friend requests to dao
        friendRequestDao.save(friendRequest);
    }

    //method that doesn't return anything takes in loggedInUserEmailAddress and friendRequestId and puts it in declineFriendRequest
    public void declineFriendRequest(String loggedInUserEmailAddress, long friendRequestId){
        //in user dao findOneByEmailAddressIgnoreCase taking in the loggedInUserEmailAddress and puts it in loggedInUser
        User loggedInUser = userDao.findOneByEmailAddressIgnoreCase(loggedInUserEmailAddress);
        //in dao takes in a friendRequestId of one and putting it in friendRequest
        FriendRequest friendRequest = friendRequestDao.getOne(friendRequestId);
        //if not usersAreSamePerson taking in the loggedInUser and the friendRequest sent by recipient, return
        if(!usersAreSamePerson(loggedInUser, friendRequest.getRecipient())){
            return;
        }
        //sets friendRequest status to declined
        friendRequest.setFriendRequestStatus((FriendRequestStatus.DECLINED));
        //saves friend request in dao
        friendRequestDao.save(friendRequest);
    }

    //method that takes in a sender, and recipient and puts it in usersAreTheSamePerson
    private boolean usersAreSamePerson(User sender, User recipient){
        //if the sender is the same as the recipient return true
        if(sender.getId() == recipient.getId()){
            return true;
        }
        //otherwise false
        return false;
    }

    //method that takes in a loggedInUserEmailAddress and puts it in a list of getFriends
    public List<User> getFriends (String loggedInUserEmailAddress){
        //makes a new list of friends
        List<User> friends = new ArrayList<>();
        //in dao findOneByEmailAddressIgnoreCase takes in loggedInUserEmailAddress and puts it in loggedInUser
        User loggedInUser = userDao.findOneByEmailAddressIgnoreCase(loggedInUserEmailAddress);
        //in dao findAllByRecipientAndFriendRequestStatus taking in loggedInUser, and accepted status puts it in a list of receivedRequests
        List<FriendRequest> receivedRequests = friendRequestDao.findAllByRecipientAndFriendRequestStatus(loggedInUser, FriendRequestStatus.ACCEPTED);
       //loop for every friendRequest in receivedRequests
        for(FriendRequest friendRequest : receivedRequests){
            //add friends from senders friendRequest
            friends.add(friendRequest.getSender());
        }
        //in dao findAllBySenderAndFriendRequestStatus taking in the logged in user and accepted request putting it in a list of sentRequests
        List<FriendRequest> sentRequests = friendRequestDao.findAllBySenderAndFriendRequestStatus(loggedInUser, FriendRequestStatus.ACCEPTED);
        //loop for every friendRequest in sentRequests
        for(FriendRequest friendRequest : sentRequests){
            //add friends from recipient friendRequest
            friends.add(friendRequest.getRecipient());
        }
        //returns your friends
        return friends;
    }



}
