Ext.define('HadocApp.controller.TopToolbarController', {
    extend: 'Ext.app.Controller',

    views: [
        'TopToolbar'
    ],

    onAproposClick: function(button, e, options) {
        Ext.create('HadocApp.view.AProposWin');
    },

    init: function(application) {
        this.control({
            "#aproposbtn": {
                click: this.onAproposClick
            }
        });
    }

});
