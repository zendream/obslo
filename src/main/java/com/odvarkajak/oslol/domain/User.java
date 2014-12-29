
package com.odvarkajak.oslol.domain;


import javax.persistence.*;

import java.sql.Blob;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity(name = "user")
@Table(name = "user")

public class User{
    
    private Long id;
    private String username;
    private String password;
    private Date created;
    private Date modified;
    private String email;
    private SecurityCode securityCode;
    private boolean accountExpired;
    private boolean accountLocked;
    private boolean credentialsExpired;
    private boolean enabled;
    
    private String picturefile;
    private String picture;
    
    private Role role;
    private Set<Project> projects = new HashSet<Project>(0);
    private Set<Observation> observations;
    private Set<ObservationToUser> observationUsers = new HashSet<ObservationToUser>(0);
    
    public User(){
    	
    }
    public User(Long id, String username, String password,  Date created,  Date modified,
    		String email,SecurityCode securityCode, 
    		boolean accountExpired, boolean accountLocked, boolean enabled,
    		Role role,
    		Set<Project> projects,Set<Observation> observations,Set<ObservationToUser> observationUsers){
    	
    	this.id = id;
    	this.username = username;
    	this.password = password;
    	this.created = created;
    	this.modified = modified;
    	this.email = email;
    	this.securityCode = securityCode;
    	this.accountExpired = accountExpired;
    	this.accountLocked = accountLocked;
    	this.enabled = enabled;
    	this.role = role;
    	this.projects = projects;
    	this.observations = observations;
    	this.observationUsers = observationUsers;    	
    	
    }
    @Column(name = "screen_name", unique = true)
    public String getUsername() {
        return username;
    }


    public void setUsername(String username) {
        this.username = username;
    }

    @Column(name = "password", unique = false)
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "userId", unique = true, nullable = false)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    
    @OneToOne(mappedBy = "user", cascade = {CascadeType.ALL})
    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
    
    @Column(name = "username", unique = true)
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
    
    @Column(name = "accountExpired", columnDefinition = "BIT", length = 1)
    public boolean isAccountExpired() {
        return accountExpired;
    }

    public void setAccountExpired(boolean accountExpired) {
        this.accountExpired = accountExpired;
    }
    
    @Column(name = "accountLocked", columnDefinition = "BIT", length = 1)
    public boolean isAccountLocked() {
        return accountLocked;
    }

    public void setAccountLocked(boolean accountLocked) {
        this.accountLocked = accountLocked;
    }

    @Column(name = "enabled", columnDefinition = "BIT", length = 1)
    public boolean getEnabled() {
        return enabled;
    }


    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
    
    @OneToOne(mappedBy = "user", fetch = FetchType.LAZY, cascade = {CascadeType.ALL})
    public SecurityCode getSecurityCode() {
        return securityCode;
    }

    public void setSecurityCode(SecurityCode securityCode) {
        this.securityCode = securityCode;
    }

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created", unique = false)
	public Date getCreated() {
		return created;
	}


	public void setCreated(Date created) {
		this.created = created;
	}

	@Temporal(TemporalType.TIMESTAMP)
    @Column(name = "modified", unique = false)
	public Date getModified() {
		return modified;
	}


	public void setModified(Date modified) {
		this.modified = modified;
	}

	@ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinTable(name = "user_project", joinColumns = { 
			@JoinColumn(name = "user", nullable = false, updatable = false) }, 
			inverseJoinColumns = { @JoinColumn(name = "project", 
					nullable = false, updatable = false) })
	public Set<Project> getProjects() {
		return projects;
	}


	public void setProjects(Set<Project> projects) {
		this.projects = projects;
	}

	@OneToMany(mappedBy = "author")
	public Set<Observation> getObservations() {
		return observations;
	}
 
	public void setObservations(Set<Observation> observations) {
		this.observations = observations;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "pk.user")
	public Set<ObservationToUser> getObservationUsers() {
		return observationUsers;
	}


	public void setObservationUsers(Set<ObservationToUser> observationUsers) {
		this.observationUsers = observationUsers;
	}
	
	@Column(name = "picturefile", unique = false, nullable = true)
	public String getPicturefile() {
		return picturefile;
	}
	public void setPicturefile(String picturefile) {
		this.picturefile = picturefile;
	}
	@Column(name = "picture", unique = false, nullable = true)
	public String getPicture() {
		return picture;
	}
	public void setPicture(String picture) {
		this.picture = picture;
	}

	@Column(name = "credentialsExpired", columnDefinition = "BIT", length = 1)
	public boolean isCredentialsExpired() {
		return credentialsExpired;
	}
	public void setCredentialsExpired(boolean credentialsExpired) {
		this.credentialsExpired = credentialsExpired;
	}
	
    @Column(name = "enabled", unique = false)
	public boolean isEnabled() {
		return enabled;
	}
}
