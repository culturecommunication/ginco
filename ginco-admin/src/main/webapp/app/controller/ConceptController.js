Ext.define('GincoApp.controller.ConceptController', {
	extend:'Ext.app.Controller',
	
	views : [ 'ConceptPanel' ],
	stores : [ 'MainTreeStore' ],
	
	onConceptFormRender : function(theForm){
		var thePanel = theForm.up('conceptPanel');
		thePanel.setTitle(thePanel.title+' : '+thePanel.thesaurusData.title);
		model = Ext.create('GincoApp.model.ConceptModel');
		model.data.thesaurusId = thePanel.thesaurusData.id;
		model.data.topconcept = thePanel.thesaurusData.defaultTopConcept;
		model.data.identifier = "";
		theForm.loadRecord(model);
	},
	
	onGridRender : function(theGrid){
		var thePanel = theGrid.up('selectTermWin');
		var theStore= theGrid.getStore();
		theStore.getProxy().setExtraParam('idThesaurus',thePanel.thesaurusData.id);
		theStore.load();
	},
	
	newTermFromConceptPrefBtn : function(theButton){
		this.newTermFromConceptBtn(theButton, true);
	},
	
	newTermFromConceptNonPrefBtn : function(theButton){
		this.newTermFromConceptBtn(theButton, false);
	},
	
	selectTermFromConceptPrefBtn : function(theButton){
		this.selectTermFromConceptBtn(theButton, true);
	},
	
	selectTermFromConceptNonPrefBtn : function(theButton){
		this.selectTermFromConceptBtn(theButton, false);
	},
	
	newTermFromConceptBtn : function(theButton, prefered) {
		var thePanel = theButton.up('conceptPanel');
		var win = Ext.create('GincoApp.view.CreateTermWin');
		win.thesaurusData = thePanel.thesaurusData;
		var theForm = win.down('form');
		var theGrid = theButton.up('gridpanel');
		win.store = theGrid.getStore();
		var model = Ext.create('GincoApp.model.ThesaurusTermModel');
		model.data.prefered = prefered;
		model.data.thesaurusId = thePanel.thesaurusData.id;
		model.data.identifier = "";
		theForm.loadRecord(model);
		win.show();
	},
	
	selectTermFromConceptBtn : function(theButton, prefered){
		var thePanel = theButton.up('conceptPanel');
		var win = Ext.create('GincoApp.view.SelectTermWin');
		var theGrid = theButton.up('gridpanel');
		win.store = theGrid.getStore();
		win.thesaurusData = thePanel.thesaurusData;
		win.show();
	},
	
	onSelectTermDblClick : function(theGrid, record, item, index, e, eOpts ) {
		var theWin = theGrid.up('selectTermWin');
		theWin.store.add(record);
		theWin.close();
	},
	
	loadLanguages : function(theCombo) {
		var thePanel = theCombo.up('createTermWin');
		var theStore = theCombo.getStore();
		theStore.getProxy().setExtraParam('thesaurusId', thePanel.thesaurusData.id);
		theStore.load();
	},
	
	loadData : function(aForm, aModel) {
		var conceptPanel = aForm.up('conceptPanel');
		conceptPanel.conceptData = aModel.data;
	
		aForm.loadRecord(aModel);
		var terms = aModel.terms().getRange();
		
		length = terms.length;
		for (i = 0; i < length ; i++) {
			
			if (terms[i].data.prefered == true) {
				conceptTitle = terms[i].data.lexicalValue;
			}
		}
		conceptPanel.setTitle(conceptTitle);
		
		var theGrid = aForm.down('gridpanel');
		var theGridStore = theGrid.getStore();
		theGridStore.removeAll();
		theGridStore.add(terms);
	},

	saveTermFromConceptBtn : function(theButton){
		var theForm = theButton.up('form');
		var theWin = theButton.up('createTermWin');
		theForm.getForm().updateRecord();
		var updatedModel = theForm.getForm().getRecord();
		theWin.store.add(updatedModel);
		theWin.close();
	},
	
	saveConcept : function(theButton){
		var me = this;
		var theForm = theButton.up('form');
		var theGrid = theForm.down('gridpanel');
		theForm.getForm().updateRecord();
		var theStore = theGrid.getStore();
		var termsData = theStore.getRange();
		
		theForm.getEl().mask("Chargement");
		var updatedModel = theForm.getForm().getRecord();
		updatedModel.terms().add(termsData);
		updatedModel.save({
			success : function(record, operation) {
				var resultRecord = operation.getResultSet().records[0];
				me.loadData(theForm, resultRecord);
				theForm.getEl().unmask();
				Thesaurus.ext.utils.msg('Succès',
						'Le concept a été enregistré!');
				
				me.application.fireEvent('conceptupdated');
			},
			failure : function(record, operation) {
				theForm.getEl().unmask();
				Thesaurus.ext.utils.msg('Problème',
						"Impossible d'enregistrer le concept ! "+operation.error);
			}
		});
	},
	
    init:function(){
         this.control({
        	 'conceptPanel form' : {
 				afterrender : this.onConceptFormRender
 			},
 			'conceptPanel  button[cls=save]' : {
 				click : this.saveConcept
 			},
 			'conceptPanel #newTermFromConceptPrefBtn' : {
				click : this.newTermFromConceptPrefBtn
			},
			'conceptPanel #newTermFromConceptNonPrefBtn' : {
				click : this.newTermFromConceptNonPrefBtn
			},
			'conceptPanel #selectTermFromConceptPrefBtn' : {
				click : this.selectTermFromConceptPrefBtn
			},
			'conceptPanel #selectTermFromConceptNonPrefBtn' : {
				click : this.selectTermFromConceptNonPrefBtn
			},
			'form #saveTermFromConcept' : {
				click : this.saveTermFromConceptBtn
			},
			'createTermWin #languageCombo' : {
				render : this.loadLanguages
			},
			'selectTermWin gridpanel' : {
 				render : this.onGridRender,
 				itemdblclick : this.onSelectTermDblClick
 			}
			
         });
    }
});