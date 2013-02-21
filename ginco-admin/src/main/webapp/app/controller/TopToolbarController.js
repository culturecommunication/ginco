Ext.define('GincoApp.controller.TopToolbarController', {
	extend : 'Ext.app.Controller',

	views : [ 'TopToolbar' ],

	onAproposClick : function(button, e, options) {
		Ext.create('GincoApp.view.AProposWin');
	},

	onNewThesaurusBtnClick : function(button, e, options) {
		this.createPanel('GincoApp.view.ThesaurusPanel');
	},
	onNewTermBtnClick : function(button, e, options) {
		this.createPanel('GincoApp.view.TermPanel');
	},
	createPanel : function(aType)
	{
		var aNewPanel = Ext.create(aType);
		var topTabs = Ext.ComponentQuery.query('topTabs')[0];
		var tab = topTabs.add(aNewPanel);
		topTabs.setActiveTab(tab);
		tab.show();
	},
	onAccessibilityClick : function(theButton) {
		if (theButton.pressed) {
			Ext.FocusManager.enable(true);
		} else {
			Ext.FocusManager.disable();
		}
	},

	init : function(application) {
		this.control({
			"#aproposbtn" : {
				click : this.onAproposClick
			},
			"#newThesaurusBtn" : {
				click : this.onNewThesaurusBtnClick
			},			
			"#accessibilitybtn" : {
				click : this.onAccessibilityClick
			}
		});
	}

});
