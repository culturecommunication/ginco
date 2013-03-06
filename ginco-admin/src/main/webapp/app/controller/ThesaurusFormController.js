Ext.define('GincoApp.controller.ThesaurusFormController', {
	extend : 'Ext.app.Controller',

	models : [ 'ThesaurusModel' ],
	stores : [ 'MainTreeStore' ],

	loadPanel : function(theForm) {
		var me = this;
		var model = this.getThesaurusModelModel();
		var thesaurusData = theForm.up('thesaurusPanel').thesaurusData;
		if (thesaurusData != null) {
			theForm.getEl().mask("Chargement");
			model.load(thesaurusData.id, {
				success : function(model) {
					me.loadData(theForm, model);
					theForm.getEl().unmask();
				}
			});
		} else {
			model = Ext.create('GincoApp.model.ThesaurusModel');
			model.id = -1;
			theForm.loadRecord(model);
		}
	},
	loadData : function(aForm, aModel) {
		var thesaurusPanel = aForm.up('thesaurusPanel');
		thesaurusPanel.setTitle(aModel.data.title);
		thesaurusPanel.thesaurusData = aModel.data;
		aForm.setTitle(aModel.data.title);
		aForm.loadRecord(aModel);
		thesaurusPanel.down('button[cls=newBtnMenu]').setDisabled(false);
		
	},
	onNewTermBtnClick : function(theButton, e, options) {
		var thePanel = theButton.up('thesaurusPanel');
		this.createPanel('GincoApp.view.TermPanel',thePanel.thesaurusData );
	},
	
	onNewConceptBtnClick : function(theButton, e, options) {
		var thePanel = theButton.up('thesaurusPanel');
		this.createPanel('GincoApp.view.ConceptPanel',thePanel.thesaurusData );
	},
	
	onNewConceptArrayBtnClick : function(theButton) {
		var thePanel = theButton.up('thesaurusPanel');
		this.createPanel('GincoApp.view.ConceptArrayPanel',thePanel.thesaurusData);
	},
	
	createPanel : function(aType, thesaurusData)
	{
		var aNewPanel = Ext.create(aType);
		aNewPanel.thesaurusData = thesaurusData;
		var topTabs = Ext.ComponentQuery.query('topTabs')[0];
		var tab = topTabs.add(aNewPanel);
		topTabs.setActiveTab(tab);
		tab.show();
		return aNewPanel;
	},

	saveForm : function(theButton) {
		var me = this;
		var theForm = theButton.up('form');
		if (theForm.getForm().isValid()) {
			theForm.getEl().mask("Chargement");
			theForm.getForm().updateRecord();
			var updatedModel = theForm.getForm().getRecord();
			updatedModel.save({
				success : function(record, operation) {
					me.loadData(theForm, record);
					theForm.getEl().unmask();
					Thesaurus.ext.utils.msg('Succès',
							'Le thesaurus a été enregistré!');
					me.application.fireEvent('thesaurusupdated');
				},
				failure : function() {
					theForm.getEl().unmask();
					Thesaurus.ext.utils.msg('Problème',
							"Impossible d'enregistrer le thesaurus!");
				}
			});
		}
	},
	init : function(application) {
		this.control({
			'thesaurusPanel form' : {
				afterrender : this.loadPanel
			},
			'thesaurusPanel button[cls=save]' : {
				click : this.saveForm
			},
			"thesaurusPanel #newTermBtn" : {
				click : this.onNewTermBtnClick
			},
			"thesaurusPanel #newConceptBtn" : {
				click : this.onNewConceptBtnClick
			},
			"thesaurusPanel #newConceptArrayBtn" : {
				click : this.onNewConceptArrayBtnClick
			}
		});
	}
});
