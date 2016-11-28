-- Add notation and dynamic groups to a concept group
ALTER TABLE concept_group ADD COLUMN notation text;
ALTER TABLE concept_group ADD COLUMN isdynamic boolean;
ALTER TABLE concept_group ADD COLUMN parentconceptid text;
ALTER TABLE concept_group
  ADD CONSTRAINT fk_group_parent_concept FOREIGN KEY (parentconceptid)
      REFERENCES  thesaurus_concept (identifier) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE SET NULL;

-- Add parameter "exportable" to a custom concept attribute types
ALTER TABLE custom_concept_attribute_type ADD COLUMN exportable boolean;

-- Table: alignment
CREATE TABLE alignment
(
  identifier text NOT NULL,
  created timestamp without time zone NOT NULL DEFAULT now(),
  modified timestamp without time zone NOT NULL DEFAULT now(),
  author text,
  source_concept_id text,
  alignment_type integer,
  external_target_thesaurus_id text,
  internal_target_thesaurus_id text,
  and_relation boolean NOT NULL,
  CONSTRAINT pk_alignment PRIMARY KEY (identifier),
  CONSTRAINT fk_alignment_sourceconcept FOREIGN KEY (source_concept_id)
      REFERENCES thesaurus_concept (identifier) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE CASCADE,
  CONSTRAINT fk_alignment_thesaurus FOREIGN KEY (internal_target_thesaurus_id)
      REFERENCES thesaurus (identifier) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE CASCADE
);

-- Table: alignment_concept
CREATE TABLE alignment_concept
(
  identifier integer NOT NULL,
  external_target_concept_id text,
  internal_target_concept_id text,
  alignment_id text,
  CONSTRAINT pk_alignment_concept PRIMARY KEY (identifier),
  CONSTRAINT fk_alignment_concept FOREIGN KEY (internal_target_concept_id)
      REFERENCES thesaurus_concept (identifier) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE CASCADE,
  CONSTRAINT fk_alignment FOREIGN KEY (alignment_id)
      REFERENCES alignment (identifier) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE CASCADE
);

-- Table: alignment_type
CREATE TABLE alignment_type
(
  identifier integer NOT NULL,
  label text NOT NULL,
  isocode text NOT NULL,
  default_type boolean NOT NULL,
  multi_concept boolean NOT NULL,
  is_resource boolean NOT NULL,
  CONSTRAINT pk_alignment_type PRIMARY KEY (identifier)
);

CREATE SEQUENCE alignment_type_identifier_seq
    INCREMENT 1
    MINVALUE 1
    MAXVALUE 9223372036854775807
    START 1
    CACHE 1;
ALTER SEQUENCE alignment_type_identifier_seq OWNED BY alignment_type.identifier;


CREATE SEQUENCE alignment_concept_identifier_seq
    INCREMENT 1
    MINVALUE 1
    MAXVALUE 9223372036854775807
    START 1
    CACHE 1;
ALTER SEQUENCE alignment_concept_identifier_seq OWNED BY alignment_concept.identifier;

-- Table: external_thesaurus
CREATE TABLE external_thesaurus
(
  identifier text NOT NULL,
  external_id text NOT NULL,
  thesaurus_type text NOT NULL,
  CONSTRAINT pk_external_thesaurus PRIMARY KEY (identifier)
);

-- ALTER TABLE alignment DROP CONSTRAINT fk_alignment_type;
ALTER TABLE alignment
  ADD CONSTRAINT fk_alignment_type FOREIGN KEY (alignment_type)
      REFERENCES alignment_type (identifier) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION;

-- ALTER TABLE alignment DROP CONSTRAINT fk_alignment_external_thesaurus;
ALTER TABLE alignment
  ADD CONSTRAINT fk_alignment_external_thesaurus FOREIGN KEY (external_target_thesaurus_id)
      REFERENCES external_thesaurus (identifier) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION;

-- Indexes on table alignment
CREATE INDEX idx_alignment_sourceconceptid
  ON alignment
  USING btree (source_concept_id);

-- Table: external_thesaurus_type
CREATE TABLE external_thesaurus_type
(
  identifier integer NOT NULL,
  label text NOT NULL,
  CONSTRAINT pk_external_thesaurus_type PRIMARY KEY (identifier)
);

CREATE SEQUENCE external_thesaurus_type_identifier_seq
    INCREMENT 1
    MINVALUE 1
    MAXVALUE 9223372036854775807
    START 1
    CACHE 1;
