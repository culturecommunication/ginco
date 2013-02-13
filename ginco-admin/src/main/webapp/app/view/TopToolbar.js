Ext.define('GincoApp.view.TopToolbar', {
    extend: 'Ext.toolbar.Toolbar',
    alias: 'widget.topToolBar',
    localized: true,
    xNewLabel: "New",
    xAdministrationLabel: "Administration",
    xControlLabel: "Control",
    xTopWelcomeLabel: "Hadoc GINCO Back-office",
    xJournalLabel: "Journal",
    xExportLabel: "Exports",
    xAboutLabel: "About",
    xSearchLabel: "Search",
    xSearchFieldText: "Search a term",
    xConnectedAsLabel: "Connected as",
    xNewMenu_ThesaurusLabel: "Thesaurus",
    xHelpLabel: "Help",
    height: 64,

    initComponent: function () {
        var me = this;

        Ext.applyIf(me, {
            items: [
                {	xtype: 'box',autoEl: {tag: 'img', src:'images/ginco-logo-xs.png', width:'32px', alt:'logo ginco'}},
                { xtype: 'box', autoEl: { tag: 'div', cls: 'title-bar', html: '<h1>Gestionnaire Thesaurus GINCO</h1>' }},
                {
                    xtype: 'tbseparator',
                    flex: 2,
                    height: 10,
                    width: 10
                },
                {
                    xtype: 'buttongroup',
                    title: me.xControlLabel,
                    columns: 1,
                    items: [
                        {
                            xtype: 'button',
                            disabled: false,
                            text: me.xNewLabel,
                            menu: {
                                xtype: 'menu',
                                width: 200,
                                items: [
                                    {
                                        xtype: 'keymenuitem',
                                        itemId: 'newThesaurusBtn',
                                        text: me.xNewMenu_ThesaurusLabel,
                                        cmdTxt: 'Ctrl+1'
                                    }
                                ]
                            }
                        }
                    ]
                },
                {
                    xtype: 'buttongroup',
                    title: me.xAdministrationLabel,
                    columns: 2,
                    items: [
                        {
                            xtype: 'button',
                            text: me.xJournalLabel,
                            disabled: true
                        },
                        {
                            xtype: 'button',
                            disabled: true,
                            text: me.xExportLabel
                        }
                    ]
                },
                {
                    xtype: 'buttongroup',
                    title: me.xHelpLabel,
                    columns: 2,
                    items: [
                        {
                            xtype: 'button',
                            itemId: 'aproposbtn',
                            text: me.xAboutLabel
                        },
                        {
                            xtype: 'button',
                            itemId: 'accessibilitybtn',
                            text: "Accessibilité",
                            enableToggle: true
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
                    fieldLabel: me.xSearchLabel,
                    emptyText: me.xSearchFieldText,
                    hideTrigger: false,
                    repeatTriggerClick: false,
                    disabled: true
                },
                {
                    xtype: 'label',
                    text: me.xConnectedAsLabel
                }
            ]
        });

        me.callParent(arguments);
    }

});