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
)

-- Table: thesaurus -- adding defaulttopconcept
alter table thesaurus add defaulttopconcept boolean NOT NULL DEFAULT FALSE