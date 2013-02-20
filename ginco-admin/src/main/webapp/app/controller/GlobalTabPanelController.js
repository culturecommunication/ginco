Ext.define('GincoApp.controller.GlobalTabPanelController', {
	extend : 'Ext.app.Controller',
	localized : true,
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
        ownerCt = tabPanel.ownerCt, 
        oldToken, newToken;
		var tokenDelimiter = ':';
		if (tab.thesaurusData && tab.thesaurusData.id) {
			tabs.push(tab.id);
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
		var tokenDelimiter = ':';
		Ext.History.on('change', function(token) {
            var parts, length;
            
            if (token) {
                parts = token.split(tokenDelimiter);
                length = parts.length;
                
                // setActiveTab in all nested tabs
                	
               	tabPanel.setActiveTab(Ext.getCmp(parts[1]));
                
            }
        });
	},

	init : function(application) {
		this.control({
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
			'topTabs' :  {
				tabchange : this.onTabChange,
				afterrender: this.onTabsAfterRender
			}
		});
	}
});