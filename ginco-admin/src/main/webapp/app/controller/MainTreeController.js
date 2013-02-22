Ext.define('GincoApp.controller.MainTreeController', {
	extend : 'Ext.app.Controller',

	views : [ 'LeftPanel' ],
	stores : [ 'MainTreeStore' ],
	xProblemLabel : 'Problem',
	xProblemLoadMsg : 'Unable to load thesaurus tree',

	onNodeDblClick : function(tree, aRecord, item, index, e, eOpts) {
		if (aRecord.data.type == "THESAURUS") {
			this.openThesaurusTab(aRecord);
		}
		if (aRecord.data.type == "FOLDER"
				&& aRecord.data.id.indexOf("SANDBOX") === 0) {
			this.openSandBoxTab(aRecord.parentNode);
		}
		return false;
	},
	openSandBoxTab : function(aRecord) {
		var topTabs = Ext.ComponentQuery.query('topTabs')[0];
		var sandBoxTabs = Ext.ComponentQuery.query('topTabs sandboxPanel');
		var tabExists = false;
		Ext.Array.each(sandBoxTabs,function(element, index, array) {
			if (element.thesaurusData!=null && element.thesaurusData.id == aRecord.data.id) {
				tabExists = element;
			}
		});
		if (!tabExists) {
			var sandBoxPanel = Ext.create('GincoApp.view.SandBoxPanel');
			sandBoxPanel.thesaurusData = aRecord.data;
			var tab = topTabs.add(sandBoxPanel);
			topTabs.setActiveTab(tab);
			tab.show();
		}
		else {
			topTabs.setActiveTab(tabExists);
		}
	},
	openThesaurusTab : function(aRecord) {
		var topTabs = Ext.ComponentQuery.query('topTabs')[0];
		var thesaurusTabs = Ext.ComponentQuery.query('topTabs thesaurusPanel');
		var tabExists = false;
		Ext.Array.each(thesaurusTabs,function(element, index, array) {
			if (element.thesaurusData!=null && element.thesaurusData.id == aRecord.data.id) {
				tabExists = element;
			}
		});

		if (!tabExists) {
			var ThesaurusPanel = Ext.create('GincoApp.view.ThesaurusPanel');
			ThesaurusPanel.thesaurusData = aRecord.data;
			var tab = topTabs.add(ThesaurusPanel);
			topTabs.setActiveTab(tab);
			tab.show();
		} else {
			topTabs.setActiveTab(tabExists);
		}
	},
	onEnterKey : function(theTree) {
		var node = theTree.getSelectionModel().getSelection();
		if (node.length > 0) {
			this.onNodeDblClick(theTree, node[0]);
		}

	},
	onTreeRender : function(theTree) {
		this.loadTreeView();

	},
	loadTreeView : function() {
		var me = this;
		var MainTreeStore = this.getMainTreeStoreStore();
		if (MainTreeStore.isLoading()==false) {
			MainTreeStore.load({
				callback: function (theStore, aOperation){
					if (aOperation.success==false)
					{
						Thesaurus.ext.utils.msg(me.xProblemLabel,
								me.xProblemLoadMsg+ " : "+ aOperation.error.statusText);
					} else{
						this.getRootNode().expand();
					}
					
			        
			    }
			});
		}
	},
	onRefreshBtnClick : function() {
		this.loadTreeView();
	},
	onTreeLoad : function()
	{
		
		var theTree = Ext.ComponentQuery.query('#mainTreeView')[0];
		
		theTree.getView().focus();
		try {
			theTree.getSelectionModel().select(0);
		} catch(e) {}
		
		var me = this;
		this.nav = new Ext.util.KeyNav({
			target : theTree.getEl(),

			"enter" : function() {
				me.onEnterKey(theTree);
			},
			scope : me
		});
	},
	onRefreshTreeEvent : function()
	{
		Ext.log({},'onRefreshTreeEvent');
		this.loadTreeView();
	},
	init : function(application) {
		 this.application.on({
			 	thesaurusupdated: this.onRefreshTreeEvent,
			 	conceptupdated: this.onRefreshTreeEvent,
		        scope: this
		 });
		this.control({
			"#mainTreeView" : {
				beforeitemdblclick : this.onNodeDblClick,
				render : this.onTreeRender,
				load : this.onTreeLoad
			},
			'#mainTreeView tool[type="refresh"]' : {
				click : this.onRefreshBtnClick
			}
		});
	}

});
