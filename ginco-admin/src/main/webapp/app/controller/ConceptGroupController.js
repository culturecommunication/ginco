Ext.define('GincoApp.controller.ConceptGroupController', {
	extend:'Ext.app.Controller',

    stores : [ 'ConceptReducedStore' ],
    models : [ 'ConceptGroupModel'],

    //localized : true,

    xProblemLabel : 'Error !',
    xProblemLoadMsg : 'Unable to load the concept',
	
	loadConceptGroupPanel : function(theForm){
        var me = this;
        var theConceptGroupPanel = theForm.up('conceptGroupPanel');
        var model = this.getConceptGroupModelModel();
        var conceptGroupId = theConceptGroupPanel.conceptGroupId;
        
        
        if (conceptGroupId != '') {
        	//TODO : implement loading of the concept group
        	theForm.getEl().mask("Chargement");
        	var conceptGroupModel = this.getConceptGroupModelModel();
        	conceptGroupModel.load(conceptGroupId, {
				success : function(model) {
					me.loadData(theForm, model);
					theForm.getEl().unmask();
				},
				failure : function(model) {
					Thesaurus.ext.utils.msg(me.xProblemLabel,
							me.xProblemLoadMsg);
					var globalTabs = theForm.up('topTabs');
					globalTabs.remove(thePanel);
				}
			});
        	
        } else {
        	//TODO : implement creating a new concept group
//        	theConceptGroupPanel.setTitle(theConceptGroupPanel.title+' : '+theConceptGroupPanel.thesaurusData.title);
//        	model = Ext.create('GincoApp.model.ConceptGroupModel');
//        	model.data.thesaurusId = theConceptGroupPanel.thesaurusData.id;
//        	theForm.loadRecord(model);
        	
        }
	},
	
	loadData : function(aForm, aModel) {
		var me = this;
		aForm.loadRecord(aModel);
		var theConceptGroupPanel = me.getActivePanel();		
		theConceptGroupPanel.setTitle("Tableau de groupes : "+aModel.data.label);
		
		//We get all the concepts included in this concept group
		var conceptsGrid  = aForm.down('#gridConceptGroupPanelConcepts');
		var conceptsGridStore = conceptsGrid.getStore();
		conceptsGridStore.getProxy().extraParams = {
            conceptIds: aModel.raw.concepts
        };
		conceptsGridStore.load();

	},
	
	getActivePanel : function() {
    	var topTabs = Ext.ComponentQuery.query('topTabs')[0];
    	return topTabs.getActiveTab();
    },
	
	saveConceptGroup : function(theButton, theCallback){
		var me = this;
	},
	
    init:function(){    	  	 
         this.control({
        	'conceptGroupPanel form' : {
 				afterrender : this.loadConceptGroupPanel
 			},
 			'conceptGroupPanel #saveConceptGroup' : {
 				click : this.saveConceptGroup
 			}
         });

    }
});