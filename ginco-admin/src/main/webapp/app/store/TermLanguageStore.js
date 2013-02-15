/*
 * Term Language Store 
 * This file contains all Term languages displayed in dropdown lists
 */
Ext.define('GincoApp.store.TermLanguageStore', {
    extend: 'GincoApp.store.VirtualLanguageStore',

    constructor: function(cfg) {
        var me = this;
        cfg = cfg || {};
        me.callParent([Ext.apply({
            autoLoad: false,
            storeId: 'JsonTermLanguageStore',
        }, cfg)]);
    }
});