/*
 * Thesaurus Term Store 
 * This file contains all Thesaurus formats displayed in dropdown lists
 */
Ext.define('HadocApp.store.ThesaurusTermStore', {
    extend: 'Ext.data.Store',

    constructor: function(cfg) {
        var me = this;
        cfg = cfg || {};
        me.callParent([Ext.apply({
            autoLoad: true,
            storeId: 'JsonThesaurusTermStore',
            proxy: {
                type: 'ajax',
                url: 'services/ui/thesaurustermservice/getAllThesaurusTerms',
                reader: {
                    type: 'json',
                    idProperty: 'identifier'
                }
            },
            fields: [
                {
                    name : 'identifier',
                    type : 'string'
                },
                {
                    name: 'lexicalValue',
                    type : 'string'
                }
            ]
        }, cfg)]);
    }
});