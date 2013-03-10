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
			if (theForm.getForm().isDirty()) {
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
							var saveButton = theForm.down('button[cls=save]');
							saveButton.fireEvent('click', saveButton);
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
			}
		});
	}
});