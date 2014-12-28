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
import com.odvarkajak.oslol.domain.User;
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
        return "view/public/signup";
    }


    @RequestMapping(value = "/project/create_confirm", method = RequestMethod.POST)
    @Transactional
    public String createUser(Model model, @ModelAttribute("user") @Valid UserForm form, BindingResult result, @RequestParam(value = "recaptcha_challenge_field", required = false) String challangeField,
                             @RequestParam(value = "recaptcha_response_field", required = false) String responseField, ServletRequest servletRequest) {
        logger.debug("Now: createUser form");
        if (!result.hasErrors()) {

            if (userRepository.isUsernameAlreadyExists(form.getUsername())) {
                FieldError fieldError = new FieldError("screen_name", "username", "username already exists");
                result.addError(fieldError);
                return "view/public/signup";
            }
            // check if email already exists
            if (userRepository.isEmailAlreadyExists(form.getEmail())) {
                FieldError fieldError = new FieldError("user", "email", "email already exists");
                result.addError(fieldError);
                return "view/public/signup";
            }
            Calendar cal = Calendar.getInstance();
            Date date = cal.getTime();
            
            User user = new User();
            Md5PasswordEncoder encoder = new Md5PasswordEncoder();
            user.setUsername(form.getUsername());
            user.setEmail(form.getEmail());
            user.setCreated(date);

            user.setPassword(encoder.encodePassword(form.getPassword(), user.getEmail()));
            Role role = new Role();
            role.setUser(user);
            role.setRole(2);

            SecurityCode securityCode = new SecurityCode();
            securityCode.setUser(user);
            securityCode.setTimeRequest(new Date());
            securityCode.setTypeActivationEnum(TypeActivationEnum.NEW_ACCOUNT);
            securityCode.setCode(SecureUtility.generateRandomCode());
            user.setRole(role);
            user.setSecurityCode(securityCode);
            user.setAccountLocked(true);
            user.setEnabled(false);
            logger.debug((user.isEnabled() ? ("user locked") : ("user unlocked")));
            userRepository.saveUser(user);
            //securityCodeRepository.persist(securityCode);


        } else {
            logger.debug("signup error");
            this.create(model);
            return "view/public/signup";

        }
        logger.debug("End: createUser");
        return "view/public/mailSent";
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
