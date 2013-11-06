-- Table: thesaurus, thesaurus_type

DROP TABLE IF EXISTS thesaurus;
DROP TABLE IF EXISTS thesaurus_type;
DROP TABLE IF EXISTS thesaurus_format;
DROP TABLE IF EXISTS thesaurus_formats;
DROP TABLE IF EXISTS thesaurus_formats_aud;
DROP TABLE IF EXISTS thesaurus_version_history;
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
DROP TABLE IF EXISTS concept_group_label;
DROP TABLE IF EXISTS concept_group_type;
DROP TABLE IF EXISTS concept_group;
DROP TABLE IF EXISTS node_label;
DROP TABLE IF EXISTS thesaurus_array_concept;
DROP TABLE IF EXISTS revinfo;
DROP TABLE IF EXISTS revinfoentitytypes;
DROP TABLE IF EXISTS thesaurus_aud;
DROP TABLE IF EXISTS note_type;
DROP TABLE IF EXISTS thesaurus_ark;
DROP TABLE IF EXISTS custom_concept_attribute_type;
DROP TABLE IF EXISTS custom_term_attribute_type;
DROP TABLE IF EXISTS custom_concept_attribute;
DROP TABLE IF EXISTS custom_term_attribute;
DROP TABLE IF EXISTS note;
DROP TABLE IF EXISTS user_role;


DROP SEQUENCE IF EXISTS thesaurus_term_role_identifier_seq;
DROP SEQUENCE IF EXISTS thesaurus_creator_identifier_seq;
DROP SEQUENCE IF EXISTS node_label_id_seq;
DROP SEQUENCE IF EXISTS revinfo_identifier_seq;
DROP SEQUENCE IF EXISTS revinfoentitytypes_identifier_seq;
DROP SEQUENCE IF EXISTS custom_concept_attribute_type_identifier_seq;
DROP SEQUENCE IF EXISTS custom_term_attribute_type_identifier_seq;
DROP SEQUENCE IF EXISTS user_role_identifier_seq;


CREATE TABLE thesaurus
(
  identifier text NOT NULL,
  contributor text,
  coverage text,
  date text,
  description text,
  publisher text,
  relation text,
  rights text,
  source text,
  subject text,
  title text NOT NULL,
  type integer,
  creator integer,
  created text,
  defaulttopconcept boolean NOT NULL DEFAULT FALSE,
  archived boolean DEFAULT FALSE,
  ispolyhierarchical boolean DEFAULT FALSE

);

CREATE TABLE thesaurus_type (
    identifier integer NOT NULL,
    label text NOT NULL
);

CREATE TABLE thesaurus_format (
    identifier integer NOT NULL,
    label text NOT NULL
);

CREATE TABLE thesaurus_version_history
(
  identifier text NOT NULL,
  date text,
  versionnote text,
  thesaurus_identifier text NOT NULL,
  thisversion boolean NOT NULL DEFAULT false,
  status integer NOT NULL DEFAULT 0,
  userid text,
  CONSTRAINT pk_thesaurus_version_history PRIMARY KEY (identifier)
);

ALTER TABLE thesaurus_version_history
    ADD FOREIGN KEY (thesaurus_identifier)
    REFERENCES thesaurus (identifier)
    ON UPDATE NO ACTION ON DELETE CASCADE;

CREATE TABLE languages_iso639 (
    id character(5) NOT NULL,
    part1 character(2),
    ref_name character varying(150) NOT NULL,
    toplanguage boolean NOT NULL DEFAULT FALSE,
    principallanguage boolean DEFAULT FALSE
);

CREATE TABLE thesaurus_organization (
  identifier integer NOT NULL,
  name text NOT NULL,
  homepage text,
  email text
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
  lang character varying(3) NOT NULL,
  hidden boolean DEFAULT false NOT NULL
);

CREATE TABLE hierarchical_relationship
(
  childconceptid text NOT NULL,
  parentconceptid text NOT NULL,
  role integer NOT NULL DEFAULT FALSE,
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
  skoslabel text,
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
  parentarrayid text,
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

ALTER TABLE thesaurus_array
      ADD FOREIGN KEY (parentarrayid)
      REFERENCES thesaurus_array (identifier)
      ON UPDATE NO ACTION ON DELETE SET NULL;

CREATE TABLE concept_group_type
(
  code text NOT NULL,
  label text NOT NULL,
  CONSTRAINT pk_concept_group_type_code PRIMARY KEY (code)
);

CREATE TABLE concept_group
(
  identifier text NOT NULL,
  thesaurusid text NOT NULL,
  conceptgrouptypecode text NOT NULL,
  parentgroupid text,
  CONSTRAINT pk_concept_group_identifier PRIMARY KEY (identifier)
);

ALTER TABLE concept_group
      ADD FOREIGN KEY (thesaurusid)
      REFERENCES thesaurus (identifier)
      ON UPDATE NO ACTION ON DELETE CASCADE;

ALTER TABLE concept_group
      ADD FOREIGN KEY (conceptgrouptypecode)
      REFERENCES concept_group_type (code)
      ON UPDATE NO ACTION ON DELETE NO ACTION;

ALTER TABLE concept_group
      ADD FOREIGN KEY (parentgroupid)
      REFERENCES concept_group (identifier)
      ON UPDATE NO ACTION ON DELETE SET NULL;

CREATE TABLE concept_group_label
(
  identifier integer NOT NULL,
  lexicalvalue text NOT NULL,
  created text NOT NULL,
  modified text NOT NULL,
  lang character(5) NOT NULL,
  conceptgroupid text NOT NULL,
  CONSTRAINT pk_concept_group_label_identifier PRIMARY KEY (identifier)
);

ALTER TABLE concept_group_label
    ADD FOREIGN KEY (conceptgroupid)
    REFERENCES concept_group (identifier)
    ON UPDATE NO ACTION ON DELETE CASCADE;

ALTER TABLE concept_group_label
    ADD FOREIGN KEY (lang)
    REFERENCES languages_iso639 (id)
    ON UPDATE NO ACTION ON DELETE NO ACTION;


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
    username character varying(255),
    thesaurusid text
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
    archived boolean,
    type integer,
    creator integer,
    ispolyhierarchical boolean
);

