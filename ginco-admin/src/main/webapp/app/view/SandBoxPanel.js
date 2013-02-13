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
			items : [ {
				xtype : 'gridpanel',
				title : me.xSandBoxPanelTitle,
				columns : [ {
					flex: 1,
					xtype : 'gridcolumn',
					dataIndex : 'string',
					text : 'Valeur lexicale'
				}, {
					xtype : 'numbercolumn',
					dataIndex : 'number',
					text : 'Langue'
				}, {
					xtype : 'datecolumn',
					dataIndex : 'created',
					text : 'Date de création'
				}, {
					xtype : 'booleancolumn',
					dataIndex : 'modified',
					text : 'Date de modification'
				} ],
				dockedItems: [{
			        xtype: 'pagingtoolbar',
			        //store: store,   // same store GridPanel is using
			        dock: 'bottom',
			        displayInfo: true
			    }]
			} ]
		});

		me.callParent(arguments);
	}
});