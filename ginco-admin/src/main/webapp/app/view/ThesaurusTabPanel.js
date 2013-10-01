/**
 * Copyright or © or Copr. Ministère Français chargé de la Culture et de la
 * Communication (2013) <p/> contact.gincoculture_at_gouv.fr <p/> This software
 * is a computer program whose purpose is to provide a thesaurus management
 * solution. <p/> This software is governed by the CeCILL license under French
 * law and abiding by the rules of distribution of free software. You can use,
 * modify and/ or redistribute the software under the terms of the CeCILL
 * license as circulated by CEA, CNRS and INRIA at the following URL
 * "http://www.cecill.info". <p/> As a counterpart to the access to the source
 * code and rights to copy, modify and redistribute granted by the license,
 * users are provided only with a limited warranty and the software's author,
 * the holder of the economic rights, and the successive licensors have only
 * limited liability. <p/> In this respect, the user's attention is drawn to the
 * risks associated with loading, using, modifying and/or developing or
 * reproducing the software by the user in light of its specific status of free
 * software, that may mean that it is complicated to manipulate, and that also
 * therefore means that it is reserved for developers and experienced
 * professionals having in-depth computer knowledge. Users are therefore
 * encouraged to load and test the software's suitability as regards their
 * requirements in conditions enabling the security of their systemsand/or data
 * to be ensured and, more generally, to use and operate it in the same
 * conditions as regards security. <p/> The fact that you are presently reading
 * this means that you have had knowledge of the CeCILL license and that you
 * accept its terms.
 */

/*
 * File: app/view/ThesaurusPanel.js Thesaurus Creation Form
 * 
 */

