CREATE INDEX associative_relationship_role_skoslabel_idx ON associative_relationship_role USING btree(skoslabel);


-- Table: suggestion
CREATE TABLE suggestion
(
  identifier integer NOT NULL,
  created timestamp without time zone DEFAULT now() NOT NULL,
  creator text NOT NULL,
  recipient text NOT NULL,
  content text NOT NULL,
  term_id text,
  concept_id text,
  CONSTRAINT pk_suggestion PRIMARY KEY (identifier),
  CONSTRAINT fk_suggestion_term_id FOREIGN KEY (term_id)
      REFERENCES thesaurus_term (identifier) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE CASCADE,
   CONSTRAINT fk_suggestion_concept_id FOREIGN KEY (concept_id)
      REFERENCES thesaurus_concept (identifier) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE CASCADE
);

CREATE SEQUENCE suggestion_identifier_seq
    INCREMENT 1
    MINVALUE 1
    MAXVALUE 9223372036854775807
    START 1
    CACHE 1;
ALTER SEQUENCE suggestion_identifier_seq OWNED BY suggestion.identifier;

ALTER TABLE compound_equivalence DROP CONSTRAINT fk_split_nonpreferredterm;
ALTER TABLE compound_equivalence
    ADD CONSTRAINT fk_split_nonpreferredterm
    FOREIGN KEY(id_split_nonpreferredterm)
    REFERENCES split_nonpreferredterm (identifier)
    MATCH SIMPLE ON UPDATE NO ACTION ON DELETE CASCADE;


ALTER TABLE concept_group ALTER COLUMN isdynamic SET DEFAULT false;