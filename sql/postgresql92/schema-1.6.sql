--Add role to hierarchical relationships
ALTER TABLE hierarchical_relationship ADD COLUMN role integer NOT NULL DEFAULT 0;

--Add parent to a group concept
ALTER TABLE concept_group ADD COLUMN parentgroupid text;

ALTER TABLE concept_group
  ADD CONSTRAINT fk_concept_group FOREIGN KEY (parentgroupid)
      REFERENCES concept_group (identifier) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION;
      

ALTER TABLE thesaurus_array_concept ADD COLUMN arrayorder integer NOT NULL DEFAULT 0;

CREATE TABLE split_nonpreferredterm
(
  identifier text NOT NULL,
  lexicalvalue text NOT NULL,
  created timestamp without time zone NOT NULL DEFAULT now(),
  modified timestamp without time zone NOT NULL DEFAULT now(),
  source text,
  status integer,
  thesaurusid text NOT NULL,
  lang character(5) NOT NULL,
  CONSTRAINT pk_snpt_identifier PRIMARY KEY (identifier),
  CONSTRAINT fk_snpt_languages_iso639 FOREIGN KEY (lang)
      REFERENCES languages_iso639 (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT fk_snpt_thesaurus FOREIGN KEY (thesaurusid)
      REFERENCES thesaurus (identifier) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE CASCADE
);

CREATE INDEX idx_split_nonpreferredterm_thesaurusid
  ON split_nonpreferredterm
  USING btree
  (thesaurusid);

CREATE TABLE compound_equivalence
(
  id_split_nonpreferredterm text NOT NULL,
  id_preferredterm text NOT NULL,
  CONSTRAINT compound_equivalence_pk PRIMARY KEY (id_split_nonpreferredterm, id_preferredterm),
  CONSTRAINT fk_preferredterm FOREIGN KEY (id_preferredterm)
      REFERENCES thesaurus_term (identifier) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE CASCADE,
  CONSTRAINT fk_split_nonpreferredterm FOREIGN KEY (id_split_nonpreferredterm)
      REFERENCES split_nonpreferredterm (identifier) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
);

ALTER TABLE concept_group DROP CONSTRAINT fk_concept_group;

ALTER TABLE concept_group
  ADD CONSTRAINT fk_concept_group FOREIGN KEY (parentgroupid)
      REFERENCES concept_group (identifier) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE SET NULL;


CREATE TABLE custom_concept_attribute_type
(
  identifier integer NOT NULL,
  code text,
  thesaurusid text,
  value text,
  CONSTRAINT pk_custom_concept_attribute_type PRIMARY KEY (identifier),
  CONSTRAINT fk_thesaurus_id FOREIGN KEY (thesaurusid)
  REFERENCES thesaurus (identifier) MATCH SIMPLE
  ON UPDATE NO ACTION ON DELETE NO ACTION
);

-- Index: fki_custom_concept

-- DROP INDEX fki_custom_concept;

CREATE INDEX fki_custom_concept
ON custom_concept_attribute_type
USING btree
(thesaurusid);

CREATE TABLE custom_term_attribute_type
(
  identifier integer NOT NULL,
  code text,
  thesaurusid text,
  value text,
  CONSTRAINT pk_custom_term_attribute_type PRIMARY KEY (identifier),
  CONSTRAINT fk_thesaurus_id FOREIGN KEY (thesaurusid)
  REFERENCES thesaurus (identifier) MATCH SIMPLE
  ON UPDATE NO ACTION ON DELETE NO ACTION
);

-- Index: fki_custom_term

-- DROP INDEX fki_custom_term;

CREATE INDEX fki_custom_term
ON custom_term_attribute_type
USING btree
(thesaurusid);


CREATE SEQUENCE custom_term_attribute_type_identifier_seq
INCREMENT 1
MINVALUE 1
MAXVALUE 9223372036854775807
START 1
CACHE 1;

CREATE SEQUENCE custom_concept_attribute_type_identifier_seq
INCREMENT 1
MINVALUE 1
MAXVALUE 9223372036854775807
START 1
CACHE 1;

CREATE TABLE custom_term_attribute
(
  identifier text NOT NULL,
  termid text NOT NULL,
  typeid integer NOT NULL,
  lang text NOT NULL,
  lexicalvalue text NOT NULL,
  CONSTRAINT pk_custom_term_attribute PRIMARY KEY (identifier),
  CONSTRAINT fk_custom_term_attribute_lang FOREIGN KEY (lang)
  REFERENCES languages_iso639 (id) MATCH SIMPLE
  ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT fk_custom_term_attribute_termid FOREIGN KEY (termid)
  REFERENCES thesaurus_term (identifier) MATCH SIMPLE
  ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT fk_custom_term_attribute_typeid FOREIGN KEY (typeid)
  REFERENCES custom_term_attribute_type (identifier) MATCH SIMPLE
  ON UPDATE NO ACTION ON DELETE NO ACTION
);

-- Index: fki_lang

-- DROP INDEX fki_lang;

CREATE INDEX fki_custom_term_attribute_lang
ON custom_term_attribute
USING btree
(lang);

-- Index: fki_termid

-- DROP INDEX fki_termid;

CREATE INDEX fki_custom_term_attribute_termid
ON custom_term_attribute
USING btree
(termid);

-- Index: fki_typeid

-- DROP INDEX fki_typeid;

CREATE INDEX fki_custom_term_attribute_typeid
ON custom_term_attribute
USING btree
(typeid);

CREATE TABLE custom_concept_attribute
(
  identifier text NOT NULL,
  conceptid text NOT NULL,
  typeid integer NOT NULL,
  lang text NOT NULL,
  lexicalvalue text NOT NULL,
  CONSTRAINT pk_custom_concept_attribute PRIMARY KEY (identifier),
  CONSTRAINT fk_custom_concept_attribute_lang FOREIGN KEY (lang)
  REFERENCES languages_iso639 (id) MATCH SIMPLE
  ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT fk_custom_concept_attribute_conceptid FOREIGN KEY (conceptid)
  REFERENCES thesaurus_concept (identifier) MATCH SIMPLE
  ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT fk_custom_concept_attribute_typeid FOREIGN KEY (typeid)
  REFERENCES custom_concept_attribute_type (identifier) MATCH SIMPLE
  ON UPDATE NO ACTION ON DELETE NO ACTION
);

-- Index: fki_lang

-- DROP INDEX fki_lang;

CREATE INDEX fki_custom_concept_attribute_lang
ON custom_concept_attribute
USING btree
(lang);

-- Index: fki_conceptid

-- DROP INDEX fki_conceptid;

CREATE INDEX fki_custom_concept_attribute_conceptid
ON custom_concept_attribute
USING btree
(conceptid);

-- Index: fki_typeid

-- DROP INDEX fki_typeid;

CREATE INDEX fki_custom_concept_attribute_typeid
ON custom_concept_attribute
USING btree
(typeid);

--Add parent to a array concept
ALTER TABLE thesaurus_array ADD COLUMN parentarrayid text;

ALTER TABLE thesaurus_array
  ADD CONSTRAINT fk_concept_array FOREIGN KEY (parentarrayid)
      REFERENCES thesaurus_array (identifier) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE SET NULL;
      
ALTER TABLE custom_term_attribute DROP CONSTRAINT fk_custom_term_attribute_termid;

ALTER TABLE custom_term_attribute
  ADD CONSTRAINT fk_custom_term_attribute_termid FOREIGN KEY (termid)
      REFERENCES thesaurus_term (identifier) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE CASCADE;
      
ALTER TABLE custom_concept_attribute DROP CONSTRAINT fk_custom_concept_attribute_conceptid;

ALTER TABLE custom_concept_attribute
  ADD CONSTRAINT fk_custom_concept_attribute_conceptid FOREIGN KEY (conceptid)
      REFERENCES thesaurus_concept (identifier) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE CASCADE;
