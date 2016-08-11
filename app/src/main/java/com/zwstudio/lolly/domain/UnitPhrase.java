package com.zwstudio.lolly.domain;

// Generated 2014-10-4 23:22:52 by Hibernate Tools 4.3.1

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * Language generated by hbm2java
 */
@Entity(name = "VUNITPHRASES")
public class UnitPhrase implements java.io.Serializable {

    @Id
    @Column(name = "ID", nullable = false)
    public int id;
    @Column(name = "TEXTBOOKID")
    public int textbookid;
    @Column(name = "UNIT")
    public int unit;
    @Column(name = "PART")
    public int part;
    @Column(name = "SEQNUM")
    public int seqnum;
    @Column(name = "LANGPHRASEID")
    public Integer langphraseid;
    @Column(name = "PHRASE")
    public String phrase;
    @Column(name = "TRANSLATION")
    public String translation;

    public UnitPhrase() {
    }
}
