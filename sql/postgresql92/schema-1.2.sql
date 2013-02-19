-- Table: log_journal
CREATE TABLE log_journal (
    identifier integer NOT NULL,
    action text NOT NULL,
    author text NOT NULL,
    date timestamp without time zone,
    entityid text NOT NULL,
    entitytype text NOT NULL
);

ALTER TABLE ONLY log_journal
    ADD CONSTRAINT pk_log_journal_identifier PRIMARY KEY (identifier);
    
CREATE SEQUENCE log_journal_identifier_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

ALTER SEQUENCE log_journal_identifier_seq OWNED BY log_journal.identifier;

-- Table: thesaurus_concept
CREATE TABLE thesaurus_concept
(
  identifier text NOT NULL,
  created timestamp without time zone NOT NULL DEFAULT now(),
  modified timestamp without time zone NOT NULL DEFAULT now(),
  status text,
  notation text,
  topconcept boolean,
  CONSTRAINT pk_thesaurus_concept_identifier PRIMARY KEY (identifier)
);

-- Table: thesaurus_term
CREATE TABLE thesaurus_term
(
  identifier text NOT NULL,
  lexicalvalue text NOT NULL,
  created timestamp without time zone NOT NULL DEFAULT now(),
  modified timestamp without time zone NOT NULL DEFAULT now(),
  source text,
  prefered boolean,
  status integer,
  role integer,
  conceptid text,
  thesaurusid text NOT NULL,
  lang character varying(3) NOT NULL,
  CONSTRAINT pk_term_identifier PRIMARY KEY (identifier),
  CONSTRAINT fk_term_languages_iso639 FOREIGN KEY (lang)
      REFERENCES languages_iso639 (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT fk_term_thesaurus FOREIGN KEY (thesaurusid)
      REFERENCES thesaurus (identifier) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
);

-- Table: thesaurus -- adding defaulttopconcept
alter table thesaurus add defaulttopconcept boolean NOT NULL DEFAULT FALSE;

-- ALTER TABLE thesaurus_term DROP CONSTRAINT fk_term_thesaurus_concept;
ALTER TABLE thesaurus_term
  ADD CONSTRAINT fk_term_thesaurus_concept FOREIGN KEY (conceptid)
      REFERENCES thesaurus_concept (identifier) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION;

-- Indexes on table thesaurus_term
CREATE INDEX idx_thesaurus_term_conceptid
  ON thesaurus_term
  USING btree (conceptid);

CREATE INDEX idx_thesaurus_term_thesaurusid
  ON thesaurus_term
  USING btree (thesaurusid);

-- Adding column thesaurusid to the thesaurus_concept table
ALTER TABLE thesaurus_concept ADD COLUMN thesaurusid text;
ALTER TABLE thesaurus_concept ALTER COLUMN thesaurusid SET NOT NULL;

-- ALTER TABLE thesaurus_concept DROP CONSTRAINT fk_concept_thesaurus;
ALTER TABLE thesaurus_concept
  ADD CONSTRAINT fk_concept_thesaurus FOREIGN KEY (thesaurusid)
      REFERENCES thesaurus (identifier) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION;

-- DROP INDEX fki_concept_thesaurus;
CREATE INDEX fki_concept_thesaurus
  ON thesaurus_concept
  USING btree
  (thesaurusid COLLATE pg_catalog."default");