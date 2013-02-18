Ext.define('GincoApp.controller.SandboxPanelController', {
	extend:'Ext.app.Controller',
	
	views : [ 'SandBoxPanel' ],
	
	onGridRender : function(theGrid){
		var thePanel = theGrid.up('sandboxPanel');
		var theStore= theGrid.getStore();
		theStore.getProxy().setExtraParam('idThesaurus',thePanel.thesaurusData.id);
		theStore.load();
		thePanel.setTitle(thePanel.title+' : '+thePanel.thesaurusData.title);
	},
	
	onNodeDblClick : function( theGrid, record, item, index, e, eOpts ) {
        this.openThesaurusTermTab(record);
	},
	openThesaurusTermTab : function(aRecord) {
		var topTabs = Ext.ComponentQuery.query('topTabs')[0];
		var termTabs = Ext.ComponentQuery.query('topTabs termPanel');
		var tabExists = false;
		Ext.Array.each(termTabs,function(element, index, array) {
			if (element.termId!=null && element.termId == aRecord.data.identifier) {
				tabExists = element;
			}
		});

		if (!tabExists) {
			var TermPanel = Ext.create('GincoApp.view.TermPanel');
			TermPanel.termId = aRecord.data.identifier;
			var tab = topTabs.add(TermPanel);
			topTabs.setActiveTab(tab);
			tab.show();
		} else {
			topTabs.setActiveTab(tabExists);
		}
	},
	
    init:function(){
         this.control({
        	 'sandboxPanel gridpanel' : {
 				render : this.onGridRender,
 				itemdblclick : this.onNodeDblClick
 			},         	
         });

       
    }
});