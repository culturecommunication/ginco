Ext.define('GincoApp.view.GlobalTabPanel', {
    extend: 'Ext.tab.Panel',
    alias: 'widget.topTabs',

    requires: [
        /*'GincoApp.view.ThesaurusPanel'/*,
        'GincoApp.view.TermPanel',
        'GincoApp.view.SearchPanel',
        'GincoApp.view.ConceptPanel',
        'GincoApp.view.ConceptGroupPanel'    */
    ],

    activeTab: 0,

    initComponent: function() {
        var me = this;

        Ext.applyIf(me, {
        });

        me.callParent(arguments);
    }

});