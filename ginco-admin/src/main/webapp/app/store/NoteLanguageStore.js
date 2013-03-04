/*
 * Note Language Store 
 * This file contains all Note languages displayed in dropdown lists
 */
Ext.define('GincoApp.store.NoteLanguageStore', {
    extend: 'GincoApp.store.VirtualLanguageStore',

    constructor: function(cfg) {
        var me = this;
        cfg = cfg || {};
        me.callParent([Ext.apply({
            autoLoad: false,
            storeId: 'JsonNoteLanguageStore'
        }, cfg)]);
    }
});