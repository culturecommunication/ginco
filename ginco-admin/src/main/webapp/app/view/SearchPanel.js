Ext.define('HadocApp.view.SearchPanel', {
    extend: 'Ext.panel.Panel',
    alias: 'widget.mypanel3',

    closable: true,
    title: 'Résultat de recherche',

    initComponent: function() {
        var me = this;

        Ext.applyIf(me, {
            dockedItems: [
                {
                    xtype: 'panel',
                    dock: 'top',
                    collapsed: false,
                    collapsible: true,
                    title: 'Recherche avancée',
                    items: [
                        {
                            xtype: 'datefield',
                            fieldLabel: 'Date de création'
                        },
                        {
                            xtype: 'multislider',
                            width: 400,
                            fieldLabel: 'Label',
                            values: [
                                10,
                                40
                            ]
                        },
                        {
                            xtype: 'combobox',
                            fieldLabel: 'Type'
                        }
                    ]
                },
                {
                    xtype: 'treepanel',
                    dock: 'top',
                    height: 250,
                    width: 400,
                    title: 'Résultat de recherche',
                    forceFit: false,
                    viewConfig: {

                    },
                    columns: [
                        {
                            xtype: 'treecolumn',
                            dataIndex: 'text',
                            flex: 1,
                            text: 'Nodes'
                        },
                        {
                            xtype: 'gridcolumn',
                            dataIndex: 'value',
                            text: 'Value'
                        }
                    ]
                }
            ]
        });

        me.callParent(arguments);
    }

});