/*
 * Thesaurus Note Store 
 * This file contains Thesaurus notes
 */
Ext.define('GincoApp.store.ThesaurusNoteStore', {
    extend: 'Ext.data.Store',
    requires: [
               'GincoApp.model.ThesaurusNoteModel'
     ],

    constructor: function(cfg) {
    	var me = this;
        cfg = cfg || {};
        me.callParent([Ext.apply({
            //TODO : keep autoload false ?
        	autoLoad: false,
        	pageSize: 10,
            alias: 'store.notestore',
            model : 'GincoApp.model.ThesaurusNoteModel',
            proxy: {
                type: 'ajax',
                api: {
                    read: 'services/ui/thesaurusnoteservice/getNotes',
                    update: 'services/ui/thesaurusnoteservice/updateNotes',
                    create: 'services/ui/thesaurusnoteservice/createNotes',
                    destroy: 'services/ui/thesaurusnoteservice/destroyNotes'
                },
                writer: {
                	type: 'json',
                	//A single note must be sent in an array :
                	allowSingle : false
                },
                reader: {
                	type: 'json',
                    root: 'data',
                    idProperty: 'identifier'
                }
            }
        }, cfg)]);
    }
});