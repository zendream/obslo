
package com.odvarkajak.oslol.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.odvarkajak.oslol.domain.User;
import com.odvarkajak.oslol.repository.UserRepository;
import com.odvarkajak.oslol.web.controller.UserController;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Transactional(readOnly = true)
@Component("myUserDetailsService")
public class MyUserDetailsService implements UserDetailsService {
	static final Logger logger = LoggerFactory.getLogger(UserController.class);
    @Autowired
    private UserRepository userRepository;

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        try {

            User domainUser = (User) entityManager
                    .createQuery("from user u where u.email = :email")
                    .setParameter("email", username)
                    .getSingleResult();
            List<String> roles = getRoles(domainUser.getRole().getRole());
            UserDetails retuser = new org.springframework.security.core.userdetails.User(domainUser.getEmail(),domainUser.getPassword(),
            		domainUser.isEnabled(),!domainUser.isAccountExpired(),!domainUser.isCredentialsExpired(),!domainUser.isAccountLocked(),getGrantedAuthorities(roles));
            return retuser;
        } catch (Exception e) {
        	logger.debug("AAARGH!");
            throw new RuntimeException(e);
        }
    }

    /**
     * Retrieves a collection of {@link GrantedAuthority} based on a numerical role
     *
     * @param role the numerical role
     * @return a collection of {@link GrantedAuthority
     */
    public static Collection<? extends GrantedAuthority> getAuthorities(Integer role) {
        List<GrantedAuthority> authList = getGrantedAuthorities(getRoles(role));
        return authList;
    }

    /**
     * Converts a numerical role to an equivalent list of roles
     *
     * @param role the numerical role
     * @return list of roles as as a list of {@link String}
     */
    public static List<String> getRoles(Integer role) {
        List<String> roles = new ArrayList<String>();
        if (role.intValue() == 1) {
            roles.add("ROLE_USER");
            roles.add("ROLE_ADMIN");
        } else if (role.intValue() == 2) {
            roles.add("ROLE_USER");
        }
        return roles;
    }

    /**
     * Wraps {@link String} roles to {@link SimpleGrantedAuthority} objects
     *
     * @param roles {@link String} of roles
     * @return list of granted authorities
     */
    public static List<GrantedAuthority> getGrantedAuthorities(List<String> roles) {
        List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
        for (String role : roles) {
            authorities.add(new SimpleGrantedAuthority(role));
        }
        return authorities;
    }


}
