Ext.define('HadocApp.store.AdminTreeStore', {
    extend: 'Ext.data.TreeStore',

    constructor: function(cfg) {
        var me = this;
        cfg = cfg || {};
        me.callParent([Ext.apply({
            storeId: 'MyJsonTreeStore1',
            root: {
                expanded: true,
                children: [
                    {
                        text: 'Utilisateurs',
                        expanded: true,
                        children: [
                            
                        ]
                    },
                    {
                        text: 'Groupes',
                        expanded: true,
                        children: [
                            
                        ]
                    },
                    {
                        text: 'Listes de valeurs',
                        expanded: true,
                        children: [
                            
                        ]
                    }
                ]
            },
            fields: [
                {
                    name: 'text'
                }
            ]
        }, cfg)]);
    }
});