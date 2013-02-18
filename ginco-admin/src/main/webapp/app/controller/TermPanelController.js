Ext.define('GincoApp.controller.TermPanelController', {
	extend:'Ext.app.Controller',
	
	views : [ 'TermPanel' ],
	models : [ 'ThesaurusTermModel' ],

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
				}
			});
		} else {
			model = Ext.create('GincoApp.model.ThesaurusTermModel');
			model.data.thesaurusId = theForm.up('termPanel').thesaurusData.id;
			model.data.identifier= "";
			theForm.loadRecord(model);
		}
	},
	
	loadData : function(aForm, aModel) {
		var termPanel = aForm.up('termPanel');
		termPanel.setTitle(aModel.data.lexicalValue);
		aForm.setTitle(aModel.data.lexicalValue);
		aForm.loadRecord(aModel);
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
							'Le terme a été enregistré!');
				},
				failure : function() {
					theForm.getEl().unmask();
					Thesaurus.ext.utils.msg('Problème',
							"Impossible d'enregistrer le terme!");
				}
			});
		}
	},
	
	loadLanguages : function(theCombo) {
		var thePanel=theCombo.up('termPanel');
		var theStore= theCombo.getStore();
		theStore.getProxy().setExtraParam('thesaurusId',thePanel.thesaurusData.id);
		theStore.load();
	},
	
    init:function(){
         this.control({
        	 'termPanel form' : {
 				afterrender : this.loadPanel
 			},
        	 'termPanel button[cls=save]' : {
 				click : this.saveForm
 			},
 			'termPanel #languageCombo' : {
 				render : this.loadLanguages
 			}
 			
         });
    }
});