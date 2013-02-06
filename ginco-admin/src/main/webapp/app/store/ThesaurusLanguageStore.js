/*
 * Thesaurus Language Store 
 * This file contains all Thesaurus languages displayed in dropdown lists
 */
Ext.define('HadocApp.store.ThesaurusLanguageStore', {
    extend: 'Ext.data.Store',

    constructor: function(cfg) {
        var me = this;
        cfg = cfg || {};
        me.callParent([Ext.apply({
            autoLoad: true,
            pageSize: 50,
            storeId: 'JsonThesaurusLanguageStore',
            proxy: {
                type: 'ajax',
                url: 'services/ui/thesaurusservice/getLanguages',
                reader: {
                    type: 'json',
                    idProperty: 'id'
                }
            },
            fields: [
                {
                    name: 'id'
                },
                {
                    name: 'refname'
                }
            ]
        }, cfg)]);
    }
});