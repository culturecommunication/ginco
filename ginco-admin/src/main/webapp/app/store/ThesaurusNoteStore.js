/*
 * Thesaurus Note Store 
 * This file contains Thesaurus notes
 */
Ext.define('GincoApp.store.ThesaurusNoteStore', {
    extend: 'Ext.data.Store',

    constructor: function(cfg) {
    	var writer = new Ext.data.JsonWriter({
            type: 'json',
            encode: false,
            allowSingle : false,
            writeAllFields: true,
            returnJson: true
        });
        var me = this;
        cfg = cfg || {};
        me.callParent([Ext.apply({
            autoLoad: false,
            alias: 'store.notestore',
            model : 'GincoApp.model.ThesaurusNoteModel',
            proxy: {
                type: 'ajax',
                api: {
                    read: 'services/ui/thesaurusnoteservice/getNotes',
                    update: 'services/ui/thesaurusnoteservice/updateNotes',
                    create: 'services/ui/thesaurusnoteservice/updateNotes'
                },
                reader: {
                    type: 'json',
                    idProperty: 'identifier',
                    root: 'data'
                },
                writer: writer
            }
        }, cfg)]);
    }
});