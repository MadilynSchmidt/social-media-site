package com.maddisportfolio.post;

import com.maddisportfolio.friendrequest.FriendRequest;
import com.maddisportfolio.friendrequest.FriendRequestDao;
import com.maddisportfolio.friendrequest.FriendRequestSerivce;
import com.maddisportfolio.friendrequest.FriendRequestStatus;
import com.maddisportfolio.user.User;
import com.maddisportfolio.user.UserDao;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class FriendRequestServiceTest {

    @InjectMocks
    FriendRequestSerivce serviceUnderTest;

    @Mock
    UserDao userDao;

    @Mock
    FriendRequestDao friendRequestDao;

    @Before
    public void setUp(){
        serviceUnderTest = new FriendRequestSerivce();
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testCreateFriendRequestUsersAreSamePerson(){
        //Arrange
        String senderEmailAddress = "Navi@navi.com";
        long recipientId = 30;
        User navi = new User();
        Mockito.when(userDao.findOneByEmailAddressIgnoreCase(senderEmailAddress)).thenReturn(navi);
        Mockito.when(userDao.findById(recipientId)).thenReturn(Optional.of(navi));
        navi.setId(30);

        //Act
        serviceUnderTest.createFriendRequest(senderEmailAddress, recipientId);

        //Assert
        Mockito.verify(friendRequestDao, Mockito.times(0)).save(Mockito.any());
    }

    @Test
    public void testCreateFriendRequestExistingFriendRequest() {
        //Arrange
        String senderEmailAddress = "Navi@navi.com";
        long recipientId = 5;
        long senderId = 10;
        User navi = new User();
        User togepi = new User();
        navi.setId(5);
        togepi.setId(10);
        FriendRequest friendRequest = new FriendRequest();

        Mockito.when(userDao.findOneByEmailAddressIgnoreCase(senderEmailAddress)).thenReturn(navi);
        Mockito.when(userDao.findById(recipientId)).thenReturn(Optional.of(togepi));
        Mockito.when(friendRequestDao.findOneBySenderAndRecipient(navi, togepi)).thenReturn(friendRequest);

        //Act
        serviceUnderTest.createFriendRequest(senderEmailAddress, recipientId);

        //Assert
        Mockito.verify(friendRequestDao, Mockito.times(0)).save(Mockito.any());
    }

    @Test
    public void testCreateFriendRequestAlreadyPending() {
        //Arrange
        String senderEmailAddress = "Navi@cat.com";
        long recipientId = 20;
        long senderId = 25;
        User navi = new User();
        User togepi = new User();
        navi.setId(20);
        togepi.setId(25);
        FriendRequest friendRequest = new FriendRequest();

        Mockito.when(userDao.findOneByEmailAddressIgnoreCase(senderEmailAddress)).thenReturn(togepi);
        Mockito.when(userDao.findById(recipientId)).thenReturn(Optional.of(navi));
        Mockito.when(friendRequestDao.findOneBySenderAndRecipient(togepi, navi)).thenReturn(friendRequest);

        //Act
        serviceUnderTest.createFriendRequest(senderEmailAddress, recipientId);

        //Assert
        Mockito.verify(friendRequestDao, Mockito.times(0)).save(Mockito.any());
    }

    @Test
    public void testCreateFriendRequestSavesPending() {
        //Arrange
        String senderEmailAddress = "Navi@cat.com";
        long recipientId = 30;
        long senderId = 35;
        User navi = new User();
        User togepi = new User();
        navi.setId(30);
        togepi.setId(35);
        FriendRequest friendRequest = new FriendRequest();

        Mockito.when(userDao.findOneByEmailAddressIgnoreCase(senderEmailAddress)).thenReturn(togepi);
        Mockito.when(userDao.findById(recipientId)).thenReturn(Optional.of(navi));

        //Act
        serviceUnderTest.createFriendRequest(senderEmailAddress, recipientId);

        //Assert
        Mockito.verify(friendRequestDao, Mockito.times(1)).save(Mockito.any());
    }

    @Test
    public void testGetPendingFriendRequestForUser() {
        //Arrange
        String loggedInUserEmailAddress = "Navi@cat.com";
        User navi = new User();
        List<FriendRequest> friendRequests = new ArrayList<>();
        FriendRequest friendRequest1 = new FriendRequest();
        friendRequest1.setFriendRequestStatus(FriendRequestStatus.PENDING);
        FriendRequest friendRequest2 = new FriendRequest();
        friendRequest2.setFriendRequestStatus(FriendRequestStatus.PENDING);

        friendRequests.add(friendRequest1);
        friendRequests.add(friendRequest2);


        Mockito.when(userDao.findOneByEmailAddressIgnoreCase(loggedInUserEmailAddress)).thenReturn(navi);
        Mockito.when(friendRequestDao.findAllByRecipientAndFriendRequestStatus(navi, FriendRequestStatus.PENDING)).thenReturn(friendRequests);

        //Act
        List<FriendRequest> returnedList = serviceUnderTest.getPendingFriendRequestsForUser(loggedInUserEmailAddress);

        //Assert
        Assert.assertEquals(friendRequests, returnedList);
        Assert.assertEquals(2, returnedList.size());
        Assert.assertEquals(friendRequest1.getFriendRequestStatus(), FriendRequestStatus.PENDING);
        Assert.assertEquals(friendRequest1.getFriendRequestStatus(), FriendRequestStatus.PENDING);

    }

    @Test
    public void testAcceptFriendRequestUsersAreNotSamePerson() {
        //Arrange
        String loggedInUserEmailAddress = "Navi@cat.com";
        User navi = new User();
        long friendRequestId = 10;
        User recipient = new User();
        FriendRequest friendRequest = new FriendRequest();
        friendRequest.setRecipient(recipient);
        long recipientId = 30;
        long senderId = 35;
        navi.setId(30);
        recipient.setId(35);


        Mockito.when(userDao.findOneByEmailAddressIgnoreCase(loggedInUserEmailAddress)).thenReturn(navi);
        Mockito.when(friendRequestDao.getOne(friendRequestId)).thenReturn(friendRequest);

        //Act
        serviceUnderTest.acceptFriendRequest(loggedInUserEmailAddress, friendRequestId);

        //Assert
        Mockito.verify(friendRequestDao, Mockito.times(0)).save(Mockito.any());
    }

    @Test
    public void testAcceptFriendRequestSaves() {
        //Arrange
        String loggedInUserEmailAddress = "Navi@cat.com";
        long friendRequestId = 10;
        User navi = new User();
        User recipient = new User();
        FriendRequest friendRequest = new FriendRequest();
        long recipientId = 30;
        long senderId = 30;
        navi.setId(30);
        recipient.setId(30);
        friendRequest.setRecipient(recipient);

        Mockito.when(userDao.findOneByEmailAddressIgnoreCase(loggedInUserEmailAddress)).thenReturn(navi);
        Mockito.when(friendRequestDao.getOne(friendRequestId)).thenReturn(friendRequest);

        //Act
        serviceUnderTest.acceptFriendRequest(loggedInUserEmailAddress, friendRequestId);


        //Assert
        Mockito.verify(friendRequestDao).save(friendRequest);
    }

    @Test
    public void testDeclineFriendRequestUsersAreNotTheSame(){
        //Arrange
        String loggedInUserEmailAddress = "Navi@cat.com";
        User navi = new User();
        long friendRequestId = 10;
        User recipient = new User();
        FriendRequest friendRequest = new FriendRequest();
        friendRequest.setRecipient(recipient);
        long recipientId = 30;
        long senderId = 35;
        navi.setId(30);
        recipient.setId(35);


        Mockito.when(userDao.findOneByEmailAddressIgnoreCase(loggedInUserEmailAddress)).thenReturn(navi);
        Mockito.when(friendRequestDao.getOne(friendRequestId)).thenReturn(friendRequest);

        //Act
        serviceUnderTest.declineFriendRequest(loggedInUserEmailAddress, friendRequestId);

        //Assert
        Mockito.verify(friendRequestDao, Mockito.times(0)).save(Mockito.any());
    }

    @Test
    public void testDeclineFriendRequestSaves(){
        //Arrange
        String loggedInUserEmailAddress = "Navi@cat.com";
        long friendRequestId = 10;
        User navi = new User();
        User recipient = new User();
        FriendRequest friendRequest = new FriendRequest();
        long recipientId = 30;
        long senderId = 30;
        navi.setId(30);
        recipient.setId(30);
        friendRequest.setRecipient(recipient);

        Mockito.when(userDao.findOneByEmailAddressIgnoreCase(loggedInUserEmailAddress)).thenReturn(navi);
        Mockito.when(friendRequestDao.getOne(friendRequestId)).thenReturn(friendRequest);

        //Act
        serviceUnderTest.declineFriendRequest(loggedInUserEmailAddress, friendRequestId);

        //Assert
        Mockito.verify(friendRequestDao).save(friendRequest);
    }


    @Test
    public void testGetFriends(){
        //Arrange
        String loggedInUserEmailAddress = "navi@cat.com";
        User navi = new User();
        User togepi = new User();
        User kupo = new User();
        User kimahri = new User();
        User nixie = new User();
        List<FriendRequest> receivedRequests = new ArrayList<>();
        FriendRequest firstFriendRequest = new FriendRequest();
        firstFriendRequest.setSender(togepi);
        FriendRequest secondFriendRequest = new FriendRequest();
        secondFriendRequest.setSender(kupo);
        receivedRequests.add(firstFriendRequest);
        receivedRequests.add(secondFriendRequest);
        List<FriendRequest> sentRequests = new ArrayList<>();
        FriendRequest thirdFriendRequest = new FriendRequest();
        FriendRequest fourthFriendRequest = new FriendRequest();
        thirdFriendRequest.setRecipient(kimahri);
        fourthFriendRequest.setRecipient(nixie);
        sentRequests.add(thirdFriendRequest);
        sentRequests.add(fourthFriendRequest);

        Mockito.when(userDao.findOneByEmailAddressIgnoreCase(loggedInUserEmailAddress)).thenReturn(navi);
        Mockito.when(friendRequestDao.findAllByRecipientAndFriendRequestStatus(navi, FriendRequestStatus.ACCEPTED)).thenReturn(receivedRequests);
        Mockito.when(friendRequestDao.findAllBySenderAndFriendRequestStatus(navi, FriendRequestStatus.ACCEPTED)).thenReturn(sentRequests);

        //Act
        List<User> friends = serviceUnderTest.getFriends(loggedInUserEmailAddress);

        //Assert
        Assert.assertEquals(4, friends.size());
        Assert.assertEquals(togepi, friends.get(0));
        Assert.assertEquals(kupo, friends.get(1));
        Assert.assertEquals(kimahri, friends.get(2));
        Assert.assertEquals(nixie, friends.get(3));





    }

}





