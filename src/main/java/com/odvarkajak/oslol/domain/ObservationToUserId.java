package com.odvarkajak.oslol.domain;


import javax.persistence.Embeddable;
import javax.persistence.ManyToOne;
 
@Embeddable
public class ObservationToUserId implements java.io.Serializable {
 

	private Observation observation;
    private User user;
 
	@ManyToOne
	public Observation getObservation() {
		return observation;
	}
 
	public void setObservation(Observation observation) {
		this.observation = observation;
	}
 
	@ManyToOne
	public User getUser() {
		return user;
	}
 
	public void setUser(User user) {
		this.user = user;
	}
 
	public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
 
        ObservationToUserId that = (ObservationToUserId) o;
 
        if (observation != null ? !observation.equals(that.observation) : that.observation != null) return false;
        if (user != null ? !user.equals(that.user) : that.user != null)
            return false;
 
        return true;
    }
	
    public int hashCode() {
        int result;
        result = (observation != null ? observation.hashCode() : 0);
        result = 17 * result + (user != null ? user.hashCode() : 0);
        return result;
    }
 
}