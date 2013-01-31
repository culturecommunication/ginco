Ext.define('HadocApp.view.HadocViewPort', {
    extend: 'Ext.container.Viewport',

    requires: [
        'HadocApp.view.GlobalTabPanel',
        'HadocApp.view.TopToolbar',
        'HadocApp.view.LeftPanel'
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