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

Ext.define('GincoApp.controller.NotePanelController', {
	extend : 'Ext.app.Controller',
	localized : true,

	xLoading : 'Loading',
	xSucessLabel : 'Success!',
	xSucessSavedMsg : 'Note saved successfully',
	xProblemLabel : 'Error !',
	xProblemSaveMsg : 'Unable to save this note!',

	onRenderGrid : function(theGrid) {
		if (theGrid.up('conceptPanel') != null) {
			var theConceptId = theGrid.up('conceptPanel').gincoId;
			theGrid.getStore().getProxy().setExtraParam('conceptId', theConceptId);
		} else {
			var theTermId = theGrid.up('termPanel').gincoId;
			theGrid.getStore().getProxy().setExtraParam('termId', theTermId);
		}
		theGrid.getStore().load();

	},

	newNoteBtn : function(theButton){
		var theGrid = theButton.up('gridpanel');
		var win = this.createNoteWindow(theGrid);
		var theForm = win.down('form');
		var model = Ext.create('GincoApp.model.ThesaurusNoteModel');
		model.data.language=win.thesaurusData.languages[0];
		theForm.loadRecord(model);
		win.show();
	},

	loadLanguages : function(theCombo) {
		var thePanel = theCombo.up('createNoteWin');
		var theStore = theCombo.getStore();
		var theForm = thePanel.down('form');
		theStore.getProxy().setExtraParam('thesaurusId', thePanel.thesaurusData.id);
		theStore.load({
			callback: function (theStore, aOperation){
				theForm.loadRecord(theForm.getRecord());
			}
		});
	},

	saveNoteWin : function(theButton) {
		var theForm = theButton.up('form');
		var theWin = theButton.up('createNoteWin');
		theForm.getForm().updateRecord();
		var updatedModel = theForm.getForm().getRecord();
		if (theWin.store.findRecord('identifier', updatedModel.data.identifier) == null ){
			theWin.store.add(updatedModel);
		}
	},

	onDeleteNote : function (gridview, el, rowIndex, colIndex, e, rec, rowEl) {
		var theGrid = gridview.up('gridpanel');
        var theStore = theGrid.getStore();
        theStore.remove(rec);
        theGrid.up('panel').down('button[itemId=saveNote]').setDisabled(false);
	},

	createNoteWindow : function (theGrid) {
		var win = null;
		var me=this;
		if (theGrid.up('conceptPanel') != null) {
			//we are editing a note for a concept
			win = Ext.create('GincoApp.view.CreateNoteWin', {
				storeNoteTypes : Ext.create('GincoApp.store.ConceptNoteTypeStore'),
				thesaurusData : theGrid.up('thesaurusTabPanel').thesaurusData,
				listeners: {
					saveNoteButton: function (theButton){
						me.afterSavingNewNote(theGrid, theButton);
					}
				}
					});
		} else {
			//we are editing a note for a term
			win = Ext.create('GincoApp.view.CreateNoteWin', {
				storeNoteTypes : Ext.create('GincoApp.store.TermNoteTypeStore'),
				thesaurusData : theGrid.up('thesaurusTabPanel').thesaurusData,
				listeners: {
					saveNoteButton: function (theButton){
						me.afterSavingNewNote(theGrid, theButton);
					}
				}
					});
		}
		win.store = theGrid.getStore();
		return win;
	},

	afterSavingNewNote : function(theGrid, theButton) {
		this.saveNoteWin(theButton);
		theGrid.up('panel').down('button[itemId=saveNote]').setDisabled(false);
	},

	onNoteDblClick : function(theGridView, record, item, index, e, eOpts ) {
		var theGrid = theGridView.up('gridpanel');
		var win = this.createNoteWindow(theGrid);
		var theForm = win.down('form');
		theForm.loadRecord(record);
		win.show();
    },

	saveNoteBtn : function(theButton,theCallback) {
		var me=this;
		var theGrid = theButton.up('panel').down('gridpanel');
		var thePanel = theButton.up('panel');
		thePanel.getEl().mask(me.xLoading);
		theGrid.getStore().sync({
			success : function(model, operation) {
				thePanel.getEl().unmask();
				Thesaurus.ext.utils.msg(me.xSucessLabel, me.xSucessSavedMsg);
				thePanel.down('button[itemId=saveNote]').setDisabled(true);
				if (theCallback && typeof theCallback == "function") {
					theCallback();
				}
			},
			failure : function(model, operation) {
				Thesaurus.ext.utils.msg(me.xProblemLabel, me.xProblemSaveMsg);
				thePanel.getEl().unmask();
			}
		});
	},

	init : function() {
		this.control({
			'notePanel #notegrid' : {
 				render : this.onRenderGrid,
 				itemdblclick : this.onNoteDblClick
 			},
			'notePanel #saveNote' : {
 				click : this.saveNoteBtn
 			},
			'notePanel #newNoteBtn' : {
				click : this.newNoteBtn
			},
			'createNoteWin #languageCombo' : {
				render : this.loadLanguages
			},
			'notePanel #noteActionColumn' : {
                click : this.onDeleteNote
            }
		});
	}
});