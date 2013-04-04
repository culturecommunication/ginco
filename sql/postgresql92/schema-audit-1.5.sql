TRUNCATE TABLE thesaurus_term_aud,
thesaurus_aud,
thesaurus_concept_aud,
thesaurus_languages_aud,
thesaurus_thesaurusterm_aud,
thesaurus_thesaurusversionhistory_aud,
hierarchical_relationship_aud,
top_relationship_aud,
associative_relationship_aud,
revinfo,
revinfoentitytypes;

--ALTER TABLE thesaurus_term_aud DROP COLUMN hidden;
ALTER TABLE thesaurus_term_aud ADD COLUMN hidden boolean DEFAULT false;

ALTER TABLE thesaurus_aud ADD COLUMN archived boolean;
ALTER TABLE revinfo ADD COLUMN thesaurusid text; 


ALTER TABLE thesaurus_term_aud ADD COLUMN prefered_mod boolean; 
ALTER TABLE thesaurus_term_aud ADD COLUMN lexicalvalue_mod boolean;
ALTER TABLE thesaurus_term_aud ADD COLUMN created_mod boolean;
ALTER TABLE thesaurus_term_aud ADD COLUMN modified_mod boolean;
ALTER TABLE thesaurus_term_aud ADD COLUMN source_mod boolean;
ALTER TABLE thesaurus_term_aud ADD COLUMN hidden_mod boolean;
ALTER TABLE thesaurus_term_aud ADD COLUMN status_mod boolean;
ALTER TABLE thesaurus_term_aud ADD COLUMN role_mod boolean;
ALTER TABLE thesaurus_term_aud ADD COLUMN language_mod boolean;
ALTER TABLE thesaurus_term_aud ADD COLUMN thesaurus_mod boolean;
ALTER TABLE thesaurus_term_aud ADD COLUMN concept_mod boolean;

ALTER TABLE thesaurus_concept_aud ADD COLUMN created_mod boolean; 
ALTER TABLE thesaurus_concept_aud ADD COLUMN modified_mod boolean;
ALTER TABLE thesaurus_concept_aud ADD COLUMN status_mod boolean;
ALTER TABLE thesaurus_concept_aud ADD COLUMN notation_mod boolean;
ALTER TABLE thesaurus_concept_aud ADD COLUMN topconcept_mod boolean;
ALTER TABLE thesaurus_concept_aud ADD COLUMN thesaurus_mod boolean;
ALTER TABLE thesaurus_concept_aud ADD COLUMN parentconcepts_mod boolean;
ALTER TABLE thesaurus_concept_aud ADD COLUMN rootconcepts_mod boolean;
ALTER TABLE thesaurus_concept_aud ADD COLUMN associativerelationshipleft_mod boolean;
ALTER TABLE thesaurus_concept_aud ADD COLUMN associativerelationshipright_mod boolean;
ALTER TABLE thesaurus_concept_aud ADD COLUMN conceptarrays_mod boolean;
