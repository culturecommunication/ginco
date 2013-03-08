/*
 * Term Language Store 
 * This file contains all Term languages displayed in dropdown lists
 */
Ext.define('GincoApp.store.TermRoleStore', {
    extend: 'Ext.data.Store',

    constructor: function(cfg) {
        var me = this;
        cfg = cfg || {};
        me.callParent([Ext.apply({
            autoLoad: true,
            storeId: 'JsonVirtualTermRoleStore',
            proxy: {
                type: 'ajax',
                url: 'services/ui/termroleservice/getRoles',
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
                }
                
            ]
        }, cfg)]);
    }
});