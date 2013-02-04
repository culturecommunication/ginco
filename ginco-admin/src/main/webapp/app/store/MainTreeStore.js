Ext.define('HadocApp.store.MainTreeStore', {
    extend: 'Ext.data.TreeStore',

    requires: [
        'HadocApp.model.MainTreeModel'
    ],

    constructor: function(cfg) {
        var me = this;
        cfg = cfg || {};
        me.callParent([Ext.apply({
            autoLoad: true,
            storeId: 'JsonMainTreeStore',
            model: 'HadocApp.model.MainTreeModel',
            proxy: {
                type: 'ajax',
                url: 'services/ui/baseservice/getVocabularies',
                reader: {
                    type: 'json'
                }
            }
        }, cfg)]);
    }
});