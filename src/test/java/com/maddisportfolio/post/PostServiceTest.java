package com.maddisportfolio.post;

import com.maddisportfolio.user.User;
import com.maddisportfolio.user.UserDao;
import com.maddisportfolio.user.UserService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.*;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class PostServiceTest {

    @InjectMocks
    PostService serviceUnderTest;

    @Mock
    PostDao postDao;

    @Mock
    UserService userService;

    @Mock
    UserDao userDao;

    @Before
    public void setup(){
        serviceUnderTest = new PostService();
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testCreatePost(){
        //Arrange
        String postText = "I'm a cat";
        String emailAddress = "cat@cat.com";
        User kupo = new User();
        Mockito.when(userService.getUser(emailAddress)).thenReturn(kupo);

        //Act
        serviceUnderTest.createPost(postText, emailAddress);

        //Assert
        ArgumentCaptor<Post> argumentCaptor = ArgumentCaptor.forClass(Post.class);
        Mockito.verify(postDao).save(argumentCaptor.capture());
        Post post = argumentCaptor.getValue();
        Assert.assertEquals(postText, post.getContent());
        Assert.assertEquals(kupo, post.getUser());
        Assert.assertNotNull(post.getTimestamp());

    }

    @Test
    public void  testFindAllPostByUser(){
        //Arrange
        String userEmailAddress = "cat@cat.com";
        String loggedInUserEmailAddress = "cattwo@cat.com";
        Instant now = Instant.now();
        java.sql.Timestamp ts = Timestamp.from(now);
        Post postOne = new Post();
        postOne.setTimestamp(ts);
        Post postTwo = new Post();
        postTwo.setTimestamp(ts);
        User kupo = new User();
        Mockito.when(userDao.findOneByEmailAddressIgnoreCase(userEmailAddress)).thenReturn(kupo);
        List<Post> posts = new ArrayList<>();
        posts.add(postOne);
        posts.add(postTwo);
        Mockito.when(postDao.findAllByUser(kupo)).thenReturn(posts);
        User kimahri = new User();
        kimahri.setTimezone("US/Mountain");
        Mockito.when(userDao.findOneByEmailAddressIgnoreCase(loggedInUserEmailAddress)).thenReturn(kimahri);

        //Act
        List<Post> result = serviceUnderTest.findAllPostByUser(userEmailAddress, loggedInUserEmailAddress);

        //Assert
        Assert.assertEquals(2, result.size());
        Assert.assertEquals(postOne, result.get(0));
        Assert.assertEquals(postTwo, result.get(1));






    }







}
