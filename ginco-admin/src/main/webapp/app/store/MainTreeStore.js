Ext.define('GincoApp.store.MainTreeStore', {
    extend: 'Ext.data.TreeStore',

    requires: [
        'GincoApp.model.MainTreeModel'
    ],

    constructor: function(cfg) {
        var me = this;
        cfg = cfg || {};
        me.callParent([Ext.apply({
            autoLoad: true,
            storeId: 'JsonMainTreeStore',
            model: 'GincoApp.model.MainTreeModel',
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