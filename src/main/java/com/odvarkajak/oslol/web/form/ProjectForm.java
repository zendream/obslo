package com.odvarkajak.oslol.web.form;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.Email;

public class ProjectForm {

	    @NotNull
	    @Size(min = 5, max = 50)
	    private String projectName;

	    @NotNull
	    @Size(min = 5, max = 20)
	    private String description;

	    @NotNull
	    @Size(min = 5, max = 50)
	    private String email;

	    public String getUsername() {
	        return projectName;
	    }

	    public void setUsername(String username) {
	        this.projectName = projectName;
	    }

	    public String getPassword() {
	        return description;
	    }

	    public void setPassword(String description) {
	        this.description = description;
	    }

	    public String getEmail() {
	        return email;
	    }

	    public void setEmail(String email) {
	        this.email = email;
	    }

	
}