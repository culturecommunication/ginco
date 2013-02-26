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
      ON UPDATE NO ACTION ON DELETE NO ACTION;

-- Foreign Key to reference the parent concept
ALTER TABLE hierarchical_relationship
  ADD CONSTRAINT fk_parent_hierarchical_relationship_thesaurus_concept FOREIGN KEY (parentconceptid)
      REFERENCES thesaurus_concept (identifier) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION;
  
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
      ON UPDATE NO ACTION ON DELETE NO ACTION;
      
-- Foreign Key to reference the root concept
ALTER TABLE top_relationship
  ADD CONSTRAINT fk_root_top_relationship_thesaurus_concept FOREIGN KEY (rootconceptid)
      REFERENCES thesaurus_concept (identifier) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION;
      
