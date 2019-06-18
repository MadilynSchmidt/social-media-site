package com.maddisportfolio.user;

import com.maddisportfolio.post.Post;
import com.maddisportfolio.post.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping(value = "/users")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private PostService postService;

    @RequestMapping(method = RequestMethod.GET)
    public String getCreateUser(Model model){
        model.addAttribute("user", new User());
        return "create-user";
    }

    @RequestMapping(method = RequestMethod.POST)
    public String createUser(@ModelAttribute User user){
        try {
            userService.createUser(user);
        }
        catch(InvalidEmailException e){
            return "redirect:/users?invalid";
        }
        catch(EmailExistsException e){
            return "redirect:/users?exists";
        }
            return "redirect:/login";

    }

    @RequestMapping(method = RequestMethod.GET, params = "query")
    public String searchUsers (@RequestParam("query") String emailAddressToSearchFor, Model model){
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String loggedInUserEmailAddress = userDetails.getUsername();
        List<User> searchList = userService.searchUsers(emailAddressToSearchFor, loggedInUserEmailAddress);
        model.addAttribute("users", searchList);
        model.addAttribute("loggedInUserEmailAddress", loggedInUserEmailAddress);
        return "search-results";
    }

    @RequestMapping(value = "/search", method = RequestMethod.GET)
    public String getSearch(){
        return "search-users";
    }

    @RequestMapping(value = "/profile", method = RequestMethod.GET)
    public String getProfile(Model model){
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String loggedInUserEmailAddress = userDetails.getUsername();
        User loggedInUser = userService.getUser(loggedInUserEmailAddress);
        model.addAttribute("user", loggedInUser);
        List<Post> posts = postService.findAllPostByUser(loggedInUserEmailAddress);
        model.addAttribute("posts", posts);
        return "users-profile";
    }

    @RequestMapping(value = "/profile", method = RequestMethod.POST)
    public String updateUserProfile(@RequestParam String firstName, @RequestParam String lastName, @RequestParam String location){
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String loggedInUserEmailAddress = userDetails.getUsername();
        userService.updateUserProfile(firstName, lastName, location, loggedInUserEmailAddress);
        return "redirect:/users/profile";

    }

    @RequestMapping(value = "/{userId}", method = RequestMethod.GET)
    public String getFriendProfile(@PathVariable Long userId, Model model){
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String loggedInUserEmailAddress = userDetails.getUsername();
        User friend = userService.findFriend(userId, loggedInUserEmailAddress);
        model.addAttribute("friend", friend);
        return "friend-profile";

    }







}
