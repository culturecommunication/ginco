/*
 * ReducedConcept Store
 * This file contains all ReducedConcept displayed in popup lists
 */
Ext.define('GincoApp.store.ConceptReducedStore', {
    extend: 'Ext.data.Store',

    thesaurusId : null,
    conceptId : null,
    searchOrphans : true,

    constructor: function(cfg) {
        var me = this;
        cfg = cfg || {};
        me.callParent([Ext.apply({
            autoLoad: false,
            proxy: {
                type: 'ajax',
                url: 'services/ui/thesaurusconceptservice/getConcepts',
                extraParams: {  id: me.conceptId,
                                thesaurusId: me.thesaurusId,
                                searchOrphans: me.searchOrphans },
                reader: {
                    type: 'json',
                    idProperty: 'identifier',
                    root: 'data'
                }
            },
            fields: [
                {
                    name : 'identifier',
                    type : 'string'
                },
                {
                    name: 'label',
                    type : 'string'
                }
            ]

        }, cfg)]);
    }
});