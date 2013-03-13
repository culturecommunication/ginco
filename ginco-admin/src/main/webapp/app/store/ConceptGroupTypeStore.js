/*
 * Concept Group Type Store 
 * This file contains all group types displayed in dropdown lists
 */
Ext.define('GincoApp.store.ConceptGroupTypeStore', {
    extend: 'Ext.data.Store',

    constructor: function(cfg) {
        var me = this;
        cfg = cfg || {};
        me.callParent([Ext.apply({
            autoLoad: true,
            storeId: 'JsonVirtualConceptGroupTypeStore',
            proxy: {
                type: 'ajax',
                url: 'services/ui/thesaurusconceptgroupservice/getConceptGroupTypes',
                reader: {
                    type: 'json',
                    idProperty: 'code',
                    root: 'data'
                }
            },
            fields: [
                {
                    name: 'code',
                    type: 'string'
                },
                {
                    name: 'label',
                    type: 'string'
                },
                {
                    name: 'isTerm',
                    type: 'boolean'
                },
                {
                    name: 'isConcept',
                    type: 'boolean'
                }
            ]
        }, cfg)]);
    }
});