CREATE TABLE note_type
(
  code text NOT NULL,
  label text NOT NULL,
  isterm boolean NOT NULL,
  isconcept boolean NOT NULL,
  CONSTRAINT pk_note_type PRIMARY KEY (code)
 );

CREATE TABLE note
(
  identifier text NOT NULL,
  lexicalvalue text NOT NULL,
  lang text NOT NULL,
  source text,
  notetypecode text NOT NULL,
  conceptid text,
  termid text,
  CONSTRAINT pk_note_identifier PRIMARY KEY (identifier)
);

  CREATE TABLE thesaurus_ark
  (
    identifier text NOT NULL,
    created text,
    entity text NOT NULL,
    CONSTRAINT pk_thesaurus_ark PRIMARY KEY (identifier)
  );

CREATE SEQUENCE custom_concept_attribute_type_identifier_seq  START WITH 1  INCREMENT BY 1 ;
CREATE SEQUENCE custom_term_attribute_type_identifier_seq START WITH 1  INCREMENT BY 1 ;

CREATE TABLE custom_concept_attribute_type
(
  identifier int NOT NULL,
  code text NOT NULL,
  value text NOT NULL,
  thesaurusid text NOT NULL,
  exportable boolean,
  CONSTRAINT pk_custom_concept_attribute_type PRIMARY KEY (identifier)
);

CREATE TABLE custom_term_attribute_type
(
  identifier int NOT NULL,
  code text NOT NULL,
  value text NOT NULL,
  thesaurusid text NOT NULL,
  CONSTRAINT pk_custom_term_attribute_type PRIMARY KEY (identifier)
);

CREATE TABLE custom_concept_attribute
(
  identifier text NOT NULL,
  conceptid text,
  typeid int,
  lang text,
  lexicalvalue text,
  CONSTRAINT pk_custom_concept_attribute PRIMARY KEY (identifier)/*,
  CONSTRAINT fk_custom_concept_attribute_lang FOREIGN KEY (lang)
  REFERENCES languages_iso639 (id) MATCH SIMPLE
  ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT fk_custom_concept_attribute_conceptid FOREIGN KEY (conceptid)
  REFERENCES thesaurus_concept (identifier) MATCH SIMPLE
  ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT fk_custom_concept_attribute_typeid FOREIGN KEY (typeid)
  REFERENCES custom_concept_attribute_type (identifier) MATCH SIMPLE
  ON UPDATE NO ACTION ON DELETE NO ACTION        */
);

CREATE TABLE custom_term_attribute
(
  identifier text NOT NULL,
  termid text,
  typeid int,
  lang text,
  lexicalvalue text,
  CONSTRAINT pk_custom_term_attribute PRIMARY KEY (identifier)/*,
  CONSTRAINT fk_custom_term_attribute_lang FOREIGN KEY (lang)
  REFERENCES languages_iso639 (id) MATCH SIMPLE
  ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT fk_custom_term_attribute_termid FOREIGN KEY (termid)
  REFERENCES thesaurus_term (identifier) MATCH SIMPLE
  ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT fk_custom_term_attribute_typeid FOREIGN KEY (typeid)
  REFERENCES custom_term_attribute_type (identifier) MATCH SIMPLE
  ON UPDATE NO ACTION ON DELETE NO ACTION  */
);

CREATE TABLE thesaurus_formats
(
  format_identifier integer NOT NULL,
  thesaurus_identifier text NOT NULL,
  CONSTRAINT pk_thesaurus_formats PRIMARY KEY (format_identifier, thesaurus_identifier)
);

CREATE TABLE thesaurus_formats_aud
(
  rev integer NOT NULL,
  thesaurus_identifier text NOT NULL,
  format_identifier integer NOT NULL,
  revtype smallint,
  CONSTRAINT thesaurus_formats_aud_pkey PRIMARY KEY (rev, thesaurus_identifier, format_identifier)
);

CREATE TABLE user_role
(
  identifier integer NOT NULL,
  username text,
  thesaurus_id text,
  role integer,
  CONSTRAINT pk_user_role PRIMARY KEY (identifier)
);

CREATE SEQUENCE user_role_identifier_seq START WITH 1  INCREMENT BY 1 ;

