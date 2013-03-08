Ext.define('GincoApp.controller.ConceptArrayController', {
	extend:'Ext.app.Controller',

    stores : [ 'ConceptReducedStore' ],
	models : [ 'ConceptArrayModel', 'ThesaurusConceptReducedModel' ],

	//localized : true,
	
	xProblemLabel : 'Error!',
	xProblemLoadMsg : 'Unable to load data',
	xErrorDoubleRecord: 'Record already present',
	
	loadConceptArrayPanel : function(theForm){
        var me = this;

        debugger;

        var model = this.getConceptArrayModelModel();
        
        var conceptArray = theForm.up('conceptArrayPanel').conceptArray;
        
        if (conceptArray != null) {
			//load existing concept array
        	model.load(conceptArray, {
				success : function(model) {
					debugger;
                    me.loadData(theForm, model);
					theForm.getEl().unmask();
				},
				failure : function(model) {
					Thesaurus.ext.utils.msg(me.xProblemLabel,
							xProblemLoadMsg);
					var globalTabs = theForm.up('topTabs');
					globalTabs.remove(thePanel);
				}
			});
		} else {
			model = Ext.create('GincoApp.model.ConceptArrayModel');
			//TODO : is it really needed ? -> model.id = -1;
			theForm.loadRecord(model);
		}
	},
	
	loadData : function(aForm, aModel) {
		var me = this;
		
		aForm.loadRecord(aModel);

		//We get all the concepts included in this concept array
		var associatedConcepts = aModel.concepts().getRange();
		
		var theGrid = aForm.down('#gridPanelConceptArray');
		var theGridStore = theGrid.getStore();
		theGridStore.removeAll();
		theGridStore.add(associatedConcepts);
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
	
	selectConceptToArray : function(theButton){
		//This method opens a popup to add concepts to the concept array
		var me = this;
		var theConceptArrayPanel = theButton.up('conceptArrayPanel');
		var theConceptArrayForm = theButton.up('form');
		
		var win = Ext.create('GincoApp.view.SelectConceptWin', {
            thesaurusData : theConceptArrayPanel.thesaurusData,
            //TODO remove after test :
            conceptId : 'http://culturecommunication.gouv.fr/ark:/12345/62f9c6fe-4346-4183-af09-44bfacfd17d1',
            getChildren : true,
            showTree : false,
            listeners: {
                selectBtn: {
                    fn: function(selectedRow) {
                            me.addConceptToArray(selectedRow, theConceptArrayForm);
                        }
                }
            }
        });
		win.show();
	},
	
	addConceptToArray : function(selectedRow, theConceptArrayForm){
		var theConceptArrayGrid = theConceptArrayForm.down('#gridPanelConceptArray');
		var theConceptArrayGridStore = theConceptArrayGrid.getStore();
		
		//Test if already present in the grid
		if (theConceptArrayGridStore.findRecord('identifier', selectedRow[0].data.identifier) !== null ){
			Ext.MessageBox.alert(this.xProblemLabel,this.xErrorDoubleRecord);
		} else {
			theConceptArrayGridStore.add(selectedRow[0]);
		}
		
	},
	
	selectConceptAsParent : function(selectedRow, theForm){
		console.log(selectedRow);
		//TODO: don't update the value of the field but the model loaded by onConceptArrayFormRender
		//theForm.down('textfield[name="parentConcept"]').setValue("test");
		//var record = theForm.getRecord();
		//console.log(selectedRow);
		//record.data.superOrdinateConceptId=selectedRow;
		//theForm.loadRecord(record);
	},
	
    init:function(){    	  	 
         this.control({
        	'conceptArrayPanel form' : {
 				afterrender : this.loadConceptArrayPanel
 			},
 			'conceptArrayPanel #saveConceptArray' : {
 				click : this.saveConceptArray
 			},
            'conceptArrayPanel  #selectParentConcept' : {
                click : this.selectParentConcept
            },
            'conceptArrayPanel  #addConceptToArray' : {
                click : this.selectConceptToArray
            }            
         });

    }
});