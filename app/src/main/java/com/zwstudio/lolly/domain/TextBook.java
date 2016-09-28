package com.zwstudio.lolly.domain;

// Generated 2014-10-4 23:22:52 by Hibernate Tools 4.3.1

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * Textbook generated by hbm2java
 */
@Entity(name = "VTEXTBOOKS")
public class Textbook implements java.io.Serializable {

	@Id
	@Column(name = "ID", nullable = false)
	public int id;
	@Column(name = "LANGID")
	public int langid;
	@Column(name = "TEXTBOOKNAME")
	public String textbookname;
	@Column(name = "UNITS")
	public int units;
	@Column(name = "PARTS")
	public String parts;
	@Column(name = "USUNITFROM")
	public int usunitfrom;
	@Column(name = "USPARTFROM")
	public int uspartfrom;
	@Column(name = "USUNITTO")
	public int usunitto;
	@Column(name = "USPARTTO")
	public int uspartto;

	public Textbook() {
	}
}