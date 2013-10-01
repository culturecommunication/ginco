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

Ext.define('GincoApp.controller.TermPanelController', {
	extend : 'Ext.app.Controller',
	localized : true,
	models : [ 'ThesaurusTermModel' ],
	xLoading : 'Loading',
	xDeleteMsgLabel : 'Do you want to delete this term?',
	xDeleteMsgTitle : 'Delete this term?',
	xSucessLabel : 'Success',
	xSucessSavedMsg : 'Thesaurus Term Successfully Saved',
	xSucessRemovedMsg : 'Thesaurus Term Successfully Deleted',
	xProblemLabel : 'Problem',
	xProblemSaveMsg : 'Impossible to save the term !',
	xProblemDeleteMsg : 'Impossible to delete the term !',
	xProblemLoadMsg : 'Unable to load the term',

	loadPanel : function(theForm) {
		var me = this;
		var termPanel = theForm.up('termPanel');
		var thesaurusData = termPanel.up('thesaurusTabPanel').thesaurusData;
		var model = this.getThesaurusTermModelModel();
		var termId = termPanel.gincoId;
		if (termId != '' && termId != null) {
			theForm.getEl().mask("Chargement");
			model.load(termId, {
				success : function(model) {
					me.loadData(theForm, model);
					theForm.getEl().unmask();
				},
				failure : function(model) {
					Thesaurus.ext.utils.msg(me.xProblemLabel,
							me.xProblemLoadMsg);
					var globalTabs = theForm.up('topTabs');
					globalTabs.remove(termPanel);
				}
			});
		} else {
			model = Ext.create('GincoApp.model.ThesaurusTermModel');
			model.data.thesaurusId = thesaurusData.id;
			model.data.identifier = "";
			model.data.language=thesaurusData.languages[0];
			//1 is the status to set by default for a new term, meaning "validated"
			model.data.status=1;
			theForm.down("#statusCombo").setReadOnly(false);
			theForm.loadRecord(model);
		}
		me.initCustomAttributForm(theForm);
	},

	initCustomAttributForm: function(theForm)
	{
		var termPanel = theForm.up('termPanel');
		var customForm = theForm.down('customattrform');
		customForm.initFields(termPanel.thesaurusData.id, function() {
			if (termPanel.gincoId!='')
			{
				customForm.load(termPanel.gincoId);
			}
		});
	},

	saveCustomFieldAttributes : function(theForm, termId, lang)
	{
		var customForm = theForm.down('#customAttributeForm');
		customForm.save(termId,lang);
	},

	loadData : function(aForm, aModel) {
		var termPanel = aForm.up('termPanel');
		var deleteBtn = aForm.down('#delete');
		var createConceptBtn = aForm.down('#createconcept');
		var displayConcept = aForm.down('#displayConcept');
		termPanel.setTitle("Terme : "+aModel.data.lexicalValue);
		aForm.loadRecord(aModel);
		termPanel.gincoId = aModel.data.identifier;

		if (Ext.isEmpty(aModel.data.conceptId)){
			aForm.down("#statusCombo").setReadOnly(false);
			if (aModel.data.status == 1) {
				//The term isn't attached to any concept and its status is validated
				//We can create a concept from it
				createConceptBtn.setDisabled(false);
			} else {
				createConceptBtn.setDisabled(true);
			}
		} else {
			displayConcept.setDisabled(false);
		}

		if (Ext.isEmpty(aModel.data.conceptId) && aModel.data.status == 2){
			//The term isn't attached to any concept and its status is rejected
			//We can delete it
			deleteBtn.setDisabled(false);
		} else
		{
			deleteBtn.setDisabled(true);
		}

		var noteTab= aForm.up('tabpanel').down('noteTermPanel');
		noteTab.setDisabled(false);
	},

	saveForm : function(theButton, theCallback) {
		var me = this;
		var theForm = theButton.up('form');
		var thePanel = theForm.up('termPanel');
		if (theForm.getForm().isValid()) {
			theForm.getEl().mask(me.xLoading);
			theForm.getForm().updateRecord();
			var updatedModel = theForm.getForm().getRecord();
			updatedModel.save({
				success : function(record, operation) {
					me.saveCustomFieldAttributes(thePanel,record.get("identifier"),record.get("language"));
					me.loadData(theForm, record);
					theForm.getEl().unmask();
					Thesaurus.ext.utils
							.msg(me.xSucessLabel, me.xSucessSavedMsg);
					me.application.fireEvent('termupdated',thePanel.thesaurusData);
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

	deleteForm : function(theButton) {
		var me = this;
		var theForm = theButton.up('form');
		var updatedModel = theForm.getForm().getRecord();
		var globalTabs = theForm.up('#thesaurusItemsTabPanel');
		var thePanel = theForm.up('termPanel');

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

	loadLanguages : function(theCombo) {
		var thePanel = theCombo.up('termPanel');
		var theStore = theCombo.getStore();
		theStore.getProxy().setExtraParam('thesaurusId',
				thePanel.thesaurusData.id);
		theStore.load();
	},

	loadStatus : function(theCombo) {
		var thePanel = theCombo.up('termPanel');
		var theStore = theCombo.getStore();
		theStore.load();
	},

	loadCreateConceptFromTerm : function(theButton) {
		var thePanel = theButton.up('termPanel');
		var theForm = theButton.up('form');
		var theTermModel = theForm.getForm().getRecord();
		var conceptPanel = this.createPanel('GincoApp.view.ConceptPanel',theButton, thePanel.thesaurusData, theTermModel);
	},
	createPanel : function(aType, child, thesaurusData, theTermModel)
	{
		var aNewPanel = Ext.create(aType);
		aNewPanel.initPreferedTermBeforeLoad = theTermModel;
		var topTabs = child.up('#thesaurusItemsTabPanel');
		var tab = topTabs.add(aNewPanel);
		topTabs.setActiveTab(tab);
		tab.show();
		return aNewPanel;
	},

	onDisplayConceptClick : function(theButton) {
		var thePanel = theButton.up('termPanel');
		var theForm = theButton.up('form');
		theModel = theForm.getForm().getRecord();
		var topTabs = Ext.ComponentQuery.query('topTabs')[0];
		topTabs.fireEvent('openconcepttab', topTabs, thePanel.thesaurusData.id,
				theModel.data.conceptId);
	},

	init : function() {
		this.control({
			'termPanel #termForm' : {
				afterrender : this.loadPanel
			},
			'termPanel #saveTerm' : {
				click : this.saveForm
			},
			'termPanel button[cls=delete]' : {
				click : this.deleteForm
			},
			'termPanel #languageCombo' : {
				render : this.loadLanguages
			},
			'termPanel #statusCombo' : {
				render : this.loadStatus
			},
			'termPanel #createconcept' : {
				click : this.loadCreateConceptFromTerm
			},
			'termPanel #displayConcept' : {
				click : this.onDisplayConceptClick
			}
		});
	}
});