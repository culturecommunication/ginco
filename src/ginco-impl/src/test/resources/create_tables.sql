-- Table: thesaurus

DROP TABLE IF EXISTS thesaurus;

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
)