ALTER SEQUENCE external_thesaurus_type_identifier_seq OWNED BY external_thesaurus_type.identifier;


CREATE SEQUENCE external_thesaurus_identifier_seq
 INCREMENT 1
    MINVALUE 1
    MAXVALUE 9223372036854775807
    START 1
    CACHE 1;
ALTER SEQUENCE external_thesaurus_identifier_seq OWNED BY external_thesaurus.identifier;

-- Indexes on table external_thesaurus
CREATE INDEX idx_thesaurus_externalid
  ON external_thesaurus
  USING btree
  (external_id COLLATE pg_catalog."default" );
  
INSERT INTO alignment_type (identifier, label, isocode, default_type, multi_concept, is_resource) VALUES (1, 'Equivalence exacte', '=EQ', false, true, false);
INSERT INTO alignment_type (identifier, label, isocode, default_type, multi_concept, is_resource) VALUES (2, 'Equivalence inexacte', '~EQ', false, true, false);
INSERT INTO alignment_type (identifier, label, isocode, default_type, multi_concept, is_resource) VALUES (3, 'Alignement générique', 'BM', false, false, false);
INSERT INTO alignment_type (identifier, label, isocode, default_type, multi_concept, is_resource) VALUES (4, 'Alignement spécifique', 'NM', false, false, false);
INSERT INTO alignment_type (identifier, label, isocode, default_type, multi_concept, is_resource) VALUES (5, 'Alignement générique générique', 'BMG', false, false, false);
INSERT INTO alignment_type (identifier, label, isocode, default_type, multi_concept, is_resource) VALUES (6, 'Alignement spécifique générique', 'NMG', false, false, false);
INSERT INTO alignment_type (identifier, label, isocode, default_type, multi_concept, is_resource) VALUES (7, 'Alignement générique partitif', 'BMP', false, false, false);
INSERT INTO alignment_type (identifier, label, isocode, default_type, multi_concept, is_resource) VALUES (8, 'Alignement spécifique partitif', 'NMP', false, false, false);
INSERT INTO alignment_type (identifier, label, isocode, default_type, multi_concept, is_resource) VALUES (9, 'Alignement générique instance', 'BMI', false, false, false);
INSERT INTO alignment_type (identifier, label, isocode, default_type, multi_concept, is_resource) VALUES (10, 'Alignement spécifique instance', 'NMI', false, false, false);
INSERT INTO alignment_type (identifier, label, isocode, default_type, multi_concept, is_resource) VALUES (11, 'Alignement associatif', 'RM', false, false, false);
INSERT INTO alignment_type (identifier, label, isocode, default_type, multi_concept, is_resource) VALUES (12, 'Image', 'IMG', false, false, true);
INSERT INTO alignment_type (identifier, label, isocode, default_type, multi_concept, is_resource) VALUES (13, 'Ressource', 'RES', false, false, true);
 

INSERT INTO  external_thesaurus_type VALUES (1, 'Autorités');
INSERT INTO  external_thesaurus_type VALUES (2, 'Classification');
INSERT INTO  external_thesaurus_type VALUES (3, 'Ontologie');
INSERT INTO  external_thesaurus_type VALUES (4, 'Taxonomie');
INSERT INTO  external_thesaurus_type VALUES (5, 'Terminologie');
INSERT INTO  external_thesaurus_type VALUES (6, 'Thésaurus');
INSERT INTO  external_thesaurus_type VALUES (7, 'Vedettes-matière');
INSERT INTO  external_thesaurus_type VALUES (8, 'Autre');

-- Add email to a thesaurus organisation
ALTER TABLE thesaurus_organization ADD COLUMN email text;

-- Table: user_role
CREATE TABLE user_role
(
  identifier integer NOT NULL,
  username text,
  thesaurus_id text,
  role integer,
  CONSTRAINT pk_user_role PRIMARY KEY (identifier),
  CONSTRAINT fk_user_role_thesaurus FOREIGN KEY (thesaurus_id)
      REFERENCES thesaurus (identifier) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE CASCADE
);

CREATE SEQUENCE user_role_identifier_seq
    INCREMENT 1
    MINVALUE 1
    MAXVALUE 9223372036854775807
    START 1
    CACHE 1;
ALTER SEQUENCE user_role_identifier_seq OWNED BY user_role.identifier;

