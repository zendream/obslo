package com.odvarkajak.oslol.web.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.odvarkajak.oslol.repository.ProjectRepository;
import com.odvarkajak.oslol.repository.UserRepository;

@Controller
public class ProjectController {
    static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    ProjectRepository projectRepository;    
    
    @Autowired
    UserRepository userRepository;
    
    @RequestMapping(value = "/project/list")
    public String projectList() {
        return "view/project/list";
    }
    
    @RequestMapping(value = "/user/searchProject")
    public String searchProject() {
        return "view/user/searchProject";
    }
    
    
    @RequestMapping(value = "/project/detail/{projectId}", method=RequestMethod.GET)
    public ModelAndView projectView(@PathVariable("projectId") int projectId) {
    	ModelAndView mav = new ModelAndView("view/project/detail");
        //mav.addObject();
        return mav;
    }


//TODO
    @RequestMapping(value = "/user/get_projects_list",
            method = RequestMethod.GET, produces = "application/json")
    public
    @ResponseBody
    Object[] getProjectsList(@RequestParam("term") String query) {
        List<String> usernameList = userRepository.findUsername(query);

        return usernameList.toArray();
    }
}
