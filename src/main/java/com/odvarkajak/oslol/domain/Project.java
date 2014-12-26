package com.odvarkajak.oslol.domain;

import javax.persistence.*;

import java.util.Set;
import java.util.HashSet;
import java.sql.Blob;
import java.util.Date;

@Entity
@Table(name = "project")
public class Project {

	private Long id;
    private String name;
    private User author;
    private Date created;
    private Date modified;
    private String description;
    private String picturefile;
    private Blob picture;
    private Set<ObservationToProject> observationProjects = new HashSet<ObservationToProject>(0);
    private Set<User> users = new HashSet<User>(0);
    
    @Id
    @Column(name = "projectId")
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
    @JoinTable(name="project_author",
        joinColumns = @JoinColumn(name="observation_id"),
        inverseJoinColumns = @JoinColumn(name="person_id")
    )
	public User getAuthor() {
		return author;
	}
	public void setAuthor(User author) {
		this.author = author;
	}
	@Temporal(TemporalType.DATE)
	@Column(name = "created", unique = false, nullable = false)
	public Date getCreated() {
		return created;
	}
	public void setCreated(Date created) {
		this.created = created;
	}
	@Temporal(TemporalType.DATE)
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
	@Column(name = "picturefile", unique = false, nullable = true)
	public String getPicturefile() {
		return picturefile;
	}
	public void setPicturefile(String picturefile) {
		this.picturefile = picturefile;
	}
	@Column(name = "picture", unique = false, nullable = true)
	@Lob
	public Blob getPicture() {
		return picture;
	}
	public void setPicture(Blob picture) {
		this.picture = picture;
	}
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "pk.project")
	public Set<ObservationToProject> getObservationProjects() {
		return observationProjects;
	}
	public void setObservationProjects(Set<ObservationToProject> observationProjects) {
		this.observationProjects = observationProjects;
	}
	@ManyToMany(fetch = FetchType.LAZY, mappedBy = "projects")
	public Set<User> getUsers() {
		return users;
	}
	public void setUsers(Set<User> users) {
		this.users = users;
	}
    
}
