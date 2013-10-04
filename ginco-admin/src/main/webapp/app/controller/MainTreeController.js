/**
 * Copyright or © or Copr. Ministère Français chargé de la Culture
 * et de la Communication (2013)
 * <p/>
 * contact.gincoculture_at_gouv.fr
 * <p/>
 * This software is a computer program whose purpose is to provide a thesaurus
 * management solution.
 * <p/>
 * This software is governed by the CeCILL license under French law and
 * abiding by the rules of distribution of free software. You can use,
 * modify and/ or redistribute the software under the terms of the CeCILL
 * license as circulated by CEA, CNRS and INRIA at the following URL
 * "http://www.cecill.info".
 * <p/>
 * As a counterpart to the access to the source code and rights to copy,
 * modify and redistribute granted by the license, users are provided only
 * with a limited warranty and the software's author, the holder of the
 * economic rights, and the successive licensors have only limited liability.
 * <p/>
 * In this respect, the user's attention is drawn to the risks associated
 * with loading, using, modifying and/or developing or reproducing the
 * software by the user in light of its specific status of free software,
 * that may mean that it is complicated to manipulate, and that also
 * therefore means that it is reserved for developers and experienced
 * professionals having in-depth computer knowledge. Users are therefore
 * encouraged to load and test the software's suitability as regards their
 * requirements in conditions enabling the security of their systemsand/or
 * data to be ensured and, more generally, to use and operate it in the
 * same conditions as regards security.
 * <p/>
 * The fact that you are presently reading this means that you have had
 * knowledge of the CeCILL license and that you accept its terms.
 */

