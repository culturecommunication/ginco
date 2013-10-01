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

Ext.define('GincoApp.controller.ComplexConceptPanelController', {
	extend : 'Ext.app.Controller',
	localized : true,
	models : [ 'SplitNonPreferredTermModel' ],

	xLoading : 'Loading',
	xDeleteMsgLabel : 'Do you want to delete this complex concept?',
	xDeleteMsgTitle : 'Delete this complex concept?',
	xSucessLabel : 'Success',
	xSucessSavedMsg : 'Complex concept Successfully Saved',
	xSucessRemovedMsg : 'Complex concept Successfully Deleted',
	xProblemLabel : 'Problem',
	xProblemSaveMsg : 'Impossible to save the complex concept !',
	xProblemDeleteMsg : 'Impossible to delete the Complex concept !',
	xProblemLoadMsg : 'Unable to load the complex concept',

	loadPanel : function(theForm) {
		var me = this;
		var thePanel = theForm.up('complexconceptPanel');
		var thesPanel = theForm.up('thesaurusTabPanel');
		var thesaurusData = thesPanel.thesaurusData;

		var model = this.getSplitNonPreferredTermModelModel();
		var termId = thePanel.gincoId;
		if (termId != '' && termId!=null) {
			theForm.getEl().mask("Chargement");
			model.load(termId, {
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
			model = Ext.create('GincoApp.model.SplitNonPreferredTermModel');
			model.data.thesaurusId = thePanel.thesaurusData.id;
			model.data.identifier = "";
			model.data.language=thesaurusData.languages[0];
			//1 is the status to set by default for a new term, meaning "validated"
			model.data.status=1;
			theForm.down("#statusCombo").setReadOnly(false);
			theForm.loadRecord(model);
		}
	},

	deleteForm : function(theButton) {
		var me = this;
		var theForm = theButton.up('form');
		var updatedModel = theForm.getForm().getRecord();
		var globalTabs = theForm.up('topTabs');
		var thePanel = theForm.up('complexconceptPanel');

		Ext.MessageBox.show({
			title : me.xDeleteMsgTitle,
			msg : me.xDeleteMsgLabel,
			buttons : Ext.MessageBox.YESNOCANCEL,
			fn : function(buttonId) {
				switch (buttonId) {
				case 'no':
					break; // manually removes tab from tab panel
				case 'yes':
					updatedModel.destroy({
						success : function(record, operation) {
							Thesaurus.ext.utils.msg(me.xSucessLabel,
									me.xSucessRemovedMsg);
							me.application.fireEvent('termdeleted',thePanel.thesaurusData);
							globalTabs.remove(thePanel);
						},
						failure : function(record, operation) {
							Thesaurus.ext.utils.msg(me.xProblemLabel,
									operation.error);
						}
					});
					break;
				case 'cancel':
					break; // leave blank if no action required on
				// cancel
				}
			},
			scope : this
		});
	},

	loadData : function(aForm, aModel) {
		var termPanel = aForm.up('complexconceptPanel');
		var deleteBtn = aForm.down('#delete');
		termPanel.setTitle("Concept complexe : "+aModel.data.lexicalValue);
		aForm.setTitle(aModel.data.lexicalValue);
		aForm.loadRecord(aModel);
		termPanel.gincoId = aModel.data.identifier;
		var preferredTerms = aModel.preferredTerms().getRange();
		var theGridStore = aForm.down('#gridPanelPreferredTerms').getStore();
		theGridStore.removeAll();
		theGridStore.add(preferredTerms);
		if (Ext.isEmpty(aModel.data.conceptId) && aModel.data.status == 2){
			//The term isn't attached to any concept and its status is rejected
			//We can delete it
			deleteBtn.setDisabled(false);
		}
	},
	loadLanguages : function(theCombo) {
		var thePanel = theCombo.up('complexconceptPanel');
		var theStore = theCombo.getStore();
		theStore.getProxy().setExtraParam('thesaurusId',
				thePanel.thesaurusData.id);
		theStore.load();
	},

	loadStatus : function(theCombo) {
		var theStore = theCombo.getStore();
		theStore.load();
	},

	saveForm: function(theButton, theCallback)
	{
		var me = this;
		var theForm = theButton.up('form');
		var thePanel = theForm.up('complexconceptPanel');
		var thesPanel = theForm.up('thesaurusTabPanel');
		if (theForm.getForm().isValid()) {
			theForm.getEl().mask(me.xLoading);
			theForm.getForm().updateRecord();
			var updatedModel = theForm.getForm().getRecord();
			var theGridStore = theForm.down('#gridPanelPreferredTerms').getStore();
			var termsData = theGridStore.getRange();
			updatedModel.preferredTerms().removeAll();
			updatedModel.preferredTerms().add(termsData);
			updatedModel.save({
				success : function(record, operation) {
					var resultRecord = operation
					.getResultSet().records[0];
					me.loadData(theForm, resultRecord);
					theForm.getEl().unmask();
					Thesaurus.ext.utils
							.msg(me.xSucessLabel, me.xSucessSavedMsg);
					me.application.fireEvent('termupdated',thesPanel.thesaurusData);
					if (theCallback && typeof theCallback == "function") {
						theCallback();
					}
				},
				failure : function(record, operation) {
					Thesaurus.ext.utils.msg(me.xProblemLabel,
							me.xProblemSaveMsg +" "+operation.error);
					theForm.getEl().unmask();
				}
			});
		}
	},
	addPreferredTerm :function(theButton)
	{
		var me = this;
		var thePanel = theButton.up('complexconceptPanel');
		var win = Ext.create('GincoApp.view.SelectTermWin', {
			onlyValidatedTerms : true,
			existingPreferredTerms : true
		});
		var theGrid = theButton.up('#gridPanelPreferredTerms');
		win.conceptGrid = theGrid;
		win.store = theGrid.getStore();
		win.thesaurusData = thePanel.thesaurusData;
		win.prefered = true;
		win.show();
	},

	preferredTermDeleteAction : function(gridview, el, rowIndex,
			colIndex, e, rec, rowEl) {
		var theStore = gridview.up('#gridPanelPreferredTerms').getStore();
		theStore.remove(rec);
	},

	onPreferredTermDblClick : function(theGrid, record, item, index, e,
			eOpts) {
		var me = this;
		var thePanel = theGrid.up('complexconceptPanel');
		var topTabs = Ext.ComponentQuery.query('topTabs')[0];
		topTabs.fireEvent('opentermtab', topTabs, thePanel.thesaurusData.id,
				record.data.identifier);
	},

	init : function() {
		this.control({
			'complexconceptPanel #termForm' : {
				afterrender : this.loadPanel
			},
			'complexconceptPanel #saveTerm' : {
				click : this.saveForm
			},
			'complexconceptPanel #delete' : {
				click : this.deleteForm
			},
            'complexconceptPanel  #addPreferredTerm' : {
                click : this.addPreferredTerm
            },
			'complexconceptPanel #languageCombo' : {
				render : this.loadLanguages
			},
			'complexconceptPanel #statusCombo' : {
				render : this.loadStatus
			},
			'complexconceptPanel #preferredTermDeleteAction' : {
				click : this.preferredTermDeleteAction
			},
			'complexconceptPanel #gridPanelPreferredTerms' : {
				itemdblclick : this.onPreferredTermDblClick
			},
		});
	}
});