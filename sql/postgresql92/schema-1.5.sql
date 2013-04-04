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

-- Table: admin_user_id
CREATE TABLE admin_user_id
(
  identifier text NOT NULL,
  CONSTRAINT pk_admin_user_id PRIMARY KEY (identifier)
);

CREATE TABLE thesaurus_ark
(
  identifier text NOT NULL,
  created timestamp without time zone,
  entity text,
  CONSTRAINT thesaurus_ark_pkey PRIMARY KEY (identifier)
);

CREATE FUNCTION int_to_text(INT4) RETURNS TEXT AS 'SELECT textin(int4out($1));' LANGUAGE SQL STRICT IMMUTABLE;
CREATE CAST (INT4 AS TEXT) WITH FUNCTION int_to_text(INT4) AS IMPLICIT;

ALTER TABLE thesaurus_version_history ADD COLUMN userid text;

ALTER TABLE thesaurus ADD COLUMN archived boolean DEFAULT FALSE;

ALTER TABLE thesaurus DROP COLUMN archived;
ALTER TABLE thesaurus ADD COLUMN archived boolean DEFAULT FALSE;

ALTER TABLE thesaurus ADD COLUMN ispolyhierarchical boolean DEFAULT FALSE;
ALTER TABLE thesaurus_aud ADD COLUMN ispolyhierarchical boolean DEFAULT FALSE;
