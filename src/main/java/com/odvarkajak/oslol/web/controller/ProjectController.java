package com.odvarkajak.oslol.web.controller;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletRequest;
import javax.validation.Valid;

import net.tanesha.recaptcha.ReCaptchaResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.encoding.Md5PasswordEncoder;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.odvarkajak.oslol.domain.Project;
import com.odvarkajak.oslol.domain.Role;
import com.odvarkajak.oslol.domain.SecurityCode;
import com.odvarkajak.oslol.repository.ProjectRepository;
import com.odvarkajak.oslol.repository.UserRepository;
import com.odvarkajak.oslol.utility.SecureUtility;
import com.odvarkajak.oslol.utility.TypeActivationEnum;
import com.odvarkajak.oslol.web.form.ProjectForm;
import com.odvarkajak.oslol.web.form.UserForm;

@Controller
public class ProjectController {
    static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    ProjectRepository projectRepository;    
    
    @Autowired
    UserRepository userRepository;
    
    @SuppressWarnings("unchecked")
	@ModelAttribute("allProjects")
    public List<Project> populateProjects() {
        return (List<Project>) this.projectRepository.loadProjects();
    }
    
    @RequestMapping(value = "/project/listAll")
    public String projectList() {
        return "view/project/list";
    }
    
    @RequestMapping(value = "/user/searchProject")
    public String searchProject() {
        return "view/user/searchProject";
    }
    
    
    @RequestMapping(value = "/project/detail/{projectId}", method=RequestMethod.GET)
    public ModelAndView projectView(@PathVariable("projectId") Long projectId) {
    	ModelAndView mav = new ModelAndView("view/project/detail");
    	Project project = projectRepository.findProjectById(projectId);
        mav.addObject("project", project);
        return mav;
    }


    @RequestMapping("/project/create")
    public String create(Model model) {
        logger.debug("Now: Create project");
        if (!model.containsAttribute("project")) {
            model.addAttribute("project", new ProjectForm());
        }        
        return "view/project/create";
    }


    @RequestMapping(value = "/project/create_confirm", method = RequestMethod.POST)
    @Transactional
    public String createProject(Model model, @ModelAttribute("project") @Valid ProjectForm form, BindingResult result, ServletRequest servletRequest) {
        logger.debug("Now: createProject form");
        User loggedUser = (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        
        if (!result.hasErrors()) {        	
            Calendar cal = Calendar.getInstance();
            Date date = cal.getTime();
            
            Project project = new Project();
            project.setName(form.getName());
            project.setDescription(form.getDescription());
            project.setCreated(date);
            project.setModified(date);
            project.setAuthor(userRepository.findUserByUsername(loggedUser.getUsername()));
            projectRepository.saveProject(project);
            logger.debug("End: createProject form success");
            logger.debug("End: createProject success");        
            return ("redirect:listAll/") ;
        } else {
            logger.debug(SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString());
            this.create(model);
            return "view/project/create";

        }
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
