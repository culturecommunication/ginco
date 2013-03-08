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
														height : 300,
														
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
															labelWidth : 150,
															anchor: '90%',
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
										                        	xtype: 'htmleditor',
										                        	name:'source',
										                        	fieldLabel: me.xSourceLabel,
										                        	enableAlignments : false,
																	enableColors : false,
																	enableFont : false,
																	enableFontSize : false,
																	enableFormat : false,
																	enableLists : false,
																	enableSourceEdit : false
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