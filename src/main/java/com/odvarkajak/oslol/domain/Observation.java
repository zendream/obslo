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
    private String picturefile;
    private Blob picture;
    private String contentfile;
    private Blob content;
    private String contentType;
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
        joinColumns = @JoinColumn(name="observationId"),
        inverseJoinColumns = @JoinColumn(name="userId")
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
	@Column(name = "contentfile", unique = false, nullable = false)
	public String getContentfile() {
		return contentfile;
	}
	public void setContentfile(String contentfile) {
		this.contentfile = contentfile;
	}
	@Column(name = "content", unique = false, nullable = false)
	@Lob
	public Blob getContent() {
		return content;
	}
	public void setContent(Blob content) {
		this.content = content;
	}
	@Column(name = "contenttype", unique = false, nullable = false)
	public String getContentType() {
		return contentType;
	}
	public void setContentType(String contentType) {
		this.contentType = contentType;
	}
	
	@Column(name = "target", unique = false, nullable = false)
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
}
