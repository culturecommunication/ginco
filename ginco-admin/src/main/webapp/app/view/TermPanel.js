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
				'GincoApp.view.TermPanel',
				{
					extend : 'GincoApp.view.ThesaurusEntityPanel',
					alias : 'widget.termPanel',
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
					xTermPanelTitle : 'New Term',
					xNotesTab : 'Notes of this term',
					xStatusLabel : 'Status',

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
																			disabled : true,
																			formBind : true,
																			cls : 'save',
																			iconCls : 'icon-save',
																			itemId : 'saveTerm'
																		},
																		{
																			xtype : 'button',
																			text : 'Supprimer',
																			disabled : true,
																			itemId : 'delete',
																			cls : 'delete',
																			iconCls : 'icon-delete'
																		},
																		{
																			xtype : 'button',
																			text : 'Créer un concept depuis ce terme',
																			disabled : true,
																			itemId : 'createconcept',
																			iconCls : 'icon-add'
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
																		name : 'prefered'
																	},
																	{
																		xtype : 'hiddenfield',
																		name : 'role'
																	},
																	{
																		xtype : 'hiddenfield',
																		name : 'conceptId'
																	},
																	{
																		xtype : 'hiddenfield',
																		name : 'thesaurusId'
																	},
																	{
																		xtype : 'combobox',
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
																		xtype : 'combobox',
																		name : 'status',
																		itemId : 'statusCombo',
																		fieldLabel : me.xStatusLabel,
																		editable : false,
																		multiSelect : false,
																		readOnly : true,
																		displayField : 'statusLabel',
																		valueField : 'status',
																		forceSelection : true,
																		store : Ext
																				.create('GincoApp.store.TermStatusStore'),
																		anchor : '70%',
																		margin : '5 0 5 0'
																	} ]
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