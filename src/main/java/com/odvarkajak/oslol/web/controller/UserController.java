
package com.odvarkajak.oslol.web.controller;

import net.tanesha.recaptcha.ReCaptchaImpl;
import net.tanesha.recaptcha.ReCaptchaResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.encoding.Md5PasswordEncoder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.GrantedAuthorityImpl;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import com.odvarkajak.oslol.component.UserSessionComponent;
import com.odvarkajak.oslol.domain.Role;
import com.odvarkajak.oslol.domain.SecurityCode;
import com.odvarkajak.oslol.domain.User;
import com.odvarkajak.oslol.repository.SecurityCodeRepository;
import com.odvarkajak.oslol.repository.UserRepository;
import com.odvarkajak.oslol.service.MailSenderService;
import com.odvarkajak.oslol.service.MyUserDetailsService;
import com.odvarkajak.oslol.utility.SecureUtility;
import com.odvarkajak.oslol.utility.TypeActivationEnum;
import com.odvarkajak.oslol.web.form.UserForm;

import javax.servlet.ServletRequest;
import javax.validation.Valid;

import java.util.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;


@Controller
public class UserController {

    static final Logger logger = LoggerFactory.getLogger(UserController.class);


    @Autowired
    UserRepository userRepository;

    @Autowired
    SecurityCodeRepository securityCodeRepository;

    @Autowired
    MailSenderService mailSenderService;

    @Autowired
    UserDetailsService myUserDetailsService;
    
    @Autowired
    private UserSessionComponent userSessionComponent;
    
    @Autowired
    private ReCaptchaImpl reCaptcha;

    private static List<GrantedAuthority> AUTHORITIES = new ArrayList<GrantedAuthority>(1) {{
        add(new GrantedAuthorityImpl("ROLE_USER"));
    }};


    public void setMyUserDetailsService(MyUserDetailsService myUserDetailsService) {
        this.myUserDetailsService = myUserDetailsService;
    }

    @RequestMapping("/public/signup")
    public String create(Model model) {
        logger.debug("Now: Create user");
        if (!model.containsAttribute("user")) {
            model.addAttribute("user", new UserForm());
        }
        logger.debug("Check: reCaptcha {}", reCaptcha != null);
        if (reCaptcha != null) {
            model.addAttribute("recaptcha", reCaptcha.createRecaptchaHtml(null, null));
        }
        return "view/public/signup";
    }


    @RequestMapping(value = "/public/signup_confirm", method = RequestMethod.POST)
    @Transactional
    public String createUser(Model model, @ModelAttribute("user") @Valid UserForm form, BindingResult result, @RequestParam(value = "recaptcha_challenge_field", required = false) String challangeField,
                             @RequestParam(value = "recaptcha_response_field", required = false) String responseField, ServletRequest servletRequest) {
        logger.debug("Now: createUser form");
        if (reCaptcha != null) {
            String remoteAdress = servletRequest.getRemoteAddr();
            ReCaptchaResponse reCaptchaResponse = reCaptcha.checkAnswer(remoteAdress, challangeField, responseField);
            if (!reCaptchaResponse.isValid()) {
                this.create(model);
                return "view/public/signup";
            }
        }
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
            mailSenderService.sendAuthorizationMail(user, user.getSecurityCode());


        } else {
            logger.debug("signup error");
            this.create(model);
            return "view/public/signup";

        }
        logger.debug("End: createUser");
        return "view/public/mailSent";
    }

    @RequestMapping(value = "/user/profile")
    public String userProfile() {
        return "view/user/profile";
    }
    

    @RequestMapping(value = "/pages/base/user/get",
            method = RequestMethod.GET)
    public
    @ResponseBody
    Map<String, String> get() {
        List<String> usernameList = userRepository.findUsername("");
        Map<String, String> result = new HashMap<>();
        for (String username : usernameList) {
            result.put("label", username);

        }
        return result;
    }

    @RequestMapping(value = "/public/activation", method = RequestMethod.GET)
    @Transactional
    public String activation(@RequestParam String mail, @RequestParam String code) {
        logger.debug("Now: user activation");
        if (userRepository.isSecurityCodeValid(mail, code)) {
            User user = userRepository.findUserByEmail(mail);
            user.setAccountLocked(false);
            user.setEnabled(true);
            securityCodeRepository.deleteSecurityCode(user.getSecurityCode());
            user.setSecurityCode(null);
            userRepository.update(user);
            UserDetails userDetails = myUserDetailsService.loadUserByUsername(user.getEmail());
            Authentication auth = new UsernamePasswordAuthenticationToken(userDetails, user.getPassword(), AUTHORITIES);
            SecurityContextHolder.getContext().setAuthentication(auth);
            userSessionComponent.setCurrentUser(user);
            logger.debug("End: activation fine");
            return "view/user/profile";
        }
        logger.debug("Exit: activation fail");
        return "view/error/error";

    }

}
