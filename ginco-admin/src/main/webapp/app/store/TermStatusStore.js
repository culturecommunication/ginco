/*
 * Term Language Store 
 * This file contains all Term languages displayed in dropdown lists
 */
Ext.define('GincoApp.store.TermStatusStore', {
    extend: 'Ext.data.Store',

    constructor: function(cfg) {
        var me = this;
        cfg = cfg || {};
        me.callParent([Ext.apply({
            autoLoad: false,
            storeId: 'TermStatusStore',
            proxy: {
                type: 'ajax',
                url: 'services/ui/thesaurustermservice/getAllTermStatus',
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