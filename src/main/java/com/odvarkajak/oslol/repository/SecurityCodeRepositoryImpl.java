
package com.odvarkajak.oslol.repository;

import org.springframework.stereotype.Repository;

import com.odvarkajak.oslol.domain.SecurityCode;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Repository
public class SecurityCodeRepositoryImpl implements SecurityCodeRepository {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public void persist(SecurityCode securityCode) {
        entityManager.merge(securityCode);
    }

    @Override
    public void deleteSecurityCode(SecurityCode securityCode) {
    	entityManager.remove(entityManager.contains(securityCode) ? securityCode : entityManager.merge(securityCode));

    }
}
