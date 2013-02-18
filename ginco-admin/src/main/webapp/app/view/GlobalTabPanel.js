Ext.define('GincoApp.view.GlobalTabPanel', {
    extend: 'Ext.tab.Panel',
    alias: 'widget.topTabs',   

    activeTab: 0,

    initComponent: function() {
        var me = this;

        Ext.applyIf(me, {
        });

        me.callParent(arguments);
    }

});