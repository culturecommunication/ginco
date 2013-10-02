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

Ext.define('GincoApp.controller.ThesaurusTabPanelController', {
	extend : 'Ext.app.Controller',
	localized : true,
	models : [ 'ThesaurusModel' ],
	xSaveMsgLabel : 'Do you want to save changes?',
	xSaveMsgTitle : 'Save changes?',
	tabs : {
		conceptPanel : 'GincoApp.view.ConceptPanel',
		termPanel : 'GincoApp.view.TermPanel',
		conceptGroupPanel: 'GincoApp.view.ConceptGroupPanel',
		conceptArrayPanel: 'GincoApp.view.ConceptArrayPanel',
		sandboxPanel : 'GincoApp.view.SandBoxPanel',
		complexconceptsPanel: 'GincoApp.view.ComplexConceptsPanel'
	},
	
	onNewThesaurus : function (thePanel) {
		var newThesaurusFormPanel = Ext.create('GincoApp.view.ThesaurusPanel');
		var itemTabPanel = thePanel.down('#thesaurusItemsTabPanel');
		itemTabPanel.add(newThesaurusFormPanel);
	},
	openThesaurusForm : function (thePanel) {
		// We look for existing ThesaurusPanel
		var existingPanel = thePanel.down('thesaurusPanel');
		var itemTabPanel = thePanel.down('#thesaurusItemsTabPanel');
		if (existingPanel!=null)
		{
			itemTabPanel.setActiveTab(existingPanel);
		} else {
			var newThesaurusFormPanel = Ext.create('GincoApp.view.ThesaurusPanel');
			var itemTabPanel = thePanel.down('#thesaurusItemsTabPanel');
			itemTabPanel.add(newThesaurusFormPanel);
		}
	},
	onLoad : function(thePanel) {
		if (thePanel.thesaurusData!=null)
		{
			thePanel.setTitle(thePanel.thesaurusData.title);
			thePanel.down('#newBtnMenu').setDisabled(false);
			thePanel.down('#exportsBtnMenu').setDisabled(false);
			thePanel.down('#exportHierarchical').setDisabled(false);
			thePanel.down('#exportSKOS').setDisabled(false);
			thePanel.down('#exportAlphabetical').setDisabled(false);
			thePanel.down('#exportGinco').setDisabled(false);
			thePanel.down('#journalBtnMenu').setDisabled(false);
			thePanel.down('#editJournal').setDisabled(false);
			thePanel.down('#publishThesaurus').setDisabled(false);
			thePanel.down('#importSandbox').setDisabled(false);
			thePanel.down('#importBranch').setDisabled(false);
		}
	},
	openConceptTab : function(tabPanel, aConceptId)
	{
		this.openGenericTab(tabPanel, "conceptPanel","GincoApp.view.ConceptPanel", {gincoId : aConceptId});
	},
	openChildConceptTab : function(tabPanel, aParentConceptId)
	{
		this.openGenericTab(tabPanel, "conceptPanel","GincoApp.view.ConceptPanel", {gincoId : null, parentConceptId : aParentConceptId, displayPrefTermCreation: true});
	},
	openTermTab : function(tabPanel, aTermId)
	{
		this.openGenericTab(tabPanel, "termPanel","GincoApp.view.TermPanel", {gincoId : aTermId});
	},
	openSandboxTab : function (tabPanel)
	{
		this.openGenericTab(tabPanel, "sandboxPanel","GincoApp.view.SandBoxPanel", {gincoId : null});
	}, 
	openGroupTab : function (tabPanel, aGroupId)
	{
		this.openGenericTab(tabPanel, "conceptGroupPanel","GincoApp.view.ConceptGroupPanel", {gincoId : aGroupId});
	}, 
	openArrayTab : function (tabPanel, aArrayId)
	{
		this.openGenericTab(tabPanel, "conceptArrayPanel","GincoApp.view.ConceptArrayPanel", {gincoId : aArrayId});
	}, 
	openComplexConceptsTab : function (tabPanel)
	{
		this.openGenericTab(tabPanel, "complexconceptsPanel","GincoApp.view.ComplexConceptsPanel", {gincoId : null});
	}, 
	openComplexConceptTab: function (tabPanel, aConceptId)
	{
		this.openGenericTab(tabPanel, "complexconceptPanel","GincoApp.view.ComplexConceptPanel", {gincoId : aConceptId});
	},
	openGenericTab : function (tabPanel, aXtype, aClass, panelOptions)
	{
		var existingTabs = tabPanel.query(aXtype);
		var tabExists = false;
		var itemTabPanel = tabPanel.down('#thesaurusItemsTabPanel');
		if (panelOptions['gincoId']!=null) {
			Ext.Array.each(existingTabs, function(element, index, array) {
				if (element.gincoId != null
						&& element.gincoId == panelOptions['gincoId']) {
					tabExists = element;
				}
			});
		} else
		{
			if (existingTabs.length>0 && !existingTabs[0].multiInstance)
				tabExists = existingTabs[0];
		}
		if (!tabExists) {
			var newPanel = Ext.create(
					aClass, panelOptions
					);
			var tab = itemTabPanel.add(newPanel);
			itemTabPanel.setActiveTab(tab);
			tab.show();
		} else {
			itemTabPanel.setActiveTab(tabExists);
		}
	},
	onNewConceptAndTermBtnClick : function(theButton, e, options) {
		var thePanel = theButton.up('thesaurusTabPanel');
		var conceptOptions = {gincoId : null,displayPrefTermCreation: true};
		this.openGenericTab(thePanel, "conceptPanel","GincoApp.view.ConceptPanel", conceptOptions);
	},
	onNewTermBtnClick : function(theButton, e, options) {
		var thePanel = theButton.up('thesaurusTabPanel');
		this.openGenericTab(thePanel,"termPanel","GincoApp.view.TermPanel", {gincoId : null});
	},
	onNewConceptBtnClick : function (theButton) {
		var thePanel = theButton.up('thesaurusTabPanel');
		this.openGenericTab(thePanel,"conceptPanel","GincoApp.view.ConceptPanel", {gincoId : null});
	},
	onNewConceptArrayBtnClick : function(theButton) {
		var thePanel = theButton.up('thesaurusTabPanel');
		this.openGenericTab(thePanel,"conceptArrayPanel","GincoApp.view.ConceptArrayPanel", {gincoId : null});
	},

	onNewConceptGroupBtnClick : function(theButton) {
		var thePanel = theButton.up('thesaurusTabPanel');
		this.openGenericTab(thePanel,"conceptGroupPanel","GincoApp.view.ConceptGroupPanel", {gincoId : null});
	},

	onNewComplexConceptBtnClick : function (theButton) {
		var thePanel = theButton.up('thesaurusTabPanel');
		this.openGenericTab(thePanel, "complexconceptPanel","GincoApp.view.ComplexConceptPanel", {gincoId : null});
	},
	
	init : function(application) {
		this.application.on({
	        scope: this
	 });
		this.control({
			'thesaurusTabPanel' : {
				afterrender : this.onLoad,
				newthesaurus : this.onNewThesaurus,
				openthesaurusform : this.openThesaurusForm,
				openconcepttab : this.openConceptTab,
				openchildconcepttab : this.openChildConceptTab,
				opensandboxtab : this.openSandboxTab,
				opentermtab : this.openTermTab,
				opengrouptab : this.openGroupTab,
				openarraytab : this.openArrayTab,
				opencomplexconceptstab : this.openComplexConceptsTab,
				opencomplexconcepttab: this.openComplexConceptTab
			},
			"thesaurusTabPanel #newConceptAndTermBtn" : {
				click : this.onNewConceptAndTermBtnClick
			},
			"thesaurusTabPanel #newTermBtn" : {
				click : this.onNewTermBtnClick
			},
			"thesaurusTabPanel #newConceptBtn" : {
				click : this.onNewConceptBtnClick
			},
            "thesaurusTabPanel #exportHierarchical" : {
                click : this.exportHierarchical
            },
            "thesaurusTabPanel #exportAlphabetical" : {
                click : this.exportAlphabetical
            },
            "thesaurusTabPanel #exportGinco" : {
                click : this.exportGinco
            },
            "thesaurusTabPanel #exportSKOS" : {
                click : this.exportSKOS
            },
			"thesaurusTabPanel #newComplexConceptBtn" : {
				click : this.onNewComplexConceptBtnClick
			},
			"thesaurusTabPanel #newConceptArrayBtn" : {
				click : this.onNewConceptArrayBtnClick
			},
			"thesaurusTabPanel #newConceptGroupBtn" : {
				click : this.onNewConceptGroupBtnClick
			},
			"thesaurusTabPanel #editJournal" : {
                click : this.exportJournal
            },
		});
	}
});