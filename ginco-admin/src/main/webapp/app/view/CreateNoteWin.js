Ext
		.define(
				'GincoApp.view.CreateNoteWin',
				{
					extend : 'Ext.window.Window',
					alias: 'widget.createNoteWin',
					localized: true,
					
					config: {
						storeNoteTypes : null,
						thesaurusData : null,
						noteId : null
			        },
			        
					xSave: 'Save',
					xCreateNoteWinTitle : 'New Note',
					xLexicalValueLabel : 'Lexical value',
				    xSourceLabel : 'Source',
				    xLanguageLabel : 'Language',
				    xTypeLabel : 'Type',
				    
					width : 500,
					title : 'Nouvelle note',
					titleAlign : 'center',
					modal : true,
					store : null,
					
					initComponent : function() {
						var me = this;
						me.addEvents('saveNoteButton');
						
						Ext
								.applyIf(
										me,
										{					
											items : [
													{
														xtype : 'form',
														height : 600,
														
														dockedItems : [ {
															xtype : 'toolbar',
															dock : 'top',
															items : [ {
																xtype : 'button',
																text : me.xSave,
																formBind : true,
																itemId : 'saveNotePopup',
																iconCls : 'icon-save',
																handler: function(theButton){
																	me.fireEvent('saveNoteButton', theButton);
																	me.close();
																}
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
										                            xtype: 'htmleditor',
										                            margin: '10 0 5 0',
										                            name:'lexicalValue',
										                            enableAlignments : false,
																	enableColors : false,
																	enableFont : false,
																	enableFontSize : false,
																	enableFormat : false,
																	enableLists : false,
																	enableSourceEdit : false,
										                            fieldLabel: me.xLexicalValueLabel,
										                            allowBlank: false
										                        },
										                        {
										                        	xtype: 'combobox',
										                        	name:'language',
										                        	itemId: 'languageCombo',
										                        	fieldLabel: me.xLanguageLabel,
										                        	editable : false,
										                        	displayField : 'refname',
										                        	valueField : 'id',
										                        	forceSelection : true,
										                        	store :  Ext.create('GincoApp.store.NoteLanguageStore'),
										                        	allowBlank : false
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
																	enableSourceEdit : false,
																	allowBlank : true
										                        },
										                        {
										                        	xtype: 'combobox',
										                        	name:'type',
										                        	itemId: 'typeCombo',
										                        	fieldLabel: me.xTypeLabel,
										                        	editable : false,
										                        	displayField : 'label',
										                        	valueField : 'code',
										                        	forceSelection : true,
										                        	store :  me.storeNoteTypes,
										                        	allowBlank : false
										                        }
										                        
										                    ]
													} ]
										});

						me.callParent(arguments);
					}

				});