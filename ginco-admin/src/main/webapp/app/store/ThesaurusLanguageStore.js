/*
 * Thesaurus Language Store 
 * This file contains all Thesaurus languages displayed in dropdown lists
 */
Ext.define('GincoApp.store.ThesaurusLanguageStore', {
    extend: 'GincoApp.store.VirtualLanguageStore',

    constructor: function(cfg) {
        var me = this;
        cfg = cfg || {};
        me.callParent([Ext.apply({
            autoLoad: true,
            storeId: 'JsonThesaurusLanguageStore'
        }, cfg)]);
    }
});