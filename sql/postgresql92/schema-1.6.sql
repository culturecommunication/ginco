--Add role to hierarchical relationships
ALTER TABLE hierarchical_relationship ADD COLUMN role integer NOT NULL DEFAULT 0;