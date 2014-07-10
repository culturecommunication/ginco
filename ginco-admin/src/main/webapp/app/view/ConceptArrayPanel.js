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
 * File: app/view/ConceptArrayPanel.js
 * Concept Array Creation/Edition Form
 *
 */

Ext.require([ 'GincoApp.view.MetaDataPanel' ]);

Ext
		.define(
				'GincoApp.view.ConceptArrayPanel',
				{
					extend : 'GincoApp.view.ThesaurusEntityPanel',
					alias : 'widget.conceptArrayPanel',

					localized : true,
					closable : true,
					trackable : true,
					layout : {
						type : 'vbox',
						align : 'stretch'
					},

					// Labels
					xConceptArrayTabTitle : 'Concept array',
					xConceptArrayFormTitle : 'Concept array',
					xSave : 'Save',
					xDelete : 'Delete',
					xIdentifierLabel : 'Identifier',
					xCreatedDateLabel : 'Creation date',
					xModifiedDateLabel : 'Modification date',
					xLabelLabel : 'Title',
					xLexicalValueLabel : 'Lexical Value',
					xLanguageLabel : 'Language',
					xConceptArrayGridTitle : 'Concepts',
					xAddConceptToArray : 'Add a concept',
					xParentConceptLabel : 'Parent Concept',
					xSelectParentConcept : 'Select a parent concept',
					xRemoveParentConcept : 'Remove parent concept',
					xActions : 'Actions',
					xAssociationRemove : 'Detach this concept',
					xOrderLabel : 'Alphabetical order',
					xConceptOrderLabel: 'Order',
					xParentArrayLabel : 'Parent array',
					xSelectParentArray : 'Select a parent array',
					xRemoveParentArray : 'Remove parent array',
					xMetadataTitle : 'Metadata',

					initComponent : function() {
						var me = this;

						Ext
								.applyIf(
										me,
										{
											title : me.xConceptArrayTabTitle,
											items : [ {
												xtype : 'form',
												requiredRoles : ['ADMIN', 'MANAGER'],
												title : me.xConceptArrayFormTitle,
												flex : 1,
												autoScroll : true,
												pollForChanges : true,
												trackResetOnLoad : true,
												itemId : 'conceptArrayForm',
												defaults : {
													anchor : '70%'
												},
												bbar : Ext.create('GincoApp.view.BottomFormToolbar'),
												dockedItems : [ {
													xtype : 'toolbar',
													dock : 'top',
													items : [
															{
																xtype : 'button',
																text : me.xSave,
																requiredRoles : ['ADMIN','MANAGER'],
																disabled : true,
																formBind : true,
																itemId : 'saveConceptArray',
																cls : 'save',
																iconCls : 'icon-save'
															},
															{
																xtype : 'button',
																text : me.xDelete,
																requiredRoles : ['ADMIN','MANAGER'],
																disabled : true,
																itemId : 'deleteConceptArray',
																cls : 'delete',
																iconCls : 'icon-delete'
															} ]
												} ],
												items : [
												        {
												        	xtype : 'metaDataPanel',
															title : me.xMetadataTitle,
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
																	}
															        ]
												        },
														{
															xtype : 'textfield',
															name : 'label',
															fieldLabel : me.xLabelLabel,
															allowBlank : false,
															padding : '5 0 0 0'
														},
														{
															xtype : 'textfield',
															name : 'thesaurusId',
															hidden : true
														},
														{
															xtype : 'combobox',
															name : 'language',
															itemId : 'conceptArrayLanguages',
															fieldLabel : me.xLanguageLabel,
															editable : false,
															displayField : 'refname',
															valueField : 'id',
															forceSelection : true,
															multiSelect : false,
															allowBlank : false,
															store : Ext
																	.create('GincoApp.store.ThesaurusLanguageStore')
														},
														{
															xtype : 'checkbox',
															name : 'order',
															fieldLabel : me.xOrderLabel
														},
														{
															xtype : 'hidden',
															name : 'parentArrayId'
														},
														{
															xtype : 'container',
															layout : 'column',
															defaults : {
																margin : '0 0 5 0',
																layout : 'anchor'
															},
															items : [
																	{
																		xtype : 'textfield',
																		name : 'parentArrayLabel',
																		fieldLabel : me.xParentArrayLabel,
																		allowBlank : true,
																		readOnly : true
																	},
																	{
																		xtype : 'button',
																		text : me.xSelectParentArray,
																		disabled : false,
																		requiredRoles : ['ADMIN','MANAGER'],
																		itemId : 'selectParentArray',
																		cls : 'add',
																		iconCls : 'icon-add'
																	},
																	{
																		xtype : 'button',
																		text : me.xRemoveParentArray,
																		disabled : true,
																		requiredRoles : ['ADMIN','MANAGER'],
																		itemId : 'removeParentArray',
																		iconCls : 'icon-delete'
																	}]
														},
														{
															xtype : 'hidden',
															name : 'superOrdinateId',
															hidden : true
														},
														{
															xtype : 'container',
															layout : 'column',
															defaults : {
																margin : '0 0 5 0',
																layout : 'anchor'
															},
															items : [
																	{
																		xtype : 'textfield',
																		name : 'superOrdinateLabel',
																		fieldLabel : me.xParentConceptLabel,
																		allowBlank : true,
																		readOnly : true
																	},
																	{
																		xtype : 'button',
																		text : me.xSelectParentConcept,
																		disabled : false,
																		requiredRoles : ['ADMIN','MANAGER'],
																		itemId : 'selectParentConcept',
																		cls : 'add',
																		iconCls : 'icon-add'
																	} ,
																	{
																		xtype : 'button',
																		text : me.xRemoveParentConcept,
																		disabled : true,
																		requiredRoles : ['ADMIN','MANAGER'],
																		itemId : 'removeParentConcept',
																		cls : 'add',
																		iconCls : 'icon-delete'
																	}]
														},
														{
															xtype : 'gridpanel',
															itemId : 'gridPanelConceptArray',
															title : me.xConceptArrayGridTitle,
															store : Ext.create('Ext.data.Store', {model: 'GincoApp.model.ArrayConceptModel'}),

															dockedItems : [ {
																xtype : 'toolbar',
																dock : 'top',
																items : [ {
																	xtype : 'button',
																	requiredRoles : ['ADMIN','MANAGER'],
																	text : me.xAddConceptToArray,
																	itemId : 'addConceptToArray',
																	cls : 'add',
																	iconCls : 'icon-add'
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
																		dataIndex : 'order',
																		text : me.xConceptOrderLabel,
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
										});

						me.callParent(arguments);
					}
				});