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

Ext.define('GincoApp.controller.ThesaurusVersionPanelController', {
	extend : 'Ext.app.Controller',
	localized : true,
	
	xLoading : 'Loading',
	xSucessLabel : 'Success!',
	xSucessSavedMsg : 'Versions saved successfully',
	xProblemLabel : 'Error !',
	
	onRenderGrid : function(theGrid) {
		var thesaurusId = theGrid.up('thesaurusTabPanel').thesaurusData.id;
		theGrid.getStore().getProxy().setExtraParam('thesaurusId', thesaurusId);
		theGrid.getStore().load();
	},
	
	onUpdateVersionToTheGrid : function( editor, e, eOpts){
		var theGrid = e.grid;
		theGrid.up('form').down('button[itemId=saveThesaurusVersion]').setDisabled(false);
	},
	
	saveThesaurusVersion : function(theButton,theCallback) {
		var me=this;
		var theGrid = theButton.up('thesaurusVersionPanel').down('gridpanel');
		var thePanel = theButton.up('thesaurusVersionPanel');

		thePanel.getEl().mask(me.xLoading);
		theGrid.getStore().sync({
			success : function(model, operation) {
				thePanel.getEl().unmask();
				Thesaurus.ext.utils.msg(me.xSucessLabel, me.xSucessSavedMsg);
				thePanel.down('button[itemId=saveThesaurusVersion]').setDisabled(true);
				theGrid.getStore().load();
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
	
	onThisVersionCheckBoxClick : function(theButton) {
		theButton.up('form').down('button[itemId=saveThesaurusVersion]').setDisabled(false);
	},
	
	createThesaurusVersion : function(theButton) {
			var me = this;
			var theGrid = theButton.up('form').down('#versionGrid');
			win = Ext.create('GincoApp.view.CreateVersionWin', {
				listeners: {
					saveVersionButton: function (theButton){
						me.afterSavingNewVersion(theGrid, theButton);
					}
				}
			});
			var theForm = win.down('form');
			var model = Ext.create('GincoApp.model.ThesaurusVersionModel');
			model.data.status = 0;
			theForm.loadRecord(model);
			win.show();
	},
	
	afterSavingNewVersion : function(theGrid, theButton) {
		var theForm = theButton.up('form');
		theForm.getForm().updateRecord();
		var updatedModel = theForm.getForm().getRecord();
		updatedModel.data.thesaurusId = theGrid.up('thesaurusTabPanel').thesaurusData.id ;
		theGrid.getStore().add(updatedModel);
		theGrid.up('form').down('button[itemId=saveThesaurusVersion]').setDisabled(false);
	},
	
	onVersionPanelActivate: function (thePanel)
	{
		var theGrid = thePanel.down('#versionGrid');
		this.onRenderGrid(theGrid);
	},

	init : function() {
		this.control({
			'thesaurusVersionPanel': {
				activate: this.onVersionPanelActivate
			},				
			'thesaurusVersionPanel #versionGrid' : {
 				validateedit : this.onUpdateVersionToTheGrid
 			},
 			'thesaurusVersionPanel #saveThesaurusVersion' : {
 				click : this.saveThesaurusVersion
			},
			'thesaurusVersionPanel #thisVersionCheckbox' : {
				click : this.onThisVersionCheckBoxClick
			},
			'thesaurusVersionPanel #createThesaurusVersion' : {
				click : this.createThesaurusVersion
			}
		});
		
	}
});