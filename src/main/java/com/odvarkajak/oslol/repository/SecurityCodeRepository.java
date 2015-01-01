
package com.odvarkajak.oslol.repository;

import org.springframework.transaction.annotation.Transactional;

import com.odvarkajak.oslol.domain.SecurityCode;

public interface SecurityCodeRepository {
	
	@Transactional(readOnly = false)
    void persist(SecurityCode securityCode);

    @Transactional(readOnly = false)
    void deleteSecurityCode(SecurityCode securityCode);
}
