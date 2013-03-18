/*
 * Concept Language Store 
 * This file contains all Concept languages displayed in dropdown lists
 */
Ext.define('GincoApp.store.ConceptStatusStore', {
    extend: 'Ext.data.Store',

    constructor: function(cfg) {
        var me = this;
        cfg = cfg || {};
        me.callParent([Ext.apply({
            autoLoad: false,
            storeId: 'ConceptStatusStore',
            proxy: {
                type: 'ajax',
                url: 'services/ui/thesaurusconceptservice/getAllConceptStatus',
                reader: {
                    type: 'json',
                    idProperty: 'status',
                    root: 'data'
                }
            },
            fields: [
                {
                    name: 'status',
                    type: 'int'
                },
                {
                    name: 'statusLabel',
                    type: 'string'
                }
            ]
        }, cfg)]);
    }
});