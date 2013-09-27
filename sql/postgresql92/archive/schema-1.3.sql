-- Table: hierarchical_relationship
CREATE TABLE hierarchical_relationship
(
  childconceptid text NOT NULL,
  parentconceptid text NOT NULL,
  CONSTRAINT pk_hierarchical_relationship PRIMARY KEY (childconceptid, parentconceptid)
);

-- Index: idx_parentconceptid
CREATE INDEX idx_hierarchical_relationship_parentconceptid
  ON hierarchical_relationship
  USING btree
  (parentconceptid);


-- Index: idx_childconceptid
CREATE INDEX idx_hierarchical_relationship_childconceptid
  ON hierarchical_relationship
  USING btree
  (childconceptid);

-- Foreign Key to reference the child concept
ALTER TABLE hierarchical_relationship
  ADD CONSTRAINT fk_child_hierarchical_relationship_thesaurus_concept FOREIGN KEY (childconceptid)
      REFERENCES thesaurus_concept (identifier) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE CASCADE;

-- Foreign Key to reference the parent concept
ALTER TABLE hierarchical_relationship
  ADD CONSTRAINT fk_parent_hierarchical_relationship_thesaurus_concept FOREIGN KEY (parentconceptid)
      REFERENCES thesaurus_concept (identifier) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE CASCADE;
  
-- Table: top_relationship
CREATE TABLE top_relationship
(
  childconceptid text NOT NULL,
  rootconceptid text NOT NULL,
  CONSTRAINT pk_top_relationship PRIMARY KEY (childconceptid, rootconceptid)
);

-- Index: idx_childconceptid
CREATE INDEX idx_top_relationship_childconceptid
  ON top_relationship
  USING btree
  (childconceptid);

-- Index: idx_parentconceptid
CREATE INDEX idx_top_relationship_rootconceptid
  ON top_relationship
  USING btree
  (rootconceptid);
  
-- Foreign Key to reference the child concept
ALTER TABLE top_relationship
  ADD CONSTRAINT fk_child_top_relationship_thesaurus_concept FOREIGN KEY (childconceptid)
      REFERENCES thesaurus_concept (identifier) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE CASCADE;
      
-- Foreign Key to reference the root concept
ALTER TABLE top_relationship
  ADD CONSTRAINT fk_root_top_relationship_thesaurus_concept FOREIGN KEY (rootconceptid)
      REFERENCES thesaurus_concept (identifier) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE CASCADE;

