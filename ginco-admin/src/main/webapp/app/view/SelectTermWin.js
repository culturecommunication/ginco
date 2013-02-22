Ext
		.define(
				'GincoApp.view.SelectTermWin',
				{
					extend : 'Ext.window.Window',
					alias: 'widget.selectTermWin',
					localized: true,
					
					thesaurusData : null,
					store : null,
					prefered: null,

				    /*Fields prompting values*/
					xIdentifierColumnLabel : "Identifier",
					xLexicalValueColumnLabel : "Lexical Value",
					xCreatedColumnLabel: "Created",
					xModifiedColumnLabel:"Modified",
					xSourceColumnLabel:"Source",
					xStatusColumnLabel:"Status",
					xLangueColumnLabel:"Language",
				    xSelectTermWinTitle : 'Select a Term',
				    xSave: 'Save',
					store : null,
					width : 500,
					height : 500,
					title : 'Sélection d\'un terme',
					titleAlign : 'center',
					modal : true,
					termStore: null,
					initComponent : function() {
						var me = this;
						
						me.termStore = Ext.create('GincoApp.store.ThesaurusTermStore');

						Ext
								.applyIf(
										me,
										{					
											items : [ {
												xtype : 'gridpanel',
												title : me.xSelectTermWinTitle,
												autoScroll:true,
												flex:1,
												store : me.termStore,
												columns : [
												           {dataIndex : 'identifier', text : me.xIdentifierColumnLabel},
												           {dataIndex : 'lexicalValue', text : me.xLexicalValueColumnLabel, flex: 1},
												           {dataIndex : 'created', text : me.xCreatedColumnLabel},
												           {dataIndex : 'modified', text : me.xModifiedColumnLabel},
												           {dataIndex : 'source', text : me.xSourceColumnLabel,  hidden: true},
												           {dataIndex : 'status', text : me.xStatusColumnLabel,  hidden: true},
												           {dataIndex : 'language', text : me.xLangueColumnLabel}
												           ],
												dockedItems: [{
											        xtype: 'pagingtoolbar',
											        store :  me.termStore,
											        dock: 'bottom',
											        displayInfo: true
											    }]
												}]	
										});

						me.callParent(arguments);
					}
				});