-- Table: thesaurus, thesaurus_type

DROP TABLE IF EXISTS thesaurus;
DROP TABLE IF EXISTS thesaurus_type;
DROP TABLE IF EXISTS thesaurus_format;
DROP TABLE IF EXISTS languages_iso639;
DROP TABLE IF EXISTS thesaurus_organization;

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

CREATE TABLE languages_iso639 (
    id character(3) NOT NULL,
    part2b character(3),
    part2t character(3),
    part1 character(2),
    scope character(1) NOT NULL,
    type character(1) NOT NULL,
    ref_name character varying(150) NOT NULL,
    toplanguage boolean NOT NULL DEFAULT FALSE,
    comment character varying(150)
);

CREATE TABLE thesaurus_organization (
  identifier integer NOT NULL,
  name text NOT NULL,
  homepage text
);