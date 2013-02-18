/*
 * File: app/view/SandBoxPanel.js
 * Term Creation/Edition Form
 * 
 */
Ext.define('GincoApp.view.SandBoxPanel', {
	extend : 'Ext.panel.Panel',
	thesaurusData : null,
	alias : 'widget.sandboxPanel',
	localized : true,
	closable : true,
	layout : {
		type : 'vbox',
		align : 'stretch'
	},
	xSandBoxPanelTitle : "Sandbox",
	xIdentifierColumnLabel : "Identifier",
	xLexicalValueColumnLabel : "Lexical Value",
	xCreatedColumnLabel: "Created",
	xModifiedColumnLabel:"Modified",
	xSourceColumnLabel:"Source",
	xStatusColumnLabel:"Status",
	xLangueColumnLabel:"Language",
	
	initComponent : function() {
		var me = this;

		Ext.applyIf(me, {
			title : me.xSandBoxPanelTitle,
			items : [
				{
				xtype : 'gridpanel',
				title : me.xSandBoxPanelTitle,
				autoScroll:true,
				flex:1,
				store : 'ThesaurusTermStore',
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
			        store : 'ThesaurusTermStore',
			        dock: 'bottom',
			        displayInfo: true
			    }]
				}]
		
		});

		me.callParent(arguments);
	}
});