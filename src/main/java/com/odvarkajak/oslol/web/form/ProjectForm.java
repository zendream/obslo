package com.odvarkajak.oslol.web.form;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.springframework.web.multipart.MultipartFile;

public class ProjectForm {

	    @NotNull
	    @Size(min = 5, max = 50)
	    private String name;

	    @NotNull
	    @Size(min = 5, max = 20)
	    private String description;	    
	    private MultipartFile file;
	    private boolean hasFile;
	    private Long modifId;
	    private int phase;
	    
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

		public MultipartFile getFile() {
			return file;
		}

		public void setFile(MultipartFile file) {
			this.file = file;
		}

		public boolean isHasFile() {
			return hasFile;
		}

		public void setHasFile(boolean hasFile) {
			this.hasFile = hasFile;
		}

		public Long getModifId() {
			return modifId;
		}

		public void setModifId(Long modifId) {
			this.modifId = modifId;
		}

		public int getPhase() {
			return phase;
		}

		public void setPhase(int phase) {
			this.phase = phase;
		}
	
}