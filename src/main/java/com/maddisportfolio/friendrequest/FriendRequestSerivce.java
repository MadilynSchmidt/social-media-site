package com.maddisportfolio.friendrequest;

import com.maddisportfolio.user.User;
import com.maddisportfolio.user.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FriendRequestSerivce {
    @Autowired
    private UserDao userDao;

    @Autowired
    private FriendRequestDao friendRequestDao;

    public void createFriendRequest(String senderEmailAddress, long recipientId){
        User sender = userDao.findOneByEmailAddressIgnoreCase(senderEmailAddress);
        User recipient = userDao.findById(recipientId).get();
        if(usersAreSamePerson(sender, recipient)){
            return;
        }
        FriendRequest existingFriendRequest = friendRequestDao.findOneBySenderAndRecipient(sender, recipient);
        if(existingFriendRequest != null){
            return;
        }
        FriendRequest friendRequest = new FriendRequest();
        friendRequest.setSender(sender);
        friendRequest.setRecipient(recipient);
        friendRequest.setFriendRequestStatus(FriendRequestStatus.PENDING);
        friendRequestDao.save(friendRequest);
    }

    public List<FriendRequest> getPendingFriendRequestsForUser(String loggedInUserEmailAddress){
        User loggedInUser = userDao.findOneByEmailAddressIgnoreCase(loggedInUserEmailAddress);
        List<FriendRequest> friendRequests = friendRequestDao.findAllByRecipientAndFriendRequestStatus(loggedInUser, FriendRequestStatus.PENDING);
        return friendRequests;
    }

    public void acceptFriendRequest(String loggedInUserEmailAddress, long friendRequestId){
        User loggedInUser = userDao.findOneByEmailAddressIgnoreCase(loggedInUserEmailAddress);
        FriendRequest friendRequest = friendRequestDao.getOne(friendRequestId);
        if(!usersAreSamePerson(loggedInUser, friendRequest.getRecipient())){
            return;
        }
        friendRequest.setFriendRequestStatus(FriendRequestStatus.ACCEPTED);
        friendRequestDao.save(friendRequest);
    }

    public void declineFriendRequest(String loggedInUserEmailAddress, long friendRequestId){
        User loggedInUser = userDao.findOneByEmailAddressIgnoreCase(loggedInUserEmailAddress);
        FriendRequest friendRequest = friendRequestDao.getOne(friendRequestId);
        if(!usersAreSamePerson(loggedInUser, friendRequest.getRecipient())){
            return;
        }
        friendRequest.setFriendRequestStatus((FriendRequestStatus.DECLINED));
        friendRequestDao.save(friendRequest);
    }

    private boolean usersAreSamePerson(User sender, User recipient){
        if(sender.getId() == recipient.getId()){
            return true;
        }
        return false;
    }
}
