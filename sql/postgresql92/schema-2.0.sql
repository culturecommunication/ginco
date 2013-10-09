ALTER TABLE concept_group ADD COLUMN notation text;
ALTER TABLE concept_group ADD COLUMN dynamic boolean;
ALTER TABLE concept_group ADD COLUMN parentconceptid text;
ALTER TABLE concept_group
  ADD CONSTRAINT fk_group_parent_concept FOREIGN KEY (parentconceptid)
      REFERENCES  thesaurus_concept (identifier) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION;
