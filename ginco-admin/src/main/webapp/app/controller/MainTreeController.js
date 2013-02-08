Ext.define('HadocApp.controller.MainTreeController', {
    extend: 'Ext.app.Controller',

    views: [
        'LeftPanel'
    ],

    onNodeDblClick: function(tree, record, item, index, e, eOpts) {
    	this.openThesaurusTab(record);
        return false;
    },
    openThesaurusTab : function (aRecord) {
        if(aRecord.data.type == "THESAURUS") {
            var topTabs = Ext.ComponentQuery.query('topTabs')[0];
            var tabExists = false;
            topTabs.items.each(function (element, index, array) {
                if(element.thesaurusId == aRecord.data.id) {
                    tabExists = element;
                }
            });

            if(!tabExists) {
                var ThesaurusPanel = Ext.create('HadocApp.view.ThesaurusPanel');
                ThesaurusPanel.thesaurusId = aRecord.data.id;

                var tab = topTabs.add(ThesaurusPanel);
                topTabs.setActiveTab(tab);
                tab.show();
            } else {
                topTabs.setActiveTab(tabExists);
            }
        }
    },
    onEnterKey : function (theTree)
    {
    	var node = theTree.getSelectionModel().getSelection();
    	if (node.length>0)
    		this.openThesaurusTab(node[0]);
    	
    },
    onTreeRender : function  (theTree)
    {
    	var me = this;
    	this.nav = new Ext.util.KeyNav({
    	    target :theTree.getEl(),
    	    
    	    enter  : function() {
    	    	me.onEnterKey(theTree);
    	    },	
    	    scope : me
    	});

    },
    init: function(application) {
        this.control({
            "#mainTreeView": {
                beforeitemdblclick: this.onNodeDblClick,
                afterrender : this.onTreeRender,
            }
        });
    }

});
