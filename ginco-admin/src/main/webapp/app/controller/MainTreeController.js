Ext.define('GincoApp.controller.MainTreeController', {
	extend : 'Ext.app.Controller',

	views : [ 'LeftPanel' ],
	stores : [ 'MainTreeStore' ],

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
		var sandBoxPanel = Ext.create('GincoApp.view.SandBoxPanel');
		sandBoxPanel.thesaurusData = aRecord.data;
		var tab = topTabs.add(sandBoxPanel);
		topTabs.setActiveTab(tab);
		tab.show();
	},
	openThesaurusTab : function(aRecord) {
		var topTabs = Ext.ComponentQuery.query('topTabs')[0];
		var tabExists = false;
		topTabs.items.each(function(element, index, array) {
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
			this.openThesaurusTab(node[0]);
		}

	},
	onTreeRender : function(theTree) {
		var me = this;
		this.nav = new Ext.util.KeyNav({
			target : theTree.getEl(),

			"enter" : function() {
				me.onEnterKey(theTree);
			},
			scope : me
		});

	},
	onRefreshBtnClick : function() {
		var MainTreeStore = this.getMainTreeStoreStore();
		MainTreeStore.load();
	},
	init : function(application) {
		this.control({
			"#mainTreeView" : {
				beforeitemdblclick : this.onNodeDblClick,
				afterrender : this.onTreeRender
			},
			'#mainTreeView tool[type="refresh"]' : {
				click : this.onRefreshBtnClick
			}
		});
	}

});