CREATE TABLE note_type
(
  code text NOT NULL,
  label text NOT NULL,
  isterm boolean NOT NULL,
  isconcept boolean NOT NULL,
  CONSTRAINT pk_note_type PRIMARY KEY (code),
  CONSTRAINT chk_not_false_values CHECK (NOT (isterm = false AND isconcept = false))
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
  CONSTRAINT pk_note_identifier PRIMARY KEY (identifier),
  CONSTRAINT fk_note_languages_iso639 FOREIGN KEY (lang)
      REFERENCES languages_iso639 (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT fk_note_notetype FOREIGN KEY (notetypecode)
      REFERENCES note_type (code) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT fk_note_thesaurus_concept FOREIGN KEY (conceptid)
      REFERENCES thesaurus_concept (identifier) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE CASCADE,
  CONSTRAINT fk_note_thesaurus_term FOREIGN KEY (termid)
      REFERENCES thesaurus_term (identifier) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE CASCADE
);

CREATE INDEX fki_note_languages_iso639
  ON note
  USING btree
  (lang);

CREATE INDEX fki_note_notetype
  ON note
  USING btree
  (notetypecode);

CREATE INDEX fki_note_thesaurus_concept
  ON note
  USING btree
  (conceptid);

CREATE INDEX fki_note_thesaurus_term
  ON note
  USING btree
  (termid);
  
ALTER TABLE note ADD COLUMN created timestamp without time zone DEFAULT now() NOT NULL;
ALTER TABLE note ADD COLUMN modified timestamp without time zone DEFAULT now() NOT NULL;

CREATE TABLE associative_relationship_role
(
  code character varying(255) NOT NULL,
  label character varying(255),
  defaultrole boolean,
  CONSTRAINT associative_relationship_role_pkey PRIMARY KEY (code)
);

CREATE TABLE associative_relationship
(
  conceptid1 character varying(255) NOT NULL,
  conceptid2 character varying(255) NOT NULL,
  "role" character varying(255),
  CONSTRAINT associative_relationship_pkey PRIMARY KEY (conceptid1, conceptid2),  
  CONSTRAINT fk2b76ee66da7e7931 FOREIGN KEY ("role")
      REFERENCES associative_relationship_role (code) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT fk2b76ee66fdebaa20 FOREIGN KEY (conceptid1)
      REFERENCES thesaurus_concept (identifier) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT fk2b76ee66fdebaa21 FOREIGN KEY (conceptid2)
      REFERENCES thesaurus_concept (identifier) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
);

CREATE INDEX fki_associative_relationship_concept1
  ON associative_relationship
  USING btree
  (conceptid1);
  
CREATE INDEX fki_associative_relationship_concept2
  ON associative_relationship
  USING btree
  (conceptid2);
  
CREATE INDEX fki_associative_relationship_role
  ON associative_relationship
  USING btree
  (role);

ALTER TABLE  associative_relationship 
    RENAME conceptId1 TO concept1;
    
ALTER TABLE  associative_relationship 
    RENAME conceptId2 TO concept2;

CREATE TABLE thesaurus_array
(
  identifier text NOT NULL,
  ordered boolean DEFAULT false NOT NULL,
  notation text,
  thesaurusid text NOT NULL,
  superordinateconceptid text,
  CONSTRAINT pk_thesaurus_array_identifier PRIMARY KEY (identifier),
  CONSTRAINT fk_thesaurus_array_thesaurus_concept FOREIGN KEY (superordinateconceptid)
      REFERENCES thesaurus_concept (identifier) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT fk_thesaurus_array_thesaurus FOREIGN KEY (thesaurusid)
      REFERENCES thesaurus (identifier) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
);

CREATE INDEX fki_thesaurus_array_thesaurus_concept
  ON thesaurus_array
  USING btree
  (superordinateconceptid);

CREATE INDEX fki_thesaurus_array_thesaurus
  ON thesaurus_array
  USING btree
  (thesaurusid);

CREATE TABLE node_label
(
  id integer NOT NULL,
  lexicalvalue text NOT NULL,
  modified  timestamp without time zone DEFAULT now() NOT NULL,
  created  timestamp without time zone DEFAULT now() NOT NULL,
  lang text,
  thesaurusarrayid text NOT NULL,
  CONSTRAINT pk_note_label_id PRIMARY KEY (id),
  CONSTRAINT fk_node_label_thesaurus_array FOREIGN KEY (thesaurusarrayid)
      REFERENCES thesaurus_array (identifier) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE CASCADE
);

CREATE SEQUENCE node_label_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

ALTER SEQUENCE node_label_id_seq OWNED BY node_label.id;

CREATE INDEX fki_node_label_thesaurus_array
  ON node_label
  USING btree
  (thesaurusarrayid);

CREATE TABLE thesaurus_array_concept
(
  thesaurusarrayid text NOT NULL,
  conceptid text NOT NULL,
  CONSTRAINT pk_thesaurus_array_concept PRIMARY KEY (thesaurusarrayid, conceptid),
  CONSTRAINT fk_thesaurus_array_concept_thesaurus_array FOREIGN KEY (thesaurusarrayid)
      REFERENCES thesaurus_array (identifier) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT fk_thesaurus_array_concept_thesaurus_concept FOREIGN KEY (conceptid)
      REFERENCES thesaurus_concept (identifier) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
);

CREATE INDEX fki_thesaurus_array_concept_thesaurus_array
  ON thesaurus_array_concept
  USING btree
  (thesaurusarrayid);
  
CREATE INDEX fki_thesaurus_array_concept_thesaurus_concept
  ON thesaurus_array_concept
  USING btree
  (conceptid);
