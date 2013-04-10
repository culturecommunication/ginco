--Add role to hierarchical relationships
ALTER TABLE hierarchical_relationship ADD COLUMN role integer NOT NULL DEFAULT 0;

--Add parent to a group concept
ALTER TABLE concept_group ADD COLUMN parentgroupid text;

ALTER TABLE concept_group
  ADD CONSTRAINT fk_concept_group FOREIGN KEY (parentgroupid)
      REFERENCES concept_group (identifier) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION;