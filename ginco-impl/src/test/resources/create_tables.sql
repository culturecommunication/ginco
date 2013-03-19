-- Table: thesaurus, thesaurus_type

DROP TABLE IF EXISTS thesaurus;
DROP TABLE IF EXISTS thesaurus_type;
DROP TABLE IF EXISTS thesaurus_format;
DROP TABLE IF EXISTS languages_iso639;
DROP TABLE IF EXISTS thesaurus_organization;
DROP TABLE IF EXISTS thesaurus_term;
DROP TABLE IF EXISTS thesaurus_term_role;
DROP TABLE IF EXISTS thesaurus_concept;
DROP TABLE IF EXISTS hierarchical_relationship;
DROP TABLE IF EXISTS top_relationship;
DROP TABLE IF EXISTS associative_relationship;
DROP TABLE IF EXISTS associative_relationship_role;
DROP TABLE IF EXISTS thesaurus_array;
DROP TABLE IF EXISTS node_label;
DROP TABLE IF EXISTS thesaurus_array_concept;
DROP TABLE IF EXISTS revinfo;
DROP TABLE IF EXISTS revinfoentitytypes;
DROP TABLE IF EXISTS thesaurus_aud;

DROP SEQUENCE IF EXISTS thesaurus_term_role_identifier_seq;
DROP SEQUENCE IF EXISTS thesaurus_creator_identifier_seq;
DROP SEQUENCE IF EXISTS node_label_id_seq;
DROP SEQUENCE IF EXISTS revinfo_identifier_seq;
DROP SEQUENCE IF EXISTS revinfoentitytypes_identifier_seq;


CREATE TABLE thesaurus
(
  identifier text NOT NULL,
  contributor text,
  coverage text,
  date text,
  description text,
  format integer,
  publisher text,
  relation text,
  rights text,
  source text,
  subject text,
  title text NOT NULL,
  type integer,
  creator integer,
  created text,
  defaulttopconcept boolean NOT NULL DEFAULT FALSE
);

CREATE TABLE thesaurus_type (
    identifier integer NOT NULL,
    label text NOT NULL
);

CREATE TABLE thesaurus_format (
    identifier integer NOT NULL,
    label text NOT NULL
);

CREATE TABLE languages_iso639 (
    id character(3) NOT NULL,
    part2b character(3),
    part2t character(3),
    part1 character(2),
    scope character(1) NOT NULL,
    type character(1) NOT NULL,
    ref_name character varying(150) NOT NULL,
    toplanguage boolean NOT NULL DEFAULT FALSE,
    comment character varying(150)
);

CREATE TABLE thesaurus_organization (
  identifier integer NOT NULL,
  name text NOT NULL,
  homepage text
);
CREATE SEQUENCE thesaurus_creator_identifier_seq START WITH 1  INCREMENT BY 1;


CREATE TABLE thesaurus_concept
(
  identifier text NOT NULL,
  created text NOT NULL,
  modified text NOT NULL,
  status integer,
  notation text,
  topconcept boolean,
  thesaurusid text
);

CREATE TABLE thesaurus_term_role (
    code text NOT NULL,
    label text NOT NULL,
    defaultrole boolean NOT NULL
);

CREATE TABLE thesaurus_term
(
  identifier text NOT NULL,
  lexicalvalue text NOT NULL,
  created text NOT NULL,
  modified text NOT NULL,
  source text,
  prefered boolean,
  status integer,
  role text,
  conceptid text,
  thesaurusid text NOT NULL,
  lang character varying(3) NOT NULL
);

CREATE TABLE hierarchical_relationship
(
  childconceptid text NOT NULL,
  parentconceptid text NOT NULL,
  CONSTRAINT pk_hierarchical_relationship PRIMARY KEY (childconceptid, parentconceptid)
);

CREATE TABLE top_relationship
(
  childconceptid text NOT NULL,
  rootconceptid text NOT NULL,
  CONSTRAINT pk_top_relationship PRIMARY KEY (childconceptid, rootconceptid)
);

CREATE TABLE associative_relationship
(
  concept1 text NOT NULL,
  concept2 text NOT NULL,
  role text,
  CONSTRAINT pk_associative_relationship PRIMARY KEY (concept1, concept2)
); 

CREATE TABLE associative_relationship_role
(
  code text NOT NULL,
  label text,
  defaultrole boolean,
  CONSTRAINT pk_associative_relationship_role PRIMARY KEY (code)
);

ALTER TABLE thesaurus_term
    ADD FOREIGN KEY (conceptid)
    REFERENCES thesaurus_concept (identifier);

ALTER TABLE thesaurus_term
    ADD FOREIGN KEY (role)
    REFERENCES thesaurus_term_role (code);

