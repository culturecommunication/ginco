Ext.define('HadocApp.view.LeftPanel', {
    extend: 'Ext.panel.Panel',
    alias: 'widget.mypanel5',

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
                    dock: 'top',
                    autoScroll: true,
                    collapsible: false,
                    title: 'Vocabulaires',
                    forceFit: false,
                    scroll: 'vertical',
                    // store: 'ThesaurusTreeStore',
                    displayField: 'label',
                    rootVisible: false,
                    useArrows: true,
                    viewConfig: {

                    }
                },
                {
                    xtype: 'treepanel',
                    dock: 'bottom',
                    collapsible: true,
                    title: 'Administration',
                    store: 'AdminTreeStore',
                    viewConfig: {

                    }
                }
            ]
        });

        me.callParent(arguments);
    }

});