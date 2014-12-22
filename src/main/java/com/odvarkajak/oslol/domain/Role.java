
package com.odvarkajak.oslol.domain;


import javax.persistence.*;

@Entity(name = "role")
@Table(name = "role")
public class Role {

    private Long id;
    private User user;
    private Integer role;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
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

    public Integer getRole() {
        return role;
    }

    public void setRole(Integer role) {
        this.role = role;
    }
}
