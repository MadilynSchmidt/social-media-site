package com.maddisportfolio.post;

import com.maddisportfolio.friendrequest.FriendRequestDao;
import com.maddisportfolio.friendrequest.FriendRequestSerivce;
import com.maddisportfolio.user.User;
import com.maddisportfolio.user.UserDao;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
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




}