Ext.define('GincoApp.view.ThesaurusTabPanel', {
	extend : 'Ext.panel.Panel',

	thesaurusData : null,
	layout : {
		type : 'vbox',
		align : 'stretch'
	},
	iconCls : 'icon-folder',

	alias : 'widget.thesaurusTabPanel',
	localized : true,
	closable : true,
	/* Fields with auto generated values */
	xIdentifierLabel : 'Identifier',
	xCreatedDateLabel : 'Creation date',
	xDateLabel : 'Last Modification Date',

	/* Fields prompting values */
	xTitleLabel : 'Title',
	xServiceLabel : 'Service',
	xUrlLabel : 'URL',
	xContributorLabel : 'Contributor',
	xPublisherLabel : 'Publisher',
	xPublisherValue : 'Ministère chargé de la culture',
	xRightsLabel : 'Rights',
	xDescriptionLabel : 'Description',
	xCoverageLabel : 'Coverage',
	xSubjectLabel : 'Subject',
	xTypeLabel : 'Type',
	xFormatLabel : 'Formats',
	xLanguagesLabel : 'Languages',
	xdefaultTopConceptLabel : 'TopTerm by default',
	xRelationLabel : 'Relation',
	xSourceLabel : 'Source',
	xThesaurusTitle : 'New Thesaurus',
	xNewLabel : 'New',
	xNewMenu_TermLabel : "Term",
	xNewMenu_ConceptLabel : "Concept",
	xNewMenu_GroupLabel : "Group of Concepts",
	xNewMenu_ConceptArrayLabel : "Array of concepts",
	xNewMenu_ComplexConceptLabel : "Complex Concept",
	xExport_Skos : "Export SKOS",
	xExport_Hierarchical : "Export text hierarchical",
	xExport_Alphabetic : "Export text alphabetical",
	xExport_Ginco : "Export Hadoc GINCO XML format",
	xSave : "Save",
	xDelete : "Delete",
	xVersionsTab : 'Versions',
	xJournal : 'Log history',
	xEditJournal : 'Edit history',
	xPublish : 'Publish',
	xArchive : 'Archive',
	xPolyHierarchical : 'Polyhierarchical',
	xCustomAttributeTypes : 'Custom atrribute types',
	xImportSandbox : 'Import sandbox terms',
	xImportBranch : 'Import a branch',

	initComponent : function() {
		var me = this;
		Ext.applyIf(me, {
			title : me.xThesaurusTitle,
			items : [ {
				xtype : 'tabpanel',
				itemId : 'thesaurusItemsTabPanel',
				flex : 1
			} ],
			dockedItems : [ {
				xtype : 'toolbar',
				dock : 'top',
				items : [ {
					xtype : 'button',
					disabled : true,
					requiredRoles : [ 'ADMIN' ],
					itemId : 'newBtnMenu',
					text : me.xNewLabel,
					menu : {
						xtype : 'menu',
						width : 200,
						items : [ {
							xtype : 'keymenuitem',
							text : me.xNewMenu_TermLabel,
							itemId : 'newTermBtn',
							cmdTxt : 'Ctrl+2'
						}, {
							xtype : 'keymenuitem',
							text : me.xNewMenu_ConceptLabel,
							itemId : 'newConceptBtn',
							cmdTxt : 'Ctrl+3'
						}, {
							xtype : 'keymenuitem',
							text : me.xNewMenu_ComplexConceptLabel,
							itemId : 'newComplexConceptBtn',
							cmdTxt : 'Ctrl+4'
						}, {
							xtype : 'keymenuitem',
							text : me.xNewMenu_ConceptArrayLabel,
							itemId : 'newConceptArrayBtn',
							cmdTxt : 'Ctrl+5'
						}, {
							xtype : 'keymenuitem',
							text : me.xNewMenu_GroupLabel,
							itemId : 'newConceptGroupBtn',
							cmdTxt : 'Ctrl+6'
						} ]
					}
				}, , {
					xtype : 'button',
					disabled : true,
					itemId : 'exportsBtnMenu',
					text : 'Exports',
					iconCls : 'exports-icon',
					menu : {
						xtype : 'menu',
						width : 200,
						items : [ {
							xtype : 'keymenuitem',
							text : me.xExport_Skos,
							disabled : true,
							itemId : 'exportSKOS'
						}, {
							xtype : 'keymenuitem',
							text : me.xExport_Hierarchical,
							disabled : true,
							itemId : 'exportHierarchical'
						}, {
							xtype : 'keymenuitem',
							text : me.xExport_Alphabetic,
							disabled : true,
							itemId : 'exportAlphabetical'
						}, {
							xtype : 'keymenuitem',
							text : me.xExport_Ginco,
							disabled : true,
							itemId : 'exportGinco'
						} ]
					}
				}, {
					xtype : 'button',
					disabled : true,
					itemId : 'journalBtnMenu',
					text : me.xJournal,
					iconCls : 'exports-icon',
					menu : {
						xtype : 'menu',
						width : 200,
						items : [ {
							xtype : 'keymenuitem',
							text : me.xEditJournal,
							disabled : true,
							itemId : 'editJournal'
						} ]
					}
				}, {
					xtype : 'button',
					text : me.xPublish,
					requiredRoles : [ 'ADMIN' ],
					disabled : true,
					cls : 'publish',
					itemId : 'publishThesaurus',
					iconCls : 'icon-publish'
				}, {
					xtype : 'button',
					text : me.xArchive,
					requiredRoles : [ 'ADMIN' ],
					disabled : true,
					cls : 'archive',
					itemId : 'archiveThesaurus',
					iconCls : 'icon-archive'
				}, {
					xtype : 'button',
					text : me.xImportSandbox,
					requiredRoles : [ 'ADMIN' ],
					disabled : true,
					itemId : 'importSandbox',
					iconCls : 'exports-icon'
				}, {
					xtype : 'button',
					text : me.xImportBranch,
					requiredRoles : [ 'ADMIN' ],
					disabled : true,
					itemId : 'importBranch',
					iconCls : 'exports-icon'
				} ]
			} ]
		});
		me.callParent(arguments);
	}
});
