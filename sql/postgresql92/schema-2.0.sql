ALTER TABLE concept_group ADD COLUMN notation text;

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
