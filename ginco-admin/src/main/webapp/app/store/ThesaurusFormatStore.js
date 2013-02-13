/*
 * Thesaurus Format Store 
 * This file contains all Thesaurus formats displayed in dropdown lists
 */
Ext.define('GincoApp.store.ThesaurusFormatStore', {
    extend: 'Ext.data.Store',

    constructor: function(cfg) {
        var me = this;
        cfg = cfg || {};
        me.callParent([Ext.apply({
            autoLoad: true,
            storeId: 'JsonThesaurusFormatStore',
            proxy: {
                type: 'ajax',
                url: 'services/ui/thesaurusservice/getThesaurusFormats',
                reader: {
                    type: 'json',
                    idProperty: 'identifier'
                }
            },
            fields: [
                {
                    name : 'identifier',
                    type : 'int'
                },
                {
                    name: 'label',
                    type : 'string'
                }
            ]
        }, cfg)]);
    }
});