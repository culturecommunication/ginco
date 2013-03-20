--Concept Group Types
INSERT INTO concept_group_type VALUES ('D','Domaine');
INSERT INTO concept_group_type VALUES ('T','Thématique');
INSERT INTO concept_group_type VALUES ('F','Facette');

INSERT INTO languages_iso639(
            id, part2b, part2t, part1, scope, "type", ref_name, toplanguage, 
            "comment")
    VALUES ('fr-FR', '', '', 'fr', 'I', 'L', 'French', true, 
            '');            
            
UPDATE thesaurus_type SET label='Thésaurus' WHERE label='Thesaurus';

UPDATE thesaurus_term SET lang='fr-FR'  WHERE lang='fra';
UPDATE thesaurus_languages SET iso639_id='fr-FR'  WHERE iso639_id='fra';
UPDATE node_label SET lang='fr-FR'  WHERE lang='fra';
UPDATE note SET lang='fr-FR'  WHERE lang='fra';
DELETE FROM languages_iso639 WHERE id='fra';

INSERT INTO note_type(
            code, label, isterm, isconcept)
    VALUES ('example', 'Exemple', false, true);