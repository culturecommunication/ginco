Ext.define('HadocApp.view.AProposWin', {
    extend: 'Ext.window.Window',

    autoShow: true,
    height: 250,
    width: 500,
    title: '<h2>A Propos</h2>',
    titleAlign: 'center',
    modal: true,

    initComponent: function() {
        var me = this;

        Ext.applyIf(me, {
            items: [
                {
                    xtype: 'label',
                    html: '<h1>Hadoc GINCO</h1><br /><p>Gestionnaire de thesaurus du MCC.</p>'
                },
                {
                	xtype: 'form',
                	title: 'Informations de version',
                	defaults: {
                		labelWidth: 150
                    },
                	listeners : {
                		afterrender : {
                			fn : function (theForm) {
                				theForm.load({
                					url : 'services/ui/infoservice/getGitInfo',
                					method : 'GET'
                				});
                			}
                		}
                	},
                	items : [
                	         {
                	        	 xtype:'displayfield',
                	        	 name : 'commitId',
                	        	 fieldLabel : 'commitId'
                	         },
                	         {
                	        	 xtype:'displayfield',
                	        	 name : 'gitCommitUserName',
                	        	 fieldLabel : 'gitCommitUserName'
                	         },
                	         {
                	        	 xtype:'displayfield',
                	        	 name : 'gitBranch',
                	        	 fieldLabel : 'Branche'
                	         },
                	         {
                	        	 xtype:'displayfield',
                	        	 name : 'gitBuildTime',
                	        	 fieldLabel : 'Date du build'
                	         }                	         
                	]
                }
            ]
        });

        me.callParent(arguments);
    }

});