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
	models : [ 'ConceptArrayModel', 'ThesaurusConceptReducedModel', 'ThesaurusModel', 'ArrayConceptModel'],

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
        var conceptArrayId = thePanel.gincoId;
		var deleteConceptArrayBtn = thePanel.down('#deleteConceptArray');
        if (conceptArrayId != '' && conceptArrayId != null ) {
    		theForm.getEl().mask("Chargement");
			model.load(conceptArrayId, {
				success : function(model) {
					me.loadData(theForm, model);
					theForm.getEl().unmask();
				},
				failure : function(model) {
					Thesaurus.ext.utils.msg(me.xProblemLabel,
							me.xProblemLoadMsg);
					var globalTabs = theForm.up('thesaurusTabPanel');
					globalTabs.remove(thePanel);
				}
			});
			deleteConceptArrayBtn.setDisabled(false);
		} else {
			thePanel.setTitle(thePanel.title);
			model = Ext.create('GincoApp.model.ConceptArrayModel');
			model.data.thesaurusId = thePanel.up('thesaurusTabPanel').thesaurusData.id;
			model.data.language=thePanel.up('thesaurusTabPanel').thesaurusData.languages[0];
			model.data.identifier = "";
			theForm.loadRecord(model);
		}
	},

	loadData : function(aForm, aModel) {
		var me = this;
		aForm.loadRecord(aModel);
		var thePanel = me.getActivePanel(aForm);
		thePanel.setTitle("Tableau de concepts : "+aModel.data.label);
		thePanel.gincoId = aModel.data.identifier;
		if (aModel.get('superOrdinateId')!="")
		{
			aForm.down('#removeParentConcept').setDisabled(false);
		}
		if (aModel.get('parentArrayId')!="")
		{
			aForm.down('#removeParentArray').setDisabled(false);
		}
		//We get all the concepts included in this concept array
		var arrayConcepts = aModel.concepts().getRange();
        var conceptsGrid = aForm
            .down('#gridPanelConceptArray');
        var conceptsGridStore = conceptsGrid
            .getStore();
        conceptsGridStore.removeAll();
        conceptsGridStore.add(arrayConcepts);
        if (aModel.data.order) {
        	conceptsGridStore.sort('label', 'ASC');
        } else {
        	conceptsGridStore.sort('order', 'ASC');
        }

        var deleteConceptArrayBtn = aForm.down('#deleteConceptArray');
        deleteConceptArrayBtn.setDisabled(false);
        thePanel.addNodePath(aModel.data.thesaurusId);
        thePanel.addNodePath("ARRAYS_"+aModel.data.thesaurusId);
        thePanel.addNodePath(aModel.data.identifier);
        thePanel.setReady();
	},

	loadLanguages : function(theCombo) {
		var theConceptArrayPanel = theCombo.up('conceptArrayPanel');
		var theConceptArrayStore = theCombo.getStore();
		theConceptArrayStore.getProxy().setExtraParam('thesaurusId',
				theConceptArrayPanel.getThesaurusData().id);
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

    	var associatedConcepts = conceptsGridStore.getRange();

        updatedModel.concepts().removeAll();
        updatedModel.concepts().add(associatedConcepts);

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
		var globalTabs = theForm.up('#thesaurusItemsTabPanel');
		var thePanel = me.getActivePanel(theButton);

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

	getActivePanel : function(child) {
		return child.up('conceptArrayPanel');
	},


	selectParentConcept : function(theButton){
		var me = this;
		var theForm = theButton.up('form');
	    var thePanel = me.getActivePanel(theButton);

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
							thesaurusData : thePanel.up('thesaurusTabPanel').thesaurusData,
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

	removeParentConcept : function (theButton){
		var me = this;
		var theForm = theButton.up('form');
		theForm.down('textfield[name="superOrdinateLabel"]').setValue("");
		theForm.down('hidden[name="superOrdinateId"]').setValue("");
		theForm.down('#removeParentConcept').setDisabled(true);
	},

	selectParentArray : function(theButton){
		var me = this;
		var theForm = theButton.up('form');
	    var thePanel =theButton.up('conceptArrayPanel');

	    var win = Ext.create('GincoApp.view.SelectArrayWin', {
			thesaurusData : thePanel.up('thesaurusTabPanel').thesaurusData,
			excludedConceptArrayId : thePanel.gincoId,
			currentParentId : theForm.down('hidden[name="parentArrayId"]').getValue(),
			listeners: {
				selectBtn: {
                fn: function(selectedRow) {
                        me.selectArrayAsParent(selectedRow, theForm);
                    }
            }
        }
	    });
	    win.show();
	},

	selectArrayAsParent : function(selectedRow, theForm){
		theForm.down('textfield[name="parentArrayLabel"]').setValue(selectedRow[0].data.label);
		theForm.down('hidden[name="parentArrayId"]').setValue(selectedRow[0].data.identifier);
		theForm.down('#removeParentArray').setDisabled(false);
	},

	removeParentArray : function (theButton) {
		var me = this;
		var theForm = theButton.up('form');
		theForm.down('textfield[name="parentArrayLabel"]').setValue("");
		theForm.down('hidden[name="parentArrayId"]').setValue("");
		theButton.setDisabled(true);
	},


	selectConceptToArray : function(theButton){
		//This method opens a popup to add concepts to the concept array
		var me = this;
		var theConceptArrayPanel = theButton.up('conceptArrayPanel');
		var theConceptArrayForm = theButton.up('form');
		var theGrid = theConceptArrayPanel.down('#gridPanelConceptArray');
		var theStore = theGrid.getStore();

        var getArrayConcepts = true;
        var arrayId = theConceptArrayPanel.gincoId ;

		var win = Ext.create('GincoApp.view.SelectConceptWin', {
            thesaurusData : theConceptArrayPanel.up('thesaurusTabPanel').thesaurusData,
            conceptId : theConceptArrayForm.down('hidden[name="superOrdinateId"]').value,
            showTree : false,
            getArrayConcepts : getArrayConcepts,
			arrayId : arrayId,
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
			selectedRow[0].setDirty();

			var maxOrder = -1;
			if (theConceptArrayGridStore.getCount() > 0) {
				 maxOrder =	theConceptArrayGridStore.max( "order") +1;
			} else {
				maxOrder = maxOrder +1;
			}
			var conceptModel = Ext.create('GincoApp.model.ArrayConceptModel');
			conceptModel.set('label',selectedRow[0].get('label'));
			conceptModel.set('identifier',selectedRow[0].get('identifier'));
			conceptModel.set('order', maxOrder);

			theConceptArrayGridStore.add(conceptModel);
		}
	},

	selectConceptAsParent : function(selectedRow, theForm){
		var me = this;
		var oldSuperOrdinate = theForm.down('hidden[name="superOrdinateId"]').getValue();

		theForm.down('textfield[name="superOrdinateLabel"]').setValue(selectedRow[0].data.label);
		theForm.down('hidden[name="superOrdinateId"]').setValue(selectedRow[0].data.identifier);
		theForm.down('#removeParentConcept').setDisabled(false);
		if (selectedRow[0].data.identifier != oldSuperOrdinate) {
			var theGrid = theForm.down('#gridPanelConceptArray');
			var theGridStore = theGrid.getStore();
			theGridStore.removeAll();
		}
	},

	onRemoveConceptClick : function(gridview, el, rowIndex, colIndex, e, rec, rowEl) {
	        var theGrid = gridview.up('#gridPanelConceptArray');
	        var theStore = theGrid.getStore();
	        var removedOrder = rec.data.order;
	        theStore.remove(rec);

	        //recalculate order
	        var arrayConcepts = theStore.getRange();
			Ext.Array.each(arrayConcepts, function(arrayConcept) {
	        	if(parseInt(arrayConcept.data.order) > parseInt(removedOrder)) {
	        		arrayConcept.data.order = arrayConcept.data.order -1;
	        	}
			});
	},

	onConceptDblClick: function(theGrid, record, item, index, e, eOpts ) {
	    	var me = this;
	        var thePanel = me.getActivePanel(theGrid);
	        var topTabs = Ext.ComponentQuery.query('thesaurusTabs')[0];
			topTabs.fireEvent('openconcepttab',topTabs, thePanel.up('thesaurusTabPanel').thesaurusData.id ,record.data.identifier);
	},


    init:function(){
         this.control({
        	'conceptArrayPanel form' : {
 				afterrender : this.loadConceptArrayPanel
 			},
 			'conceptArrayPanel #saveConceptArray' : {
 				click : this.saveConceptArray
 			},
            'conceptArrayPanel  #selectParentArray' : {
                click : this.selectParentArray
            },
            'conceptArrayPanel  #selectParentConcept' : {
            	click : this.selectParentConcept
            },
            'conceptArrayPanel  #removeParentArray' : {
                click : this.removeParentArray
            },
            'conceptArrayPanel  #removeParentConcept' : {
                click : this.removeParentConcept
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