package com.maddisportfolio.friendrequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping(value = "/friend-requests")
public class FriendRequestController {

    @Autowired
    private FriendRequestSerivce friendRequestSerivce;

    @RequestMapping(method = RequestMethod.GET)
    public String getPendingFriendRequests(Model model){
        UserDetails userDetails = (UserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String loggedInUserEmailAddress = userDetails.getUsername();
        List<FriendRequest> friendRequests = friendRequestSerivce.getPendingFriendRequestsForUser(loggedInUserEmailAddress);
        model.addAttribute("friendRequests", friendRequests);
        return "friend-requests";
    }

    @RequestMapping(method = RequestMethod.POST, consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public String createFriendRequest(@RequestParam long recipientId){
        UserDetails userDetails = (UserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String senderEmailAddress = userDetails.getUsername();
        friendRequestSerivce.createFriendRequest(senderEmailAddress, recipientId);
        return "redirect:/users/search";
    }

    @RequestMapping(method = RequestMethod.POST, value = "/accept")
    public String acceptFriendRequest(@RequestParam long friendRequestId){
        UserDetails userDetails = (UserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String loggedInUserEmailAddress = userDetails.getUsername();
        friendRequestSerivce.acceptFriendRequest(loggedInUserEmailAddress, friendRequestId);
        return "redirect:/friend-requests";
    }

    @RequestMapping(method = RequestMethod.POST, value = "/decline")
    public String declineFriendRequest(@RequestParam long friendRequestId){
        UserDetails userDetails = (UserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String loggedInUserEmailAddress = userDetails.getUsername();
        friendRequestSerivce.declineFriendRequest(loggedInUserEmailAddress, friendRequestId);
        return "redirect:/friend-requests";
    }






}


