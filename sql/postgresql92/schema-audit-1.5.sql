--ALTER TABLE thesaurus_term_aud DROP COLUMN hidden;
ALTER TABLE thesaurus_term_aud ADD COLUMN hidden boolean DEFAULT false;

ALTER TABLE revinfo ADD COLUMN thesaurusid text; 