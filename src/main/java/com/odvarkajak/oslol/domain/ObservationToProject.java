package com.odvarkajak.oslol.domain;
import java.util.Date;

import javax.persistence.AssociationOverride;
import javax.persistence.AssociationOverrides;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;


@Entity(name = "observation_project")
@Table(name = "observation_project")
@AssociationOverrides({
		@AssociationOverride(name = "pk.observation", 
			joinColumns = @JoinColumn(name = "observation")),
		@AssociationOverride(name = "pk.project", 
			joinColumns = @JoinColumn(name = "project")) })
public class ObservationToProject implements java.io.Serializable {	

	private ObservationToProjectId pk = new ObservationToProjectId();	
	
	private Integer rights;	
	
	@Column(name = "rights", unique = false, nullable = false)
	public Integer getRights() {
		return rights;
	}
	public void setRights(Integer rights) {
		this.rights = rights;
	}
	
	@EmbeddedId
	public ObservationToProjectId getPk() {
		return pk;
	}
 
	public void setPk(ObservationToProjectId pk) {
		this.pk = pk;
	}
	@Transient
	public Observation getObservation() {
		return getPk().getObservation();
	}
 
	public void setStock(Observation observation) {
		getPk().setObservation(observation);
	}
	@Transient
	public Project getProject() {
		return getPk().getProject();
	}
 
	public void setProject(Project project) {
		getPk().setProject(project);
	}
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
 
		ObservationToProject that = (ObservationToProject) o;
 
		if (getPk() != null ? !getPk().equals(that.getPk())
				: that.getPk() != null)
			return false;
 
		return true;
	}
	public int hashCode() {
		return (getPk() != null ? getPk().hashCode() : 0);
	}
}
