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


@Entity(name = "observation_user")
@Table(name = "observation_user")
@AssociationOverrides({
		@AssociationOverride(name = "pk.observation", 
			joinColumns = @JoinColumn(name = "observationId")),
		@AssociationOverride(name = "pk.user", 
			joinColumns = @JoinColumn(name = "userId")) })
public class ObservationToUser implements java.io.Serializable {


	private ObservationToUserId pk = new ObservationToUserId();	
	private Integer rights;	
	
	@Column(name = "rights", unique = false, nullable = false)
	public Integer getRights() {
		return rights;
	}
	public void setRights(Integer rights) {
		this.rights = rights;
	}
	
	@EmbeddedId
	public ObservationToUserId getPk() {
		return pk;
	}
 
	public void setPk(ObservationToUserId pk) {
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
	public User getUser() {
		return getPk().getUser();
	}
	
	
	public void setUser(User user) {
		getPk().setUser(user);
	}
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
 
		ObservationToUser that = (ObservationToUser) o;
 
		if (getPk() != null ? !getPk().equals(that.getPk())
				: that.getPk() != null)
			return false;
 
		return true;
	}
	public int hashCode() {
		return (getPk() != null ? getPk().hashCode() : 0);
	}
}