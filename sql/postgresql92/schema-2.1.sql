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
