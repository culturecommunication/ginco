-- ALTER TABLE thesaurus_term DROP COLUMN hidden;
ALTER TABLE thesaurus_term ADD COLUMN hidden boolean NOT NULL DEFAULT false;

--ALTER TABLE thesaurus_term
--  ADD CONSTRAINT chk_hidden_values CHECK (NOT (prefered = true AND hidden = true));

ALTER TABLE thesaurus_version_history DROP CONSTRAINT fk_thesaurus_organization_thesaurus;
ALTER TABLE thesaurus_version_history
  ADD CONSTRAINT fk_thesaurus_version_history_thesaurus FOREIGN KEY (thesaurus_identifier)
      REFERENCES thesaurus (identifier) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE CASCADE;

ALTER TABLE thesaurus_version_history DROP COLUMN currentversion;
ALTER TABLE thesaurus_version_history ADD COLUMN status text NOT NULL;

ALTER TABLE thesaurus_version_history DROP COLUMN thisversion;
ALTER TABLE thesaurus_version_history ADD COLUMN thisversion boolean NOT NULL DEFAULT false;

ALTER TABLE thesaurus_version_history DROP COLUMN status;
ALTER TABLE thesaurus_version_history ADD COLUMN status integer NOT NULL DEFAULT 0;

--create unique index chk_uniq_thisversion on thesaurus_version_history(thesaurus_identifier, thisversion) where thisversion;
