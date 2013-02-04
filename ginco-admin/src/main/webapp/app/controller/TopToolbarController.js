Ext.define('HadocApp.controller.TopToolbarController', {
    extend: 'Ext.app.Controller',

    views: [
        'TopToolbar'
    ],

    onAproposClick: function(button, e, options) {
        Ext.create('HadocApp.view.AProposWin');
    },
    
    onNewThesaurusBtnClick: function(button, e, options) {
    	var ThesaurusPanel = Ext.create('HadocApp.view.ThesaurusPanel');
    	var topTabs = Ext.ComponentQuery.query('topTabs')[0];
    	var tab = topTabs.add(ThesaurusPanel);
    	topTabs.setActiveTab(tab);
    	tab.show();
    },

    init: function(application) {
        this.control({
            "#aproposbtn": {
                click: this.onAproposClick
            },
        
        "#newThesaurusBtn":{
        	click: this.onNewThesaurusBtnClick
        }
        });
    }

});
