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
            getChildren : false,
            searchOrphans : null,
            showTree : false,
            checkstore: null
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
            
            if (!me.getChildren) {
                me.conceptReducedStore.getProxy().extraParams = {
	                id: me.conceptId,
	                thesaurusId: me.thesaurusData.id,
	                searchOrphans: me.searchOrphans
	            };
            } else {
            	me.conceptReducedStore.getProxy().url = 'services/ui/thesaurusconceptservice/getSimpleChildrenConcepts';
            	me.conceptReducedStore.getProxy().extraParams = {
	                conceptId: me.conceptId
	            };
            	}
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
                        } ],
                        listeners: {
                        	itemclick : function (view, record, item, index, e) {                        		
                        		var me = this;
            					var theButton = me.down('#selectButton');
            					var win = me.up('selectConceptWin');
            					var checkAgainstStore = win.checkstore;
            					if (checkAgainstStore) {
            						if (checkAgainstStore.findRecord('identifier',record.data.identifier) == null) {
            							theButton.setDisabled(false);
            						} else {
            							theButton.setDisabled(true);
            						}
                        		} else {
            						theButton.setDisabled(false);
            					}
                            }
                         }                        

                    }]
                });

            me.callParent(arguments);
        }
    });