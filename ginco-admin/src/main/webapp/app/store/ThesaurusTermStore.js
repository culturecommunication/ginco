/*
 * Thesaurus Term Store 
 * This file contains all Thesaurus formats displayed in dropdown lists
 */
Ext.define('GincoApp.store.ThesaurusTermStore', {
    extend: 'Ext.data.Store',

    constructor: function(cfg) {
        var me = this;
        cfg = cfg || {};
        me.callParent([Ext.apply({
            autoLoad: false,
            storeId: 'JsonThesaurusTermStore',
            pageSize: 50,
            proxy: {
                type: 'ajax',
                url: 'services/ui/thesaurustermservice/getAllThesaurusTerms',
                reader: {
                    type: 'json',
                    idProperty: 'identifier',
                    root: 'data'
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
                },
                {
                    name: 'created',
                    type : 'string'
                },
                {
                    name: 'modified',
                    type : 'string'
                },
                {
                    name: 'source',
                    type : 'string'
                },
                {
                    name: 'prefered',
                    type : 'boolean'
                },
                {
                    name: 'status',
                    type : 'int'
                },
                {
                    name: 'role',
                    type : 'int'
                },
                {
                    name: 'conceptId',
                    type : 'string'
                },
                {
                    name: 'thesaurusId',
                    type : 'string'
                },
                {
                    name: 'language',
                    type : 'string'
                }
            ]
        }, cfg)]);
    }
});