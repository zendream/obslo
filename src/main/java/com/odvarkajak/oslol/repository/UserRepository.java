
package com.odvarkajak.oslol.repository;


import org.springframework.transaction.annotation.Transactional;

import com.odvarkajak.oslol.domain.User;

import java.util.Collection;
import java.util.List;

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
       
    void update(User user);
}

