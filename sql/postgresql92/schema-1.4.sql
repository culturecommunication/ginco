--DROP SEQUENCE concept_group_type_code_seq;
--DROP SEQUENCE concept_group_label_identifier_seq;
--DROP TABLE concept_group_label;
--DROP TABLE concept_group_concepts;
--DROP TABLE concept_group;
--DROP TABLE concept_group_type;

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
  conceptgrouptypecode text  NOT NULL,
  CONSTRAINT pk_concept_group_identifier PRIMARY KEY (identifier),
  CONSTRAINT fk_concept_group_thesaurus_identifier FOREIGN KEY (thesaurusid)
      REFERENCES thesaurus (identifier) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE CASCADE,
  CONSTRAINT fk_concept_group_concept_group_type_code FOREIGN KEY (conceptgrouptypecode)
      REFERENCES concept_group_type (code) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
);

CREATE TABLE concept_group_concepts
(
  conceptgroupid text NOT NULL,
  conceptid text NOT NULL,
  CONSTRAINT pk_concept_group_concepts PRIMARY KEY (conceptgroupid, conceptid),
  CONSTRAINT fk_concept_group_concepts_concept_group_identifier FOREIGN KEY (conceptgroupid)
      REFERENCES concept_group (identifier) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT fk_concept_group_concepts_thesaurus_concept_identifier FOREIGN KEY (conceptid)
      REFERENCES thesaurus_concept (identifier) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
);

CREATE TABLE concept_group_label
(
  identifier integer NOT NULL,
  lexicalvalue text  NOT NULL,
  created timestamp without time zone DEFAULT now() NOT NULL,
  modified timestamp without time zone DEFAULT now() NOT NULL,
  lang text NOT NULL,
  conceptgroupid text  NOT NULL,
  CONSTRAINT pk_concept_group_label_identifier PRIMARY KEY (identifier),
  CONSTRAINT fk_concept_group_label_languages_iso639_id FOREIGN KEY (lang)
      REFERENCES languages_iso639 (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT fk_concept_group_label_concept_group_identifier FOREIGN KEY (conceptgroupid)
      REFERENCES concept_group (identifier) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE CASCADE
);

CREATE SEQUENCE concept_group_label_identifier_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;