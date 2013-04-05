INSERT INTO admin_user_id(identifier)
    VALUES ('katell.briatte.culture.gouv.fr');

INSERT INTO admin_user_id(identifier)
    VALUES ('frederic.rolland.culture.gouv.fr');
    
    
DELETE FROM languages_iso639 where toplanguage =false;
UPDATE languages_iso639 SET principallanguage=true;

UPDATE languages_iso639 SET ref_name='Français/France' where id='fr-FR';
INSERT INTO languages_iso639(
            id, part1, ref_name, toplanguage, principallanguage)
    VALUES ('en-US', 'en', 'Anglais/USA', true, true);  
INSERT INTO languages_iso639(
            id, part1, ref_name, toplanguage, principallanguage)
    VALUES ('it-IT', 'it', 'Italien/Italie', true, true);
    
INSERT INTO languages_iso639(
            id, part1, ref_name, toplanguage, principallanguage)
    VALUES ('de-DE', 'de', 'Allemand/Allemagne', true, true);   


UPDATE thesaurus_term SET lang='en-US' where lang='eng';
UPDATE thesaurus_term SET lang='it-IT' where lang='ita';
UPDATE thesaurus_term SET lang='de-DE' where lang='deu';

UPDATE thesaurus_languages SET iso639_id='en-US' where iso639_id='eng';
UPDATE thesaurus_languages SET iso639_id='it-IT' where iso639_id='ita';
UPDATE thesaurus_languages SET iso639_id='de-DE' where iso639_id='deu';

UPDATE node_label SET lang='en-US'  WHERE lang='eng';
UPDATE node_label SET lang='it-IT'  WHERE lang='ita';
UPDATE node_label SET lang='de-DE'  WHERE lang='deu';

UPDATE concept_group_label SET lang='en-US'  WHERE lang='eng';
UPDATE concept_group_label SET lang='it-IT'  WHERE lang='ita';
UPDATE concept_group_label SET lang='de-DE'  WHERE lang='deu';

UPDATE note SET lang='en-US'  WHERE lang='eng';
UPDATE note SET lang='it-IT'  WHERE lang='ita';
UPDATE note SET lang='de-DE'  WHERE lang='deu';