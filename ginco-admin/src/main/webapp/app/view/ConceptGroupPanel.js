/*
 * File: app/view/ConceptGroupPanel.js
 */

Ext
		.define(
				'GincoApp.view.ConceptGroupPanel',
				{
					extend : 'Ext.panel.Panel',
					alias : 'widget.conceptGroupPanel',

					localized : true,
					closable : true,
					layout : {
						type : 'vbox',
						align : 'stretch'
					},

					thesaurusData : '',
					conceptGroupId : '',

					// Labels
					xConceptGroupTitle : 'Concept Group',
					xSave : 'Save',
					xDelete : 'Delete',
					xIdentifierLabel : 'Identifier',
					xCreatedLabel : 'Created',
					xModifiedLabel : 'Modified',
					xLabelLabel : 'Group Title',
					xTypeLabel : 'Type',
					xLanguageLabel : 'Language',
					xConceptGroupFormTitle : 'Concept group',
					xConceptsGrid : 'Concepts',
					xActions : 'Actions',
					xLexicalValueLabel : 'Lexical value',
					xAssociationRemove : 'Detach this concept',
					xAddConceptToGroupArray : 'Add a concept',

					initComponent : function() {
						var me = this;

					// This store is used to store the concepts linked to
					// the group
					me.associatedConceptToGroupStore = Ext.create(
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

											title : me.xConceptGroupTitle,
											items : [ {
												xtype : 'form',
												title : me.xConceptGroupFormTitle,
												flex : 1,
												autoScroll : true,
												pollForChanges : true,
												trackResetOnLoad : true,
												itemId : 'conceptGroupForm',
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
													items : [ {
														xtype : 'button',
														text : me.xSave,
														disabled : true,
														formBind : true,
														itemId : 'saveConceptGroup',
														cls : 'save',
														iconCls : 'icon-save'
													},
                                                    {
                                                        xtype : 'button',
                                                        text : me.xDelete,
                                                        disabled : true,
                                                        cls : 'delete',
                                                        itemId : 'deleteConceptGroupBtn',
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
															fieldLabel : me.xCreatedLabel
														},
														{
															xtype : 'displayfield',
															name : 'modified',
															fieldLabel : me.xModifiedLabel
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
															name : 'type',
															fieldLabel : me.xTypeLabel,
															displayField : 'label',
															valueField : 'code',
															editable : false,
															forceSelection : true,
															multiSelect : false,
															allowBlank : false,
														    store : 'ConceptGroupTypeStore'
														},
														{
															xtype : 'combobox',
															name : 'language',
															itemId : 'conceptGroupLanguages',
															fieldLabel : me.xLanguageLabel,
															editable : false,
															displayField : 'refname',
															valueField : 'id',
															forceSelection : true,
															multiSelect : false,
															allowBlank : false,
															store : Ext.create('GincoApp.store.ThesaurusLanguageStore')
														},
														{
															xtype : 'gridpanel',
															itemId : 'gridConceptGroupPanelConcepts',
															title : me.xConceptsGrid,
															store : me.associatedConceptToGroupStore,
															
															dockedItems : [ {
																xtype : 'toolbar',
																dock : 'top',
																items : [ {
																	xtype : 'button',
																	text : me.xAddConceptToGroupArray,
																	disabled : false,
																	itemId : 'addConceptToGroupArray',
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
																		itemId : 'conceptToGroupActionColumn',
																		header : me.xActions,
																		items : [ {
																			icon : 'images/detach.png',
																			tooltip : me.xAssociationRemove
																		} ]
																	} ]
														} ]
											} ]
										});

						me.callParent(arguments);
					}

				});