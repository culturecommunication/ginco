Ext.define('HadocApp.view.GlobalTabPanel', {
    extend: 'Ext.tab.Panel',
    alias: 'widget.topTabs',

    requires: [
        'HadocApp.view.ThesaurusPanel'/*,
        'HadocApp.view.TermPanel',
        'HadocApp.view.SearchPanel',
        'HadocApp.view.ConceptPanel',
        'HadocApp.view.ConceptGroupPanel'    */
    ],

    activeTab: 0,

    initComponent: function() {
        var me = this;

        Ext.applyIf(me, {
           /* items: [
                    {
                        xtype: 'thesaurusPanel'
                    }
            ]*/
        });

        me.callParent(arguments);
    }

});