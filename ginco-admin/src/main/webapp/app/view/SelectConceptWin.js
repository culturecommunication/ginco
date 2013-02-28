Ext.define(
    'GincoApp.view.SelectConceptWin',
    {
        extend : 'Ext.window.Window',
        alias: 'widget.selectConceptWin',
        localized: true,

        config: {
            thesaurusData : null,
            conceptId : null,
            searchOrphans : true,
            showTree : false
        },

        /*Fields prompting values*/
        xIdentifierColumnLabel : "Identifier",
        xLexicalValueColumnLabel : "Label",
        xSelect : "Select as parent",

        width : 500,
        title : 'Sélection d\'un concept',
        titleAlign : 'center',
        modal : true,
        conceptReducedStore: null,
        initComponent : function() {
            var me = this;

            me.conceptReducedStore = Ext.create('GincoApp.store.ConceptReducedStore');
            me.conceptReducedStore.getProxy().extraParams = {
                id: me.conceptId,
                thesaurusId: me.thesaurusData.id,
                searchOrphans: me.searchOrphans
            };
            me.conceptReducedStore.load();

            Ext
                .applyIf(
                me,
                {
                    items : [ {
                        xtype : 'gridpanel',
                        title : me.xSelectTermWinTitle,
                        autoScroll:true,
                        flex:1,
                        store : me.conceptReducedStore,
                        columns : [
                            {dataIndex : 'identifier', text : me.xIdentifierColumnLabel},
                            {dataIndex : 'label', text : me.xLexicalValueColumnLabel, flex: 1}
                        ],
                        dockedItems: [{
                            xtype: 'pagingtoolbar',
                            store :  me.conceptReducedStore,
                            dock: 'bottom',
                            displayInfo: true
                        }, {
                            xtype : 'toolbar',
                            dock : 'top',
                            items : [ {
                                xtype : 'button',
                                text : me.xSelect,
                                formBind : true,
                                itemId : 'selectConceptAsParent',
                                iconCls : 'icon-save'
                            }]
                        } ]
                    }]
                });

            me.callParent(arguments);
        }
    });