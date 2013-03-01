DROP TABLE IF EXISTS log_journal;
--
-- Name: revinfo; Type: TABLE; Schema: public; Owner: hadocdb; Tablespace: 
--

CREATE TABLE revinfo (
    rev integer NOT NULL,
    revtstmp bigint,
    username character varying(255)
);



--
-- Name: revinfo_identifier_seq; Type: SEQUENCE; Schema: public; Owner: hadocdb
--

CREATE SEQUENCE revinfo_identifier_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;



--
-- Name: revinfoentitytypes; Type: TABLE; Schema: public; Owner: hadocdb; Tablespace: 
--

CREATE TABLE revinfoentitytypes (
    id integer NOT NULL,
    entityclassname character varying(255),
    revision integer
);



--
-- Name: revinfoentitytypes_identifier_seq; Type: SEQUENCE; Schema: public; Owner: hadocdb
--

CREATE SEQUENCE revinfoentitytypes_identifier_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;



--
-- Name: thesaurus_aud; Type: TABLE; Schema: public; Owner: hadocdb; Tablespace: 
--

CREATE TABLE thesaurus_aud (
    identifier character varying(255) NOT NULL,
    rev integer NOT NULL,
    revtype smallint,
    contributor character varying(255),
    coverage character varying(255),
    date timestamp without time zone,
    description character varying(255),
    publisher character varying(255),
    relation character varying(255),
    rights character varying(255),
    source character varying(255),
    subject character varying(255),
    title character varying(255),
    created timestamp without time zone,
    defaulttopconcept boolean,
    format integer,
    type integer,
    creator integer
);



--
-- Name: thesaurus_languages_aud; Type: TABLE; Schema: public; Owner: hadocdb; Tablespace: 
--

CREATE TABLE thesaurus_languages_aud (
    rev integer NOT NULL,
    thesaurus_identifier character varying(255) NOT NULL,
    iso639_id character varying(255) NOT NULL,
    revtype smallint
);





--
-- Name: thesaurus_term_aud; Type: TABLE; Schema: public; Owner: hadocdb; Tablespace: 
--

CREATE TABLE thesaurus_term_aud (
    identifier character varying(255) NOT NULL,
    rev integer NOT NULL,
    revtype smallint,
    lexicalvalue character varying(255),
    created timestamp without time zone,
    modified timestamp without time zone,
    source character varying(255),
    prefered boolean,
    status integer,
    role character varying(255),
    lang character varying(255),
    thesaurusid character varying(255),
    conceptid character varying(255)
);



--
-- Name: thesaurus_thesaurusterm_aud; Type: TABLE; Schema: public; Owner: hadocdb; Tablespace: 
--

CREATE TABLE thesaurus_thesaurusterm_aud (
    rev integer NOT NULL,
    thesaurusid character varying(255) NOT NULL,
    identifier character varying(255) NOT NULL,
    revtype smallint
);



--
-- Name: thesaurus_thesaurusversionhistory_aud; Type: TABLE; Schema: public; Owner: hadocdb; Tablespace: 
--

CREATE TABLE thesaurus_thesaurusversionhistory_aud (
    rev integer NOT NULL,
    identifier character varying(255) NOT NULL,
    revtype smallint
);



--
-- Name: revinfo_pkey; Type: CONSTRAINT; Schema: public; Owner: hadocdb; Tablespace: 
--

ALTER TABLE ONLY revinfo
    ADD CONSTRAINT revinfo_pkey PRIMARY KEY (rev);


--
-- Name: revinfoentitytypes_pkey; Type: CONSTRAINT; Schema: public; Owner: hadocdb; Tablespace: 
--

ALTER TABLE ONLY revinfoentitytypes
    ADD CONSTRAINT revinfoentitytypes_pkey PRIMARY KEY (id);


--
-- Name: thesaurus_aud_pkey; Type: CONSTRAINT; Schema: public; Owner: hadocdb; Tablespace: 
--

ALTER TABLE ONLY thesaurus_aud
    ADD CONSTRAINT thesaurus_aud_pkey PRIMARY KEY (identifier, rev);


--
-- Name: thesaurus_languages_aud_pkey; Type: CONSTRAINT; Schema: public; Owner: hadocdb; Tablespace: 
--

ALTER TABLE ONLY thesaurus_languages_aud
    ADD CONSTRAINT thesaurus_languages_aud_pkey PRIMARY KEY (rev, thesaurus_identifier, iso639_id);


--
-- Name: thesaurus_term_aud_pkey; Type: CONSTRAINT; Schema: public; Owner: hadocdb; Tablespace: 
--

ALTER TABLE ONLY thesaurus_term_aud
    ADD CONSTRAINT thesaurus_term_aud_pkey PRIMARY KEY (identifier, rev);


--
-- Name: thesaurus_thesaurusterm_aud_pkey; Type: CONSTRAINT; Schema: public; Owner: hadocdb; Tablespace: 
--

ALTER TABLE ONLY thesaurus_thesaurusterm_aud
    ADD CONSTRAINT thesaurus_thesaurusterm_aud_pkey PRIMARY KEY (rev, thesaurusid, identifier);


--
-- Name: thesaurus_thesaurusversionhistory_aud_pkey; Type: CONSTRAINT; Schema: public; Owner: hadocdb; Tablespace: 
--

