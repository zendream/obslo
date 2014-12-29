
package com.odvarkajak.oslol.web.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.odvarkajak.oslol.component.UserSessionComponent;
import com.odvarkajak.oslol.repository.UserRepository;

@Controller
@RequestMapping
public class AccessController {

    @Autowired
    private UserSessionComponent userSessionComponent;
    @Autowired
    private UserRepository userRepository;


    @RequestMapping("/login")
    public String login(Model model, @RequestParam(required = false) String message) {

        model.addAttribute("message", message);
        return "view/public/login";
    }

    @RequestMapping("/login/success")
    public String loginSuccess() {
        userSessionComponent.setCurrentUser(userRepository.findUserByUsername(((UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername()));
        return "view/user/profile";
    }

    @RequestMapping(value = "/login/failure")
    public String loginFailure(Model model) {
       // String message = "Login Failure!";
        model.addAttribute("loginError", true);
        return "view/public/login";
    }

    @RequestMapping(value = "/logout/success")
    public String logoutSuccess() {
        return "/user/profile";
    }

    @RequestMapping("/login/error")
    public String loginError(Model model) {
        model.addAttribute("loginError", true);
        return "view/public/login";
    }

    @RequestMapping("/error")
    public String loginError() {

        return "view/error/error";
    }

}
