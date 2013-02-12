Ext.define('HadocApp.view.LeftPanel', {
    extend: 'Ext.panel.Panel',
    alias: 'widget.leftPanel',
    xThesaurusLabel: "Thesaurus Explorer",
    xRefreshBtnTooltip: "Refresh explorer",
    localized: true,
    frame: false,
    width: 250,
    collapsible: true,
    header: true,
    title: '',

    initComponent: function() {
        var me = this;

        Ext.applyIf(me, {
            dockedItems: [
                {
                    xtype: 'treepanel',
                    id: 'mainTreeView',
                    dock: 'top',
                    autoScroll: true,
                    dblClickExpand: false,
                    collapsible: false,
                    title: me.xThesaurusLabel,
                    forceFit: false,
                    scroll: 'vertical',
                    store: 'MainTreeStore',
                    displayField: 'title',
                    rootVisible: false,
                    useArrows: true,
                    viewConfig: {
                    	loadMask : true
                    },
	                tools:[{
	                    type:'refresh',
	                    tooltip: this.xRefreshBtnTooltip,
	                }]
	          }
                
            ]
        });

        me.callParent(arguments);
    }

});