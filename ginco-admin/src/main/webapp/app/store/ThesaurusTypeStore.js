/*
 * Thesaurus Type Store 
 * 
 */
Ext.define('HadocApp.store.ThesaurusTypeStore', {
    extend: 'Ext.data.Store',

    constructor: function(cfg) {
        var me = this;
        cfg = cfg || {};
        me.callParent([Ext.apply({
            autoLoad: true,
            storeId: 'JsonThesaurusStore',
            proxy: {
                type: 'ajax',
                url: 'services/ui/thesaurusservice/getThesaurusTypes',
                reader: {
                    type: 'json',
                    idProperty: 'identifier'
                }
            },
            fields: [
                {
                    name: 'identifier'
                },
                {
                    name: 'label'
                }
            ]
        }, cfg)]);
    }
});