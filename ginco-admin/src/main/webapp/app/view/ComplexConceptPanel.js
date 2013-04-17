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

/*
 * File: app/view/ThesaurusPanel.js
 * Term Creation/Edition Form
 * 
 */
Ext.Loader.setPath('Ext.ux', 'extjs/ux');
Ext.require([ 'Ext.ux.CheckColumn', 'GincoApp.view.NoteTermPanel' ]);

Ext
		.define(
				'GincoApp.view.ComplexConceptPanel',
				{
					extend : 'GincoApp.view.ThesaurusEntityPanel',
					alias : 'widget.complexconceptPanel',
					requires : ['GincoApp.controller.ComplexConceptPanelController'],
					localized : true,
					closable : true,
					layout : {
						type : 'vbox',
						align : 'stretch'
					},

					/* Fields with auto generated values */
					xIdentifierLabel : 'Identifier',
					xCreatedDateLabel : 'Creation date',
					xDateLabel : 'Last Modification Date',

					/* Fields prompting values */
					xLexicalValueLabel : 'Lexical value',
					xLanguagesLabel : 'Languages',
					xRelationLabel : 'Relation',
					xSourceLabel : 'Source',
					xTermPanelTitle : 'New Complex Concept',
					xNotesTab : 'Notes of this term',
					xStatusLabel : 'Status',
					xPreferredTermsTableTitle : 'Preferred terms',
					xIdentifierColumnLabel : "Identifier",
					xLexicalValueColumnLabel : "Lexical Value",
					xCreatedColumnLabel: "Created",
					xModifiedColumnLabel:"Modified",
					xSourceColumnLabel:"Source",
					xStatusColumnLabel:"Status",
					xLangueColumnLabel:"Language",
					xAddPreferredTerm:"Add a preferred term",
					xRemoveTerm : "Remove this term",
					xActions : "Actions",

					initComponent : function() {
						var me = this;

						Ext
								.applyIf(
										me,
										{
											title : me.xTermPanelTitle,
											items : [ {
												xtype : 'tabpanel',
												flex:1,
												items : [
														{
															xtype : 'form',
															itemId : 'termForm',
															title : me.xTermPanelTitle,
															flex : 1,
															requiredRoles : ['ADMIN'],
															autoScroll : true,
															pollForChanges : true,
															trackResetOnLoad : true,
															defaults : {
															},
															bbar : Ext.create('GincoApp.view.BottomFormToolbar'),
															dockedItems : [ {
																xtype : 'toolbar',
																dock : 'top',
																items : [
																		{
																			xtype : 'button',
																			text : 'Enregistrer',
																			requiredRoles : ['ADMIN'],
																			disabled : true,
																			formBind : true,
																			cls : 'save',
																			iconCls : 'icon-save',
																			itemId : 'saveTerm'
																		},
																		{
																			xtype : 'button',
																			text : 'Supprimer',
																			requiredRoles : ['ADMIN'],
																			disabled : true,
																			itemId : 'delete',
																			cls : 'delete',
																			iconCls : 'icon-delete'
																		}]
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
																		fieldLabel : me.xDateLabel
																	},
																	{
																		xtype : 'textfield',
																		name : 'lexicalValue',
																		fieldLabel : me.xLexicalValueLabel,
																		allowBlank : false,
																		anchor : '70%'
																	},
																	{
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
																		anchor : '70%'
																	},
																	{
																		xtype : 'hiddenfield',
																		name : 'thesaurusId'
																	},
																	{
																		xtype : 'ariacombo',
																		name : 'language',
																		itemId : 'languageCombo',
																		fieldLabel : me.xLanguagesLabel,
																		editable : false,
																		displayField : 'refname',
																		valueField : 'id',
																		forceSelection : true,
																		store : Ext
																				.create('GincoApp.store.TermLanguageStore'),
																		anchor : '70%',
																		margin : '5 0 5 0'
																	},
																	{
																		xtype : 'ariacombo',
																		name : 'status',
																		itemId : 'statusCombo',
																		fieldLabel : me.xStatusLabel,
																		editable : false,
																		multiSelect : false,
																		displayField : 'statusLabel',
																		valueField : 'status',
																		forceSelection : true,
																		store : Ext
																				.create('GincoApp.store.TermStatusStore'),
																		anchor : '70%',
																		margin : '5 0 5 0'
																	},
																	{
																		xtype : 'gridpanel',
																		title : me.xPreferredTermsTableTitle,
																		itemId : 'gridPanelPreferredTerms',
																		store : Ext.create('GincoApp.store.ThesaurusTermStore'),
																		dockedItems : [ {
																			xtype : 'toolbar',
																			dock : 'top',
																			items : [ {
																				xtype : 'button',
																				text : me.xAddPreferredTerm,
																				itemId : 'addPreferredTerm',
																				iconCls : 'icon-add-parent'
																			} ]
																		} ],
																		columns : [
																		           {dataIndex : 'identifier', text : me.xIdentifierColumnLabel},
																		           {dataIndex : 'lexicalValue', text : me.xLexicalValueColumnLabel, flex: 1},
																		           {dataIndex : 'created', text : me.xCreatedColumnLabel},
																		           {dataIndex : 'modified', text : me.xModifiedColumnLabel},
																		           {dataIndex : 'source', text : me.xSourceColumnLabel,  hidden: true},
																		           {dataIndex : 'status', text : me.xStatusColumnLabel,  hidden: true},
																		           {dataIndex : 'language', text : me.xLangueColumnLabel},
																				   {
																						xtype : 'actioncolumn',
																						itemId : 'preferredTermDeleteAction',
																						header : me.xActions,
																						items : [ {
																							iconCls : 'icon-detach',
																							tooltip : me.xRemoveTerm
																						} ]
																					}
																		           ]
																	}]
														},
														{
															title : me.xNotesTab,
															xtype : 'noteTermPanel',
															closable : false,
															disabled : true
														} ]
											} ]
										});

						me.callParent(arguments);
					}
				});