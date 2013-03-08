/*
 * File: app/view/ConceptArrayPanel.js
 * Concept Array Creation/Edition Form
 * 
 */

Ext
		.define(
				'GincoApp.view.ConceptArrayPanel',
				{
					extend : 'Ext.panel.Panel',
					thesaurusData : '',

					conceptArray : null,
					associatedConceptStore : null,
					alias : 'widget.conceptArrayPanel',

					localized : true,
					closable : true,
					layout : {
						type : 'vbox',
						align : 'stretch'
					},

					//Labels
					xConceptArrayTabTitle : 'Concept array',
					xConceptArrayFormTitle : 'Concept array',
					xSave : 'Save',
					xIdentifierLabel : 'Identifier',
					//xCreatedDateLabel : 'Creation date',
					//xModifiedDateLabel : 'Modification date',
					xLabelLabel : 'Title',
					xLexicalValueLabel : 'Lexical Value',
					xLanguageLabel : 'Language',
					xConceptArrayGridTitle : 'Concepts',
					xAddConceptToArray : 'Add a concept',
					xParentConceptLabel : 'Parent Concept',
					xSelectParentConcept : 'Select a parent concept',
					
					initComponent : function() {
						var me = this;
						
						//This store is used to get only the children concepts of the superordinateconcept
						me.conceptChildrenArrayStore = Ext
                        .create('GincoApp.store.SimpleConceptStore');
						me.conceptChildrenArrayStore.getProxy().url = 'services/ui/thesaurusconceptservice/getSimpleChildrenConcepts';

						Ext
								.applyIf(
										me,
										{
											title : me.xConceptArrayTabTitle,
											items : [ {
												xtype : 'form',
												//TODO title with domain
												title : me.xConceptArrayFormTitle,
												flex : 1,
												autoScroll : true,
												pollForChanges : true,
												trackResetOnLoad : true,
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
																itemId: 'saveConceptArray',
																cls : 'save',
																iconCls : 'icon-save'
															}]
												} ],
												items : [
														{
															xtype : 'displayfield',
															name : 'identifier',
															fieldLabel : me.xIdentifierLabel
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
															hidden: true
														},
														{
															xtype : 'combobox',
															name : 'language',
															fieldLabel : me.xLanguageLabel,
															editable : false,
															displayField : 'refname',
															valueField : 'id',
															forceSelection : true,
															multiSelect : false,
															allowBlank : false,
															store : 'ThesaurusLanguageStore'
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
																	name : 'superOrdinateConcept_label',
																	fieldLabel : me.xParentConceptLabel,
																	allowBlank : false,
																	readOnly:true
																},
																{
																	xtype : 'button',
				                                                    text : me.xSelectParentConcept,
				                                                    disabled : false,
				                                                    itemId : 'selectParentConcept',
				                                                    cls : 'add',
				                                                    iconCls : 'icon-add'
																}]
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
																		dataIndex : 'lang',
																		text : me.xLanguageLabel,
																		flex : 1
																	}															
                                                            ]
														}]
											} ]
										});

						me.callParent(arguments);
					}
				});