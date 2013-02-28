/*
 * Thesaurus Note Store 
 * This file contains Thesaurus notes
 */
Ext.define('GincoApp.store.ThesaurusNoteStore', {
    extend: 'Ext.data.Store',

    constructor: function(cfg) {
        var me = this;
        cfg = cfg || {};
        me.callParent([Ext.apply({
            autoLoad: false,
            alias: 'store.notestore',
            model : 'GincoApp.model.ThesaurusNoteModel',
            proxy: {
                type: 'ajax',
                url: '',
                reader: {
                    type: 'json',
                    idProperty: 'identifier',
                    root: 'data'
                }
            }
        }, cfg)]);
    }
});