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
  libelle text NOT NULL,
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