ALTER TABLE thesaurus_concept
    ADD FOREIGN KEY (thesaurusid)
    REFERENCES thesaurus (identifier)
    ON UPDATE NO ACTION ON DELETE CASCADE;
    
ALTER TABLE hierarchical_relationship
   ADD FOREIGN KEY (childconceptid)
   REFERENCES thesaurus_concept (identifier)
    ON UPDATE NO ACTION ON DELETE CASCADE;
   
ALTER TABLE hierarchical_relationship
   ADD FOREIGN KEY (parentconceptid)
   REFERENCES thesaurus_concept (identifier)
   ON UPDATE NO ACTION ON DELETE CASCADE;;

ALTER TABLE top_relationship
   ADD FOREIGN KEY (childconceptid)
   REFERENCES thesaurus_concept (identifier)
   ON UPDATE NO ACTION ON DELETE CASCADE;
      
ALTER TABLE top_relationship
   ADD FOREIGN KEY (rootconceptid)
   REFERENCES thesaurus_concept (identifier)
   ON UPDATE NO ACTION ON DELETE CASCADE;
   
ALTER TABLE associative_relationship
      ADD FOREIGN KEY (role)
      REFERENCES associative_relationship_role (code);
   
ALTER TABLE associative_relationship
      ADD FOREIGN KEY (concept1)      
      REFERENCES thesaurus_concept (identifier)
      ON UPDATE NO ACTION ON DELETE CASCADE;
      
ALTER TABLE associative_relationship
      ADD FOREIGN KEY (concept2)
      REFERENCES thesaurus_concept (identifier)
      ON UPDATE NO ACTION ON DELETE CASCADE;
      
      
CREATE TABLE thesaurus_array
(
  identifier text NOT NULL,
  ordered boolean DEFAULT false NOT NULL,
  notation text,
  thesaurusid text NOT NULL,
  superordinateconceptid text,
  CONSTRAINT pk_thesaurus_array_identifier PRIMARY KEY (identifier)
);

ALTER TABLE thesaurus_array
      ADD FOREIGN KEY (superordinateconceptid)
      REFERENCES thesaurus_concept (identifier)
      ON UPDATE NO ACTION ON DELETE CASCADE;

      
ALTER TABLE thesaurus_array
      ADD FOREIGN KEY (thesaurusid)
      REFERENCES thesaurus (identifier)
      ON UPDATE NO ACTION ON DELETE CASCADE;

CREATE TABLE node_label
(
  id integer NOT NULL,
  lexicalvalue text NOT NULL,
  modified text NOT NULL,
  created text NOT NULL,
  lang text,
  thesaurusarrayid text NOT NULL,
  CONSTRAINT pk_note_label_id PRIMARY KEY (id)
);

ALTER TABLE node_label
      ADD FOREIGN KEY (thesaurusarrayid)
      REFERENCES thesaurus_array (identifier)
      ON UPDATE NO ACTION ON DELETE CASCADE;
    
CREATE SEQUENCE node_label_id_seq START WITH 1  INCREMENT BY 1;

CREATE TABLE thesaurus_array_concept
(
  thesaurusarrayid text NOT NULL,
  conceptid text NOT NULL,
  CONSTRAINT pk_thesaurus_array_concept PRIMARY KEY (thesaurusarrayid, conceptid) 
);

ALTER TABLE thesaurus_array_concept
      ADD FOREIGN KEY (thesaurusarrayid)
      REFERENCES thesaurus_array (identifier)
       ON UPDATE NO ACTION ON DELETE CASCADE; 
      
ALTER TABLE thesaurus_array_concept
      ADD FOREIGN KEY (conceptid)
      REFERENCES thesaurus_concept (identifier)
      ON UPDATE NO ACTION ON DELETE CASCADE; 
      
CREATE SEQUENCE revinfo_identifier_seq  START WITH 1  INCREMENT BY 1 ;
CREATE SEQUENCE revinfoentitytypes_identifier_seq START WITH 1  INCREMENT BY 1 ; 


CREATE TABLE revinfo (
    rev integer NOT NULL,
    revtstmp bigint,
    username character varying(255)
);

CREATE TABLE revinfoentitytypes (
    id integer NOT NULL,
    entityclassname character varying(255),
    revision integer
);

CREATE TABLE thesaurus_aud (
    identifier character varying(255) NOT NULL,
    rev integer NOT NULL,
    revtype smallint,
    contributor character varying(255),
    coverage character varying(255),
    date text,
    description character varying(255),
    publisher character varying(255),
    relation character varying(255),
    rights character varying(255),
    source character varying(255),
    subject character varying(255),
    title character varying(255),
    created text,
    defaulttopconcept boolean,
    format integer,
    type integer,
    creator integer
);
