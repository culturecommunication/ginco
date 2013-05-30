ALTER TABLE thesaurus_aud DROP COLUMN format;

CREATE TABLE thesaurus_formats_aud (
    rev integer NOT NULL,
    thesaurus_identifier text NOT NULL,
    format_identifier integer NOT NULL,
    revtype smallint
);

ALTER TABLE ONLY thesaurus_formats_aud
    ADD CONSTRAINT thesaurus_formats_aud_pkey PRIMARY KEY (rev, thesaurus_identifier, format_identifier);
  
    
