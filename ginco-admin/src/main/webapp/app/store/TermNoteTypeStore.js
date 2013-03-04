/*
 * Term Language Store 
 * This file contains all Term languages displayed in dropdown lists
 */
Ext.define('GincoApp.store.TermNoteTypeStore', {
    extend: 'Ext.data.Store',

    constructor: function(cfg) {
        var me = this;
        cfg = cfg || {};
        me.callParent([Ext.apply({
            autoLoad: true,
            storeId: 'JsonVirtualTermNoteTypeStore',
            proxy: {
                type: 'ajax',
                url: 'services/ui/thesaurusnoteservice/getTermNoteTypes',
                reader: {
                    type: 'json',
                    idProperty: 'id',
                    root: 'data'
                }
            },
            fields: [
                {
                    name: 'id',
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