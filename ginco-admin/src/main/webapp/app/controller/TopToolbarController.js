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

Ext.define('GincoApp.controller.TopToolbarController', {
	extend : 'Ext.app.Controller',

	views : [ 'TopToolbar' ],
	
	onAproposClick : function(button, e, options) {
		Ext.create('GincoApp.view.AProposWin');
	},

	onNewThesaurusBtnClick : function(button, e, options) {
		this.createPanel('GincoApp.view.ThesaurusPanel');
	},
	createPanel : function(aType)
	{
		var aNewPanel = Ext.create(aType);
		var topTabs = Ext.ComponentQuery.query('topTabs')[0];
		var tab = topTabs.add(aNewPanel);
		topTabs.setActiveTab(tab);
		tab.show();
		return aNewPanel;
	},	
	onImportBtnClick: function(theButton) {
		Ext.create('GincoApp.view.ImportWin', {importType: 'skos', xTitleLabel: '<h1>Import SKOS</h1>'});
	},

	onImportGincoXmlBtnClick: function(theButton) {
		Ext.create('GincoApp.view.ImportWin', {importType: 'gincoxml', xTitleLabel: '<h1>Import XML Ginco</h1>'});
	},
	
	onUserInfoLoaded : function(theController) {
		
		var userNameLabel = Ext.ComponentQuery.query('#username')[0];
		userNameLabel.setText(Thesaurus.ext.utils.userInfo.data.username);	
		
		if  (Thesaurus.ext.utils.userInfo!=null && Thesaurus.ext.utils.userInfo.data.admin == false) { 
			var topToolBar = Ext.ComponentQuery.query('topToolBar')[0];
			topToolBar.restrictUI('ADMIN');
		}
	},
	
	
	onLogoutBtn : function () {
		window.location.href = "logout";
	},
	
	onSearchTrigger : function(theTrigger) {
		var searchPanel = Ext.create("GincoApp.view.SearchPanel");
		searchPanel.searchQuery = theTrigger.getValue();
		var topTabs = Ext.ComponentQuery.query('topTabs')[0];
		var tab = topTabs.add(searchPanel);
		topTabs.setActiveTab(tab);
		tab.show();
		
	},
	onSearchTriggerKey : function (theTrigger,e )
	{
		if (e.getKey() == e.ENTER) {
			this.onSearchTrigger(theTrigger);
        }
	},

	init : function(application) {
		this.application.on({
			userinfoloaded: this.onUserInfoLoaded,
	        scope: this
	 });
		this.control({
			"#aproposbtn" : {
				click : this.onAproposClick
			},
			"#newThesaurusBtn" : {
				click : this.onNewThesaurusBtnClick
			},			
			"#importBtn" : {
				click: this.onImportBtnClick
			},
			"#importGincoXmlBtn" : {
				click: this.onImportGincoXmlBtnClick
			},
			'#logoutbtn' : {
				click : this.onLogoutBtn
			},
			'#searchBtn' : {
				trigger : this.onSearchTrigger,
				specialkey : this.onSearchTriggerKey
			}
		});
	}

});
