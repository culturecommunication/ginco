INSERT INTO note_type(
            code, label, isterm, isconcept)
    VALUES ('scopeNote','Note d''application', false, true);

INSERT INTO note_type(
            code, label, isterm, isconcept)
    VALUES ('historyNote','Note historique', true, true);

INSERT INTO note_type(
            code, label, isterm, isconcept)
    VALUES ('definition','Définition', true, false);

INSERT INTO note_type(
            code, label, isterm, isconcept)
    VALUES ('editorialNote','Note éditoriale', true, false);
    
INSERT INTO associative_relationship_role(
            code, label, defaultrole)
    VALUES ('TA', 'Terme associé', true);
    
    
UPDATE thesaurus_term SET role=null;
DELETE FROM thesaurus_term_role;
INSERT INTO thesaurus_term_role(
            code, label, defaultrole)
    VALUES ('AB', 'Abréviation', false);
INSERT INTO thesaurus_term_role(
            code, label, defaultrole)
    VALUES ('AV', 'Appellation vernaculaire', false);
INSERT INTO thesaurus_term_role(
            code, label, defaultrole)
    VALUES ('D', 'Dénomination', false);
INSERT INTO thesaurus_term_role(
            code, label, defaultrole)
    VALUES ('EM', 'Employer', true);
INSERT INTO thesaurus_term_role(
            code, label, defaultrole)
    VALUES ('EP', 'Employé', false);
INSERT INTO thesaurus_term_role(
            code, label, defaultrole)
    VALUES ('FD', 'Forme développée', false);
INSERT INTO thesaurus_term_role(
            code, label, defaultrole)
    VALUES ('NC', 'Nom commun', false);
INSERT INTO thesaurus_term_role(
            code, label, defaultrole)
    VALUES ('NS', 'Nom scientifique', false);
INSERT INTO thesaurus_term_role(
            code, label, defaultrole)
    VALUES ('VO', 'Variante orthographique', false);