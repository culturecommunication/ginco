Ext.define('GincoApp.view.LeftPanel', {
	extend : 'Ext.panel.Panel',
	alias : 'widget.leftPanel',
	xThesaurusLabel : "Thesaurus Explorer",
	xRefreshBtnTooltip : "Refresh explorer",
	localized : true,
	frame : false,
	width : 250,
	collapsible : true,
	header : true,
	title : '',
	layout : {
		type : 'vbox',
		align : 'stretch'
	},

	initComponent : function() {
		var me = this;

		Ext.applyIf(me, {
			items : [ {
				xtype : 'treepanel',
				id : 'mainTreeView',
				autoScroll : true,
				dblClickExpand : false,
				collapsible : false,
				title : me.xThesaurusLabel,
				store : 'MainTreeStore',
				displayField : 'title',
				rootVisible : false,
				useArrows : true,
				flex : 1,
				viewConfig : {
					loadMask : true
				},
				tools : [ {
					type : 'refresh',
					tooltip : this.xRefreshBtnTooltip
				} ]
			}

			]
		});

		me.callParent(arguments);
	}

});