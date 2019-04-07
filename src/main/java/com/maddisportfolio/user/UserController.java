package com.maddisportfolio.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping(value = "/users")
public class UserController {

    @Autowired
    private UserService userService;

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
            //todo when login html is created, redirect to it instead
            return "create-user";

    }

}
