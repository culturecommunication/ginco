Ext.define(
    'GincoApp.view.SelectConceptWin',
    {
        extend : 'Ext.window.Window',
        alias: 'widget.selectConceptWin',
        localized: true,

        thesaurusData : null,
        store : null,
        conceptId : null,
        searchOrphans : true,
        showTree : false,

        /*Fields prompting values*/
        xIdentifierColumnLabel : "Identifier",
        xLexicalValueColumnLabel : "Label",
//        xCreatedColumnLabel: "Created",
//        xModifiedColumnLabel:"Modified",
//        xSourceColumnLabel:"Source",
//        xStatusColumnLabel:"Status",
//        xLangueColumnLabel:"Language",
//        xSelectTermWinTitle : 'Select a Term',
//        xSave: 'Save',

        width : 500,
        height : 530,
        title : 'Sélection d\'un concept',
        titleAlign : 'center',
        modal : true,
        conceptReducedStore: null,
        initComponent : function() {
            var me = this;

            me.conceptReducedStore = Ext.create('GincoApp.store.ConceptReducedStore');

            me.conceptReducedStore.thesaurusId = me.thesaurusData.id,
            me.conceptReducedStore.conceptId = me.conceptId;
            //me.conceptReducedStore.searchOrphans = me.searchOrphans;
//            me.conceptReducedStore.pageSize=20;

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
                        }]
                    }]
                });

            me.callParent(arguments);
        }
    });