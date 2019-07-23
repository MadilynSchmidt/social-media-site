package com.maddisportfolio.user;
import com.maddisportfolio.friendrequest.FriendRequest;
import com.maddisportfolio.friendrequest.FriendRequestDao;
import com.maddisportfolio.friendrequest.FriendRequestSerivce;
import com.maddisportfolio.friendrequest.FriendRequestStatus;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import java.util.ArrayList;
import java.util.List;

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
    FriendRequestSerivce friendRequestSerivceService;

    @Mock
    UserService userService;

    @Mock
    PasswordEncoder passwordEncoder;

    @Mock
    FriendRequest friendRequest;

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
        User navi = new User();
        Mockito.when(userDao.findOneByEmailAddressIgnoreCase(loggedInUserEmailAddress)).thenReturn(navi);

        //Act
        User result = serviceUnderTest.getUser(loggedInUserEmailAddress);

        //Assert
        Assert.assertEquals(navi, result);

    }

    @Test
    public void testCreateUserEmailExistsException() throws InvalidEmailException, EmailExistsException{
        //Arrange
        User existingUser = new User();
        String emailAddress = "kupo@cat.com";
        existingUser.setEmailAddress(emailAddress);
        Mockito.when(userDao.findOneByEmailAddressIgnoreCase(emailAddress)).thenReturn(existingUser);
        expectedException.expect(EmailExistsException.class);
        //Act
        serviceUnderTest.createUser(existingUser);
    }

    @Test
    public void testCreateUserWithInvalidEmail() throws InvalidEmailException, EmailExistsException{
        //Arrange
        String emailAdress = "hahahaha";
        User user = new User();
        user.setEmailAddress(emailAdress);
        expectedException.expect(InvalidEmailException.class);

        //Act
        serviceUnderTest.createUser(user);
    }

    @Test
    public void testCreateUserWithEmptyFirstName() throws InvalidEmailException, EmailExistsException{
     //Arrange
        User user = new User();
        String firstName = " ";
        user.setFirstName(firstName);
        String lastName = "schmidt";
        user.setLastName(lastName);
        String location = "Colorado Springs";
        user.setLocation(location);
        String password = "password";
        user.setPassword(password);
        String emailAddress = "kupo@cat.com";
        user.setEmailAddress(emailAddress);
        expectedException.expect(RuntimeException.class);

        //Act
        serviceUnderTest.createUser(user);
    }

    @Test
    public void testCreateUserWithEmptyLastName() throws InvalidEmailException, EmailExistsException{
        //Arrange
        User user = new User();
        String firstName = "Kupo";
        user.setFirstName(firstName);
        String lastName = " ";
        user.setLastName(lastName);
        String location = "Colorado Springs";
        user.setLocation(location);
        String password = "password";
        user.setPassword(password);
        String emailAddress = "kupo@cat.com";
        user.setEmailAddress(emailAddress);
        expectedException.expect(RuntimeException.class);

        //Act
        serviceUnderTest.createUser(user);
    }

    @Test
    public void testCreateUserWithEmptyLocation() throws InvalidEmailException, EmailExistsException{
        //Arrange
        User user = new User();
        String firstName = "Kupo";
        user.setFirstName(firstName);
        String lastName = "McConville";
        user.setLastName(lastName);
        String location = " ";
        user.setLocation(location);
        String password = "password";
        user.setPassword(password);
        String emailAddress = "kupo@cat.com";
        user.setEmailAddress(emailAddress);
        expectedException.expect(RuntimeException.class);

        //Act
        serviceUnderTest.createUser(user);
    }

    @Test
    public void createUserSaveUser() throws InvalidEmailException, EmailExistsException{
        //Arrange
        User user = new User();
        String firstName = "Kupo";
        user.setFirstName(firstName);
        String lastName = "McConville";
        user.setLastName(lastName);
        String location = "Colorado Springs";
        user.setLocation(location);
        String password = "kupokupo";
        user.setPassword(password);
        String emailAddress = "kupo@cat.com";
        user.setEmailAddress(emailAddress);
        Mockito.when(userDao.findOneByEmailAddressIgnoreCase(user.getEmailAddress())).thenReturn(null);
        //Act
        serviceUnderTest.createUser(user);

        //Assert
        Mockito.verify(userDao).save(user);
    }

    @Test
    public void testSearchUsersByFirstNameAndLastName(){
        //Arrange
        User user = new User();
        String firstName = "kupo";
        user.setFirstName(firstName);
        String lastName = "mcconville";
        user.setLastName(lastName);
        String loggedInUserEmailAddress = "kupo@cat.com";
        user.setEmailAddress(loggedInUserEmailAddress);
        List<User> searchUser = new ArrayList<>();
        searchUser.add(user);

        Mockito.when(userDao.findByFirstNameIgnoreCaseContainingAndLastNameIgnoreCaseContaining(firstName, lastName)).thenReturn(searchUser);

        //Act
        List<User> returnedUser = serviceUnderTest.searchUsers(firstName, lastName, loggedInUserEmailAddress);

        //Assert
        Assert.assertEquals(user, returnedUser.get(0));
        Assert.assertEquals(1, returnedUser.size());
    }

    @Test
    public void testSearchUsersByFirstName(){
        //Arrange
        User user = new User();
        String firstName = "kupo";
        user.setFirstName(firstName);
        String lastName = "";
        user.setLastName(lastName);
        String loggedInUserEmailAddress = "kupo@cat.com";
        user.setEmailAddress(loggedInUserEmailAddress);
        List<User> searchUsers = new ArrayList<>();
        searchUsers.add(user);

        Mockito.when(userDao.findByFirstNameIgnoreCaseContaining(firstName)).thenReturn(searchUsers);

        //Act
        List<User> returnedUser = serviceUnderTest.searchUsers(firstName, lastName, loggedInUserEmailAddress);

        //Assert
        Assert.assertEquals(user, returnedUser.get(0));
        Assert.assertEquals(1, returnedUser.size());
    }

    @Test
    public void testSearchUsersByLastName(){
        //Arrange
        User user = new User();
        String firstName = "";
        user.setFirstName(firstName);
        String lastName = "mcconville";
        user.setLastName(lastName);
        String loggedInUserEmailAddress = "kupo@cat.com";
        user.setEmailAddress(loggedInUserEmailAddress);
        List<User> searchUsers = new ArrayList<>();
        searchUsers.add(user);

        Mockito.when(userDao.findByLastNameIgnoreCaseContaining(lastName)).thenReturn(searchUsers);

        //Act
        List<User> returnedUser = serviceUnderTest.searchUsers(firstName, lastName, loggedInUserEmailAddress);

        //Assert
        Assert.assertEquals(user, returnedUser.get(0));
        Assert.assertEquals(1, returnedUser.size());
    }

    @Test
    public void testSearchUsersNotFound(){
        //Arrange
        User user = new User();
        String firstName = "maddi";
        String searchFirstName = "Navi";
        user.setFirstName(firstName);
        String lastName = "schmidt";
        user.setLastName(lastName);
        String loggedInUserEmailAddress = "maddi@maddi.com";
        user.setEmailAddress(loggedInUserEmailAddress);
        List<User> searchUser = new ArrayList<>();

        Mockito.when(userDao.findByFirstNameIgnoreCaseContaining(searchFirstName)).thenReturn(searchUser);

        //Act
        List<User> returnedUser = serviceUnderTest.searchUsers(firstName, lastName, loggedInUserEmailAddress);


        //Assert
        Assert.assertEquals(0, returnedUser.size());
    }

    @Test
    public void testSearchUserIsFriendsWith(){
        //Arrange
        User navi = new User();
        User kupo = new User();
        long id = 10;
        kupo.setId(id);
        String firstName = "kupo";
        kupo.setFirstName(firstName);
        String lastName = "mcconville";
        kupo.setLastName(lastName);
        String loggedInUserEmailAddress = "navi@cat.com";
        navi.setEmailAddress(loggedInUserEmailAddress);
        FriendRequest friendRequest = new FriendRequest();
        List<FriendRequest> correspondingFriendRequests = new ArrayList<>();
        correspondingFriendRequests.add(friendRequest);
        List<User> recipients = new ArrayList<>();
        recipients.add(kupo);
        friendRequest.setRecipient(kupo);
        friendRequest.setSender(navi);
        friendRequest.setFriendRequestStatus(FriendRequestStatus.ACCEPTED);
        Mockito.when(userDao.findOneByEmailAddressIgnoreCase(loggedInUserEmailAddress)).thenReturn(navi);
        Mockito.when(friendRequestDao.findAllBySenderAndRecipientIn(navi, recipients)).thenReturn(correspondingFriendRequests);
        Mockito.when(userDao.findByFirstNameIgnoreCaseContainingAndLastNameIgnoreCaseContaining(firstName, lastName)).thenReturn(recipients);

        //Act
        List<User> resultList = serviceUnderTest.searchUsers(firstName, lastName, loggedInUserEmailAddress);

        //Assert
        Assert.assertEquals(1, resultList.size());
        Assert.assertEquals(kupo, resultList.get(0));
        Assert.assertTrue(kupo.isFriendsWithLoggedInUser());
    }

    @Test
    public void testSearchUserWithPendingRequests(){
        //Arrange
        User navi = new User();
        User kupo = new User();
        long id = 10;
        kupo.setId(id);
        String firstName = "kupo";
        kupo.setFirstName(firstName);
        String lastName = "mcconville";
        kupo.setLastName(lastName);
        String loggedInUserEmailAddress = "navi@cat.com";
        navi.setEmailAddress(loggedInUserEmailAddress);
        FriendRequest friendRequest = new FriendRequest();
        List<FriendRequest> correspondingFriendRequests = new ArrayList<>();
        correspondingFriendRequests.add(friendRequest);
        friendRequest.setFriendRequestStatus(FriendRequestStatus.PENDING);
        friendRequest.setRecipient(navi);
        friendRequest.setSender(kupo);
        List<User> searchResults = new ArrayList<>();
        searchResults.add(kupo);

        Mockito.when(userDao.findOneByEmailAddressIgnoreCase(loggedInUserEmailAddress)).thenReturn(navi);
        Mockito.when(userDao.findByFirstNameIgnoreCaseContainingAndLastNameIgnoreCaseContaining(firstName, lastName)).thenReturn(searchResults);
        Mockito.when(friendRequestDao.findAllByRecipientAndFriendRequestStatus(navi, FriendRequestStatus.PENDING)).thenReturn(correspondingFriendRequests);

        //Act
        List<User> returnedList = serviceUnderTest.searchUsers(firstName, lastName, loggedInUserEmailAddress);

        //Assert
        Assert.assertEquals(1, returnedList.size());
        Assert.assertTrue(kupo.isHasReceivedRequestFromSearchedUser());
        Assert.assertEquals(kupo, returnedList.get(0));
    }

    @Test
    public void testSearchUsersIsFriendsByRecipient(){
        //Arrange
        User navi = new User();
        User kupo = new User();
        long id = 10;
        kupo.setId(id);
        String firstName = "kupo";
        kupo.setFirstName(firstName);
        String lastName = "mcconville";
        kupo.setLastName(lastName);
        String loggedInUserEmailAddress = "navi@cat.com";
        navi.setEmailAddress(loggedInUserEmailAddress);
        FriendRequest friendRequest = new FriendRequest();
        List<FriendRequest> correspondingFriendRequests = new ArrayList<>();
        correspondingFriendRequests.add(friendRequest);
        friendRequest.setFriendRequestStatus(FriendRequestStatus.ACCEPTED);
        friendRequest.setRecipient(navi);
        friendRequest.setSender(kupo);
        List<User> searchResults = new ArrayList<>();
        searchResults.add(kupo);

        Mockito.when(userDao.findOneByEmailAddressIgnoreCase(loggedInUserEmailAddress)).thenReturn(navi);
        Mockito.when(userDao.findByFirstNameIgnoreCaseContainingAndLastNameIgnoreCaseContaining(firstName, lastName)).thenReturn(searchResults);
        Mockito.when(friendRequestDao.findAllByRecipientAndFriendRequestStatus(navi, FriendRequestStatus.ACCEPTED)).thenReturn(correspondingFriendRequests);

        //Act
        List<User> returnedList = serviceUnderTest.searchUsers(firstName, lastName, loggedInUserEmailAddress);

        //Assert
        Assert.assertEquals(1, returnedList.size());
        Assert.assertTrue(navi.isHasReceivedRequestFromSearchedUser());
        Assert.assertEquals(kupo, returnedList.get(0));
        Assert.assertTrue(kupo.isFriendsWithLoggedInUser());
    }

    @Test
    public void testUpdateUserProfile(){
        User user = new User();
        String firstName = "Kupo";
        String lastName = "McConville";
        String location = "Colorado Springs";
        String timeZone = "MST";
        String loggedInUserEmailAddress = "kupo@cat.com";
        user.setEmailAddress(loggedInUserEmailAddress);

        Mockito.when(userDao.findOneByEmailAddressIgnoreCase(loggedInUserEmailAddress)).thenReturn(user);

        serviceUnderTest.updateUserProfile(firstName, lastName, location, loggedInUserEmailAddress, timeZone);

        ArgumentCaptor<User> argumentCaptor = ArgumentCaptor.forClass(User.class);
        Mockito.verify(userDao).save(argumentCaptor.capture());
        User returnedUser = argumentCaptor.getValue();
        Assert.assertEquals(firstName, returnedUser.getFirstName());
        Assert.assertEquals(lastName, returnedUser.getLastName());
        Assert.assertEquals(location, returnedUser.getLocation());
        Assert.assertEquals(loggedInUserEmailAddress, returnedUser.getEmailAddress());
        Assert.assertEquals(timeZone, returnedUser.getTimezone());
        Mockito.verify(userDao).save(user);
    }

    @Test
    public void testFindFriend(){
        User navi = new User();
        User kupo = new User();
        long id = 1;
        String loggedInUserEmailAddress = "navi@cat.com";
        kupo.setId(id);
        List<User> friends = new ArrayList<>();
        friends.add(kupo);
        navi.setEmailAddress(loggedInUserEmailAddress);

        Mockito.when(friendRequestSerivce.getFriends(loggedInUserEmailAddress)).thenReturn(friends);

        User user = serviceUnderTest.findFriend(id, loggedInUserEmailAddress);

        Assert.assertEquals(kupo, user);
    }

    @Test
    public void testFindFriendRuntimeException() throws  RuntimeException{
        User navi = new User();
        User kupo = new User();
        long id = 1;
        String loggedInUserEmailAddress = "navi@cat.com";
        kupo.setId(id);
        List<User> friends = new ArrayList<>();
        friends.add(kupo);
        navi.setEmailAddress(loggedInUserEmailAddress);
        long wrongId = 2;

        expectedException.expect(RuntimeException.class);

        Mockito.when(friendRequestSerivce.getFriends(loggedInUserEmailAddress)).thenReturn(friends);

        serviceUnderTest.findFriend(wrongId, loggedInUserEmailAddress);
    }
}

