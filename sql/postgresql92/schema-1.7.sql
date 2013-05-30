ALTER TABLE thesaurus DROP COLUMN format;

CREATE TABLE thesaurus_formats (
	format_identifier integer NOT NULL,
	thesaurus_identifier text NOT NULL
);

ALTER TABLE ONLY thesaurus_formats
    ADD CONSTRAINT pk_thesaurus_formats PRIMARY KEY (format_identifier, thesaurus_identifier);

ALTER TABLE thesaurus_formats
  ADD CONSTRAINT fk_thesaurus_formats_format_identifier FOREIGN KEY (format_identifier)
      REFERENCES thesaurus_format (identifier) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE CASCADE;
    
ALTER TABLE thesaurus_formats
  ADD CONSTRAINT fk_thesaurus_formats_thesaurus_identifier FOREIGN KEY (thesaurus_identifier)
      REFERENCES thesaurus (identifier) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE CASCADE;