ALTER TABLE ONLY thesaurus_thesaurusversionhistory_aud
    ADD CONSTRAINT thesaurus_thesaurusversionhistory_aud_pkey PRIMARY KEY (rev, identifier);


--
-- Name: fk29bf1de7d0d1bcb5; Type: FK CONSTRAINT; Schema: public; Owner: hadocdb
--

ALTER TABLE ONLY thesaurus_languages_aud
    ADD CONSTRAINT fk29bf1de7d0d1bcb5 FOREIGN KEY (rev) REFERENCES revinfo(rev);


--
-- Name: fk85e9bf12d0d1bcb5; Type: FK CONSTRAINT; Schema: public; Owner: hadocdb
--

ALTER TABLE ONLY thesaurus_thesaurusterm_aud
    ADD CONSTRAINT fk85e9bf12d0d1bcb5 FOREIGN KEY (rev) REFERENCES revinfo(rev);

--
-- Name: fkb34829cbd0d1bcb5; Type: FK CONSTRAINT; Schema: public; Owner: hadocdb
--

ALTER TABLE ONLY thesaurus_aud
    ADD CONSTRAINT fkb34829cbd0d1bcb5 FOREIGN KEY (rev) REFERENCES revinfo(rev);


--
-- Name: fkbfbbfde2d0d1bcb5; Type: FK CONSTRAINT; Schema: public; Owner: hadocdb
--

ALTER TABLE ONLY thesaurus_term_aud
    ADD CONSTRAINT fkbfbbfde2d0d1bcb5 FOREIGN KEY (rev) REFERENCES revinfo(rev);


--
-- Name: fke94d7a05c144bbed; Type: FK CONSTRAINT; Schema: public; Owner: hadocdb
--

ALTER TABLE ONLY revinfoentitytypes
    ADD CONSTRAINT fke94d7a05c144bbed FOREIGN KEY (revision) REFERENCES revinfo(rev);


--
-- Name: fke9bd902d0d1bcb5; Type: FK CONSTRAINT; Schema: public; Owner: hadocdb
--

ALTER TABLE ONLY thesaurus_thesaurusversionhistory_aud
    ADD CONSTRAINT fke9bd902d0d1bcb5 FOREIGN KEY (rev) REFERENCES revinfo(rev);
    
    
    
--
-- Name: hierarchical_relationship_aud; Type: TABLE; Schema: public; Owner: hadocdb; Tablespace: 
--

CREATE TABLE hierarchical_relationship_aud (
    rev integer NOT NULL,
    childconceptid character varying(255) NOT NULL,
    parentconceptid character varying(255) NOT NULL,
    revtype smallint
);

--
-- Name: thesaurus_concept_aud; Type: TABLE; Schema: public; Owner: hadocdb; Tablespace: 
--

CREATE TABLE thesaurus_concept_aud (
    identifier character varying(255) NOT NULL,
    rev integer NOT NULL,
    revtype smallint,
    created timestamp without time zone,
    modified timestamp without time zone,
    status character varying(255),
    notation character varying(255),
    topconcept boolean,
    thesaurusid character varying(255)
);

--
-- Name: top_relationship_aud; Type: TABLE; Schema: public; Owner: hadocdb; Tablespace: 
--

CREATE TABLE top_relationship_aud (
    rev integer NOT NULL,
    childconceptid character varying(255) NOT NULL,
    rootconceptid character varying(255) NOT NULL,
    revtype smallint
);

--
-- Name: hierarchical_relationship_aud_pkey; Type: CONSTRAINT; Schema: public; Owner: hadocdb; Tablespace: 
--

ALTER TABLE ONLY hierarchical_relationship_aud
    ADD CONSTRAINT hierarchical_relationship_aud_pkey PRIMARY KEY (rev, childconceptid, parentconceptid);

    --
-- Name: fkcd658dffd0d1bcb5; Type: FK CONSTRAINT; Schema: public; Owner: hadocdb
--

ALTER TABLE ONLY hierarchical_relationship_aud
    ADD CONSTRAINT fkcd658dffd0d1bcb5 FOREIGN KEY (rev) REFERENCES revinfo(rev);

--
-- Name: thesaurus_concept_aud_pkey; Type: CONSTRAINT; Schema: public; Owner: hadocdb; Tablespace: 
--

ALTER TABLE ONLY thesaurus_concept_aud
    ADD CONSTRAINT thesaurus_concept_aud_pkey PRIMARY KEY (identifier, rev);
    
    --
-- Name: fk2cf9f474d0d1bcb5; Type: FK CONSTRAINT; Schema: public; Owner: hadocdb
--

ALTER TABLE ONLY thesaurus_concept_aud
    ADD CONSTRAINT fk2cf9f474d0d1bcb5 FOREIGN KEY (rev) REFERENCES revinfo(rev);

    --
-- Name: top_relationship_aud_pkey; Type: CONSTRAINT; Schema: public; Owner: hadocdb; Tablespace: 
--

ALTER TABLE ONLY top_relationship_aud
    ADD CONSTRAINT top_relationship_aud_pkey PRIMARY KEY (rev, childconceptid, rootconceptid);

--
-- Name: fkb18c9db3d0d1bcb5; Type: FK CONSTRAINT; Schema: public; Owner: hadocdb
--

ALTER TABLE ONLY top_relationship_aud
    ADD CONSTRAINT fkb18c9db3d0d1bcb5 FOREIGN KEY (rev) REFERENCES revinfo(rev);
