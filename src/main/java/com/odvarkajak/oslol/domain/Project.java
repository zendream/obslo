package com.odvarkajak.oslol.domain;

import javax.persistence.*;

import org.springframework.format.annotation.DateTimeFormat;

import java.util.Set;
import java.util.HashSet;
import java.util.Date;

@Entity(name = "project")
@Table(name = "project")
public class Project {

	private Long id;
	private int phase;
    private String name;
    private User author;
    private Date created;
    private Date modified;
    private String description;
    private String pictureFile;
    private String pictureDescription;
    private Set<Observation> observations = new HashSet<Observation>();
    private Set<User> users = new HashSet<User>();
    
    @Id
    @Column(name = "projectId")
    @GeneratedValue(strategy = GenerationType.AUTO)
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	@Column(name = "phase", unique = false, nullable = false)
	public int getPhase() {
		return phase;
	}
	public void setPhase(int phase) {
		this.phase = phase;
	}
	@Column(name = "name", unique = true, nullable = false)
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	@ManyToOne( cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch=FetchType.EAGER )
    @JoinTable(name="project_author",
        joinColumns = @JoinColumn(name="project"),
        inverseJoinColumns = @JoinColumn(name="person")
    )
	public User getAuthor() {
		return author;
	}
	public void setAuthor(User author) {
		this.author = author;
	}
	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat (pattern="dd-MMM-YYYY")
	@Column(name = "created", unique = false, nullable = false)
	public Date getCreated() {
		return created;
	}
	public void setCreated(Date created) {
		this.created = created;
	}
	@Temporal(TemporalType.TIMESTAMP)
	@DateTimeFormat (pattern="dd-MMM-YYYY")
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
	@Column(name = "pictureFile", unique = false, nullable = true)
	public String getPictureFile() {
		return pictureFile;
	}
	public void setPictureFile(String picturefile) {
		this.pictureFile = picturefile;
	}
	@Column(name = "pictureDescription", unique = false, nullable = true)
	public String getPicture() {
		return pictureDescription;
	}
	public void setPicture(String picture) {
		this.pictureDescription = picture;
	}
	@ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
	@JoinTable(name = "project_observation", joinColumns = { 
			@JoinColumn(name = "project", nullable = false) }, 
			inverseJoinColumns = { @JoinColumn(name = "observation", 
					nullable = false) })
	public Set<Observation> getObservations() {
		return observations;
	}
	public void setObservations(Set<Observation> observations) {
		this.observations = observations;
	}
	@ManyToMany(fetch = FetchType.EAGER, mappedBy = "projects")
	public Set<User> getUsers() {
		return users;
	}
	public void setUsers(Set<User> users) {
		this.users = users;
	}

    
}
