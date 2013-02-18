Ext.define('GincoApp.controller.TermPanelController', {
	extend : 'Ext.app.Controller',
	localized : true,

	views : [ 'TermPanel' ],
	models : [ 'ThesaurusTermModel' ],

	xLoading : 'Loading',
	xDeleteMsgLabel : 'Do you want to delete this term?',
	xDeleteMsgTitle : 'Delete this term?',
	xSucessLabel : 'Success',
	xSucessSavedMsg : 'Thesaurus Term Successfully Saved',
	xSucessRemovedMsg : 'Thesaurus Term Successfully Deleted',
	xProblemLabel : 'Problem',
	xProblemSaveMsg : 'Impossible to save the term !',
	xProblemDeleteMsg : 'Impossible to delete the term !',
	xProblemLoadMsg : 'Unable to load the term',

	loadPanel : function(theForm) {
		var me = this;
		var model = this.getThesaurusTermModelModel();
		var termId = theForm.up('termPanel').termId;
		if (termId != null) {
			theForm.getEl().mask("Chargement");
			model.load(termId, {
				success : function(model) {
					me.loadData(theForm, model);
					theForm.getEl().unmask();
				},
				failure : function(model) {
					Thesaurus.ext.utils.msg(me.xProblemLabel,
							me.xProblemLoadMsg);
					var globalTabs = theForm.up('topTabs');
					var thePanel = theForm.up('termPanel');
					globalTabs.remove(thePanel);
				}
			});
		} else {
			model = Ext.create('GincoApp.model.ThesaurusTermModel');
			model.data.thesaurusId = theForm.up('termPanel').thesaurusData.id;
			model.data.identifier = "";
			theForm.loadRecord(model);
		}
	},

	loadData : function(aForm, aModel) {
		var termPanel = aForm.up('termPanel');
		var deleteBtn = aForm.down('#delete');
		termPanel.setTitle(aModel.data.lexicalValue);
		aForm.setTitle(aModel.data.lexicalValue);
		aForm.loadRecord(aModel);
		deleteBtn.setDisabled(false);
	},

	saveForm : function(theButton) {
		var me = this;
		var theForm = theButton.up('form');
		if (theForm.getForm().isValid()) {
			theForm.getEl().mask(me.xLoading);
			theForm.getForm().updateRecord();
			var updatedModel = theForm.getForm().getRecord();
			updatedModel.save({
				success : function(record, operation) {
					me.loadData(theForm, record);
					theForm.getEl().unmask();
					Thesaurus.ext.utils
							.msg(me.xSucessLabel, me.xSucessSavedMsg);
				},
				failure : function() {
					theForm.getEl().unmask();
					Thesaurus.ext.utils.msg(me.xProblemLabel,
							me.xProblemSaveMsg);
				}
			});
		}
	},

	deleteForm : function(theButton) {
		var me = this;
		var theForm = theButton.up('form');
		var updatedModel = theForm.getForm().getRecord();
		var globalTabs = theForm.up('topTabs');
		var thePanel = theForm.up('termPanel');

		Ext.MessageBox.show({
			title : me.xDeleteMsgTitle,
			msg : me.xDeleteMsgLabel,
			buttons : Ext.MessageBox.YESNOCANCEL,
			fn : function(buttonId) {
				switch (buttonId) {
				case 'no':
					break; // manually removes tab from tab panel
				case 'yes':
					updatedModel.destroy({
						success : function(record, operation) {
							Thesaurus.ext.utils.msg(me.xSucessLabel,
									me.xSucessRemovedMsg);
							globalTabs.remove(thePanel);
						},
						failure : function() {
							Thesaurus.ext.utils.msg(me.xProblemLabel,
									me.xProblemDeleteMsg);
						}
					});
					break;
				case 'cancel':
					break; // leave blank if no action required on
				// cancel
				}
			},
			scope : this
		});
	},

	loadLanguages : function(theCombo) {
		var thePanel = theCombo.up('termPanel');
		var theStore = theCombo.getStore();
		theStore.getProxy().setExtraParam('thesaurusId',
				thePanel.thesaurusData.id);
		theStore.load();
	},

	init : function() {
		this.control({
			'termPanel form' : {
				afterrender : this.loadPanel
			},
			'termPanel button[cls=save]' : {
				click : this.saveForm
			},
			'termPanel button[cls=delete]' : {
				click : this.deleteForm
			},
			'termPanel #languageCombo' : {
				render : this.loadLanguages
			}

		});
	}
});