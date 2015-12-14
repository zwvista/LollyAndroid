package com.zwstudio.lolly.domain;

// Generated 2014-10-12 21:44:14 by Hibernate Tools 4.3.1

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * DictAll generated by hbm2java
 */
@Entity	(name = "DICTALL")
public class DictAll implements java.io.Serializable {

	@Column(name = "LANGID")
	public Integer langid;
	@Column(name = "DICTNAME", length = 2000000000)
	public String dictname;
	@Column(name = "ORD")
	public Integer ord;
	@Column(name = "LANGIDTO")
	public Integer langidto;
	@Column(name = "DICTTYPENAME", length = 2000000000)
	public String dicttypename;
	@Column(name = "URL", length = 2000000000)
	public String url;
	@Column(name = "CHCONV", length = 2000000000)
	public String chconv;
	@Column(name = "AUTOMATION", length = 2000000000)
	public String automation;
	@Column(name = "AUTOJUMP")
	public Integer autojump;
	@Column(name = "DICTTABLE", length = 2000000000)
	public String dicttable;
	@Column(name = "TRANSFORM_WIN", length = 2000000000)
	public String transformWin;
	@Column(name = "TRANSFORM_MAC", length = 2000000000)
	public String transformMac;
	@Column(name = "WAIT")
	public Integer wait;
	@Column(name = "TEMPLATE", length = 2000000000)
	public String template;

	public DictAll() {
	}
}