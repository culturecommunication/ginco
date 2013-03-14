--Concept Group Types
INSERT INTO concept_group_type VALUES ('D','Domaine');
INSERT INTO concept_group_type VALUES ('T','Thématique');
INSERT INTO concept_group_type VALUES ('F','Facette');

INSERT INTO languages_iso639(
            id, part2b, part2t, part1, scope, "type", ref_name, toplanguage, 
            "comment")
    VALUES ('fr-FR', '', '', '', 'I', 'L', 'French', true, 
            '');            
            
UPDATE thesaurus_type SET label='Thésaurus' WHERE label='Thesaurus';
