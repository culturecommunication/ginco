-- Table: thesaurus, thesaurus_type

DROP TABLE IF EXISTS thesaurus;
DROP TABLE IF EXISTS thesaurus_type;
DROP TABLE IF EXISTS thesaurus_format;
DROP TABLE IF EXISTS languages_iso639;
DROP TABLE IF EXISTS thesaurus_organization;
DROP TABLE IF EXISTS log_journal;
DROP TABLE IF EXISTS thesaurus_term;
DROP TABLE IF EXISTS thesaurus_term_role;
DROP TABLE IF EXISTS thesaurus_concept;
DROP TABLE IF EXISTS hierarchical_relationship;
DROP TABLE IF EXISTS top_relationship;

DROP SEQUENCE IF EXISTS log_journal_identifier_seq;
DROP SEQUENCE IF EXISTS thesaurus_term_role_identifier_seq;
DROP SEQUENCE IF EXISTS thesaurus_creator_identifier_seq;

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


CREATE TABLE log_journal (
    identifier integer NOT NULL,
    action text NOT NULL,
    author text NOT NULL,
    date text,
    entityid text NOT NULL,
    entitytype text NOT NULL
);
CREATE SEQUENCE log_journal_identifier_seq START WITH 1  INCREMENT BY 1;

CREATE TABLE thesaurus_concept
(
  identifier text NOT NULL,
  created text NOT NULL,
  modified text NOT NULL,
  status text,
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

ALTER TABLE thesaurus_term
    ADD FOREIGN KEY (conceptid)
    REFERENCES thesaurus_concept (identifier);

ALTER TABLE thesaurus_term
    ADD FOREIGN KEY (role)
    REFERENCES thesaurus_term_role (code);

ALTER TABLE thesaurus_concept
    ADD FOREIGN KEY (thesaurusid)
    REFERENCES thesaurus (identifier);
    
ALTER TABLE hierarchical_relationship
   ADD FOREIGN KEY (childconceptid)
   REFERENCES thesaurus_concept (identifier);
   
ALTER TABLE hierarchical_relationship
   ADD FOREIGN KEY (parentconceptid)
   REFERENCES thesaurus_concept (identifier);

ALTER TABLE top_relationship
   ADD FOREIGN KEY (childconceptid)
   REFERENCES thesaurus_concept (identifier);
      
ALTER TABLE top_relationship
   ADD FOREIGN KEY (rootconceptid)
   REFERENCES thesaurus_concept (identifier);
