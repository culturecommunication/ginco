Ext.define('GincoApp.controller.ConceptArrayController', {
	extend:'Ext.app.Controller',
	
	models : [ 'ConceptArrayModel', 'ThesaurusConceptReducedModel', 'NodeLabelModel' ],

	//localized : true,
	
	loadConceptArrayPanel : function(theForm){
        var me = this;
        var model = this.getConceptArrayModelModel();
        
        var conceptArray = theForm.up('conceptArrayPanel').conceptArray;
        
        if (conceptArray != null) {
			//load existing concept array
        	model.load(conceptArray, {
				success : function(model) {
					me.loadData(theForm, model);
					theForm.getEl().unmask();
				},
				failure : function(model) {
					Thesaurus.ext.utils.msg("erreur",
							"erreur");
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
		var conceptArrayPanel = me.getActivePanel();
		
		aForm.loadRecord(aModel);
		
		//We get all the concepts included in this concept array
		var associatedConcepts = aModel.concepts().getRange();
		
		//We get all the node labels for this concept array, containing the title, created & modified dates...
		var associatedNodeLabel = aModel.nodeLabelViewList().getRange();
		var currentNodeLabel = associatedNodeLabel[0];
		
		var theGrid = aForm.down('#gridPanelConceptArray');
		var theGridStore = theGrid.getStore();
		theGridStore.removeAll();
		theGridStore.add(associatedConcepts);
	},
	
	getActivePanel : function() {
    	var topTabs = Ext.ComponentQuery.query('topTabs')[0];
    	return topTabs.getActiveTab();
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
	
	selectConceptToArray : function(theButton){
		//This method opens a popup to add concepts to the concept array
		var me = this;
		var theConceptArrayPanel = theButton.up('conceptArrayPanel');
		var theConceptArrayForm = theButton.up('form');
		
		var win = Ext.create('GincoApp.view.SelectConceptWin', {
            thesaurusData : theConceptArrayPanel.thesaurusData,
            //TODO : filter on parent concept id by setting conceptId parameter in the win
            showTree : false,
            listeners: {
                selectBtn: {
                    fn: function(selectedRow) {
                            me.addConceptToArray(selectedRow, me.theConceptArrayForm);
                        }
                }
            }
        });
		win.show();
	},
	
	addConceptToArray : function(selectedRow, theConceptArrayForm){
		var theConceptArrayGrid = theConceptArrayForm.down('#gridPanelConceptArray');
		var theConceptArrayGridStore = theConceptArrayGrid.getStore();
		theConceptArrayGridStore.add(selectedRow[0]);
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
            },
            'selectConceptWin gridpanel' : {
            	itemclick : function(theGrid) {
            		var thePanel = theGrid.up('gridpanel');
                    var theButton = thePanel.down('#selectButton');
                    theButton.setDisabled(false);
            	}
            },
         });

    }
});