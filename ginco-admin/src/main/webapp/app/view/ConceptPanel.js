/*
 * File: app/view/ConceptPanel.js
 * Concept Creation/Edition Form + Notes
 * 
 */

Ext.Loader.setPath('Ext.ux', 'extjs/ux');
Ext.require([ 'Ext.ux.CheckColumn', 'GincoApp.view.NoteConceptPanel' ]);

Ext
		.define(
				'GincoApp.view.ConceptPanel',
				{
					extend : 'Ext.panel.Panel',

					thesaurusData : '',
					conceptId : '',
					initPreferedTermBeforeLoad : '',

					alias : 'widget.conceptPanel',
					localized : true,
					closable : true,
					layout : {
						type : 'vbox',
						align : 'stretch'
					},

					// Labels
					xIdentifierLabel : 'Identifier',
					xCreatedDateLabel : 'Creation date',
					xModificationDateLabel : 'Modification date',
					xTopTermConceptLabel : 'Is a TopTerm Concept',
					xLexicalValueLabel : 'Lexical value',
					xLanguagesLabel : 'Languages',
					xRoleColumnLabel : 'Role',
					xPreferedColumnLabel : 'Prefered',
					xConceptPanelTitle : 'New Concept',
					xTermListGridTitle : 'Terms list',
					xSave : 'Save',
					xDelete : 'Delete',
					xAddTerm : 'Add a term',
					xPreferedTerm : 'Prefered Term',
					xNonPreferedTerm : 'Non Prefered Term',
					xCreateTerm : 'Create Term',
					xExistingTerm : 'Select Existing Term',
					xDetach : 'Detach from Concept',
					xAddParent : 'Add parent Concept',
					xNotesTab : 'Notes of this concept',
					xActions : 'Actions',
					xAddRelationship : 'Add associative relationship',
					xAssociatedConceptsListGridTitle : 'Associated terms',
					xRootConcepts : 'Root Concepts',
					xParentConcepts : 'Parent Concepts',
					xRemoveParent : 'Remove connection to parent Concept',
					xAssociationRemove : 'Remove association',
					xChildrenConcepts : 'Children Concepts',
					xConceptStatusLabel : 'Concept status',

					initComponent : function() {
						var cellEditing = Ext.create(
								'Ext.grid.plugin.CellEditing', {
									clicksToEdit : 1
								});
						var me = this;
						me.conceptTermStore = Ext
								.create('GincoApp.store.ThesaurusTermStore');

						me.associatedConceptStore = Ext
								.create('GincoApp.store.SimpleConceptStore');
						me.rootConceptStore = Ext
								.create('GincoApp.store.SimpleConceptStore');
						me.parentConceptStore = Ext
								.create('GincoApp.store.SimpleConceptStore');
						me.childrenConceptStore = Ext
								.create('GincoApp.store.SimpleConceptStore');
						me.childrenConceptStore.getProxy().url = 'services/ui/thesaurusconceptservice/getSimpleChildrenConcepts';

						me.termRoleStore = Ext
								.create('GincoApp.store.TermRoleStore');

						Ext
								.applyIf(
										me,
										{
											title : me.xConceptPanelTitle,
											items : [ {
												xtype : 'tabpanel',
												flex : 1,
												items : [
														{
															xtype : 'panel',
															title : me.xConceptPanelTitle,
															layout : {
																type : 'vbox',
																align : 'stretch'
															},
															items : [ {
																xtype : 'form',
																itemId : 'conceptForm',
																flex : 1,
																autoScroll : true,
																pollForChanges : true,
																trackResetOnLoad : true,
																defaults : {
																	afterLabelTextTpl : new Ext.XTemplate(
																			'<tpl if="allowBlank === false"><span style="color:red;">*</span></tpl>',
																			{
																				disableFormats : true
																			})
																},
																dockedItems : [ {
																	xtype : 'toolbar',
																	dock : 'top',
																	items : [
																			{
																				xtype : 'button',
																				text : me.xSave,
																				disabled : true,
																				formBind : true,
																				cls : 'save',
																				iconCls : 'icon-save',
																				itemId : 'saveConcept'
																			},
																			{
																				xtype : 'button',
																				text : me.xDelete,
																				disabled : true,
																				itemId : 'deleteConcept',
																				cls : 'delete',
																				iconCls : 'icon-delete'
																			} ]
																} ],
																items : [
																		{
																			xtype : 'displayfield',
																			name : 'identifier',
																			fieldLabel : me.xIdentifierLabel
																		},
																		{
																			xtype : 'displayfield',
																			name : 'created',
																			fieldLabel : me.xCreatedDateLabel
																		},
																		{
																			xtype : 'displayfield',
																			name : 'modified',
																			fieldLabel : me.xModificationDateLabel
																		},
																		{
																			xtype : 'checkbox',
																			name : 'topconcept',
																			fieldLabel : me.xTopTermConceptLabel
																		},
												                        {
												                        	xtype: 'combobox',
												                        	name:'status',
												                        	itemId: 'conceptStatusCombo',
												                        	fieldLabel: me.xConceptStatusLabel,
												                        	editable : false,
												                        	multiSelect : false,
												                        	displayField : 'statusLabel',
												                        	valueField : 'status',
												                        	forceSelection : true,
												                        	store :  Ext.create('GincoApp.store.ConceptStatusStore'),
												                        	anchor: '70%',
												                        	margin : '5 0 5 0'
												                        },
																		{
																			xtype : 'gridpanel',
																			itemId : 'gridPanelTerms',
																			title : me.xTermListGridTitle,
																			store : me.conceptTermStore,
																			plugins : [ cellEditing ],
																			dockedItems : [ {
																				xtype : 'toolbar',
																				dock : 'top',
																				items : [ {
																					xtype : 'button',
																					text : me.xAddTerm,
																					menu : {
																						xtype : 'menu',
																						items : [
																								{
																									xtype : 'menuitem',
																									text : me.xPreferedTerm,
																									menu : {
																										xtype : 'menu',
																										items : [
																												{
																													xtype : 'menuitem',
																													itemId : 'newTermFromConceptPrefBtn',
																													text : me.xCreateTerm
																												},
																												{
																													xtype : 'menuitem',
																													itemId : 'selectTermFromConceptPrefBtn',
																													text : me.xExistingTerm
																												} ]
																									}
																								},
																								{
																									xtype : 'menuitem',
																									text : me.xNonPreferedTerm,
																									menu : {
																										xtype : 'menu',
																										items : [
																												{
																													xtype : 'menuitem',
																													itemId : 'newTermFromConceptNonPrefBtn',
																													text : me.xCreateTerm
																												},
																												{
																													xtype : 'menuitem',
																													itemId : 'selectTermFromConceptNonPrefBtn',
																													text : me.xExistingTerm
																												} ]
																									}
																								} ]
																					}
																				} ]
																			} ],

																			columns : [
																					{
																						dataIndex : 'identifier',
																						text : me.xIdentifierLabel
																					},
																					{
																						dataIndex : 'lexicalValue',
																						text : me.xLexicalValueLabel,
																						flex : 1
																					},
																					{
																						dataIndex : 'language',
																						text : me.xLanguagesLabel
																					},
																					{
																						xtype : 'checkcolumn',
																						dataIndex : 'prefered',
																						header : me.xPreferedColumnLabel,
																						stopSelection : false
																					},
																					{
																						dataIndex : 'role',
																						header : me.xRoleColumnLabel,
																						stopSelection : false,
																						editor : new Ext.form.field.ComboBox(
																								{
																									typeAhead : true,
																									triggerAction : 'all',
																									selectOnTab : true,
																									store : me.termRoleStore,
																									lazyRender : true,
																									listClass : 'x-combo-list-small',
																									displayField : 'label',
																									valueField : 'code'
																								})
																					},
																					{
																						dataIndex : 'created',
																						text : me.xCreatedDateLabel
																					},

																					{
																						xtype : 'actioncolumn',
																						itemId : 'conceptActionColumn',
																						header : me.xActions,
																						items : [ {
																							icon : 'images/detach.png',
																							tooltip : me.xDetach,
																							handler : function(
																									view,
																									rowIndex,
																									colIndex,
																									item,
																									e,
																									record,
																									row) {

																							}
																						} ]
																					} ]
																		},
																		{
																			xtype : 'gridpanel',
																			title : me.xParentConcepts,
																			store : me.parentConceptStore,
																			itemId : 'gridPanelParentConcepts',

																			dockedItems : [ {
																				xtype : 'toolbar',
																				dock : 'top',
																				items : [ {
																					xtype : 'button',
																					text : me.xAddParent,
																					disabled : false,
																					itemId : 'addParent',
																					cls : 'addParent',
																					iconCls : 'icon-add-parent'
																				} ]
																			} ],

																			columns : [
																					{
																						dataIndex : 'identifier',
																						text : me.xIdentifierLabel
																					},
																					{
																						dataIndex : 'label',
																						text : me.xLexicalValueLabel,
																						flex : 1
																					},
																					{
																						xtype : 'actioncolumn',
																						itemId : 'parentConceptActionColumn',
																						header : me.xActions,
																						items : [ {
																							icon : 'images/detach.png',
																							tooltip : me.xRemoveParent,
																							handler : function(
																									view,
																									rowIndex,
																									colIndex,
																									item,
																									e,
																									record,
																									row) {

																							}
																						} ]
																					} ]
																		},
																		{
																			xtype : 'gridpanel',
																			title : me.xChildrenConcepts,
																			store : me.childrenConceptStore,
																			itemId : 'gridPanelChildrenConcepts',

																			columns : [
																					{
																						dataIndex : 'identifier',
																						text : me.xIdentifierLabel
																					},
																					{
																						dataIndex : 'label',
																						text : me.xLexicalValueLabel,
																						flex : 1
																					} ]
																		},
																		{
																			xtype : 'gridpanel',
																			title : me.xRootConcepts,
																			store : me.rootConceptStore,
																			itemId : 'gridPanelRootConcepts',

																			columns : [
																					{
																						dataIndex : 'identifier',
																						text : me.xIdentifierLabel
																					},
																					{
																						dataIndex : 'label',
																						text : me.xLexicalValueLabel,
																						flex : 1
																					} ]
																		},
																		{
																			xtype : 'gridpanel',
																			title : me.xAssociatedConceptsListGridTitle,
																			store : me.associatedConceptStore,
																			itemId : 'gridPanelAssociatedConcepts',

																			dockedItems : [ {
																				xtype : 'toolbar',
																				dock : 'top',
																				items : [ {
																					xtype : 'button',
																					text : me.xAddRelationship,
																					disabled : false,
																					itemId : 'addAssociativeRelationship',
																					cls : 'addAssociativeRelationship',
																					iconCls : 'icon-add-associative-relationship'
																				} ]
																			} ],

																			columns : [
																					{
																						dataIndex : 'identifier',
																						text : me.xIdentifierLabel
																					},
																					{
																						dataIndex : 'label',
																						text : me.xLexicalValueLabel,
																						flex : 1
																					},
																					{
																						xtype : 'actioncolumn',
																						itemId : 'associatedConceptActionColumn',
																						header : me.xActions,
																						items : [ {
																							icon : 'images/detach.png',
																							tooltip : me.xAssociationRemove,
																							handler : function(
																									view,
																									rowIndex,
																									colIndex,
																									item,
																									e,
																									record,
																									row) {

																							}
																						} ]
																					} ]
																		} ]
															} ]
														},
														{
															title : me.xNotesTab,
															xtype : 'noteConceptPanel',
															closable : false,
															disabled : true
														} ]
											} ]
										});

						me.callParent(arguments);
					}
				});