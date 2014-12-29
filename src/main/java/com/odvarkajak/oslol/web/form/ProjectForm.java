package com.odvarkajak.oslol.web.form;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class ProjectForm {

	    @NotNull
	    @Size(min = 5, max = 50)
	    private String name;

	    @NotNull
	    @Size(min = 5, max = 20)
	    private String description;

	    public String getName() {
	        return name;
	    }

	    public void setName(String name) {
	        this.name = name;
	    }

	    public String getDescription() {
	        return description;
	    }

	    public void setDescription(String description) {
	        this.description = description;
	    }
	
}