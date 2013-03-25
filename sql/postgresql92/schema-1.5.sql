-- ALTER TABLE thesaurus_term DROP COLUMN hidden;
ALTER TABLE thesaurus_term ADD COLUMN hidden boolean;
ALTER TABLE thesaurus_term ALTER COLUMN hidden SET NOT NULL;
ALTER TABLE thesaurus_term ALTER COLUMN hidden SET DEFAULT false;

-- ALTER TABLE thesaurus_term_aud DROP COLUMN hidden;
ALTER TABLE thesaurus_term_aud ADD COLUMN hidden boolean;
ALTER TABLE thesaurus_term_aud ALTER COLUMN hidden SET NOT NULL;
ALTER TABLE thesaurus_term_aud ALTER COLUMN hidden SET DEFAULT false;