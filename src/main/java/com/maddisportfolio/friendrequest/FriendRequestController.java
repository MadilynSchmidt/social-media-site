package com.maddisportfolio.friendrequest;


import com.maddisportfolio.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping(value = "/friend-requests")
public class FriendRequestController {

    @Autowired
    private FriendRequestSerivce friendRequestSerivce;

    @RequestMapping(method = RequestMethod.POST, consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public String createFriendRequest(@RequestParam long recipientId){
        UserDetails userDetails = (UserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String senderEmailAddress = userDetails.getUsername();
        friendRequestSerivce.createFriendRequest(senderEmailAddress, recipientId);
        return "redirect:/users/search";

    }







}


