Ext.define('GincoApp.controller.ConceptArrayController', {
	extend:'Ext.app.Controller',

    stores : [ 'ConceptReducedStore' ],
	models : [ 'ConceptArrayModel', 'ThesaurusConceptReducedModel', 'ThesaurusModel'],

	localized : true,
	
	xProblemLabel : 'Error!',
	xProblemLoadMsg : 'Unable to load data',
	xErrorDoubleRecord: 'Record already present',
	xSucessLabel : 'Success!',
	xSucessSavedMsg : 'Array saved successfully',
	xProblemSaveMsg : 'Unable to save this array!',
	xChangingParentWarningMsgTitle : 'Warning',
	xChangingParentWarningMsgLabel : 'Changing the parent concept will erase the children, please confirm',
	
	
	loadConceptArrayPanel : function(theForm){
        var me = this;
		var thePanel = theForm.up('conceptArrayPanel');
        var model = this.getConceptArrayModelModel();
        var conceptArrayId = thePanel.conceptArrayId;
        if (conceptArrayId != '') {    		
    		theForm.getEl().mask("Chargement");

			var thesaurusId= thePanel.thesaurusData.data.id;
			var thesaurusModel= this.getThesaurusModelModel();
			thesaurusModel.load(thesaurusId, {
				success : function(model) {
					thePanel.thesaurusData = model.data;
				}
			});
			model.load(conceptArrayId, {
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
			thePanel.setTitle(thePanel.title+' : '+thePanel.thesaurusData.title);
			model = Ext.create('GincoApp.model.ConceptArrayModel');
			model.data.thesaurusId = thePanel.thesaurusData.id;
			model.data.identifier = "";
			theForm.loadRecord(model);			
		}
	},
	
	loadData : function(aForm, aModel) {
		var me = this;		
		aForm.loadRecord(aModel);
		var thePanel = me.getActivePanel();		
		thePanel.setTitle("Tableau de concepts : "+aModel.data.label);

		//We get all the concepts included in this concept array
		var conceptsGrid  = aForm.down('#gridPanelConceptArray');
        var conceptsGridStore = conceptsGrid.getStore();
        conceptsGridStore.getProxy().extraParams = {
            conceptIds: aModel.raw.concepts
        };
        conceptsGridStore.load();		
	},
	
	saveConceptArray : function(theButton, theCallback){
		var me = this;
		var theForm = theButton.up('#conceptArrayForm');
		var theGrid = theForm.down('#gridPanelConceptArray');
		theForm.getEl().mask(me.xLoading);
		
		theForm.getForm().updateRecord();
		var updatedModel = theForm.getForm().getRecord();
		//We update the model with the concepts added to the grid
        var conceptsGridStore = theGrid.getStore();
        var conceptData = conceptsGridStore.getRange();
        var conceptIds = Ext.Array.map(conceptData, function(concept){
            return concept.data.identifier;
        });
        updatedModel.data.concepts = conceptIds;
		
		updatedModel.save({
			success : function(record, operation) {
				var resultRecord = operation.getResultSet().records[0];
				me.loadData(theForm, resultRecord);
				theForm.getEl().unmask();
				Thesaurus.ext.utils.msg(me.xSucessLabel, me.xSucessSavedMsg);
				if (theCallback) {
					theCallback();
				}
			},
			failure : function(record, operation) {
				Thesaurus.ext.utils.msg(me.xProblemLabel, me.xProblemSaveMsg+" "+operation.error);
				theForm.getEl().unmask();
			}
		});
		
	},
	
	getActivePanel : function() {
    	var topTabs = Ext.ComponentQuery.query('topTabs')[0];
    	return topTabs.getActiveTab();
    }, 
    
	selectParentConcept : function(theButton){
		var me = this;
		var theForm = theButton.up('form');
	    var thePanel = me.getActivePanel();
	    
	 	Ext.MessageBox.show({
			title : me.xChangingParentWarningMsgTitle,
			msg : me.xChangingParentWarningMsgLabel,
			buttons : Ext.MessageBox.YESNO,
			fn : function(buttonId) {
				switch (buttonId) {
				case 'no':
					break; // manually removes tab from tab panel
					case 'yes':
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
					break;
					case 'cancel':
					break; // leave blank if no action required on
				// cancel
				}
			},
			scope : this
		});
		
	},
	
	selectConceptToArray : function(theButton){
		//This method opens a popup to add concepts to the concept array
		var me = this;
		var theConceptArrayPanel = theButton.up('conceptArrayPanel');
		var theConceptArrayForm = theButton.up('form');
		var theGrid = theConceptArrayPanel.down('#gridPanelConceptArray');
		var theStore = theGrid.getStore();
		    
		var win = Ext.create('GincoApp.view.SelectConceptWin', {
            thesaurusData : theConceptArrayPanel.thesaurusData,
            conceptId : theConceptArrayForm.down('textfield[name="superOrdinateId"]').value,
            getChildren : true,
            showTree : false,
            checkstore: theStore,
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
		var me = this;		
		var oldSuperOrdinate = theForm.down('textfield[name="superOrdinateId"]').getValue();

		theForm.down('textfield[name="superOrdinateLabel"]').setValue(selectedRow[0].data.label);
		theForm.down('textfield[name="superOrdinateId"]').setValue(selectedRow[0].data.identifier);

		if (selectedRow[0].data.identifier != oldSuperOrdinate) {
			var theGrid = theForm.down('#gridPanelConceptArray');
			var theGridStore = theGrid.getStore();
			theGridStore.removeAll();
		}
	},
	
	onRemoveConceptClick : function(gridview, el, rowIndex, colIndex, e, rec, rowEl) {
	        var theGrid = gridview.up('#gridPanelConceptArray');
	        var theStore = theGrid.getStore();
	        theStore.remove(rec);
	},
	 onConceptDblClick: function(theGrid, record, item, index, e, eOpts ) {
	    	var me = this;
	        var thePanel = me.getActivePanel();
	        Thesaurus.ext.tabs.openConceptTab(this.getThesaurusModelModel(), thePanel.thesaurusData.id ,record.data.identifier);
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
            'conceptArrayPanel #gridPanelConceptArray #associatedConceptActionColumn' : {
                click : this.onRemoveConceptClick
            },
            'conceptArrayPanel #gridPanelConceptArray' : {
            	itemdblclick : this.onConceptDblClick
            }
         });

    }
});