package com.maddisportfolio.post;

import com.maddisportfolio.user.User;
import com.maddisportfolio.user.UserDao;
import com.maddisportfolio.user.UserService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
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

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

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

    @Test
    public void testDeletePostThrowRuntimeException() throws RuntimeException{
        //Arrange
        String loggedInUserEmailAddress = "navi@cat.com";
        long postId = 1;
        User navi = new User();
        User kupo = new User();
        long wrongId = 3;
        kupo.setId(wrongId);
        Post post = new Post();
        post.setId(postId);
        long userId = 2;
        navi.setId(userId);
        post.setUser(kupo);

        expectedException.expect(RuntimeException.class);

        Mockito.when(userDao.findOneByEmailAddressIgnoreCase(loggedInUserEmailAddress)).thenReturn(navi);
        Mockito.when(postDao.getOne(postId)).thenReturn(post);

        //Act
        serviceUnderTest.deletePost(loggedInUserEmailAddress, postId);
    }

    @Test
    public void testDeletePost() throws RuntimeException{
        //Arrange
        String loggedInUserEmailAddress = "navi@cat.com";
        long postId = 1;
        User navi = new User();
        Post post = new Post();
        post.setId(postId);
        long userId = 2;
        navi.setId(userId);
        post.setUser(navi);

        Mockito.when(userDao.findOneByEmailAddressIgnoreCase(loggedInUserEmailAddress)).thenReturn(navi);
        Mockito.when(postDao.getOne(postId)).thenReturn(post);

        //Act
        serviceUnderTest.deletePost(loggedInUserEmailAddress, postId);

        //Assert
        Mockito.verify(postDao).delete(post);
    }

    @Test
    public void testUpdatePostThrowsNewRuntimeException() throws RuntimeException {
        //Arrange
        String loggedInUserEmailAddress = "navi@cat.com";
        long postId = 1;
        User navi = new User();
        User kupo = new User();
        long wrongId = 3;
        kupo.setId(wrongId);
        Post post = new Post();
        post.setId(postId);
        long userId = 2;
        navi.setId(userId);
        post.setUser(kupo);

        expectedException.expect(RuntimeException.class);

        Mockito.when(userDao.findOneByEmailAddressIgnoreCase(loggedInUserEmailAddress)).thenReturn(navi);
        Mockito.when(postDao.getOne(postId)).thenReturn(post);

        //Act
        serviceUnderTest.updatePost(loggedInUserEmailAddress, postId, post);
    }

    @Test
    public void testUpdatePost() throws RuntimeException{
        //Arrange
        String loggedInUserEmailAddress = "navi@cat.com";
        long postId = 1;
        User navi = new User();
        Post post = new Post();
        post.setId(postId);
        long userId = 2;
        navi.setId(userId);
        post.setUser(navi);
        String content = "Hello, I'm sassy";
        post.setContent(content);

        Mockito.when(userDao.findOneByEmailAddressIgnoreCase(loggedInUserEmailAddress)).thenReturn(navi);
        Mockito.when(postDao.getOne(postId)).thenReturn(post);

        //Act
        serviceUnderTest.updatePost(loggedInUserEmailAddress, postId, post);

        //Assert
        ArgumentCaptor<Post> argumentCaptor = ArgumentCaptor.forClass(Post.class);
        Mockito.verify(postDao).save(argumentCaptor.capture());
        Post returnedPost = argumentCaptor.getValue();
        Assert.assertEquals(content, returnedPost.getContent());

        Mockito.verify(postDao).save(post);
    }







}
