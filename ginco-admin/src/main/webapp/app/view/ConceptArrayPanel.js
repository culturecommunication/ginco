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

Ext
		.define(
				'GincoApp.view.ConceptArrayPanel',
				{
					extend : 'GincoApp.view.ThesaurusEntityPanel',
					alias : 'widget.conceptArrayPanel',

					localized : true,
					closable : true,
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
					xActions : 'Actions',
					xAssociationRemove : 'Detach this concept',

					initComponent : function() {
						var me = this;

						// This store is used to get only the children concepts
						// of the superordinateconcept
						me.associatedConceptStore = Ext.create(
								'GincoApp.store.SimpleConceptStore', {
									sorters : [ {
										property : 'label',
										direction : 'asc'
									} ]
								});

						Ext
								.applyIf(
										me,
										{
											title : me.xConceptArrayTabTitle,
											items : [ {
												xtype : 'form',
												title : me.xConceptArrayFormTitle,
												flex : 1,
												autoScroll : true,
												pollForChanges : true,
												trackResetOnLoad : true,
												itemId : 'conceptArrayForm',
												defaults : {
													anchor : '70%',
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
																itemId : 'saveConceptArray',
																cls : 'save',
																iconCls : 'icon-save'
															},
															{
																xtype : 'button',
																text : me.xDelete,
																disabled : true,
																itemId : 'deleteConceptArray',
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
															xtype : 'textfield',
															name : 'label',
															fieldLabel : me.xLabelLabel,
															allowBlank : false
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
															xtype : 'textfield',
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
																		itemId : 'selectParentConcept',
																		cls : 'add',
																		iconCls : 'icon-add'
																	} ]
														},
														{
															xtype : 'gridpanel',
															itemId : 'gridPanelConceptArray',
															title : me.xConceptArrayGridTitle,
															store : me.associatedConceptStore,

															dockedItems : [ {
																xtype : 'toolbar',
																dock : 'top',
																items : [ {
																	xtype : 'button',
																	text : me.xAddConceptToArray,
																	disabled : false,
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