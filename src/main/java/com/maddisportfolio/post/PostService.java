package com.maddisportfolio.post;

import com.maddisportfolio.user.User;
import com.maddisportfolio.user.UserDao;
import com.maddisportfolio.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
        postDao.save(postToCreate);
    }

    public List<Post> findAllPostByUser(String userEmailAddress){
        User user = userDao.findOneByEmailAddressIgnoreCase(userEmailAddress);
        List<Post> posts = postDao.findAllByUser(user);
        return posts;
    }


}
