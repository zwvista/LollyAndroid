package com.zwstudio.lolly.domain;

// Generated 2014-10-12 21:44:14 by Hibernate Tools 4.3.1

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Dictionary generated by hbm2java
 */
@Entity
@Table(name = "DICTIONARIES")
public class Dictionary implements java.io.Serializable {

	@Column(name = "LANGID", nullable = false)
	public int langid;
	@Column(name = "DICTNAME", nullable = false, length = 2000000000)
	public String dictname;
	@Column(name = "ORD", nullable = false)
	public int ord;
	@Column(name = "DICTTYPEID", nullable = false)
	public int dicttypeid;
	@Column(name = "LANGIDTO", nullable = false)
	public int langidto;
	@Column(name = "URL", length = 2000000000)
	public String url;
	@Column(name = "CHCONV", length = 2000000000)
	public String chconv;
	@Column(name = "AUTOMATION", length = 2000000000)
	public String automation;
	@Column(name = "AUTOJUMP", nullable = false)
	public int autojump;
	@Column(name = "DICTTABLE", length = 2000000000)
	public String dicttable;
	@Column(name = "TEMPLATE", length = 2000000000)
	public String template;

	public Dictionary() {
	}

}
