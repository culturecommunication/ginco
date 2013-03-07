/*
 * Concept Note Type Store 
 * This file contains all note types displayed in dropdown lists
 */
Ext.define('GincoApp.store.ConceptNoteTypeStore', {
    extend: 'Ext.data.Store',

    constructor: function(cfg) {
        var me = this;
        cfg = cfg || {};
        me.callParent([Ext.apply({
            autoLoad: true,
            storeId: 'JsonVirtualConceptNoteTypeStore',
            proxy: {
                type: 'ajax',
                url: 'services/ui/thesaurusnoteservice/getConceptNoteTypes',
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