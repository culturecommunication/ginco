Ext.define('HadocApp.view.AProposWin', {
    extend: 'Ext.window.Window',

    autoShow: true,
    height: 250,
    width: 400,
    title: 'A Propos',
    titleAlign: 'center',
    modal: true,

    initComponent: function() {
        var me = this;

        Ext.applyIf(me, {
            items: [
                {
                    xtype: 'label',
                    html: '<h1>Hadoc GINCO</h1><p>Gestionnaire de thesaurus du MCC</p>'
                }
            ]
        });

        me.callParent(arguments);
    }

});