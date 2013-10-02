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
    xDeleteMsgLabel : 'Are you sure you want to delete this concept group?',
	xDeleteMsgTitle : 'Delete this concept group?',

	loadConceptGroupPanel : function(theForm){
        var me = this;
        var theConceptGroupPanel = theForm.up('conceptGroupPanel');
        var model = this.getConceptGroupModelModel();
        var conceptGroupId = theConceptGroupPanel.gincoId;

        if (conceptGroupId != '' && conceptGroupId!=null) {
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
					var globalTabs = theForm.up('#thesaurusItemsTabPanel');
					globalTabs.remove(thePanel);
				}
			});

        } else {
        	theConceptGroupPanel.setTitle(theConceptGroupPanel.title);
        	model = Ext.create('GincoApp.model.ConceptGroupModel');
        	model.data.thesaurusId = theConceptGroupPanel.up('thesaurusTabPanel').thesaurusData.id;
        	model.data.language=theConceptGroupPanel.up('thesaurusTabPanel').thesaurusData.languages[0];
        	theForm.loadRecord(model);
        }
	},

	loadData : function(aForm, aModel) {
		var me = this;
		aForm.loadRecord(aModel);
		var theConceptGroupPanel = me.getActivePanel(aForm);
		theConceptGroupPanel.setTitle("Groupe : "+aModel.data.label);
		theConceptGroupPanel.gincoId = aModel.data.identifier;
		if (aModel.get('parentGroupId')!="")
		{
			aForm.down('#removeParentGroup').setDisabled(false);
		}
		//We get all the concepts included in this concept group
		var conceptsGrid  = aForm.down('#gridConceptGroupPanelConcepts');
		var conceptsGridStore = conceptsGrid.getStore();
		conceptsGridStore.getProxy().extraParams = {
            conceptIds: aModel.raw.concepts
        };
		conceptsGridStore.load();

		var deleteConceptGroupBtn = aForm.down('#deleteConceptGroupBtn');
		deleteConceptGroupBtn.setDisabled(false);

	},

	loadLanguages : function(theCombo) {
		var theConceptGroupPanel = theCombo.up('conceptGroupPanel');
		var theConceptGroupStore = theCombo.getStore();
		theConceptGroupStore.getProxy().setExtraParam('thesaurusId',
				theConceptGroupPanel.thesaurusData.id);
		theConceptGroupStore.load();
	},

	getActivePanel : function(child) { 
		return child.up('conceptGroupPanel');
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
				me.application.fireEvent('conceptgroupupdated');
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

	deleteConceptGroup : function(theButton){
		var me = this;
		var theConceptGroupForm = theButton.up('form');
		var globalTabs = theConceptGroupForm.up('#thesaurusItemsTabPanel');
		var theConceptGroupPanel = me.getActivePanel(theButton);

		var updatedModel = theConceptGroupForm.getForm().getRecord();

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
    							me.application.fireEvent('conceptgroupdeleted');
    							globalTabs.remove(theConceptGroupPanel);
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

	selectConceptToGroupArray : function(theButton){
		//This method opens a popup to add concepts to the concept group array
		var me = this;
		var theConceptGroupPanel = theButton.up('conceptGroupPanel');
		var theConceptGroupForm = theButton.up('form');
		var theConceptGroupGrid = theConceptGroupPanel.down('#gridConceptGroupPanelConcepts');
		var theConceptGroupStore = theConceptGroupGrid.getStore();

		var getGroupConcepts = true;
		var groupId = theConceptGroupPanel.gincoId ;

		var win = Ext.create('GincoApp.view.SelectConceptWin', {
			thesaurusData : theConceptGroupPanel.thesaurusData,
			getChildren : false,
			showTree : false,
			getGroupConcepts : getGroupConcepts,
			groupId : groupId,
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
			theSelectedRow[0].setDirty();
			theConceptGroupGridStore.add(theSelectedRow[0]);
		}
	},

	onRemoveConceptFromGroupClick : function(gridview, el, rowIndex, colIndex, e, rec, rowEl) {
		var theConceptGroupGrid = gridview.up('#gridConceptGroupPanelConcepts');
        var theConceptGroupStore = theConceptGroupGrid.getStore();
        theConceptGroupStore.remove(rec);
	},

	onConceptDblClick: function(theGrid, record, item, index, e, eOpts ) {
	        var thePanel = theGrid.up('conceptGroupPanel');
	        var topTabs = Ext.ComponentQuery.query('thesaurusTabs')[0];
			topTabs.fireEvent('openconcepttab',topTabs, thePanel.up('thesaurusTabPanel').thesaurusData.id ,record.data.identifier);
	    },

    selectParentGroup : function(theButton){
		var me = this;
		var theForm = theButton.up('form');
	    var thePanel =theButton.up('conceptGroupPanel');

	    var win = Ext.create('GincoApp.view.SelectGroupWin', {
			thesaurusData : thePanel.thesaurusData,
			excludedConceptGroupId : thePanel.gincoId,
			currentParentId : theForm.down('hidden[name="parentGroupId"]').getValue(),
			listeners: {
				selectBtn: {
                fn: function(selectedRow) {
                        me.selectGroupAsParent(selectedRow, theForm);
                    }
            }
        }
	    });
	    win.show();
	},

	selectGroupAsParent : function(selectedRow, theForm){
		theForm.down('textfield[name="parentGroupLabel"]').setValue(selectedRow[0].data.label);
		theForm.down('hidden[name="parentGroupId"]').setValue(selectedRow[0].data.identifier);
		theForm.down('#removeParentGroup').setDisabled(false);
	},

	removeParentGroup : function(theButton) {
		var me = this;
		var theForm = theButton.up('form');
		theForm.down('textfield[name="parentGroupLabel"]').setValue("");
		theForm.down('hidden[name="parentGroupId"]').setValue("");
		theForm.down('#removeParentGroup').setDisabled(true);
	},

    init:function(){
         this.control({
        	'conceptGroupPanel form' : {
 				afterrender : this.loadConceptGroupPanel
 			},
 			'conceptGroupPanel #saveConceptGroup' : {
 				click : this.saveConceptGroup
 			},
 			'conceptGroupPanel #deleteConceptGroupBtn' : {
 				click : this.deleteConceptGroup
 			},
            'conceptGroupPanel  #selectParentGroup' : {
                click : this.selectParentGroup
            },
            'conceptGroupPanel  #removeParentGroup' : {
                click : this.removeParentGroup
            },
            'conceptGroupPanel  #addConceptToGroupArray' : {
                click : this.selectConceptToGroupArray
            },
            'conceptGroupPanel #gridConceptGroupPanelConcepts' : {
            	itemdblclick : this.onConceptDblClick
            },
            'conceptGroupPanel #gridConceptGroupPanelConcepts #conceptToGroupActionColumn' : {
                click : this.onRemoveConceptFromGroupClick
            },
			'conceptGroupPanel #conceptGroupLanguages' : {
				render : this.loadLanguages
			}
         });

    }
});