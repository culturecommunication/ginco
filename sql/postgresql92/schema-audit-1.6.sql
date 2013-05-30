ALTER TABLE hierarchical_relationship_aud ADD COLUMN role integer;


CREATE TABLE thesaurus_array_concept_aud (
    rev integer NOT NULL,
    arrayorder integer,
    thesaurusarrayid character varying(255) NOT NULL,
    conceptId character varying(255) NOT NULL,
    revtype smallint
);