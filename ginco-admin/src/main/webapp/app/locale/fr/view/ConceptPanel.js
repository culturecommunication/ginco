/**
 * Copyright or © or Copr. Ministère Français chargé de la Culture
 * et de la Communication (2013)
 * <p/>
 * contact.gincoculture_at_gouv.fr
 * <p/>
 * This software is a computer program whose purpose is to provide a thesaurus
 * management solution.
 * <p/>
 * This software is governed by the CeCILL license under French law and
 * abiding by the rules of distribution of free software. You can use,
 * modify and/ or redistribute the software under the terms of the CeCILL
 * license as circulated by CEA, CNRS and INRIA at the following URL
 * "http://www.cecill.info".
 * <p/>
 * As a counterpart to the access to the source code and rights to copy,
 * modify and redistribute granted by the license, users are provided only
 * with a limited warranty and the software's author, the holder of the
 * economic rights, and the successive licensors have only limited liability.
 * <p/>
 * In this respect, the user's attention is drawn to the risks associated
 * with loading, using, modifying and/or developing or reproducing the
 * software by the user in light of its specific status of free software,
 * that may mean that it is complicated to manipulate, and that also
 * therefore means that it is reserved for developers and experienced
 * professionals having in-depth computer knowledge. Users are therefore
 * encouraged to load and test the software's suitability as regards their
 * requirements in conditions enabling the security of their systemsand/or
 * data to be ensured and, more generally, to use and operate it in the
 * same conditions as regards security.
 * <p/>
 * The fact that you are presently reading this means that you have had
 * knowledge of the CeCILL license and that you accept its terms.
 */

Ext.define('GincoApp.locale.fr.view.ConceptPanel',
    {
	xIdentifierLabel : 'Identifiant',
	xCreatedDateLabel : 'Date de création',
	xModificationDateLabel : 'Date de modification',
	xTopTermConceptLabel: 'Le concept est TT',
	xLexicalValueLabel : 'Valeur lexicale',
	xLanguagesLabel : 'Langue',
	xPreferedColumnLabel : 'Terme préférentiel',
	xConceptPanelTitle : 'Nouveau concept',
	xConceptTabPanelTitle: 'Fiche concept',
	xTermListGridTitle : 'Liste des termes <span class="mandatory-field"><abbr title="obligatoire">*</abbr></span> (au moins un terme préférentiel)',
	xAssociatedConceptsListGridTitle : 'Concepts Associés (<abbr title="Terme associé">TA</abbr>)',
	xSave: 'Enregistrer',
	xDelete: 'Supprimer',
	xAddTerm: 'Ajouter un terme',
	xPreferedTerm: 'Terme préférentiel',
	xNonPreferedTerm: 'Terme non préférentiel',
	xCreateTerm: 'Créer un nouveau terme',
	xExistingTerm: 'Sélectionner un terme existant',
    xDetach: 'Détacher du concept',
    xNotesTab: 'Notes du concept',
    xAddParent: 'Ajouter un concept parent',
    xAddChildMenu : 'Ajouter un concept enfant',
	xAddChild : 'Créer un nouveau concept',
	xAddExistingChild : 'Selectionner un concept existant',
    xActions: 'Actions',
    xAddRelationship: 'Ajouter une relation associative',
    xRootConcepts: 'Concepts de tête',
    xParentConcepts: 'Concepts Parents (<abbr title="Terme générique">TG</abbr>)',
    xRemoveParent: 'Supprimer la relation hiérarchique avec le concept générique',
    xRemoveChild : 'Supprimer la relation hiérarchique avec le concept spécifique',
    xAssociationRemove: 'Supprimer l\'association',
    xChildrenConcepts: 'Concepts Enfants (<abbr title="Termes spécifiques">TS</abbr>)',
    xRoleColumnLabel: 'Rôle',
    xConceptStatusLabel : 'Statut du concept',
    xHiddenTermColumnLabel : 'Terme caché',
    xConceptHierarchicalRoleLabels : ['TG-TS','TGG-TSG','TGI-TSI','TGP-TSP'],
    xNotationLabel : 'Notation',
    xExportBranch : 'Exporter cette branche',
    xMetadataTitle : 'Métadonnées',
    xAlignments : 'Alignements',
    xAddAlignment: 'Ajouter un alignement',
	xAlignmentType : 'Type',
	xAlignmentAndRelationHeader : 'Relation',
	xAlignmentConcepts: 'Concepts',
	xAlignmentRemove: 'Supprimer l\'alignment',
	xAlignmentAndRelation: 'ET',
	xAlignmentOrRelation: 'OU',
	xAlignmentEdit: 'Modifier l\'alignement',
	xAlignmentGoBtn : 'Aller à',
	xSuggestionsTab : 'Suggestions'
});