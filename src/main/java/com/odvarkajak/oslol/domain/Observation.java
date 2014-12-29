package com.odvarkajak.oslol.domain;

import javax.persistence.*;

import java.sql.Blob;
import java.util.Date;
import java.util.Set;
import java.util.HashSet;

import com.odvarkajak.oslol.utility.ObservationTargetEnum;

@Entity(name="observation")
@Table(name="observation")
public class Observation {

	private Long id;
    private String name;
    private User author;
    private Date created;
    private Date modified;
    private String description;
    private String pictureName;
    private String pictureDescription;
    private String graphName;
    private String graphDescription;
    private Set<DataFile> files;
    
    private Set<ObservationToProject> observationProjects = new HashSet<ObservationToProject>(0);
    private Set<ObservationToUser> observationUsers = new HashSet<ObservationToUser>(0);
    private ObservationTargetEnum target;
    
    @Id
    @Column(name = "observationId")
    @GeneratedValue(strategy = GenerationType.AUTO)
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	
	@Column(name = "name", unique = false, nullable = false)
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	@ManyToOne( cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch=FetchType.EAGER )
    @JoinTable(name="observation_author",
        joinColumns = @JoinColumn(name="observation"),
        inverseJoinColumns = @JoinColumn(name="user")
    )
	public User getAuthor() {
		return author;
	}
	public void setAuthor(User author) {
		this.author = author;
	}
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "created", unique = false, nullable = false)
	public Date getCreated() {
		return created;
	}
	public void setCreated(Date created) {
		this.created = created;
	}
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "modified", unique = false, nullable = false)
	public Date getModified() {
		return modified;
	}
	public void setModified(Date modified) {
		this.modified = modified;
	}
	@Column(name = "description", unique = false, nullable = false)
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	
	@Column(name = "target", unique = false, nullable = true)
	public ObservationTargetEnum getTarget() {
		return target;
	}
	public void setTarget(ObservationTargetEnum target) {
		this.target = target;
	}
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "pk.observation", cascade = {CascadeType.ALL})
	public Set<ObservationToProject> getObservationProjects() {
		return observationProjects;
	}
	public void setObservationProjects(Set<ObservationToProject> observationProjects) {
		this.observationProjects = observationProjects;
	}
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "pk.observation", cascade = {CascadeType.ALL})
	public Set<ObservationToUser> getObservationUsers() {
		return observationUsers;
	}
	public void setObservationUsers(Set<ObservationToUser> observationUsers) {
		this.observationUsers = observationUsers;
	}
	@Column(name = "pictureFile", unique = false, nullable = true)
	public String getPictureName() {
		return pictureName;
	}
	public void setPictureName(String pictureName) {
		this.pictureName = pictureName;
	}
	@Column(name = "pictureDescription", unique = false, nullable = true)
	public String getPictureDescription() {
		return pictureDescription;
	}
	public void setPictureDescription(String pictureDescription) {
		this.pictureDescription = pictureDescription;
	}
	@Column(name = "graphFile", unique = false, nullable = true)
	public String getGraphName() {
		return graphName;
	}
	public void setGraphName(String graphName) {
		this.graphName = graphName;
	}
	@Column(name = "graphDescription", unique = false, nullable = true)
	public String getGraphDescription() {
		return graphDescription;
	}
	public void setGraphDescription(String graphDescription) {
		this.graphDescription = graphDescription;
	}
	@OneToMany(mappedBy = "parentObservation")
	public Set<DataFile> getFiles() {
		return files;
	}
	public void setFiles(Set<DataFile> files) {
		this.files = files;
	}
}
