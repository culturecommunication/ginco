/*
 * Thesaurus Language Store 
 * This file contains all Thesaurus languages displayed in dropdown lists
 */
Ext.define('GincoApp.store.VirtualLanguageStore', {
    extend: 'Ext.data.Store',

    constructor: function(cfg) {
        var me = this;
        cfg = cfg || {};
        me.callParent([Ext.apply({
            autoLoad: true,
            storeId: 'JsonVirtualLanguageStore',
            proxy: {
                type: 'ajax',
                url: 'services/ui/thesaurusservice/getTopLanguages',
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
                    name: 'refname',
                    type: 'string'
                }
            ]
        }, cfg)]);
    }
});