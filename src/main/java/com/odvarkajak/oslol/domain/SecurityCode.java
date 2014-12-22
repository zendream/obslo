
package com.odvarkajak.oslol.domain;


import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.odvarkajak.oslol.utility.TypeActivationEnum;

import java.util.Date;

@Entity(name = "security_code")
@Table(name = "security_code")
public class
        SecurityCode {


    private Long id;
    private User user;
    private String code;
    private Date timeRequest;
    private TypeActivationEnum typeActivationEnum;

    @Id
    @GeneratedValue
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    
    @OneToOne
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String secureString) {
        this.code = secureString;
    }

    @Temporal(TemporalType.DATE)
    public Date getTimeRequest() {
        return timeRequest;
    }

    public void setTimeRequest(Date timeRequest) {
        this.timeRequest = timeRequest;
    }

    public TypeActivationEnum getTypeActivationEnum() {
        return typeActivationEnum;
    }

    public void setTypeActivationEnum(TypeActivationEnum typeActivationEnum) {
        this.typeActivationEnum = typeActivationEnum;
    }
}
