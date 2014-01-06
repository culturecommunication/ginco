CREATE INDEX idx_thesaurus_term_lang_lexicalvalue
  ON thesaurus_term
  USING btree
  (lexicalvalue COLLATE pg_catalog."default", lang COLLATE pg_catalog."default");
