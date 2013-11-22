GINCO
=====

[English version see below] (#what-is-ginco-)


GINCO est une application multiutilisateurs dédiée à la gestion de référentiels terminologiques. Elle met en oeuvre les principes définis dans la norme ISO 25964-1:2011 Information et documentation -- Thésaurus et interopérabilité avec d'autres vocabulaires -- Partie 1: Thésaurus pour la recherche documentaire.

# Fonctionnalités
Les principales fonctionnalités d'ores et déjà développées permettent :
* la conception et la gestion de multiples listes d'autorités, taxonomies, thésaurus ;
* la gestion des termes : statut, langue, identifiant unique de type URI, notes typées associées aux termes, affectation des termes à des concepts, équivalences entre termes ... ;
* la gestion des concepts : statut, identifiant unique de type URI, notes typées associées aux concepts, relations hiérarchiques et associatives entre concepts ... ;
* la gestion des tableaux de concepts (relais virtuels, facettes) et leur exploitation dans les éditions ;
* la gestion des groupes de concepts (microthésaurus, thématique, domaine ...) ; 
* les éditions alphabétiques et hiérarchiques ;
* les imports et les exports de thésaurus au format SKOS/RDF ; 
* les alignements entre vocabulaires ou vers des ressources externes ;
* des fonctions collaboratives simples ;
* la consultation par des web services.

# Méthode de développement

L'application est développée selon la méthodologie agile SCRUM. Le « backlog » présente la liste priorisée des fonctionnalités développées ou prévues. Il est mis à la disposition de la communauté, qui peut nous faire part de ses suggestions et propositions.

# Perspectives

GINCO est mis à la disposition des utilisateurs et des développeurs. Pour faciliter la découverte et la prise en main des fonctionnalités, une machine virtuelle (VM) a été proposée.
La V1 du projet est totalement opérationnelle et peut être déployée et utilisée pour gérer au quotidien des vocabulaires et faire des exports simples.
Le version 2 en cours de développement s'appuie sur la démarche de repprochement entre ISO 25964 et SKOS pour proposer des exports SKOS enrichis (statut des concepts, groupes de concepts, comcepts complexes ...).

Le "[backlog] (https://www.pivotaltracker.com/s/projects/926794)" du projet suggère à la communauté des évolutions possibles de l'application.



What is GINCO ?
---------------

GINCO is a free software developped by the Ministry of Culture and Communication (France) and is dedicated to the management of vocabularies. It implements the principles defined in the ISO standard 25964-1:2011 Information and documentation -- Thesauri and interoperability with other vocabularies -- Part 1 : Thesauri for information retrieval.

License
-------

GINCO is released under the terms of the CeCiLL v2 license.

GINCO features
--------------
The main features already developed allow : 
- the design and the management of multiple lists of authorities, taxonomies, thesauruses ;
- the management of terms: status, language, unique identifier of type URI, notes of different types associated with the terms, assigment of terms to concepts, equivalence relationships between terms;
- the management of concepts: status, unique identifier of type URI, notes of different types associated with concepts, hierarchical ans associative relationships between concepts ;
- the management of thesaurus arrays (virtual relays, facets) and their exploitation in hierarchical editions ;
- the management of groups of concepts (microthesaurus, theme, domain);
- the alphabetical and hierarchical editions ;
- the import and export of thesaurus in SKOS / RDF format ;
- the consultation through web services.

The first version of the software is under development according to the SCRUM agile methodology. The "[backlog] (https://www.pivotaltracker.com/s/projects/926794)" presents the prioritized list of developed or planned features. It is made available to the community, which can provide us with suggestions and proposals.

The developments envisaged in version 2 of the application focus on :
- collaborative features ;
- alignments between vocabularies and to external ressources ;
- the implementation of a reference terminology, in order to provide users with unified access to all vocabularies (terminologies server) 

Installation
------------

Read [doc/INSTALL.md] (doc/INSTALL.md)

Demo
----
Read [doc/VM_INSTALL.md] (doc/VM_INSTALL.md)
