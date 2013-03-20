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

Ext.define('GincoApp.controller.GlobalTabPanelController', {
	extend : 'Ext.app.Controller',
	localized : true,
	models : [ 'ThesaurusModel' ],
	xSaveMsgLabel : 'Do you want to save changes?',
	xSaveMsgTitle : 'Save changes?',
	onPanelBeforeClose : function(thePanel) {
		var me = this;
		var theForm = thePanel.down('form');		
		var globalTabs = thePanel.up('topTabs');
		if (theForm) {
			var dirtyForms = [];
			var gridPanels = thePanel.query('gridpanel');
			Ext.Array.forEach(gridPanels,function (gridPanel) {
				var gridStore = gridPanel.getStore();
				if(gridStore.getModifiedRecords().length>0 || gridStore.getRemovedRecords().length>0 )
				{
					dirtyForms.push(gridPanel.up('form'));
				}
			});
			
			var theForms = thePanel.query('form');
			Ext.Array.forEach(theForms,function (aForm) {
				if (aForm.getForm().isDirty())
				{
					if (dirtyForms.indexOf(aForm)==-1)
					{
						dirtyForms.push(aForm);
					}
				}
			}
			);
			
			if (dirtyForms.length>0) {
				Ext.MessageBox.show({
					title : me.xSaveMsgTitle,
					msg : me.xSaveMsgLabel,
					buttons : Ext.MessageBox.YESNOCANCEL,
					fn : function(buttonId) {
						switch (buttonId) {
						case 'no':
							globalTabs.remove(thePanel);
							break; // manually removes tab from tab panel
						case 'yes':
							for (var i=0;i<dirtyForms.length;i++)
							{
								var dirtyForm = dirtyForms[i];
								var saveButton = dirtyForm.down('button[cls=save]');
								if (saveButton != null) {
									if (i==dirtyForms.length-1) {
										saveButton.fireEvent('click', saveButton, function(){
											globalTabs.remove(thePanel);
										});
									} else
									{
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
	onTabChange : function ( tabPanel, tab, oldCard, eOpts )
	{
		// History handling
		var tabs = [],
        oldToken, newToken;
		var tokenDelimiter = ':';
		if (tab.thesaurusData && tab.thesaurusData.id) {
			tabs.push(tab.id);
			tabs.push(tab.getXType());
			tabs.push(encodeURIComponent(tab.thesaurusData.id));
		    newToken = tabs.reverse().join(tokenDelimiter);
		    
		    oldToken = Ext.History.getToken();
		   
		    if (oldToken === null || oldToken.search(newToken) === -1) {
		        Ext.History.add(newToken);
		    }
		}
	},
	onTabsAfterRender : function (tabPanel)
	{
		var token, parts,length, tokenDelimiter = ':';
		Ext.History.on('change', function(aToken) {
            var parts, length;
            if (aToken) {
            	console.log("History change "+aToken );
                parts = aToken.split(tokenDelimiter);
                length = parts.length;
                // setActiveTab in all nested tabs
               	tabPanel.setActiveTab(Ext.getCmp(parts[length-1]));
                
            }
        });
		token = window.location.hash.substr(1);
		if (token!="") {
			console.log("Load hash "+token );
			parts = token.split(tokenDelimiter);
            length = parts.length;
		}
	},
	openConceptTab : function(tabPanel, aThesaurusId, aConceptId)
	{
		var conceptTabs = Ext.ComponentQuery.query('topTabs conceptPanel');
		var tabExists = false;
		Ext.Array
				.each(
						conceptTabs,
						function(element, index, array) {
							if (element.conceptId != null
									&& aConceptId
											.indexOf(
													element.conceptId,
													aConceptId.length
															- element.conceptId.length) !== -1) {
								tabExists = element;
							}
						});
		if (!tabExists) {
			var model = this.getThesaurusModelModel();
			model.load(aThesaurusId, {
				success : function(aModel) {

					var conceptPanel = Ext.create(
							'GincoApp.view.ConceptPanel', {
								thesaurusData : aModel,
								conceptId : aConceptId
							});

					var tab = tabPanel.add(conceptPanel);
					tabPanel.setActiveTab(tab);
					tab.show();
				}
			});

		} else {
			tabPanel.setActiveTab(tabExists);
		}
	},
	openTermTab : function (tabPanel, aTermId, aThesaurusData)
	{
		var termTabs = Ext.ComponentQuery.query('topTabs termPanel');
		var tabExists = false;
		Ext.Array.each(termTabs, function(element, index, array) {
			if (element.termId != null && element.termId == aTermId) {
				tabExists = element;
			}
		});
		if (!tabExists) {
			var TermPanel = Ext.create('GincoApp.view.TermPanel');
			TermPanel.thesaurusData = aThesaurusData;
			TermPanel.termId = aTermId;
			var tab = tabPanel.add(TermPanel);
			tabPanel.setActiveTab(tab);
			tab.show();
		} else {
			tabPanel.setActiveTab(tabExists);
		}
	},
	openArrayTab : function (tabPanel, aThesaurusId, aArrayId)
	{
		var arrayTabsTabs = Ext.ComponentQuery
				.query('topTabs conceptArrayPanel');
		var tabExists = false;
		Ext.Array.each(arrayTabsTabs, function(element, index, array) {
			if (element.conceptArrayId != null
					&& element.conceptArrayId == aArrayId) {
				tabExists = element;
			}
		});

		if (!tabExists) {
			var model = this.getThesaurusModelModel();
			model.load(aThesaurusId, {
				success : function(aModel) {
					var arrayPanel = Ext.create(
							'GincoApp.view.ConceptArrayPanel', {
								thesaurusData : aModel.data,
								conceptArrayId : aArrayId
							});

					var tab = tabPanel.add(arrayPanel);
					tabPanel.setActiveTab(tab);
					tab.show();
				}
			});
		} else {
			tabPanel.setActiveTab(tabExists);
		}
	},
	openGroupTab: function(tabPanel, aThesaurusId, aGroupId)
	{
		var groupTabsTabs = Ext.ComponentQuery
				.query('topTabs conceptGroupPanel');
		var tabExists = false;
		Ext.Array.each(groupTabsTabs, function(element, index, array) {
			if (element.conceptGroupId != null
					&& element.conceptGroupId == aGroupId) {
				tabExists = element;
			}
		});

		if (!tabExists) {
			var model = this.getThesaurusModelModel();
			model.load(aThesaurusId, {
				success : function(aModel) {
					var groupPanel = Ext.create(
							'GincoApp.view.ConceptGroupPanel', {
								thesaurusData : aModel.data,
								conceptGroupId : aGroupId
							});

					var tab = tabPanel.add(groupPanel);
					tabPanel.setActiveTab(tab);
					tab.show();
				}
			});
		} else {
			tabPanel.setActiveTab(tabExists);
		}
	},
	openSandboxTab : function (tabPanel, aThesaurusId)
	{
		var sandBoxTabs = Ext.ComponentQuery.query('topTabs sandboxPanel');
		var tabExists = false;
		Ext.Array.each(sandBoxTabs,function(element, index, array) {
			if (element.thesaurusData!=null && element.thesaurusData.id == aThesaurusId) {
				tabExists = element;
			}
		});
		if (!tabExists) {
			var model = this.getThesaurusModelModel();
			model.load(aThesaurusId, {
				success : function(aModel) {
					var sandBoxPanel = Ext.create('GincoApp.view.SandBoxPanel');
					sandBoxPanel.thesaurusData = aModel.data;
					var tab = tabPanel.add(sandBoxPanel);
					tabPanel.setActiveTab(tab);
					tab.show();
				}
			});
		} else {
			tabPanel.setActiveTab(tabExists);
		}
	},
	openThesaurusTab : function (tabPanel, aThesaurusId)
	{
		var topTabs = Ext.ComponentQuery.query('topTabs')[0];
		var thesaurusTabs = Ext.ComponentQuery.query('topTabs thesaurusPanel');
		var tabExists = false;
		Ext.Array.each(thesaurusTabs,function(element, index, array) {
			if (element.thesaurusData!=null && element.thesaurusData.id == aThesaurusId) {
				tabExists = element;
			}
		});
		if (!tabExists) {
			var model = this.getThesaurusModelModel();
			model.load(aThesaurusId, {
				success : function(aModel) {
					var ThesaurusPanel = Ext.create('GincoApp.view.ThesaurusPanel');
					ThesaurusPanel.thesaurusData = aModel.data;
					var tab = topTabs.add(ThesaurusPanel);
					topTabs.setActiveTab(tab);
					tab.show();
				}
			});
		} else {
			topTabs.setActiveTab(tabExists);
		}
	},
	init : function(application) {
		this.control({
			'thesaurusPanel' : {
				beforeclose : this.onPanelBeforeClose,
				
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
			'topTabs' :  {
				tabchange : this.onTabChange,
				afterrender: this.onTabsAfterRender,
				openconcepttab : this.openConceptTab,
				opentermtab : this.openTermTab,
				opengrouptab : this.openGroupTab,
				openarraytab : this.openArrayTab,
				opensandboxtab : this.openSandboxTab,
				openthesaurustab : this.openThesaurusTab
			}
		});
	}
});