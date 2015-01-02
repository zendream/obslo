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
    private String pictureFile;
    private String pictureDescription;
    private String graphFile;
    private String graphDescription;
    private Set<DataFile> files;
    
    private Set<Project> projects = new HashSet<Project>();
    private Set<User> users = new HashSet<User>();
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
	@ManyToMany(fetch = FetchType.EAGER, mappedBy = "observations")
	public Set<Project> getProjects() {
		return projects;
	}
	public void setProjects(Set<Project> projects) {
		this.projects = projects;
	}
	@ManyToMany(fetch = FetchType.EAGER, mappedBy = "accessibleObservations")
	public Set<User> getUsers() {
		return users;
	}
	public void setUsers(Set<User> users) {
		this.users = users;
	}
	@Column(name = "pictureFile", unique = false, nullable = true)
	public String getPictureFile() {
		return pictureFile;
	}
	public void setPictureFile(String pictureFile) {
		this.pictureFile = pictureFile;
	}
	@Column(name = "pictureDescription", unique = false, nullable = true)
	public String getPictureDescription() {
		return pictureDescription;
	}
	public void setPictureDescription(String pictureDescription) {
		this.pictureDescription = pictureDescription;
	}
	@Column(name = "graphFile", unique = false, nullable = true)
	public String getGraphFile() {
		return graphFile;
	}
	public void setGraphFile(String graphFile) {
		this.graphFile = graphFile;
	}
	@Column(name = "graphDescription", unique = false, nullable = true)
	public String getGraphDescription() {
		return graphDescription;
	}
	public void setGraphDescription(String graphDescription) {
		this.graphDescription = graphDescription;
	}
	@OneToMany(mappedBy = "parentObservation", fetch = FetchType.EAGER)
	public Set<DataFile> getFiles() {
		return files;
	}
	public void setFiles(Set<DataFile> files) {
		this.files = files;
	}
}