Ext.define('GincoApp.controller.MainTreeController', {
	extend : 'Ext.app.Controller',
	
	views : [ 'LeftPanel' ],
	stores : [ 'MainTreeStore' ],
    models : [ 'ThesaurusModel' ],
	xProblemLabel : 'Problem',
	xProblemLoadMsg : 'Unable to load thesaurus tree',
	trackTabs : false,
	onNodeDblClick : function(tree, aRecord, item, index, e, eOpts) {
		if (aRecord.data.type == "THESAURUS") {
			this.openThesaurusTab(aRecord);
		}
		if (aRecord.data.type == "CONCEPT") {
			this.openConceptTab(aRecord);
		}	
		if (aRecord.data.type == "ARRAYS") {
			this.openTabArray(aRecord);
		}
		if (aRecord.data.type == "GROUPS") {
			this.openTabGroup(aRecord);
		}
		if (aRecord.data.type == "FOLDER"
				&& aRecord.data.id.indexOf("SANDBOX") === 0) {
			this.openSandBoxTab(aRecord.parentNode);
		}
		if (aRecord.data.type == "FOLDER"
			&& aRecord.data.id.indexOf("COMPLEXCONCEPTS") === 0) {
			this.openComplexConceptTab(aRecord.parentNode);
		}
		return false;
	},
	openConceptTab: function (aRecord) {
		var thesaurusTabs = Ext.ComponentQuery.query('thesaurusTabs')[0];
		var conceptId = aRecord.data.id.substring(aRecord.data.id.indexOf('*')+1);
		thesaurusTabs.fireEvent('openconcepttab',thesaurusTabs, aRecord.data.thesaurusId, conceptId);
	},
	openTabArray: function (aRecord) {
		var thesaurusTabs = Ext.ComponentQuery.query('thesaurusTabs')[0];
		thesaurusTabs.fireEvent('openarraytab',thesaurusTabs, aRecord.data.thesaurusId, aRecord.data.id);
	},
	openTabGroup: function (aRecord) {
		var thesaurusTabs = Ext.ComponentQuery.query('thesaurusTabs')[0];
		thesaurusTabs.fireEvent('opengrouptab',thesaurusTabs, aRecord.data.thesaurusId, aRecord.data.id);
	},
	openComplexConceptTab: function (aRecord) {
		var thesaurusTabs = Ext.ComponentQuery.query('thesaurusTabs')[0];
		thesaurusTabs.fireEvent('opencomplexconceptstab',thesaurusTabs,aRecord.data.id);
	},
    openSandBoxTab : function(aRecord) {
    	var thesaurusTabs = Ext.ComponentQuery.query('thesaurusTabs')[0];
    	thesaurusTabs.fireEvent('opensandboxtab',thesaurusTabs,aRecord.data.id);
	},
	openThesaurusTab : function(aRecord) {
		var thesaurusTabs = Ext.ComponentQuery.query('thesaurusTabs')[0];
		thesaurusTabs.fireEvent('openthesaurustab',thesaurusTabs,aRecord.data.id,null,true);
	},
	onEnterKey : function(theTree) {
		var node = theTree.getSelectionModel().getSelection();
		if (node.length > 0) {
			this.onNodeDblClick(theTree, node[0]);
		}

	},
	onTreeRender : function(theTree) {
		this.loadTreeView(theTree);
	},
	loadTreeView : function(theTree) {
		theTree.setLoading(true);
		var treeState,scrollPosition,me = this;
		if (theTree)
		{
		    scrollPosition = theTree.getEl().down('.x-grid-view').getScroll();
			treeState = theTree.getState();
		}
		var theFilterCombo = theTree.down('#authorFilter');
		theFilterCombo.getStore().load();	
		var MainTreeStore = this.getMainTreeStoreStore();
		if (MainTreeStore.isLoading()==false) {
			MainTreeStore.load({
				callback: function (theStore, aOperation){
					theTree.setLoading(false);
					if (aOperation.success==false)
					{
						Thesaurus.ext.utils.msg(me.xProblemLabel,
								me.xProblemLoadMsg+ " : "+ aOperation.error.statusText);
					} else{
						MainTreeStore.filter();
						this.getRootNode().expand();
						if (treeState)
						{
							theTree.applyState(treeState, function(){
								var task = new Ext.util.DelayedTask(function() {
								theTree.getEl().down('.x-grid-view').scrollTo('top', scrollPosition.top, false);
								});
								task.delay(50);
							});
						}
					}
					
					theTree.getView().focus();
					try {
						var selectModel = theTree.getSelectionModel();
						selectModel.select(0);
					} catch(e) {} 
			    }
			});
		}
		
	},
	onRefreshBtnClick : function(theButton) {
		var theTreeView = theButton.up("treepanel");
		this.loadTreeView(theTreeView);
	},
	onRefreshTreeEvent : function()
	{
		var theTree = Ext.ComponentQuery.query('#mainTreeView')[0];
		this.loadTreeView(theTree);
	},
	onItemSelect : function(theTree, theRecord)
	{
		theTree = Ext.ComponentQuery.query('#mainTreeView')[0];
		var selectBtn = theTree.down("#selectBtn");
		selectBtn.setDisabled(!theRecord.get("displayable"));
	}, 
	onSelectBtnClick : function()
	{
		var theTree = Ext.ComponentQuery.query('#mainTreeView')[0];
		var node = theTree.getSelectionModel().getSelection();
		if (node.length > 0) {
			this.onNodeDblClick(theTree, node[0]);
		}
	},
    onAuthorFilterSelect : function(theCombo, records) {
        var theTreeStore = theCombo.up('treepanel').getStore();
        var orgName = records[0].get('name');
        if(orgName == '-') {
        	theTreeStore.clearFilter(true);
        } else {
        	theTreeStore.setFilter('organizationName',orgName);
        	this.loadTreeView(theCombo.up('treepanel'));	
        }
 	},    
 	onPinBtnClic : function (theBtn) {
 		if (theBtn.type=="pin")
 		{
 			this.trackTabs = false;
 			theBtn.setType('unpin');
 		} else
 		{
 			this.trackTabs = true;
 			theBtn.setType('pin');
 			var globalTabs = Ext.ComponentQuery.query('thesaurusTabs')[0];
 			var activeTab = globalTabs.getActiveTab();
 			if (activeTab!=null)
 			{
 				activeTab = activeTab.down('#thesaurusItemsTabPanel').getActiveTab();
 				if (activeTab!=null)
 					this.onTabChange(activeTab);
 			}
 		}
 	},
 	onTabChange : function (theChangedPanel) {
 		if (this.trackTabs == true && theChangedPanel.trackable==true)
 		{
 			this.trackTreeSelection(theChangedPanel);
 		}
 	},
 	trackTreeSelection : function (theChangedPanel) {
 		console.log("trackTreeSelection",theChangedPanel.getNodesPath());
 		var theTree = Ext.ComponentQuery.query('#mainTreeView')[0];
 		theTree.selectPath(theChangedPanel.getNodesPath(),'id', '|');
 	},
	init : function(application) {
		// Handling application treeview refresh requests
		 this.application.on({
			 	thesaurusupdated: this.onRefreshTreeEvent,
			 	conceptupdated: this.onRefreshTreeEvent,
			 	conceptdeleted: this.onRefreshTreeEvent,
                thesaurusdeleted: this.onRefreshTreeEvent,
                conceptgroupupdated: this.onRefreshTreeEvent,
                conceptgroupdeleted: this.onRefreshTreeEvent,
                conceptarrayupdated: this.onRefreshTreeEvent,
                conceptarraydeleted: this.onRefreshTreeEvent,
		        scope: this
		 });
		this.control({
			"#mainTreeView" : {
				beforeitemdblclick : this.onNodeDblClick,
				render : this.onTreeRender,
				select : this.onItemSelect,
				tabchange : this.onTabChange
			},
			"#mainTreeView #selectBtn" : {
				click : this.onSelectBtnClick
			},
			'#mainTreeView tool[type="refresh"]' : {
				click : this.onRefreshBtnClick
			},
            '#authorFilter' : {
                select : this.onAuthorFilterSelect
            },
            '#mainTreeView #pinBtn' : {
            	click : this.onPinBtnClic
            }
		});
	}

});
