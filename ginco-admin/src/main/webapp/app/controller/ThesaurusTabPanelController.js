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
    xImportTermsTitle : "Import terms",
    xImportBranchTitle : "Import branch",
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
			itemTabPanel.setActiveTab(newThesaurusFormPanel);
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
			thePanel.down('#importSandbox').setDisabled(false);
			thePanel.down('#importBranch').setDisabled(false);
		}
	},
	openConceptTab : function(tabPanel, aConceptId, aSubTab)
	{
		this.openGenericTab(tabPanel, "conceptPanel","GincoApp.view.ConceptPanel", {gincoId : aConceptId}, aSubTab);
	},
	openChildConceptTab : function(tabPanel, aParentConceptId)
	{
		this.openGenericTab(tabPanel, "conceptPanel","GincoApp.view.ConceptPanel", {gincoId : null, parentConceptId : aParentConceptId, displayPrefTermCreation: true});
	},
	openTermTab : function(tabPanel, aTermId, aSubTab)
	{
		this.openGenericTab(tabPanel, "termPanel","GincoApp.view.TermPanel", {gincoId : aTermId}, aSubTab);
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
	openGenericTab : function (tabPanel, aXtype, aClass, panelOptions, aSubTab)
	{
		var existingTabs = tabPanel.query(aXtype);
		var tab = null, tabExists = false;
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
			if (existingTabs.length>0 && !existingTabs[0].multiInstance) {
				tabExists = existingTabs[0];
			}
		}
		if (!tabExists) {
			var newPanel = Ext.create(
					aClass, panelOptions
					);
			tab = itemTabPanel.add(newPanel);
			itemTabPanel.setActiveTab(tab);
			tab.show();
		} else {
			itemTabPanel.setActiveTab(tabExists);
			tab = tabExists;
		}
		if (Ext.isString(aSubTab) && aSubTab != "") {
			var subTab = tab.down(aSubTab);
			subTab.setDisabled(false);
			subTab.up('tabpanel').setActiveTab(subTab);
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
		var groupOptions = {gincoId : null, isDynamic : false};
		this.openGenericTab(thePanel,"conceptGroupPanel","GincoApp.view.ConceptGroupPanel", groupOptions);
	},

	onNewConceptDynamicGroupBtnClick : function(theButton) {
		var thePanel = theButton.up('thesaurusTabPanel');
		var groupOptions = {gincoId : null, isDynamic : true};
		this.openGenericTab(thePanel,"conceptGroupPanel","GincoApp.view.ConceptGroupPanel", groupOptions);
	},

	onNewComplexConceptBtnClick : function (theButton) {
		var thePanel = theButton.up('thesaurusTabPanel');
		this.openGenericTab(thePanel, "complexconceptPanel","GincoApp.view.ComplexConceptPanel", {gincoId : null});
	},

	exportHierarchical : function(theButton) {
        var thePanel = theButton.up('thesaurusTabPanel');
        var url = "services/ui/exportservice/getHierarchical?thesaurusId="
            + encodeURIComponent(thePanel.thesaurusData.id);
        window.open(url);
    },

    exportJournal : function(theButton) {
    	var thePanel = theButton.up('thesaurusTabPanel');
        var url = "services/ui/journalservice/exportLogJournal?thesaurusId="
            + encodeURIComponent(thePanel.thesaurusData.id);
        window.open(url);
    },

    exportAlphabetical : function(theButton) {
    	var thePanel = theButton.up('thesaurusTabPanel');
        var url = "services/ui/exportservice/getAlphabetical?thesaurusId="
            + encodeURIComponent(thePanel.thesaurusData.id);
        window.open(url);
    },

    exportGinco : function(theButton) {
    	var thePanel = theButton.up('thesaurusTabPanel');
        var url = "services/ui/exportservice/getGincoThesaurusExport?thesaurusId="
            + encodeURIComponent(thePanel.thesaurusData.id);
        window.open(url);
    },

    exportSKOS : function(theButton) {
    	var thePanel = theButton.up('thesaurusTabPanel');
        var url = "services/ui/exportservice/getSKOS?thesaurusId="
            + encodeURIComponent(thePanel.thesaurusData.id);
        window.open(url);
    },

    importSandboxClick : function(theButton) {
    	var me = this;
    	var thePanel = theButton.up('thesaurusTabPanel');
    	Ext.create('GincoApp.view.ImportWin', {importType : 'txt', thesaurusData : thePanel.thesaurusData, xTitleLabel: '<h1>'+me.xImportTermsTitle+'</h1>'});
    },
    importBranch : function(theButton) {
    	var me = this;
    	var thePanel = theButton.up('thesaurusTabPanel');
    	Ext.create('GincoApp.view.ImportWin', {importType: 'gincoBranchXml', thesaurusData:thePanel.thesaurusData, xTitleLabel: '<h1>' + me.xImportBranchTitle + '</h1>'});
	},

	onPanelBeforeClose : function(thePanel) {
		var me = this;
		if (thePanel.forceClose && thePanel.forceClose==true)
			return true;
		var theForm = thePanel.down('form');
		var globalTabs = thePanel.up('#thesaurusItemsTabPanel');
		if (theForm) {
			var dirtyForms = [];
			var gridPanels = thePanel.query('gridpanel');
			Ext.Array.forEach(gridPanels,function (gridPanel) {
				var gridStore = gridPanel.getStore();
				if(gridStore.getModifiedRecords().length>0 || gridStore.getRemovedRecords().length>0 )
				{
                    var curForm;
					if (gridPanel.up('form') != null){
                        curForm = gridPanel.up('form');
					} else {
						if (gridPanel.up('panel').down('form') != null){
                            curForm = gridPanel.up('panel').down('form');
						} else {
                            curForm = gridPanel
						}
					}
                    me.pushToDirtyForms(dirtyForms,curForm);
				}
			});

			var theForms = thePanel.query('form');
			Ext.Array.forEach(theForms,function (aForm) {
				if (aForm.getForm().isDirty())
				{
                    me.pushToDirtyForms(dirtyForms,aForm);
				}
			});

			if (dirtyForms.length>0) {
				if  (Thesaurus.ext.utils.userInfo != null ) {
					var thesData = thePanel.up('thesaurusTabPanel').thesaurusData;
					if (thesData) {
						var userRoles = Thesaurus.ext.utils.getUserRoles(thesData.id);
						if (theForm.checkRoles(userRoles) == true) {
							return true;
						}
					}
				}
				Ext.MessageBox.show({
					title : me.xSaveMsgTitle,
					msg : me.xSaveMsgLabel,
					buttons : Ext.MessageBox.YESNOCANCEL,
					fn : function(buttonId) {
						switch (buttonId) {
						case 'no':
							globalTabs.remove(thePanel);
							globalTabs.up().fireEvent('subpanelclosed',globalTabs.up());
							break; // manually removes tab from tab panel
						case 'yes':
							for (var i=0;i<dirtyForms.length;i++)
							{
								var dirtyForm = dirtyForms[i];
								var saveButton = dirtyForm.down('button[cls=save]');
								if (saveButton != null){
									if (i==dirtyForms.length-1) {
										saveButton.fireEvent('click', saveButton, function(){
											globalTabs.remove(thePanel);
											globalTabs.up().fireEvent('subpanelclosed',globalTabs.up());
										});
									} else	{
										saveButton.fireEvent('click', saveButton);
									}
								}
							}


							break;
						case 'cancel':
							break; // leave blank if no action required on
						// cancel
						}
					},
					scope : this
				});

				return false; /*
								 * returning false to beforeclose cancels the
								 * close event
								 */
			}
		}
		return true;
	},

    pushToDirtyForms : function(dirtyForms,formToPush)
    {
        if (Ext.Array.indexOf(dirtyForms, formToPush)==-1)
        {
            dirtyForms.push(formToPush);
        }
    },
	onThesaurusUpdated : function(thePanel, thesaurusData)
	{
		thePanel.thesaurusData = thesaurusData;
		this.onLoad(thePanel);
	},
	onTabChange : function ( tabPanel, tab, oldCard, eOpts )
	{
		var theTree = Ext.ComponentQuery.query('#mainTreeView')[0];
		theTree.fireEvent('tabchange', tab);
		if (tab.tab)
		{
			tab.tab.focus();
		}
		// History handling
		var tabs = [],
        oldToken, newToken;
		var tokenDelimiter = ';';
		if (tab.thesaurusData && tab.thesaurusData.id) {
			tabs.push(tab.id);
			tabs.push(tab.getXType());
			if (tab.gincoId && tab.gincoId!='')
			{
				tabs.push(encodeURIComponent(tab.gincoId));
			}
			tabs.push(encodeURIComponent(tab.thesaurusData.id));
		    newToken = tabs.reverse().join(tokenDelimiter);

		    oldToken = Ext.History.getToken();

		    if (oldToken === null || oldToken.search(newToken) === -1) {
		        Ext.History.add(newToken);
		    }
		}
	},
	onTabAdd : function ( tabPanel, component, index, eOpts) {
		if (tabPanel.ownerCt.thesaurusData) {
			var thesaurusId = tabPanel.ownerCt.thesaurusData.id;
			Thesaurus.ext.utils.restrictRoles(component, thesaurusId);
		}
		tabPanel.ownerCt.down('#closeAllBtn').setDisabled(false);
	},
	onCloseAllBtn : function (theBtn) {
		theBtn.up('thesaurusTabPanel').down('#thesaurusItemsTabPanel').items.each(
			function(c){
				c.close();
			}
		);
	},
	onCloseAll : function (thePanel) {
		var nbPanelToClose = 0;
		thePanel.isClosing = true;
		thePanel.down('#thesaurusItemsTabPanel').items.each(
			function(c){
				nbPanelToClose++;
				c.close();
			}
		);
		if (nbPanelToClose==0)
		{
			thePanel.fireEvent('alltabsclosed',thePanel);
		}
	},
	onSubPanelClose : function (thePanel)
	{
		var nbPanel = thePanel.down('#thesaurusItemsTabPanel').items.getCount();
		if (nbPanel <= 1 && thePanel.isClosing == true)
		{
			thePanel.fireEvent('alltabsclosed',thePanel);
		}
		if (nbPanel <= 1 && thePanel.isClosing == false)
		{
			thePanel.down('#closeAllBtn').setDisabled(true);
		}
		
	},
	onPanelClose : function (thePanel)
	{
		thePanel.up('thesaurusTabPanel').fireEvent('subpanelclosed',thePanel.up('thesaurusTabPanel'));
	},
	onAllTabsClosed : function (thePanel)
	{
		thePanel.up().remove(thePanel);
	},
	onSearchInThesaurusTrigger : function(theTrigger) {
		var thePanel = theTrigger.up('thesaurusTabPanel');
		var thesaurusTabs = Ext.ComponentQuery.query('thesaurusTabs')[0];
		thesaurusTabs.fireEvent('searchquery',thesaurusTabs,theTrigger.getValue(), thePanel.thesaurusData.id);
	},
	onSearchInThesaurusTriggerKey : function (theTrigger,e )
	{
		if (e.getKey() == e.ENTER) {
			this.onSearchInThesaurusTrigger(theTrigger);
        }
	},
	init : function(application) {
		this.application.on({
	        scope: this
	 });
		this.control({
			'#thesaurusItemsTabPanel' : {
				tabchange : this.onTabChange,
				add : this.onTabAdd
			},
			'#thesaurusItemsTabPanel panel' : {
				close : this.onPanelClose
			},
			'thesaurusPanel' : {
				beforeclose : this.onPanelBeforeClose
			},
			'termPanel' : {
				beforeclose : this.onPanelBeforeClose
			},
			'conceptPanel' : {
				beforeclose : this.onPanelBeforeClose
			},
			'conceptArrayPanel' : {
				beforeclose : this.onPanelBeforeClose
			},
			'conceptGroupPanel' : {
				beforeclose : this.onPanelBeforeClose
			},
			'complexconceptPanel' : {
				beforeclose : this.onPanelBeforeClose
			},
			'thesaurusTabPanel' : {
				subpanelclosed : this.onSubPanelClose,
				alltabsclosed : this.onAllTabsClosed,
				closeall : this.onCloseAll,
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
				opencomplexconcepttab: this.openComplexConceptTab,
				thesaurusupdated : this.onThesaurusUpdated
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
            "thesaurusTabPanel #newConceptDynamicGroupBtn" : {
            	click : this.onNewConceptDynamicGroupBtnClick
            },
			"thesaurusTabPanel #editJournal" : {
                click : this.exportJournal
            },
            "thesaurusTabPanel #importSandbox" : {
                click : this.importSandboxClick
            },
            "thesaurusTabPanel #importBranch" : {
                click : this.importBranch
            },
            "thesaurusTabPanel #closeAllBtn" : {
            	click : this.onCloseAllBtn
            },
			'#searchInThesaurusBtn' : {
				trigger : this.onSearchInThesaurusTrigger,
				specialkey : this.onSearchInThesaurusTriggerKey
			}
		});
	}
});