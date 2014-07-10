-- Add email to a thesaurus organisation
ALTER TABLE thesaurus_organization ADD COLUMN email text;

-- Table: user_role
CREATE TABLE user_role
(
  identifier integer NOT NULL,
  username text,
  thesaurus_id text,
  role integer,
  CONSTRAINT pk_user_role PRIMARY KEY (identifier),
  CONSTRAINT fk_user_role_thesaurus FOREIGN KEY (thesaurus_id)
      REFERENCES thesaurus (identifier) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE CASCADE
);

CREATE SEQUENCE user_role_identifier_seq
    INCREMENT 1
    MINVALUE 1
    MAXVALUE 9223372036854775807
    START 1
    CACHE 1;
ALTER SEQUENCE user_role_identifier_seq OWNED BY user_role.identifier;

CREATE INDEX user_role_username_thesaurus_id_idx ON user_role USING btree(username, thesaurus_id);
CREATE INDEX user_role_thesaurus_id_idx ON user_role USING btree(thesaurus_id);

ALTER TABLE thesaurus_organization ALTER COLUMN name DROP NOT NULL;

-- Add skos label to a concept group type
ALTER TABLE concept_group_type ADD COLUMN skoslabel text;

-- Add skos label to associative relationship roles
ALTER TABLE associative_relationship_role ADD COLUMN skoslabel text;
