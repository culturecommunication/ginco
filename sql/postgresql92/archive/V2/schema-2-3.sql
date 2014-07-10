ALTER TABLE languages_iso639
   ALTER COLUMN id TYPE character varying(5);
   
ALTER TABLE thesaurus_languages
   ALTER COLUMN iso639_id TYPE character varying(5);

ALTER TABLE concept_group_label
   ALTER COLUMN lang TYPE character varying(5);

ALTER TABLE custom_concept_attribute
   ALTER COLUMN lang TYPE character varying(5);

ALTER TABLE custom_term_attribute
   ALTER COLUMN lang TYPE character varying(5);
   
ALTER TABLE node_label
   ALTER COLUMN lang TYPE character varying(5);
   
ALTER TABLE note
   ALTER COLUMN lang TYPE character varying(5);
   
ALTER TABLE split_nonpreferredterm
   ALTER COLUMN lang TYPE character varying(5);

ALTER TABLE thesaurus_term
   ALTER COLUMN lang TYPE character varying(5);   
