CREATE INDEX idx_thesaurus_term_lang_lexicalvalue
  ON thesaurus_term
  USING btree
  (lexicalvalue COLLATE pg_catalog."default", lang COLLATE pg_catalog."default");

  ALTER TABLE thesaurus_concept_aud DROP CONSTRAINT fk2cf9f474d0d1bcb5;

ALTER TABLE thesaurus_concept_aud
  ADD CONSTRAINT fk2cf9f474d0d1bcb5 FOREIGN KEY (rev)
      REFERENCES revinfo (rev) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE CASCADE;


ALTER TABLE thesaurus_term_aud DROP CONSTRAINT fkbfbbfde2d0d1bcb5;

ALTER TABLE thesaurus_term_aud
  ADD CONSTRAINT fkbfbbfde2d0d1bcb5 FOREIGN KEY (rev)
      REFERENCES revinfo (rev) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE CASCADE;

ALTER TABLE hierarchical_relationship_aud DROP CONSTRAINT fkcd658dffd0d1bcb5;

ALTER TABLE hierarchical_relationship_aud
  ADD CONSTRAINT fkcd658dffd0d1bcb5 FOREIGN KEY (rev)
      REFERENCES revinfo (rev) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE CASCADE;


ALTER TABLE associative_relationship_aud DROP CONSTRAINT fka0197937d0d1bcb5;

ALTER TABLE associative_relationship_aud
  ADD CONSTRAINT fka0197937d0d1bcb5 FOREIGN KEY (rev)
      REFERENCES revinfo (rev) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE CASCADE;

ALTER TABLE top_relationship_aud DROP CONSTRAINT fkb18c9db3d0d1bcb5;

ALTER TABLE top_relationship_aud
  ADD CONSTRAINT fkb18c9db3d0d1bcb5 FOREIGN KEY (rev)
      REFERENCES revinfo (rev) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE CASCADE;

ALTER TABLE thesaurus_aud DROP CONSTRAINT fkb34829cbd0d1bcb5;

ALTER TABLE thesaurus_aud
  ADD CONSTRAINT fkb34829cbd0d1bcb5 FOREIGN KEY (rev)
      REFERENCES revinfo (rev) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE CASCADE;

ALTER TABLE revinfoentitytypes DROP CONSTRAINT fke94d7a05c144bbed;

ALTER TABLE revinfoentitytypes
  ADD CONSTRAINT fke94d7a05c144bbed FOREIGN KEY (revision)
      REFERENCES revinfo (rev) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE CASCADE;
