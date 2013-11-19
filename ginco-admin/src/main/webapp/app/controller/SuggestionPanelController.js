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

Ext.define('GincoApp.controller.SuggestionPanelController', {
	extend : 'Ext.app.Controller',
	localized : true,

	xLoading : 'Loading',
	xSucessLabel : 'Success!',
	xSucessSavedMsg : 'Suggestion saved successfully',
	xProblemLabel : 'Error !',
	xProblemSaveMsg : 'Unable to save this suggestion!',

	onRenderSuggestionGrid : function(theGrid) {
		if (theGrid.up('conceptPanel') != null){
			var theConceptId = theGrid.up('conceptPanel').gincoId;
			theGrid.getStore().getProxy().setExtraParam('conceptId', theConceptId);
			theGrid.getStore().getProxy().setExtraParam('termId', "");
		} else if (theGrid.up('termPanel') != null){
			var theTermId = theGrid.up('termPanel').gincoId;
			theGrid.getStore().getProxy().setExtraParam('termId', theTermId);
			theGrid.getStore().getProxy().setExtraParam('conceptId', "");
		}
		theGrid.getStore().load();
	},
	
	newSuggestionBtn : function(theButton){
		var theGrid = theButton.up('gridpanel');
		var win = this.createSuggestionWindow(theGrid);
		var theForm = win.down('form');
		var model = Ext.create('GincoApp.model.SuggestionModel');
		theForm.loadRecord(model);
		win.show();
	},
	
	createSuggestionWindow : function (theGrid) {
		var win = null;
		var me=this;
			win = Ext.create('GincoApp.view.CreateSuggestionWin', {
				thesaurusData : theGrid.up('thesaurusTabPanel').thesaurusData,
				listeners: {
					saveSuggestionButton: function (theButton){
						me.afterSavingNewSuggestion(theGrid, theButton);
					}
				}
		});
		
		win.store = theGrid.getStore();
		return win;
	},
	
	afterSavingNewSuggestion : function(theGrid, theButton) {
		this.saveSuggestionWin(theButton);
		theGrid.up('panel').down('button[itemId=saveSuggestion]').setDisabled(false);
	},
	
	saveSuggestionWin : function(theButton) {
		var theForm = theButton.up('form');
		var theWin = theButton.up('createSuggestionWin');
		theForm.getForm().updateRecord();
		var updatedModel = theForm.getForm().getRecord();
		updatedModel.set('creator',Thesaurus.ext.utils.userInfo.data.username);
		updatedModel.set('thesaurusId',theWin.thesaurusData.id);
		if (theWin.store.findRecord('identifier', updatedModel.data.identifier) == null ){
			theWin.store.add(updatedModel);
		}
	},
	
	saveSuggestionBtn : function(theButton,theCallback) {
		var me=this;
		var theGrid = theButton.up('panel').down('gridpanel');
		var thePanel = theButton.up('panel');
		thePanel.getEl().mask(me.xLoading);
		theGrid.getStore().sync({
			success : function(model, operation) {
				thePanel.getEl().unmask();
				Thesaurus.ext.utils.msg(me.xSucessLabel, me.xSucessSavedMsg);
				thePanel.down('button[itemId=saveSuggestion]').setDisabled(true);
				if (theCallback && typeof theCallback == "function") {
					theCallback();
				}
			},
			failure : function(model, operation) {
				Thesaurus.ext.utils.msg(me.xProblemLabel, model.exceptions[0].error);
				thePanel.getEl().unmask();
			}
		});
	},
	
	onSuggestionDblClick : function(theGridView, record, item, index, e, eOpts ) {
		var theGrid = theGridView.up('gridpanel');
		var win = this.createSuggestionWindow(theGrid);
		var theForm = win.down('form');
		theForm.loadRecord(record);
		win.show();
    },
	
	init : function() {
		this.control({
			'suggestionPanel #suggestiongrid' : {
 				render : this.onRenderSuggestionGrid,
 				itemdblclick : this.onSuggestionDblClick
 			},
 			'suggestionPanel #newSuggestionBtn' : {
 				click : this.newSuggestionBtn
 			},
 			'suggestionPanel #saveSuggestion' : {
 				click : this.saveSuggestionBtn
 			}
		});
	}
});