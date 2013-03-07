Ext.define(
    'GincoApp.view.SelectConceptWin',
    {
        extend : 'Ext.window.Window',
        alias: 'widget.selectConceptWin',
        localized: true,

        requires: [
            'Ext.grid.PagingScroller'
        ],

        config: {
            thesaurusData : null,
            conceptId : null,
            searchOrphans : null,
            showTree : false
        },

        viewConfig : {
            style : { overflow: 'auto', overflowX: 'hidden' }
        },

        /*Fields prompting values*/
        xIdentifierColumnLabel : "Identifier",
        xLexicalValueColumnLabel : "Label",
        xSelect : "Select",

        width : 500,
        title : 'Sélectionner un concept',
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
            me.addEvents('selectBtn');

            Ext
                .applyIf(
                me,
                {
                    items : [ {
                        xtype : 'gridpanel',
                        title : me.xSelectTermWinTitle,
                        autoScroll : true,
                        height : 300,
                        flex : 1,
                        store : me.conceptReducedStore,
                        columns : [
                            {dataIndex : 'identifier', text : me.xIdentifierColumnLabel},
                            {dataIndex : 'label', text : me.xLexicalValueColumnLabel, flex: 1}
                        ],
                        dockedItems: [ {
                            xtype : 'toolbar',
                            dock : 'top',
                            items : [ {
                                xtype : 'button',
                                text : me.xSelect,
                                formBind : true,
                                disabled : true,
                                itemId : 'selectButton',
                                iconCls : 'icon-save',
                                handler : function(theButton) {
                                    var thePanel = theButton.up('gridpanel');
                                    var record = thePanel.getSelectionModel().getSelection();

                                    if(record.length == 1) {
                                        me.fireEvent('selectBtn', record);
                                        me.close();
                                    }
                                }
                            }]
                        } ]
                    }]
                });

            me.callParent(arguments);
        }
    });