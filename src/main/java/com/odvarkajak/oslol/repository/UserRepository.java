
package com.odvarkajak.oslol.repository;


import org.springframework.transaction.annotation.Transactional;

import com.odvarkajak.oslol.domain.Observation;
import com.odvarkajak.oslol.domain.Project;
import com.odvarkajak.oslol.domain.Role;
import com.odvarkajak.oslol.domain.User;

import java.util.Collection;
import java.util.List;
import java.util.Set;

public interface UserRepository {
    Collection loadUsers();

    void saveUser(User user);

    @Transactional(readOnly = true)
    String findPasswordByUsername(String username);

    @Transactional(readOnly = true)
    User findUserByUsername(String username);
    
    @Transactional(readOnly = true)
    User findUserByEmail(String email);
    
    List<String> findUsername(String query);

    @Transactional(readOnly = true)
    boolean isEmailAlreadyExists(String email);
    
    @Transactional(readOnly = true)
    boolean isUsernameAlreadyExists(String username);

    @Transactional(readOnly = true)
    boolean isSecurityCodeValid(String email, String securityCode);

    /*
    @Transactional(readOnly = true)
    Role getUserRoles(String username);
    */
    
    @Transactional(readOnly = true)
    Set<Project> loadUserOwnProjectsByName(String username);
    
    @Transactional(readOnly = true)
    Set<Observation> loadUserOwnObservationByName(String username);
    
    @Transactional(readOnly = true)
    Set<Project> loadUserAllowedProjectsByName(String username);
    
    @Transactional(readOnly = true)
    Set<Observation> loadUserAllowedObservationsByName(String username);
    
    @Transactional(readOnly = true)
    Integer loadUserIdByName(String username);
       
    void update(User user);
}

