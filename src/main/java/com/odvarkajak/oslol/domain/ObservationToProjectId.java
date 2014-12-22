package com.odvarkajak.oslol.domain;

import javax.persistence.Embeddable;
import javax.persistence.ManyToOne;
 
@Embeddable
public class ObservationToProjectId implements java.io.Serializable {
 

	private Observation observation;
    private Project project;
 
	@ManyToOne
	public Observation getObservation() {
		return observation;
	}
 
	public void setObservation(Observation observation) {
		this.observation = observation;
	}
 
	@ManyToOne
	public Project getProject() {
		return project;
	}
 
	public void setProject(Project project) {
		this.project = project;
	}
 
	public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
 
        ObservationToProjectId that = (ObservationToProjectId) o;
 
        if (observation != null ? !observation.equals(that.observation) : that.observation != null) return false;
        if (project != null ? !project.equals(that.project) : that.project != null)
            return false;
 
        return true;
    }
	
    public int hashCode() {
        int result;
        result = (observation != null ? observation.hashCode() : 0);
        result = 31 * result + (project != null ? project.hashCode() : 0);
        return result;
    }
 
}