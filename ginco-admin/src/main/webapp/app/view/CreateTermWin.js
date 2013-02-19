Ext
		.define(
				'GincoApp.view.CreateTermWin',
				{
					extend : 'Ext.window.Window',
					alias: 'widget.createTermWin',
					localized: true,
					
					thesaurusData: null,

				    /*Fields prompting values*/
				    xLexicalValueLabel : 'Lexical value',
				    xLanguagesLabel : 'Languages',
				    xSourceLabel : 'Source',
				    xCreateTermWinTitle : 'New Term',
				    xSave: 'Save',
					store : null,
					width : 500,
					title : 'Création de nouveau terme',
					titleAlign : 'center',
					modal : true,
					initComponent : function() {
						var me = this;

						Ext
								.applyIf(
										me,
										{					
											items : [
													{
														xtype : 'form',
														defaults : {
															labelWidth : 150
														},
														
														dockedItems : [ {
															xtype : 'toolbar',
															dock : 'top',
															items : [ {
																xtype : 'button',
																text : me.xSave,
																formBind : true,
																itemId : 'saveTermFromConcept',
																iconCls : 'icon-save'
															}]
														} ],
														defaults : {
															afterLabelTextTpl : new Ext.XTemplate(
																	'<tpl if="allowBlank === false"><span style="color:red;">*</span></tpl>',
																	{
																		disableFormats : true
																	})
														},
														
														items: [
										                        {
										                        	xtype: 'hiddenfield',
										                        	name:'identifier'
										                        },
										                        {
										                        	xtype: 'hiddenfield',
										                        	name:'created'
										                        },
										                        {
										                            xtype: 'hiddenfield',
										                            name:'modified'
										                        },
										                        {
										                            xtype: 'textfield',
										                            margin: '10 0 5 0',
										                            name:'lexicalValue',
										                            fieldLabel: me.xLexicalValueLabel,
										                            allowBlank: false
										                        },
										                        {
										                        	xtype: 'textareafield',
										                        	name:'source',
										                        	fieldLabel: me.xSourceLabel,
										                        	allowBlank: false,
										                        	grow: true
										                        },
										                        {
										                        	xtype: 'hiddenfield',
										                        	name:'prefered'
										                        },
										                        {
										                        	xtype: 'hiddenfield',
										                        	name:'status'
										                        },
										                        {
										                        	xtype: 'hiddenfield',
										                        	name:'role'
										                        },
										                        {
										                        	xtype: 'hiddenfield',
										                        	name:'conceptId'
										                        },
										                        {
										                        	xtype: 'hiddenfield',
										                        	name:'thesaurusId'
										                        },
										                        {
										                        	xtype: 'combobox',
										                        	name:'language',
										                        	itemId: 'languageCombo',
										                        	fieldLabel: me.xLanguagesLabel,
										                        	editable : false,
										                        	displayField : 'refname',
										                        	valueField : 'id',
										                        	forceSelection : true,
										                        	store :  Ext.create('GincoApp.store.TermLanguageStore'),
										                        	allowBlank : false
										                        }
										                    ]
													} ]
										});

						me.callParent(arguments);
					}

				});