CREATE INDEX user_role_username_thesaurus_id_idx ON user_role USING btree(username, thesaurus_id);
CREATE INDEX user_role_thesaurus_id_idx ON user_role USING btree(thesaurus_id);

ALTER TABLE thesaurus_organization ALTER COLUMN name DROP NOT NULL;

-- Add skos label to a concept group type
ALTER TABLE concept_group_type ADD COLUMN skoslabel text;

-- Add skos label to associative relationship roles
ALTER TABLE associative_relationship_role ADD COLUMN skoslabel text;

UPDATE concept_group_type SET skoslabel = 'Thematique' WHERE label = 'Thématique';
UPDATE concept_group_type SET skoslabel = 'Facette' WHERE label = 'Facette';
UPDATE concept_group_type SET skoslabel = 'Domaine' WHERE label = 'Domaine';

UPDATE associative_relationship_role SET skoslabel = 'TermeAssocie' WHERE code = 'TA';
UPDATE associative_relationship_role SET skoslabel = 'TermeLie' WHERE code = 'TL';

CREATE INDEX associative_relationship_role_skoslabel_idx ON associative_relationship_role USING btree(skoslabel);


-- Table: suggestion
CREATE TABLE suggestion
(
  identifier integer NOT NULL,
  created timestamp without time zone DEFAULT now() NOT NULL,
  creator text NOT NULL,
  recipient text NOT NULL,
  content text NOT NULL,
  term_id text,
  concept_id text,
  CONSTRAINT pk_suggestion PRIMARY KEY (identifier),
  CONSTRAINT fk_suggestion_term_id FOREIGN KEY (term_id)
      REFERENCES thesaurus_term (identifier) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE CASCADE,
   CONSTRAINT fk_suggestion_concept_id FOREIGN KEY (concept_id)
      REFERENCES thesaurus_concept (identifier) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE CASCADE
);

CREATE SEQUENCE suggestion_identifier_seq
    INCREMENT 1
    MINVALUE 1
    MAXVALUE 9223372036854775807
    START 1
    CACHE 1;
ALTER SEQUENCE suggestion_identifier_seq OWNED BY suggestion.identifier;

ALTER TABLE compound_equivalence DROP CONSTRAINT fk_split_nonpreferredterm;
ALTER TABLE compound_equivalence
    ADD CONSTRAINT fk_split_nonpreferredterm
    FOREIGN KEY(id_split_nonpreferredterm)
    REFERENCES split_nonpreferredterm (identifier)
    MATCH SIMPLE ON UPDATE NO ACTION ON DELETE CASCADE;


ALTER TABLE concept_group ALTER COLUMN isdynamic SET DEFAULT false;

ALTER TABLE languages_iso639
   ALTER COLUMN id TYPE character varying(5);
   
ALTER TABLE thesaurus_languages
   ALTER COLUMN iso639_id TYPE character varying(5);

ALTER TABLE concept_group_label
   ALTER COLUMN lang TYPE character varying(5);

ALTER TABLE custom_concept_attribute
   ALTER COLUMN lang TYPE character varying(5);

ALTER TABLE custom_term_attribute
   ALTER COLUMN lang TYPE character varying(5);
   
ALTER TABLE node_label
   ALTER COLUMN lang TYPE character varying(5);
   
ALTER TABLE note
   ALTER COLUMN lang TYPE character varying(5);
   
ALTER TABLE split_nonpreferredterm
   ALTER COLUMN lang TYPE character varying(5);

ALTER TABLE thesaurus_term
   ALTER COLUMN lang TYPE character varying(5); 
   
CREATE TABLE alignment_resource (
    identifier integer NOT NULL,
    external_target_resource_id text,
    alignment_id text
);

CREATE SEQUENCE alignment_resource_identifier_seq
START WITH 1
INCREMENT BY 1
NO MINVALUE
NO MAXVALUE
CACHE 1;

ALTER SEQUENCE alignment_resource_identifier_seq OWNED BY alignment_resource.identifier;

SELECT pg_catalog.setval('alignment_resource_identifier_seq', 1, false);

ALTER TABLE ONLY alignment_resource
ADD CONSTRAINT pk_alignment_resource PRIMARY KEY (identifier);

ALTER TABLE ONLY alignment_resource
ADD CONSTRAINT fk_alignment FOREIGN KEY (alignment_id) REFERENCES alignment(identifier) ON DELETE CASCADE;
