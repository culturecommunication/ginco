Ext.define('GincoApp.controller.TermPanelController', {
	extend : 'Ext.app.Controller',
	localized : true,
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
		var termPanel = theForm.up('termPanel');
		var thesaurusData = theForm.up('termPanel').thesaurusData;
		
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
			model.data.language=thesaurusData.languages[0];
			
			theForm.loadRecord(model);
		}
	},

	loadData : function(aForm, aModel) {
		var termPanel = aForm.up('termPanel');
		var deleteBtn = aForm.down('#delete');
		var createConceptBtn = aForm.down('#createconcept');
		termPanel.setTitle(aModel.data.lexicalValue);
		aForm.setTitle(aModel.data.lexicalValue);
		aForm.loadRecord(aModel);
		termPanel.termId = aModel.data.identifier;
		
		if (Ext.isEmpty(aModel.data.conceptId)){
			createConceptBtn.setDisabled(false);
		}
		
		if (Ext.isEmpty(aModel.data.conceptId)){
			deleteBtn.setDisabled(false);
		}
	},

	saveForm : function(theButton) {
		var me = this;
		var theForm = theButton.up('form');
		var thePanel = theForm.up('termPanel');
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
					me.application.fireEvent('termupdated',thePanel.thesaurusData);
				},
				failure : function() {
					Thesaurus.ext.utils.msg(me.xProblemLabel,
							me.xProblemSaveMsg);
					theForm.getEl().unmask();
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
							me.application.fireEvent('termdeleted',thePanel.thesaurusData);
							globalTabs.remove(thePanel);
						},
						failure : function(record, operation) {
							Thesaurus.ext.utils.msg(me.xProblemLabel,
									operation.error);
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
	
	loadCreateConceptFromTerm : function(theButton) {
		var thePanel = theButton.up('termPanel');
		var theForm = theButton.up('form');
		var theTermModel = theForm.getForm().getRecord();
		var termId = theButton.up('termPanel').termId;
		var conceptPanel = this.createPanel('GincoApp.view.ConceptPanel', thePanel.thesaurusData, theTermModel);
	},
	
	createPanel : function(aType,thesaurusData, theTermModel)
	{
		var aNewPanel = Ext.create(aType);
		aNewPanel.thesaurusData = thesaurusData;
		aNewPanel.initPreferedTermBeforeLoad = theTermModel;
		var topTabs = Ext.ComponentQuery.query('topTabs')[0];
		var tab = topTabs.add(aNewPanel);
		topTabs.setActiveTab(tab);
		tab.show();
		return aNewPanel;
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
			},
			'termPanel #createconcept' : {
				click : this.loadCreateConceptFromTerm
			}
		});
	}
});