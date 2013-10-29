/**
 * Copyright or © or Copr. Ministère Français chargé de la Culture et de la
 * Communication (2013) <p/> contact.gincoculture_at_gouv.fr <p/> This software
 * is a computer program whose purpose is to provide a thesaurus management
 * solution. <p/> This software is governed by the CeCILL license under French
 * law and abiding by the rules of distribution of free software. You can use,
 * modify and/ or redistribute the software under the terms of the CeCILL
 * license as circulated by CEA, CNRS and INRIA at the following URL
 * "http://www.cecill.info". <p/> As a counterpart to the access to the source
 * code and rights to copy, modify and redistribute granted by the license,
 * users are provided only with a limited warranty and the software's author,
 * the holder of the economic rights, and the successive licensors have only
 * limited liability. <p/> In this respect, the user's attention is drawn to the
 * risks associated with loading, using, modifying and/or developing or
 * reproducing the software by the user in light of its specific status of free
 * software, that may mean that it is complicated to manipulate, and that also
 * therefore means that it is reserved for developers and experienced
 * professionals having in-depth computer knowledge. Users are therefore
 * encouraged to load and test the software's suitability as regards their
 * requirements in conditions enabling the security of their systemsand/or data
 * to be ensured and, more generally, to use and operate it in the same
 * conditions as regards security. <p/> The fact that you are presently reading
 * this means that you have had knowledge of the CeCILL license and that you
 * accept its terms.
 */

Ext.define('GincoApp.controller.GlobalThesaurusTabsPanelController', {
	extend : 'Ext.app.Controller',
	localized : false,
	models : [ 'ThesaurusModel' ],

	onNewThesaurus : function(theTabPanel) {
		var newThesaurusPanel = Ext.create('GincoApp.view.ThesaurusTabPanel');
		theTabPanel.add(newThesaurusPanel);
		theTabPanel.setActiveTab(newThesaurusPanel);
		newThesaurusPanel.fireEvent('newthesaurus', newThesaurusPanel);
	},

	openThesaurusTab : function(tabPanel, aThesaurusId, aItemId,
			openThesaurusForm, aEventName) {
		var thesaurusTabs = Ext.ComponentQuery
				.query('thesaurusTabs thesaurusTabPanel');
		var tabExists = false;
		Ext.Array.each(thesaurusTabs, function(element, index, array) {
			if (element.thesaurusData != null
					&& element.thesaurusData.id == aThesaurusId) {
				tabExists = element;
			}
		});
		if (!tabExists) {
			var model = this.getThesaurusModelModel();
			model.load(aThesaurusId, {
				success : function(aModel) {
					var ThesaurusPanel = Ext
							.create('GincoApp.view.ThesaurusTabPanel');
					ThesaurusPanel.thesaurusData = aModel.data;
					var tab = tabPanel.add(ThesaurusPanel);
					tabPanel.setActiveTab(tab);
					tab.show();
					if (openThesaurusForm == true)
						tab.fireEvent('openthesaurusform', tab);
					else {
						tab.fireEvent(aEventName, tab, aItemId);
					}
				}
			});
		} else {
			tabPanel.setActiveTab(tabExists);
			if (openThesaurusForm == true)
				tabExists.fireEvent('openthesaurusform', tabExists);
			else
				tabExists.fireEvent(aEventName, tabExists, aItemId);
		}
	},
	openConceptTab : function(tabPanel, aThesaurusId, aConceptId) {
		this.openThesaurusTab(tabPanel, aThesaurusId, aConceptId, false,
				"openconcepttab");
	},
	openSandboxTab : function(tabPanel, aThesaurusId) {
		this.openThesaurusTab(tabPanel, aThesaurusId, null, false,
				"opensandboxtab");
	},
	openTermTab : function(tabPanel, aThesaurusId, aTermId) {
		this.openThesaurusTab(tabPanel, aThesaurusId, aTermId, false,
				"opentermtab");
	},
	openGroupTab : function(tabPanel, aThesaurusId, aGroupId) {
		this.openThesaurusTab(tabPanel, aThesaurusId, aGroupId, false,
				"opengrouptab");
	},
	openArrayTab : function(tabPanel, aThesaurusId, aArrayId) {
		this.openThesaurusTab(tabPanel, aThesaurusId, aArrayId, false,
				"openarraytab");
	},
	openComplexConceptsTab : function(tabPanel, aThesaurusId) {
		this.openThesaurusTab(tabPanel, aThesaurusId, null, false,
				"opencomplexconceptstab");
	},
	openComplexConceptTab : function (tabPanel, aThesaurusId, aConceptId) {
		this.openThesaurusTab(tabPanel, aThesaurusId, aConceptId, false,
		"opencomplexconcepttab");
	},
	onSearchQuery : function(tabPanel, aSearchQuery) 
	{
		var searchPanel = Ext.create("GincoApp.view.SearchPanel");
		searchPanel.searchQuery = aSearchQuery;
		var tab = tabPanel.add(searchPanel);
		tabPanel.setActiveTab(tab);
		tab.show();
	},
	onTabChange : function ( tabPanel, tab, oldCard, eOpts )
	{
		var thesaurusTabPanel = tab.down("#thesaurusItemsTabPanel");
		if (thesaurusTabPanel!=null) {
			var activeTab = thesaurusTabPanel.getActiveTab();
			if (activeTab!=null) {
				thesaurusTabPanel.fireEvent("tabchange", tab,activeTab);
			}
		}
	},
	onTabAdd : function ( tabPanel, component, index, eOpts) {
		if (component.thesaurusData) {
			var thesaurusId = component.thesaurusData.id;
			Thesaurus.ext.utils.restrictRoles(component, thesaurusId);
		}
	},
	
	onPanelBeforeClose : function(thePanel) {
		
		thePanel.fireEvent('closeall',thePanel);
		return false;
	},

	init : function(application) {
		this.application.on({
			// userinfoloaded: this.onUserInfoLoaded,
			scope : this
		});
		this.control({
			'thesaurusTabPanel' : {
				beforeclose : this.onPanelBeforeClose
			},
			'thesaurusTabs' : {
				newthesaurus : this.onNewThesaurus,
				openthesaurustab : this.openThesaurusTab,
				openconcepttab : this.openConceptTab,
				opensandboxtab : this.openSandboxTab,
				opentermtab : this.openTermTab,
				opengrouptab : this.openGroupTab,
				openarraytab : this.openArrayTab,
				opencomplexconceptstab : this.openComplexConceptsTab,
				opencomplexconcepttab: this.openComplexConceptTab,
				searchquery : this.onSearchQuery,
				tabchange : this.onTabChange,
				add : this.onTabAdd
			}

		});
	}
});