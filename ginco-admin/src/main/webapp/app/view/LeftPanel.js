Ext.define('HadocApp.view.LeftPanel', {
    extend: 'Ext.panel.Panel',
    alias: 'widget.leftPanel',
    xThesaurusLabel: "Thesaurus Explorer",
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
                    dock: 'top',
                    autoScroll: true,
                    collapsible: false,
                    title: me.xThesaurusLabel,
                    forceFit: false,
                    scroll: 'vertical',
                    // store: 'ThesaurusTreeStore',
                    displayField: 'label',
                    rootVisible: false,
                    useArrows: true,
                    viewConfig: {

                    }
                }
            ]
        });

        me.callParent(arguments);
    }

});