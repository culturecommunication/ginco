UPDATE concept_group_type SET skoslabel = 'Thematique' WHERE label = 'Thématique';
UPDATE concept_group_type SET skoslabel = 'Facette' WHERE label = 'Facette';
UPDATE concept_group_type SET skoslabel = 'Domaine' WHERE label = 'Domaine';

UPDATE associative_relationship_role SET skoslabel = 'TermeAssocie' WHERE code = 'TA';
UPDATE associative_relationship_role SET skoslabel = 'TermeLie' WHERE code = 'TL';