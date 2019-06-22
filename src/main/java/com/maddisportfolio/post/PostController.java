package com.maddisportfolio.post;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping(value = "/posts")
public class PostController {


    @Autowired
    private PostService postService;

    @RequestMapping(method = RequestMethod.POST)
    public String createPost(@RequestParam String post){
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String loggedInUserEmailAddress = userDetails.getUsername();
        postService.createPost(post, loggedInUserEmailAddress);
        return "redirect:/users/profile";
    }

    @RequestMapping(value = "/{postId}", method = RequestMethod.DELETE)
    @ResponseBody
    public void deletePost(@PathVariable long postId){
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String loggedInUserEmailAddress = userDetails.getUsername();
        postService.deletePost(loggedInUserEmailAddress, postId);
    }

    @RequestMapping(value ="/{postId}", method = RequestMethod.PATCH, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public void updatePost(@PathVariable long postId, @RequestBody Post post){
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String loggedInUserEmailAddress = userDetails.getUsername();
        postService.updatePost(loggedInUserEmailAddress, postId, post);
    }

}
