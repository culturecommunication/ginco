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

Ext.require([ 'GincoApp.view.ThesaurusVersionPanel',
              'GincoApp.view.CustomAttributeTypesPanel',
              'GincoApp.view.ThesaurusStatisticsTabPanel',
              'GincoApp.view.MetaDataPanel']);
Ext.define('GincoApp.view.ThesaurusPanel', {
	extend : 'GincoApp.view.ThesaurusEntityPanel',

	thesaurusData : null,

	alias : 'widget.thesaurusPanel',
	localized : true,
	closable : true,
	trackable : true,
	layout : {
		type : 'vbox',
		align : 'stretch'
	},
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
	xThesaurusTitle : 'Thesaurus data',
	xThesaurusTabTitle: 'Metadata',
	xNewLabel : 'New',	
	xNewMenu_GroupLabel : "Group of Concepts",
	xNewMenu_ConceptArrayLabel : "Array of concepts",
	xNewMenu_ComplexConceptLabel: "Complex Concept",
	xExport_Skos : "Export SKOS",
	xExport_Hierarchical : "Export text hierarchical",
	xExport_Alphabetic : "Export text alphabetical",
	xExport_Ginco : "Export Hadoc GINCO XML format",
	xSave : "Save",
	xDelete : "Delete",
	xVersionsTab : 'Versions',
	xJournal: 'Log history',
	xEditJournal: 'Edit history',
    xPublish: 'Publish',
    xArchive: 'Archive',
    xPolyHierarchical: 'Polyhierarchical',
    xCustomAttributeTypes: 'Custom atrribute types',
    xImportSandbox: 'Import sandbox terms',
    xImportBranch: 'Import a branch',
    xMetadataTitle : 'Metadata',
    xStatisticsTabTile : 'Statistics',
    xMetadataTooltip : 'Click here to expand/collapse metadatas of this thesaurus',

	initComponent : function() {
		var me = this;

						Ext
								.applyIf(
										me,
										{
											title : me.xThesaurusTitle,

											items : [ {
												xtype : 'tabpanel',
												itemId: 'thesaurusTabPanel',
												tabPosition: 'bottom',
												flex : 1,
											items : [ {
												xtype : 'form',
												requiredRoles : ['ADMIN'],
												title : me.xThesaurusTabTitle,
												flex : 1,
												autoScroll : true,
												pollForChanges : true,
												trackResetOnLoad : true,
												defaults : {
													anchor : '70%'
												},
												bbar : Ext.create('GincoApp.view.BottomFormToolbar'),
												dockedItems : [ {
													xtype : 'toolbar',
													dock : 'top',
													items : [{
														xtype : 'button',
														text : me.xSave,
														disabled : true,
														formBind : true,
														requiredRoles : ['ADMIN'],
														cls : 'save',
														itemId : 'saveThesaurus',
														iconCls : 'icon-save'
												},{
							                            xtype : 'button',
							                            text : me.xDelete,
							                            requiredRoles : ['ADMIN'],
							                            disabled : true,
							                            cls : 'delete',
							                            itemId : 'deleteThesaurus',
							                            iconCls : 'icon-delete'
							                    },{
                                                        xtype : 'button',
                                                        text : me.xPublish,
                                                        requiredRoles : ['ADMIN'],
                                                        disabled : true,
                                                        cls : 'publish',
                                                        itemId : 'publishThesaurus',
                                                        iconCls : 'icon-publish'
                                            },{
                                                        xtype : 'button',
                                                        text : me.xArchive,
                                                        requiredRoles : ['ADMIN'],
                                                        disabled : true,
                                                        cls : 'archive',
                                                        itemId : 'archiveThesaurus',
                                                        iconCls : 'icon-archive'
                                            } ]
									} ],
										items : [ {
                                            xtype : 'hiddenfield',
                                            name : 'archived'
                                        },{
                                        	xtype : 'metaDataPanel',
											title : me.xMetadataTitle,
											collapseTooltip : me.xMetadataTooltip,
											items : [
													{
														xtype : 'displayfield',
														name : 'id',
														fieldLabel : me.xIdentifierLabel
													},{
														xtype : 'displayfield',
														name : 'created',
														fieldLabel : me.xCreatedDateLabel
													},{
														xtype : 'displayfield',
														name : 'date',
														fieldLabel : me.xDateLabel
													}
											        ]
                                        },{
											xtype : 'textfield',
											name : 'title',
											fieldLabel : me.xTitleLabel,
											allowBlank : false,
											padding : '5 0 0 0'
										},{
											xtype : 'container',
											layout : 'column',
											defaults : {
												margin : '0 50 5 0',
												layout : 'anchor'
											},
											items : [{
												xtype : 'textfield',
												name : 'creatorName',
												columnWidth : 0.50,
												fieldLabel : me.xServiceLabel
											},{
												xtype : 'textfield',
												name : 'creatorHomepage',
												columnWidth : 0.50,
												margin : '0 0 5 0',
												fieldLabel : me.xUrlLabel,
												labelWidth : 50,
												vtype : 'url'
											} ]
										},{
												xtype : 'textareafield',
												name : 'contributor',
												fieldLabel : me.xContributorLabel,
												grow : true
										},{
												xtype : 'textfield',
												name : 'publisher',
												fieldLabel : me.xPublisherLabel,
												value : me.xPublisherValue
										},{
												xtype : 'textareafield',
												name : 'rights',
												fieldLabel : me.xRightsLabel,
												allowBlank : false,
												grow : true
										},{
												xtype : 'textareafield',
												name : 'description',
												fieldLabel : me.xDescriptionLabel,
												grow : true
										},{
												xtype : 'textareafield',
												name : 'coverage',
												fieldLabel : me.xCoverageLabel,
												grow : true
										},{
												xtype : 'textareafield',
												name : 'subject',
												fieldLabel : me.xSubjectLabel,
												grow : true
										},{
												xtype : 'ariacombo',
												name : 'type',
												queryMode : 'local',
												fieldLabel : me.xTypeLabel,
												editable : false,
												displayField : 'label',
												valueField : 'identifier',
												forceSelection : true,
												multiSelect : false,
												store : 'ThesaurusTypeStore'
										},{
												xtype : 'ariacombo',
												name : 'formats',
												fieldLabel : me.xFormatLabel,
												editable : false,
												displayField : 'label',
												valueField : 'identifier',
												forceSelection : true,
												multiSelect : true,
												store : 'ThesaurusFormatStore'
										},{
												xtype : 'ariacombo',
												name : 'languages',
												fieldLabel : me.xLanguagesLabel,
												editable : false,
												displayField : 'refname',
												valueField : 'id',
												forceSelection : true,
												multiSelect : true,
												allowBlank : false,
												store : 'ThesaurusLanguageStore',
												itemId : 'thesauruslang'
										},{
												xtype : 'checkbox',
												name : 'defaultTopConcept',
												fieldLabel : me.xdefaultTopConceptLabel,
												displayField : 'defaultTopConcept'
										},{
                                            xtype : 'checkbox',
                                            name : 'polyHierarchical',
                                            fieldLabel : me.xPolyHierarchical,
                                            displayField : 'polyHierarchical',
                                            cls : 'poly'
                                        },{
												xtype : 'htmleditor',
												name : 'relation',
												fieldLabel : me.xRelationLabel,
												enableAlignments : false,
												enableColors : false,
												enableFont : false,
												enableFontSize : false,
												enableFormat : false,
												enableLists : false,
												enableSourceEdit : false,
												iframeAttrTpl : 'title="'+me.xRelationLabel+'"',
												inputAttrTpl : 'title="'+me.xRelationLabel+'"'
										},{
												xtype : 'htmleditor',
												name : 'source',
												fieldLabel : me.xSourceLabel,
												enableAlignments : false,
												enableColors : false,
												enableFont : false,
												enableFontSize : false,
												enableFormat : false,
												enableLists : false,
												enableSourceEdit : false,
												iframeAttrTpl : 'title="'+me.xSourceLabel+'"',
												inputAttrTpl : 'title="'+me.xSourceLabel+'"'
											} ]
										}, {
												title : me.xVersionsTab,
												itemId : 'versionTab',
												xtype : 'thesaurusVersionPanel',
												closable : false,
												disabled :  true
											},

                                                {
                                                    title : me.xCustomAttributeTypes,
                                                    itemId : 'customAttributeTypesTab',
                                                    xtype : 'customAttributeTypesPanel',
                                                    closable : false,
                                                    disabled :  false
                                                },
                                                {
                                                    itemId : 'statisticsTab',
                                                    xtype : 'thesaurusStatisticsTabPanel',
                                                    closable : false,
                                                    disabled :  false
                                                }]
										}]
									});

		me.callParent(arguments);
	}
});
