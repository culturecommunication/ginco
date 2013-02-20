/*
 * Thesaurus Term Store 
 * This file contains all Thesaurus formats displayed in dropdown lists
 */
Ext.define('GincoApp.store.ConceptTermStore', {
    extend: 'Ext.data.Store',

    constructor: function(cfg) {
        var me = this;
        cfg = cfg || {};
        me.callParent([Ext.apply({
            autoLoad: false,
            alias: 'store.termstore',
            pageSize: 50,
            model : 'GincoApp.model.ThesaurusTermModel',
            proxy: {
                type: 'ajax',
                url: 'services/ui/thesaurusconceptservice/getConceptTerms',
                reader: {
                    type: 'json',
                    idProperty: 'identifier',
                    root: 'data'
                }
            }

        }, cfg)]);
    }
});