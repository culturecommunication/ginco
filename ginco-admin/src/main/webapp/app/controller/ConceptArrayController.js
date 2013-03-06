Ext.define('GincoApp.controller.ConceptArrayController', {
	extend:'Ext.app.Controller',

	localized : true,
	
	onConceptArrayFormRender : function(theForm){
        var me = this;
        
	},
	
	saveConceptArray : function(theButton){
		var me = this;
	},
	
	selectParentConcept : function(theButton){
		var thePanel = theButton.up('conceptArrayPanel');
		var theForm = theButton.up('form');
		var me = this;
		var win = Ext.create('GincoApp.view.SelectConceptWin', {
	            thesaurusData : thePanel.thesaurusData,
	            //conceptId : thePanel.conceptId,
	            showTree : false,
	            listeners: {
	                selectBtn: {
	                    fn: function(selectedRow) {
	                            me.selectConceptAsParent(selectedRow, theForm);
	                        }
	                }
	            }
	        });
		win.show();
	},
	
	selectConceptAsParent : function(selectedRow, theForm){
		console.log(selectedRow);
		//TODO: don't update the value of the field but the model loaded by onConceptArrayFormRender
		//theForm.down('textfield[name="parentConcept"]').setValue("test");
	},
	
    init:function(){    	  	 
         this.control({
        	'conceptArrayPanel form' : {
 				afterrender : this.onConceptArrayFormRender
 			},
 			'conceptArrayPanel #saveConceptArray' : {
 				click : this.saveConceptArray
 			},
            'conceptArrayPanel  #selectParentConcept' : {
                click : this.selectParentConcept
            }
         });

    }
});
