package com.odvarkajak.oslol.domain;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity(name= "datafile")
@Table(name = "datafile")
public class DataFile {
	
	private Long id;
	private String path;
	private String name;
	private String description;
	private Long size;
	private Date created;
	private Observation parentObservation;
	
	@Id
    @Column(name = "fileId")
    @GeneratedValue(strategy = GenerationType.AUTO)
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	@Column(name = "path", unique = true, nullable = false)
	public String getPath() {
		return path;
	}	
	public void setPath(String path) {
		this.path = path;
	}
	@Column(name = "name", unique = false, nullable = false)
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	@Column(name = "description", unique = false, nullable = false)
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	@Column( name = "size", unique = false, nullable = false)
	public Long getSize() {
		return size;
	}
	public void setSize(Long size) {
		this.size = size;
	}
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "created", unique = false, nullable = false)
	public Date getCreated() {
		return created;
	}
	public void setCreated(Date created) {
		this.created = created;
	}
	@ManyToOne( cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch=FetchType.EAGER )
    @JoinTable(name="datafile_observation",
        joinColumns = @JoinColumn(name="datafile"),
        inverseJoinColumns = @JoinColumn(name="observation")
    )
	public Observation getParentObservation() {
		return parentObservation;
	}
	public void setParentObservation(Observation parentObservation) {
		this.parentObservation = parentObservation;
	}
	
	
	
	
}
