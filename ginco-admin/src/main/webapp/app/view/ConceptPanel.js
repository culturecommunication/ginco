/*
 * File: app/view/ThesaurusPanel.js
 * Term Creation/Edition Form
 * 
 */
Ext
		.define(
				'GincoApp.view.ConceptPanel',
				{
					extend : 'Ext.panel.Panel',

					thesaurusData : '',

					alias : 'widget.conceptPanel',
					localized : true,
					closable : true,
					layout : {
						type : 'vbox',
						align : 'stretch'
					},

					/*Fields with auto generated values*/
					xIdentifierLabel : 'Identifier',
					xCreatedDateLabel : 'Creation date',

					/*Fields prompting values*/
					xLexicalValueLabel : 'Lexical value',
					xLanguagesLabel : 'Languages',
					xRoleColumnLabel : 'Role',
					xPreferedColumnLabel : 'Prefered',
					xConceptPanelTitle : 'New Concept',

					initComponent : function() {
						var me = this;

						Ext
								.applyIf(
										me,
										{
											title : me.xConceptPanelTitle,
											items : [ {
												xtype : 'form',
												title : me.xConceptPanelTitle,
												flex : 1,
												autoScroll : true,
												pollForChanges : true,
												trackResetOnLoad : true,
												defaults : {
													anchor : '100%',
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
														text : 'Enregistrer',
														disabled : true,
														formBind : true,
														cls : 'save',
														iconCls : 'icon-save'
													}, {
														xtype : 'button',
														text : 'Supprimer',
														disabled : true,
														itemId : 'delete',
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
															fieldLabel : me.xDateLabel
														},
														{
															xtype : 'gridpanel',
															title : 'Liste des termes',

															dockedItems : [ {
																xtype : 'toolbar',
																dock : 'top',
																items : [ {
																	xtype : 'button',
																	text : 'Ajouter un terme',
																	menu : {
																		xtype : 'menu',
																		items : [
																				{
																					xtype : 'menuitem',
																					text : 'Terme préférentiel ',
																					menu : {
																						xtype : 'menu',
																						items : [
																								{
																									xtype : 'menuitem',
																									disabled : true,
																									text : 'Créer le terme '
																								},
																								{
																									xtype : 'menuitem',
																									disabled : true,
																									text : 'Sélectionner le terme'
																								} ]
																					}
																				},
																				{
																					xtype : 'menuitem',
																					text : 'Terme non préférentiel',
																					menu : {
																						xtype : 'menu',
																						items : [
																								{
																									xtype : 'menuitem',
																									disabled : true,
																									text : 'Créer le terme '
																								},
																								{
																									xtype : 'menuitem',
																									disabled : true,
																									text : 'Sélectionner le terme'
																								} ]
																					}
																				} ]
																	}
																} ]
															} ],

															columns : [
																	{
																		dataIndex : 'identifier',
																		text : me.xIdentifierLabel,
																		id : 'identifier'
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
																		dataIndex : 'role',
																		text : me.xRoleColumnLabel
																	},
																	{
																		dataIndex : 'prefered',
																		text : me.xPreferedColumnLabel
																	},
																	{
																		dataIndex : 'created',
																		text : me.xCreatedDateLabel
																	} ]
														} ]
											} ]
										});

						me.callParent(arguments);
					}
				});