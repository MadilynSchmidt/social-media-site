package com.maddisportfolio.post;

import com.maddisportfolio.user.User;
import com.maddisportfolio.user.UserDao;
import com.maddisportfolio.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;

@Service
public class PostService {

    @Autowired
    private PostDao postDao;

    @Autowired
    private UserService userService;

    @Autowired
    private UserDao userDao;


    public void createPost(String postText, String loggedInEmailAddress){
        Post postToCreate = new Post();
        User loggedInUser = userService.getUser(loggedInEmailAddress);
        postToCreate.setUser(loggedInUser);
        postToCreate.setContent(postText);
        Instant now = Instant.now();
        java.sql.Timestamp ts = Timestamp.from(now);
        postToCreate.setTimestamp(ts);
        postDao.save(postToCreate);
    }

    public List<Post> findAllPostByUser(String userEmailAddress, String loggedInUserEmailAddress){
        User user = userDao.findOneByEmailAddressIgnoreCase(userEmailAddress);
        User loggedInUser = userDao.findOneByEmailAddressIgnoreCase(loggedInUserEmailAddress);
        List<Post> posts = postDao.findAllByUser(user);
        ZoneId loggedInTimezone = ZoneId.of(loggedInUser.getTimezone());
        for(Post post : posts){
          Instant instant = post.getTimestamp().toInstant();
          post.setDisplayZonedDateTime(ZonedDateTime.ofInstant(instant, loggedInTimezone));
        }

        return posts;
    }

    public void deletePost(String loggedInUserEmailAddress, long postId){
        User user = userDao.findOneByEmailAddressIgnoreCase(loggedInUserEmailAddress);
        Post post = postDao.getOne(postId);
        if(user.getId() != post.getUser().getId()){
            throw new RuntimeException();
        }
        postDao.delete(post);
    }

    public void updatePost(String loggedInUserEmailAddress, long postId, Post post){
        User user = userDao.findOneByEmailAddressIgnoreCase(loggedInUserEmailAddress);
        Post postToUpdate = postDao.getOne(postId);
        if(user.getId() != postToUpdate.getUser().getId()){
            throw new RuntimeException();
        }
        postToUpdate.setContent(post.getContent());
        postDao.save(postToUpdate);
        }

    }




