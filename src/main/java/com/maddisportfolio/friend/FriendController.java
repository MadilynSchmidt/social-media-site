package com.maddisportfolio.friend;

import com.maddisportfolio.friendrequest.FriendRequestSerivce;;
import com.maddisportfolio.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@Controller
@RequestMapping(value = "/friends")
public class FriendController {

    @Autowired
    private FriendRequestSerivce friendRequestSerivce;

    @RequestMapping(method = RequestMethod.GET)
    public String getFriends(Model model) {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String loggedInUserEmailAddress = userDetails.getUsername();
        List<User> friends = friendRequestSerivce.getFriends(loggedInUserEmailAddress);
        model.addAttribute("friends", friends);
        return "friends";

}

}
