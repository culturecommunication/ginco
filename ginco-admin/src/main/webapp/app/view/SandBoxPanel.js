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
	initComponent : function() {
		var me = this;

		Ext.applyIf(me, {
			title : me.xSandBoxPanelTitle,
			items : [
				{
				xtype : 'gridpanel',
				title : me.xSandBoxPanelTitle,
				store : 'ThesaurusTermStore',
				columns : [
				           {dataIndex : 'identifier', text : 'Identifiant',flex: 1},
				           {dataIndex : 'lexicalValue', text : 'Valeur lexicale'},
				           {dataIndex : 'created', text : 'Date de création'},
				           {dataIndex : 'modified', text : 'Date de mofication'},
				           {dataIndex : 'source', text : 'Source'},
				           {dataIndex : 'prefered', text : 'Préférentiel'},
				           {dataIndex : 'status', text : 'Statut'},
				           {dataIndex : 'role', text : 'Role'},
				           {dataIndex : 'language', text : 'Langue'}
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