--Add role to hierarchical relationships
ALTER TABLE hierarchical_relationship ADD COLUMN role integer;
ALTER TABLE hierarchical_relationship ALTER COLUMN role SET NOT NULL;
ALTER TABLE hierarchical_relationship ALTER COLUMN role SET DEFAULT 0;