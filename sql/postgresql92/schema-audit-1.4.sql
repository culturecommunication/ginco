ALTER TABLE thesaurus_aud ALTER identifier TYPE text;
ALTER TABLE thesaurus_aud ALTER contributor TYPE text;
ALTER TABLE thesaurus_aud ALTER coverage TYPE text;
ALTER TABLE thesaurus_aud ALTER description TYPE text;
ALTER TABLE thesaurus_aud ALTER publisher TYPE text;
ALTER TABLE thesaurus_aud ALTER relation TYPE text;
ALTER TABLE thesaurus_aud ALTER rights TYPE text;
ALTER TABLE thesaurus_aud ALTER source TYPE text;
ALTER TABLE thesaurus_aud ALTER subject TYPE text;
ALTER TABLE thesaurus_aud ALTER title TYPE text;

ALTER TABLE thesaurus_languages_aud ALTER thesaurus_identifier TYPE text;

ALTER TABLE thesaurus_term_aud ALTER identifier TYPE text;
ALTER TABLE thesaurus_term_aud ALTER lexicalvalue TYPE text;
ALTER TABLE thesaurus_term_aud ALTER source TYPE text;
ALTER TABLE thesaurus_term_aud ALTER conceptid TYPE text;
ALTER TABLE thesaurus_term_aud ALTER thesaurusid TYPE text;

ALTER TABLE thesaurus_thesaurusterm_aud ALTER thesaurusid TYPE text;
ALTER TABLE thesaurus_thesaurusterm_aud ALTER identifier TYPE text;

ALTER TABLE thesaurus_thesaurusversionhistory_aud ALTER identifier TYPE text;

ALTER TABLE hierarchical_relationship_aud ALTER childconceptid TYPE text;
ALTER TABLE hierarchical_relationship_aud ALTER parentconceptid TYPE text;

ALTER TABLE thesaurus_concept_aud ALTER identifier TYPE text;
ALTER TABLE thesaurus_concept_aud ALTER notation TYPE text;
ALTER TABLE thesaurus_concept_aud ALTER thesaurusid TYPE text;

ALTER TABLE top_relationship_aud ALTER childconceptid TYPE text;
ALTER TABLE top_relationship_aud ALTER rootconceptid TYPE text;

ALTER TABLE associative_relationship_aud ALTER concept1 TYPE text;
ALTER TABLE associative_relationship_aud ALTER concept2 TYPE text;
ALTER TABLE associative_relationship_aud ALTER "role" TYPE text;