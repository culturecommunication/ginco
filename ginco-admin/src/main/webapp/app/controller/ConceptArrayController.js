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
		
		//We set in the model the field of superordinate with associated data (hasone)
		var superOrdinateConceptLabel = aModel.superOrdinateConceptStore.data.items[0].data.label;
		aForm.down('textfield[name="superOrdinateConcept_label"]').setValue(superOrdinateConceptLabel);
		
		var theGrid = aForm.down('#gridPanelConceptArray');
		var theGridStore = theGrid.getStore();
		theGridStore.removeAll();
		theGridStore.add(associatedConcepts);
	},
	
	saveConceptArray : function(theButton){
		var me = this;
		var theForm = theButton.up('form');
		var theGrid = theForm.down('#gridPanelConceptArray');
		var theStore = theGrid.getStore();
		var addedConceptsToArray = theStore.getRange();
		theForm.getEl().mask(me.xLoading);
		
		//We update the model with the form fields
		theForm.getForm().updateRecord();
		
		//We update the model with the concepts added to the grid
		var updatedModel = theForm.getForm().getRecord();
		updatedModel.concepts().removeAll();
		updatedModel.concepts().add(addedConceptsToArray);
		
		updatedModel.save({
			success : function(record, operation) {
				var resultRecord = operation.getResultSet().records[0];
				me.loadData(theForm, resultRecord);
				theForm.getEl().unmask();
				Thesaurus.ext.utils.msg("ok", "ok");				
				//me.application.fireEvent('conceptupdated', thePanel.thesaurusData);
				
			},
			failure : function(record, operation) {
				Thesaurus.ext.utils.msg("erreur", "erreur"+" "+operation.error);
				theForm.getEl().unmask();
			}
		});
		
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
		var model = theForm.getRecord();
		
		//We get label and id of parent concept from selected row and add to the model + display the label in the field superOrdinateConcept_label
		//model.data.superOrdinateConcept_label = selectedRow[0].data.label;
		//aModel.superOrdinateConceptStore.data.items[0].data.label;
		model.superOrdinateConceptStore.data.items[0].data.identifier = selectedRow[0].data.identifier;
		model.superOrdinateConceptStore.data.items[0].data.label = selectedRow[0].data.label;
		theForm.down('textfield[name="superOrdinateConcept_label"]').setValue(selectedRow[0].data.label);
		
		//TODO : flush the grid with alert before
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
            },
            'selectConceptWin gridpanel' : {
            	itemclick : function(theGrid) {
            		var thePanel = theGrid.up('gridpanel');
                    var theButton = thePanel.down('#selectButton');
                    theButton.setDisabled(false);
            	}
            }
         });

    }
});