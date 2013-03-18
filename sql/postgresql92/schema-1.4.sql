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
    
ALTER TABLE thesaurus_concept DROP CONSTRAINT fk_concept_thesaurus;

ALTER TABLE thesaurus_concept
  ADD CONSTRAINT fk_concept_thesaurus FOREIGN KEY (thesaurusid)
      REFERENCES thesaurus (identifier) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE CASCADE;

ALTER TABLE thesaurus_array DROP CONSTRAINT fk_thesaurus_array_thesaurus;

ALTER TABLE thesaurus_array
  ADD CONSTRAINT fk_thesaurus_array_thesaurus FOREIGN KEY (thesaurusid)
      REFERENCES thesaurus (identifier) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE CASCADE;

ALTER TABLE thesaurus_array_concept DROP CONSTRAINT fk_thesaurus_array_concept_thesaurus_concept;
ALTER TABLE thesaurus_array_concept DROP CONSTRAINT fk_thesaurus_array_concept_thesaurus_array;

ALTER TABLE thesaurus_array_concept
  ADD CONSTRAINT fk_thesaurus_array_concept_thesaurus_array FOREIGN KEY (thesaurusarrayid)
      REFERENCES thesaurus_array (identifier) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE CASCADE;


ALTER TABLE thesaurus_array_concept
  ADD CONSTRAINT fk_thesaurus_array_concept_thesaurus_concept FOREIGN KEY (conceptid)
      REFERENCES thesaurus_concept (identifier) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE CASCADE;

ALTER TABLE thesaurus_languages DROP CONSTRAINT fk_thesaurus_languages_thesaurus_identifier;
ALTER TABLE thesaurus_languages DROP CONSTRAINT fk_thesaurus_languages_languages_iso639_id;

ALTER TABLE thesaurus_languages
  ADD CONSTRAINT fk_thesaurus_languages_thesaurus_identifier FOREIGN KEY (thesaurus_identifier)
      REFERENCES thesaurus (identifier) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE CASCADE;

ALTER TABLE thesaurus_languages
  ADD CONSTRAINT fk_thesaurus_languages_languages_iso639_id FOREIGN KEY (iso639_id)
      REFERENCES languages_iso639 (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE CASCADE;

ALTER TABLE thesaurus_term DROP CONSTRAINT fk_term_thesaurus;
ALTER TABLE thesaurus_term DROP CONSTRAINT fk_term_thesaurus_concept;


ALTER TABLE thesaurus_term
  ADD CONSTRAINT fk_term_thesaurus FOREIGN KEY (thesaurusid)
      REFERENCES thesaurus (identifier) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE CASCADE;

ALTER TABLE thesaurus_term
  ADD CONSTRAINT fk_term_thesaurus_concept FOREIGN KEY (conceptid)
      REFERENCES thesaurus_concept (identifier) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE CASCADE;

ALTER TABLE associative_relationship DROP CONSTRAINT fk2b76ee66da7e7931;
ALTER TABLE associative_relationship DROP CONSTRAINT fk2b76ee66fdebaa20;
ALTER TABLE associative_relationship DROP CONSTRAINT fk2b76ee66fdebaa21;

ALTER TABLE associative_relationship
  ADD CONSTRAINT fk_role FOREIGN KEY (role)
      REFERENCES associative_relationship_role (code) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION;

ALTER TABLE associative_relationship
  ADD CONSTRAINT fk_concept1 FOREIGN KEY (concept1)
      REFERENCES thesaurus_concept (identifier) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE CASCADE;

ALTER TABLE associative_relationship
  ADD CONSTRAINT fk_concept2 FOREIGN KEY (concept2)
      REFERENCES thesaurus_concept (identifier) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE CASCADE;
      
ALTER TABLE languages_iso639 ALTER id TYPE character(5);
ALTER TABLE thesaurus_languages ALTER iso639_id TYPE character(5);

-- Index: idx_childconceptid
CREATE INDEX idx_languages_iso639_part1
  ON languages_iso639
  USING btree
  (part1);

