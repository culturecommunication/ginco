Ext.define('GincoApp.controller.ConceptGroupController', {
	extend:'Ext.app.Controller',

    stores : [ 'ConceptReducedStore' ],
    models : [ 'ConceptGroupModel'],

	//localized : true,
	
	loadConceptGroupPanel : function(theForm){
        var me = this;
        var theConceptGroupPanel = theForm.up('conceptGroupPanel');
        var model = this.getConceptGroupModelModel();
        var conceptGroupId = theConceptGroupPanel.conceptGroupId;
        
        if (conceptGroupId != '') {
        	theForm.getEl().mask("Chargement");
        	//TODO : implement loading of the concept group
        } else {
        	//TODO : implement creating a new concept group
        	theConceptGroupPanel.setTitle(theConceptGroupPanel.title+' : '+theConceptGroupPanel.thesaurusData.title);
        }
	},
	
	loadData : function(aForm, aModel) {
		var me = this;		
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