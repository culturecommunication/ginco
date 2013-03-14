Ext.define('GincoApp.controller.ConceptGroupController', {
	extend:'Ext.app.Controller',

    stores : [ 'ConceptReducedStore' ],
    models : [ 'ConceptGroupModel'],

    localized : true,

    xLoading : 'Loading',
    xSucessLabel : 'Success!',
    xSucessSavedMsg : 'Concept group saved successfully',
    xProblemLabel : 'Error !',
    xProblemLoadMsg : 'Unable to load the concept group',
    xProblemSaveMsg : 'Unable to save this concept group!',
    xErrorDoubleRecord: 'Record already present',
	
	loadConceptGroupPanel : function(theForm){
        var me = this;
        var theConceptGroupPanel = theForm.up('conceptGroupPanel');
        var model = this.getConceptGroupModelModel();
        var conceptGroupId = theConceptGroupPanel.conceptGroupId;
        
        
        if (conceptGroupId != '') {
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
        	theConceptGroupPanel.setTitle(theConceptGroupPanel.title+' : '+theConceptGroupPanel.thesaurusData.title);
        	model = Ext.create('GincoApp.model.ConceptGroupModel');
        	model.data.thesaurusId = theConceptGroupPanel.thesaurusData.id;
        	theForm.loadRecord(model);
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
		var theForm = theButton.up('#conceptGroupForm');
		var conceptsGrid = theForm.down('#gridConceptGroupPanelConcepts');
		theForm.getEl().mask(me.xLoading);
		
		theForm.getForm().updateRecord();
		var updatedModel = theForm.getForm().getRecord();
		//We update the model with the concepts added to the grid
		var conceptsGridStore = conceptsGrid.getStore();
		var concepts = conceptsGridStore.getRange();
		var conceptIds = Ext.Array.map(concepts, function(concept){
            return concept.data.identifier;
        });
		updatedModel.data.concepts = conceptIds;
		
		updatedModel.save({
			success : function(record, operation) {
				var resultRecord = operation.getResultSet().records[0];
				me.loadData(theForm, resultRecord);
				theForm.getEl().unmask();
				Thesaurus.ext.utils.msg(me.xSucessLabel, me.xSucessSavedMsg);
				if (theCallback && typeof theCallback == "function") {
					theCallback();
				}
			},
			failure : function(record, operation) {
				Thesaurus.ext.utils.msg(me.xProblemLabel, me.xProblemSaveMsg+" "+operation.error);
				theForm.getEl().unmask();
			}
		});
	},
	
	selectConceptToGroupArray : function(theButton){
		//This method opens a popup to add concepts to the concept group array
		var me = this;
		var theConceptGroupPanel = theButton.up('conceptGroupPanel');
		var theConceptGroupForm = theButton.up('form');
		var theConceptGroupGrid = theConceptGroupPanel.down('#gridConceptGroupPanelConcepts');
		var theConceptGroupStore = theConceptGroupGrid.getStore();
		
		var win = Ext.create('GincoApp.view.SelectConceptWin', {
			thesaurusData : theConceptGroupPanel.thesaurusData.data,
			getChildren : false,
			showTree : false,
			checkstore: theConceptGroupStore,
			listeners: {
                selectBtn: {
                    fn: function(selectedRow) {
                            me.addConceptToGroupArray(selectedRow, theConceptGroupForm, theConceptGroupGrid);
                        }
                }
            }
		});
		win.show();
		
	},
	
	addConceptToGroupArray : function(theSelectedRow,theConceptGroupForm, theConceptGroupGrid){
		var theConceptGroupGridStore = theConceptGroupGrid.getStore();
		
		//Test if already present in the grid
		if (theConceptGroupGridStore.findRecord('identifier', theSelectedRow[0].data.identifier) !== null ){
			Ext.MessageBox.alert(this.xProblemLabel,this.xErrorDoubleRecord);
		} else {
			theConceptGroupGridStore.add(theSelectedRow[0]);
		}
	},
	
	onRemoveConceptFromGroupClick : function(gridview, el, rowIndex, colIndex, e, rec, rowEl) {
		var theConceptGroupGrid = gridview.up('#gridConceptGroupPanelConcepts');
        var theConceptGroupStore = theConceptGroupGrid.getStore();
        theConceptGroupStore.remove(rec);
	},
	
    init:function(){    	  	 
         this.control({
        	'conceptGroupPanel form' : {
 				afterrender : this.loadConceptGroupPanel
 			},
 			'conceptGroupPanel #saveConceptGroup' : {
 				click : this.saveConceptGroup
 			},
            'conceptGroupPanel  #addConceptToGroupArray' : {
                click : this.selectConceptToGroupArray
            },
            'conceptGroupPanel #gridConceptGroupPanelConcepts #conceptToGroupActionColumn' : {
                click : this.onRemoveConceptFromGroupClick
            }
         });

    }
});