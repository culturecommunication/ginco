Ext.define('GincoApp.view.GincoViewPort', {
    extend: 'Ext.container.Viewport',

    requires: [
        'GincoApp.view.GlobalTabPanel',
        'GincoApp.view.TopToolbar',
        'GincoApp.view.LeftPanel'
    ],

    layout: {
        type: 'border'
    },

    initComponent: function() {
        var me = this;

        Ext.applyIf(me, {
            items: [
                {
                    xtype: 'topTabs',
                    region: 'center'
                },
                {
                    xtype: 'topToolBar',
                    region: 'north'
                },
                {
                    xtype: 'leftPanel',
                    region: 'west',
                    split: true
                }
            ]
        });

        me.callParent(arguments);
    }

});