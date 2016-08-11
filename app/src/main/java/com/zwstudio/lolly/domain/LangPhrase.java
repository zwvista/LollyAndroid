package com.zwstudio.lolly.domain;

// Generated 2014-10-4 23:22:52 by Hibernate Tools 4.3.1

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * Language generated by hbm2java
 */
@Entity(name = "LANGPHRASES")
public class LangPhrase implements java.io.Serializable {

    @Id
    @Column(name = "ID", nullable = false)
    public int id;
    @Column(name = "LANGID", nullable = false)
    public int langid;
    @Column(name = "PHRASE", nullable = false)
    public String phrase;
    @Column(name = "TRANSLATION")
    public String translation;

    public LangPhrase() {
    }
}
