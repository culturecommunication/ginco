-- Table: thesaurus, thesaurus_type

DROP TABLE IF EXISTS thesaurus;
DROP TABLE IF EXISTS thesaurus_type;
DROP TABLE IF EXISTS thesaurus_format;

CREATE TABLE thesaurus
(
  identifier text NOT NULL,
  contributor text,
  coverage text,
  date text,
  description text,
  format integer,
  publisher text,
  relation text,
  rights text,
  source text,
  subject text,
  title text NOT NULL,
  type integer,
  creator integer,
  created text
);

CREATE TABLE thesaurus_type (
    identifier integer NOT NULL,
    label text NOT NULL
);

CREATE TABLE thesaurus_format (
    identifier integer NOT NULL,
    label text NOT NULL
);