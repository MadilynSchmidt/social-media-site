package com.maddisportfolio.post;
import com.maddisportfolio.friendrequest.FriendRequestDao;
import com.maddisportfolio.friendrequest.FriendRequestSerivce;
import com.maddisportfolio.user.*;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.*;

public class UserServiceTest {

    @InjectMocks
    UserService serviceUnderTest;

    @Mock
    UserDao userDao;

    @Mock
    FriendRequestDao friendRequestDao;

    @Mock
    FriendRequestSerivce friendRequestSerivce;

    @Mock
    UserService userService;

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Before
    public void setup() {
        serviceUnderTest = new UserService();
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testGetUser() {
        //Arrange
        String loggedInUserEmailAddress = "navi@cat.com";
        User result = new User();
        Mockito.when(userDao.findOneByEmailAddressIgnoreCase(loggedInUserEmailAddress)).thenReturn(result);

        //Act
        User getUser = serviceUnderTest.getUser(loggedInUserEmailAddress);

        //Assert
        Assert.assertEquals(result, getUser);

    }

}

