/**
 * Copyright or © or Copr. Ministère Français chargé de la Culture
 * et de la Communication (2013)
 * <p/>
 * contact.gincoculture_at_gouv.fr
 * <p/>
 * This software is a computer program whose purpose is to provide a thesaurus
 * management solution.
 * <p/>
 * This software is governed by the CeCILL license under French law and
 * abiding by the rules of distribution of free software. You can use,
 * modify and/ or redistribute the software under the terms of the CeCILL
 * license as circulated by CEA, CNRS and INRIA at the following URL
 * "http://www.cecill.info".
 * <p/>
 * As a counterpart to the access to the source code and rights to copy,
 * modify and redistribute granted by the license, users are provided only
 * with a limited warranty and the software's author, the holder of the
 * economic rights, and the successive licensors have only limited liability.
 * <p/>
 * In this respect, the user's attention is drawn to the risks associated
 * with loading, using, modifying and/or developing or reproducing the
 * software by the user in light of its specific status of free software,
 * that may mean that it is complicated to manipulate, and that also
 * therefore means that it is reserved for developers and experienced
 * professionals having in-depth computer knowledge. Users are therefore
 * encouraged to load and test the software's suitability as regards their
 * requirements in conditions enabling the security of their systemsand/or
 * data to be ensured and, more generally, to use and operate it in the
 * same conditions as regards security.
 * <p/>
 * The fact that you are presently reading this means that you have had
 * knowledge of the CeCILL license and that you accept its terms.
 */

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
	xSucessRemovedMsg: 'Concept array removed successfully',
	xProblemSaveMsg : 'Unable to save this array!',
	xChangingParentWarningMsgTitle : 'Warning',
	xChangingParentWarningMsgLabel : 'Changing the parent concept will erase the children, please confirm',
	xDeleteMsgLabel : 'Are you sure you want to delete this concept array?',
	xDeleteMsgTitle : 'Delete this concept array?',
	
	
	loadConceptArrayPanel : function(theForm){
        var me = this;
		var thePanel = theForm.up('conceptArrayPanel');
        var model = this.getConceptArrayModelModel();
        var conceptArrayId = thePanel.conceptArrayId;
		var deleteConceptArrayBtn = thePanel.down('#deleteConceptArray');
        if (conceptArrayId != '') {    		
    		theForm.getEl().mask("Chargement");
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
			deleteConceptArrayBtn.setDisabled(false);
		} else {
			thePanel.setTitle(thePanel.title+' : '+thePanel.thesaurusData.title);
			model = Ext.create('GincoApp.model.ConceptArrayModel');
			model.data.thesaurusId = thePanel.thesaurusData.id;
			model.data.language=thePanel.thesaurusData.languages[0];
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
        var deleteConceptArrayBtn = aForm.down('#deleteConceptArray');
        deleteConceptArrayBtn.setDisabled(false);
	},
	
	loadLanguages : function(theCombo) {
		var theConceptArrayPanel = theCombo.up('conceptArrayPanel');
		var theConceptArrayStore = theCombo.getStore();
		theConceptArrayStore.getProxy().setExtraParam('thesaurusId',
				theConceptArrayPanel.thesaurusData.id);
		theConceptArrayStore.load();
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
				me.application.fireEvent('conceptarrayupdated');
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
	
	deleteConceptArray: function(theButton){
		var me = this;
		var theForm = theButton.up('form');
		var globalTabs = theForm.up('topTabs');
		var thePanel = me.getActivePanel();
		
		var updatedModel = theForm.getForm().getRecord();      
        
        Ext.MessageBox.show({
    			title : me.xDeleteMsgTitle,
    			msg : me.xDeleteMsgLabel,
    			buttons : Ext.MessageBox.YESNO,
    			fn : function(buttonId) {
    				switch (buttonId) {
    				case 'no':
    					break;
    				case 'yes':
    					updatedModel.destroy({
    						success : function(record, operation) {
    							Thesaurus.ext.utils.msg(me.xSucessLabel,
    									me.xSucessRemovedMsg);
    							me.application.fireEvent('conceptarraydeleted');
    							globalTabs.remove(thePanel);
    						},
    						failure : function(record, operation) {
    							Thesaurus.ext.utils.msg(me.xProblemLabel,
    									operation.error);
    						}
    					});
    					break;    				
    				}
    			},
    			scope : this
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
                            searchOrphans : false,
							showTree : false,
							onlyValidatedConcepts : true,
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

        var searchOrphans = null;
        var getChildren = true;
        if(theConceptArrayForm.down('textfield[name="superOrdinateId"]').value == "") {
            searchOrphans = false;
            getChildren = false;
        }

		var win = Ext.create('GincoApp.view.SelectConceptWin', {
            thesaurusData : theConceptArrayPanel.thesaurusData,
            conceptId : theConceptArrayForm.down('textfield[name="superOrdinateId"]').value,
            searchOrphans : searchOrphans,
            getChildren : getChildren,
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
	        var topTabs = Ext.ComponentQuery.query('topTabs')[0];
			topTabs.fireEvent('openconcepttab',topTabs,this.getThesaurusModelModel(), thePanel.thesaurusData.id ,record.data.identifier);
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
            },
 			'conceptArrayPanel #deleteConceptArray' : {
 				click : this.deleteConceptArray
 			},
			'conceptArrayPanel #conceptArrayLanguages' : {
				render : this.loadLanguages
			}
         });

    }
});