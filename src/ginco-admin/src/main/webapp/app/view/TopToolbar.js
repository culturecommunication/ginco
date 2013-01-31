Ext.define('HadocApp.view.TopToolbar', {
    extend: 'Ext.toolbar.Toolbar',
    alias: 'widget.mytoolbar',

    height: 64,

    initComponent: function() {
        var me = this;

        Ext.applyIf(me, {
            items: [
                {
                    xtype: 'label',
                    width: 100,
                    text: 'Gestionnaire Hadoc GINCO'
                },
                {
                    xtype: 'buttongroup',
                    title: 'Gestion',
                    columns: 2,
                    items: [
                        {
                            xtype: 'splitbutton',
                            disabled: false,
                            text: 'Nouveau',
                            menu: {
                                xtype: 'menu',
                                width: 120,
                                items: [

                                ]
                            }
                        }
                    ]
                },
                {
                    xtype: 'buttongroup',
                    title: 'Administration',
                    columns: 2,
                    items: [
                        {
                            xtype: 'button',
                            text: 'Journal'
                        },
                        {
                            xtype: 'button',
                            text: 'Exports'
                        }
                    ]
                },
                {
                    xtype: 'buttongroup',
                    title: 'Aide',
                    columns: 2,
                    items: [
                        {
                            xtype: 'button',
                            id: 'aproposbtn',
                            text: 'À propos'
                        }
                    ]
                },
                {
                    xtype: 'tbseparator',
                    flex: 2,
                    height: 10,
                    width: 10
                },
                {
                    xtype: 'triggerfield',
                    width: 276,
                    fieldLabel: 'Recherche',
                    emptyText: 'Rechercher un terme',
                    hideTrigger: false,
                    repeatTriggerClick: false
                },
                {
                    xtype: 'label',
                    text: 'Connecté en tant que Admin'
                }
            ]
        });

        me.callParent(arguments);
    }

});