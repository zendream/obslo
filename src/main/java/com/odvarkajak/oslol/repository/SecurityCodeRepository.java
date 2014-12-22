
package com.odvarkajak.oslol.repository;

import com.odvarkajak.oslol.domain.SecurityCode;

public interface SecurityCodeRepository {
    void persist(SecurityCode securityCode);

    void deleteSecurityCode(SecurityCode securityCode);
}
