/**
 * Copyright or © or Copr. Ministère Français chargé de la Culture et de la
 * Communication (2013) <p/> contact.gincoculture_at_gouv.fr <p/> This software
 * is a computer program whose purpose is to provide a thesaurus management
 * solution. <p/> This software is governed by the CeCILL license under French
 * law and abiding by the rules of distribution of free software. You can use,
 * modify and/ or redistribute the software under the terms of the CeCILL
 * license as circulated by CEA, CNRS and INRIA at the following URL
 * "http://www.cecill.info". <p/> As a counterpart to the access to the source
 * code and rights to copy, modify and redistribute granted by the license,
 * users are provided only with a limited warranty and the software's author,
 * the holder of the economic rights, and the successive licensors have only
 * limited liability. <p/> In this respect, the user's attention is drawn to the
 * risks associated with loading, using, modifying and/or developing or
 * reproducing the software by the user in light of its specific status of free
 * software, that may mean that it is complicated to manipulate, and that also
 * therefore means that it is reserved for developers and experienced
 * professionals having in-depth computer knowledge. Users are therefore
 * encouraged to load and test the software's suitability as regards their
 * requirements in conditions enabling the security of their systemsand/or data
 * to be ensured and, more generally, to use and operate it in the same
 * conditions as regards security. <p/> The fact that you are presently reading
 * this means that you have had knowledge of the CeCILL license and that you
 * accept its terms.
 */

Ext.define('GincoApp.controller.ConceptController', {
	extend : 'Ext.app.Controller',

	stores : ['MainTreeStore', 'SimpleConceptStore', 'AssociationStore',
			'AssociationRoleStore', 'HierarchicalAssociationStore',
			'AlignmentsStore'],
	models : ['ConceptModel', 'ThesaurusModel', 'SimpleConceptModel',
			'AssociationModel', 'HierarchicalAssociationModel',
			'AlignmentModel', 'ExternalThesaurusModel'],

	localized : true,
	_myAppGlobal : this,
	refs : [{
				ref : 'topTabs',
				selector : 'thesaurusTabs'
			}],

	xLoading : 'Loading',
	xDeleteMsgLabel : 'Are you sure to delete this concept?',
	xDeleteMsgTitle : 'Delete this concept?',
	xAlreadyExistingTerm : 'This term already exists...',
	xSucessLabel : 'Success!',
	xSucessSavedMsg : 'Concept saved successfully',
	xSucessRemovedMsg : 'Concept removed successfully',
	xProblemLabel : 'Error !',
	xProblemSaveMsg : 'Unable to save this concept!',
	xProblemDeleteMsg : 'Unable to delete this concept!',
	xErrorDoubleRecord : 'This record has already been selected!',
	xProblemLoadMsg : 'Unable to load the concept',
	xDeleteNotAvailableMsgTitle : 'Concept deletion not available',
	xDeleteNotAvailableParentMsgLabel : 'Please remove the parents relationships',
	xDeleteNotAvailableAssociationMsgLabel : 'Please remove the associative relationships',
	xDeleteNotAvailableParentAssociationMsgLabel : 'Please remove the parent and associative relationships',

	onConceptFormRender : function(theForm) {
		var me = this;
		var thePanel = theForm.up('conceptPanel');

		var conceptId = thePanel.gincoId;
		var model = this.getConceptModelModel();
		if (conceptId != '' && conceptId != null) {
			theForm.getEl().mask("Chargement");
			model.load(conceptId, {
						success : function(model) {
							me.loadData(theForm, model);
							theForm.getEl().unmask();
						},
						failure : function(model) {
							Thesaurus.ext.utils.msg(me.xProblemLabel,
									me.xProblemLoadMsg);
							var globalTabs = theForm
									.up('#thesaurusItemsTabPanel');
							globalTabs.remove(thePanel);
						}
					});
		} else {
			thePanel.setTitle(thePanel.title);
			model = Ext.create('GincoApp.model.ConceptModel');
			model.data.thesaurusId = thePanel.up('thesaurusTabPanel').thesaurusData.id;
			model.data.topconcept = thePanel.up('thesaurusTabPanel').thesaurusData.defaultTopConcept;
			model.data.identifier = "";

			// 1 is the status to set by default for a new
			// concept, meaning "validated"
			if (Thesaurus.ext.utils.userInfo.userThesaurusRolesStore.getById( thePanel.up('thesaurusTabPanel').thesaurusData.id) != null
					&& Thesaurus.ext.utils.userInfo.userThesaurusRolesStore.getById( thePanel.up('thesaurusTabPanel').thesaurusData.id).data.role == 1) {
				model.data.status = 0;
				model.data.topconcept = false;
			} else {
				model.data.status = 1;
			}
			if (!Ext.isEmpty(thePanel.initPreferedTermBeforeLoad)) {
				if (!Ext
						.isEmpty(thePanel.initPreferedTermBeforeLoad.data.identifier)) {
					// adding a term as prefered term (creation
					// of a concept from a sandboxed term)
					var theGrid = thePanel.down('#gridPanelTerms');
					var theStore = theGrid.getStore();
					thePanel.initPreferedTermBeforeLoad.data.prefered = true;
					theStore.add(thePanel.initPreferedTermBeforeLoad);
				}
			}
			theForm.loadRecord(model);
			if (thePanel.displayPrefTermCreation) {
				var theGrid = thePanel.down('#gridPanelTerms');
				me.newTermFromTermsGrid(theGrid, true);
			}
			if (!Ext.isEmpty(thePanel.parentConceptId)) {
				var theGrid = thePanel.down('#gridPanelParentConcepts');
				me.initParent(theGrid, thePanel.parentConceptId);

			}
			this.enableSaveBtn(theForm);
		}
		me.initCustomAttributForm(theForm);

	},

	initCustomAttributForm : function(theForm) {
		var conceptPanel = theForm.up('conceptPanel');
		var customForm = theForm.down('customattrform');
		customForm.initFields(
				conceptPanel.up('thesaurusTabPanel').thesaurusData.id,
				function() {
					if (conceptPanel.gincoId != ''
							&& conceptPanel.gincoId != null) {
						customForm.load(conceptPanel.gincoId);
					}
				});
	},

	saveCustomFieldAttributes : function(theForm, conceptId) {
		var customForm = theForm.down('#customAttributeForm');
		customForm.save(conceptId, 'fr-FR');
	},

	onGridRender : function(theGrid) {
		var thePanel = theGrid.up('selectTermWin');
		var theStore = theGrid.getStore();
		theStore.getProxy().setExtraParam('idThesaurus',
				thePanel.thesaurusData.id);
		theStore.load();
	},

	newTermFromConceptPrefBtn : function(theButton) {
		this.newTermFromConceptBtn(theButton, true);
	},

	newTermFromConceptNonPrefBtn : function(theButton) {
		this.newTermFromConceptBtn(theButton, false);
	},

	selectTermFromConceptPrefBtn : function(theButton) {
		this.selectTermFromConceptBtn(theButton, true);
	},

	selectTermFromConceptNonPrefBtn : function(theButton) {
		this.selectTermFromConceptBtn(theButton, false);
	},

	newTermFromTermsGrid : function(theGrid, prefered) {
		var me = this;
		var thePanel = me.getActivePanel(theGrid);
		var win = Ext.create('GincoApp.view.CreateTermWin');
		win.thesaurusData = thePanel.up('thesaurusTabPanel').thesaurusData;
		var theForm = win.down('form');
		win.store = theGrid.getStore();
		var model = Ext.create('GincoApp.model.ThesaurusTermModel');
		model.data.prefered = prefered;
		model.data.thesaurusId = thePanel.up('thesaurusTabPanel').thesaurusData.id;
		model.data.identifier = "";
		model.data.language = thePanel.up('thesaurusTabPanel').thesaurusData.languages[0];
		model.data.conceptId = thePanel.gincoId;
		theForm.loadRecord(model);
		win.show();
	},

	newTermFromConceptBtn : function(theButton, prefered) {
		var me = this;
		var theGrid = theButton.up('#gridPanelTerms');
		me.newTermFromTermsGrid(theGrid, prefered);
	},

	selectTermFromConceptBtn : function(theButton, prefered) {
		var me = this;
		var thePanel = me.getActivePanel(theButton);
		var win = Ext.create('GincoApp.view.SelectTermWin', {
					onlyValidatedTerms : true
				});
		var theGrid = theButton.up('#gridPanelTerms');
		win.gincoId = thePanel.gincoId;
		win.conceptGrid = theGrid;
		win.store = theGrid.getStore();
		win.thesaurusData = thePanel.up('thesaurusTabPanel').thesaurusData;
		win.prefered = prefered;
		win.show();
	},

	onTermDblClick : function(theGrid, record, item, index, e, eOpts) {
		var thePanel = theGrid.up('conceptPanel');
		var topTabs = Ext.ComponentQuery.query('thesaurusTabs')[0];
		topTabs.fireEvent('opentermtab', topTabs, thePanel
						.up('thesaurusTabPanel').thesaurusData.id,
				record.data.identifier);
	},

	onConceptDblClick : function(theGrid, record, item, index, e, eOpts) {
		var thePanel = theGrid.up('conceptPanel');
		var topTabs = Ext.ComponentQuery.query('thesaurusTabs')[0];
		topTabs.fireEvent('openconcepttab', topTabs, thePanel
						.up('thesaurusTabPanel').thesaurusData.id,
				record.data.identifier);
	},

	onDetachClick : function(gridview, el, rowIndex, colIndex, e, rec, rowEl) {
		var theGrid = gridview.up('#gridPanelTerms');
		var theStore = theGrid.getStore();
		theStore.remove(rec);
	},

	onRemoveAssociationClick : function(gridview, el, rowIndex, colIndex, e,
			rec, rowEl) {
		var theGrid = gridview.up('#gridPanelAssociatedConcepts');
		var theStore = theGrid.getStore();
		theStore.remove(rec);
	},

	onRemoveParentClick : function(gridview, el, rowIndex, colIndex, e, rec,
			rowEl) {
		var theGrid = gridview.up('#gridPanelParentConcepts');
		var theStore = theGrid.getStore();
		theStore.remove(rec);
	},

	onRemoveChildClick : function(gridview, el, rowIndex, colIndex, e, rec,
			rowEl) {
		var theGrid = gridview.up('#gridPanelChildrenConcepts');
		var theStore = theGrid.getStore();
		theStore.remove(rec);
	},

	onRemoveAlignmentClick : function(gridview, el, rowIndex, colIndex, e, rec,
			rowEl) {
		var theGrid = gridview.up('#gridPanelAlignments');
		var theStore = theGrid.getStore();
		theStore.remove(rec);
	},

	onAlignmentAction : function(gridview, el, rowIndex, colIndex, e, rec,
			rowEl) {
		var m = e.getTarget().className.match(/\bicon-(\w+)\b/);
		if (m) {
			switch (m[1]) {
				case 'edit' :
					this.onEditAlignmentClick(gridview, el, rowIndex, colIndex,
							e, rec, rowEl);
					break;
				case 'delete' :
					this.onRemoveAlignmentClick(gridview, el, rowIndex,
							colIndex, e, rec, rowEl);
					break;
			}
		}

	},

	onSelectTermDblClick : function(theGrid, record) {
		var theWin = theGrid.up('selectTermWin');
		if (theWin.prefered == true) {
			record.data.prefered = true;
		} else {
			record.data.prefered = false;
		}
		record.data.conceptId = theWin.gincoId;
		if (theWin.store.findRecord('identifier', record.data.identifier) !== null) {
			Ext.MessageBox.alert(this.xProblemLabel, this.xErrorDoubleRecord);
		} else {
			record.setDirty();
			theWin.store.add(record);
			theWin.close();
		}
	},

	initParent : function(theGrid, parentId) {
		var theStore = theGrid.getStore();
		var parentModel = Ext
				.create('GincoApp.model.HierarchicalAssociationModel');
		parentModel.set('identifier', parentId);
		theStore.add(parentModel);
	},

	addExistingChild : function (theButton) {
		var me = this;
		var thePanel = me.getActivePanel(theButton);
		var theGrid = thePanel.down('#gridPanelChildrenConcepts');
		var theStore = theGrid.getStore();
		var win = Ext.create('GincoApp.view.SelectConceptWin', {
					thesaurusData : thePanel.up('thesaurusTabPanel').thesaurusData,
					conceptId : thePanel.gincoId,
					showTree : false,
					checkstore : theStore,
					onlyValidatedConcepts : true,
					listeners : {
						selectBtn : {
							fn : function(selectedRow) {
								me.selectConceptAsParent(selectedRow, theGrid);
							}
						}
					}
				});
		win.show();
	},

	addParent : function(theButton) {
		var me = this;
		var thePanel = me.getActivePanel(theButton);
		var theGrid = thePanel.down('#gridPanelParentConcepts');
		var theStore = theGrid.getStore();
		var win = Ext.create('GincoApp.view.SelectConceptWin', {
					thesaurusData : thePanel.up('thesaurusTabPanel').thesaurusData,
					conceptId : thePanel.gincoId,
					showTree : false,
					checkstore : theStore,
					onlyValidatedConcepts : true,
					listeners : {
						selectBtn : {
							fn : function(selectedRow) {
								me.selectConceptAsParent(selectedRow, theGrid);
							}
						}
					}
				});
		win.show();
	},

	addAlignment : function(theButton) {
		var me = this;
		var thePanel = me.getActivePanel(theButton);
		var theGrid = thePanel.down('#gridPanelAlignments');
		var win = this.createAlignmentWin(thePanel, theGrid);
		win.show();

	},

	onEditAlignmentClick : function(gridview, el, rowIndex, colIndex, e, rec,
			rowEl) {
		var me = this;
		var theGrid = gridview.up('#gridPanelAlignments');
		var thePanel = me.getActivePanel(theGrid);
		var win = this.createAlignmentWin(thePanel, theGrid, rec);
		win.show();
		win.fireEvent('loadAlignment', rec, win);

	},

	createAlignmentWin : function(thePanel, theGrid, record) {
		var me = this;
		var sAlignmentTypeStore = Ext.create('GincoApp.store.AlignmentTypeStore', {
			model: 'User',
			filters: [{
				property: 'resource',
				value: false
			}]
		});
		var sAlignmentTypeStoreResource = Ext.create('GincoApp.store.AlignmentTypeStore', {
			model: 'User',
			filters: [{
				property: 'resource',
				value: true
			}]
		});
		var win = Ext.create('GincoApp.view.AlignmentWin', {
					thesaurusData : thePanel.thesaurusData,
					conceptId : thePanel.gincoId,
					storeAlignmentTypes : sAlignmentTypeStore,
					storeAlignmentTypesResource : sAlignmentTypeStoreResource,
					listeners : {
						addAlignment : {
							fn : function(theButton) {
								me.saveAlignment(theGrid, theButton, record);
							}
						}
					}
				});
		return win;
	},

	saveAlignment : function(theGrid, theButton, record) {
		var me = this;

		var theForm = theButton.up('form');
		var theWin = theButton.up('alignmentWin');
		var theAlignmentGridStore = theGrid.getStore();

		var model = Ext.create('GincoApp.model.AlignmentModel');
		if (record) {
			model = record;
		}

		model.data.andRelation = theForm.down('#isAndCheckbox').value;
		model.data.alignmentType = theForm.down('#typeCombo').value;

		var externalThesaurusModel = Ext.create('GincoApp.model.ExternalThesaurusModel');

		var fields = theForm.getForm().getFields();
		model.targetConcepts().removeAll();
		model.targetResources().removeAll();
		for (idx in fields.items) {
			field = fields.items[idx];
			if(field.getName() == 'internal_concept'  && field.value && Ext.String.trim(field.value) != '') {
            	var alignmentConceptModel = Ext.create('GincoApp.model.AlignmentTargetConceptModel');
            	alignmentConceptModel.set('internalTargetConcept',field.value);
				model.targetConcepts().add(alignmentConceptModel);
            }
            if(field.getName() == 'external_concept' && field.value  && Ext.String.trim(field.value) != '') {
            	var alignmentConceptModel = Ext.create('GincoApp.model.AlignmentTargetConceptModel');
            	alignmentConceptModel.set('externalTargetConcept',field.value);
				model.targetConcepts().add(alignmentConceptModel);
            }
			if (field.getName() == 'external_resource' && field.value  && Ext.String.trim(field.value) != '') {
				model.data.alignmentType = theForm.down('#typeComboResource').value;
				var alignmentResourceModel = Ext.create('GincoApp.model.AlignmentTargetResourceModel');
				alignmentResourceModel.set('externalTargetResource',field.value);
				model.targetResources().add(alignmentResourceModel);
			}
			if (field.getName() == 'external_thesaurus') {
				externalThesaurusModel.set('externalId', field.getValue());
			}
			if (field.getName() == 'external_thesaurus_id') {
				if (field.value) {
					externalThesaurusModel.set('identifier', field.value);
				}
			}
			if (field.getName() == 'external_thesaurus_type') {
				externalThesaurusModel.set('externalThesaurusType', field.value);
			}
		}
		model.externalThesaurus().removeAll();
		model.externalThesaurus().add(externalThesaurusModel);

		if (theAlignmentGridStore.findRecord('identifier',
				model.data.identifier) == null
				&& !record) {
			theAlignmentGridStore.add(model);
		} else {
			record.setDirty();
			theGrid.getView().refresh();
			me.enableSaveBtn(theGrid);
		}
		theWin.close();
	},

	addChild : function(theButton) {
		var theThesaurusTabPanel = theButton.up('thesaurusTabPanel');
		var thePanel = theButton.up('conceptPanel');
		var conceptId = thePanel.gincoId;
		theThesaurusTabPanel.fireEvent('openchildconcepttab',
				theThesaurusTabPanel, conceptId);
	},

	getActivePanel : function(child) {
		return child.up('conceptPanel');
	},
	getThesaurusData : function(child) {
		return child.up('thesaurusTabPanel').thesaurusData;
	},

	addAssociativeRelationship : function(theButton) {
		var me = this;
		var thePanel = me.getActivePanel(theButton);
		var theGrid = thePanel.down('#gridPanelAssociatedConcepts');
		var theStore = theGrid.getStore();
		var win = Ext.create('GincoApp.view.SelectConceptWin', {
					thesaurusData : thePanel.up('thesaurusTabPanel').thesaurusData,
					conceptId : thePanel.gincoId,
					showTree : false,
					checkstore : theStore,
					onlyValidatedConcepts : true,
					listeners : {
						selectBtn : {
							fn : function(selectedRow) {
								me.selectAssociativeConcept(selectedRow,
										theGrid);
							}
						}
					}
				});
		win.show();
	},

	// *********** Start SelectConceptWin.js

	/**
	 * User clicks on button "Select as parent"
	 *
	 * @param selectedRow
	 */
	selectConceptAsParent : function(selectedRow, theGrid) {
		var theStore = theGrid.getStore();
		var selectedItem = selectedRow[0];
		selectedItem.setDirty();

		var parentModel = Ext
				.create('GincoApp.model.HierarchicalAssociationModel');
		parentModel.set('label', selectedItem.get('label'));
		parentModel.set('identifier', selectedItem.get('identifier'));

		theStore.add(parentModel);
	},

	selectAssociativeConcept : function(selectedRow, theGrid) {
		var theStore = theGrid.getStore();
		var selectedItem = selectedRow[0];
		selectedItem.setDirty();

		var assocModel = Ext.create('GincoApp.model.AssociationModel');
		assocModel.set('label', selectedItem.get('label'));
		assocModel.set('identifier', selectedItem.get('identifier'));

		theStore.add(assocModel);

	},

	// *********** End SelectConceptWin.js

	loadLanguages : function(theCombo) {
		var thePanel = theCombo.up('createTermWin');
		var theStore = theCombo.getStore();
		var theForm = thePanel.down('form');
		theStore.getProxy().setExtraParam('thesaurusId',
				thePanel.thesaurusData.id);
		theStore.load({
					callback : function(theStore, aOperation) {
						var record = theForm.getRecord();
						record.data.language = thePanel.thesaurusData.languages[0];
						theForm.loadRecord(record);
					}
				});
	},

	loadConceptStatus : function(theCombo) {
		var theStore = theCombo.getStore();
		theStore.load();
	},

	loadData : function(aForm, aModel) {
		var me = this;
		var conceptPanel = me.getActivePanel(aForm);
		var thesaurusData = me.getThesaurusData(aForm);
		conceptPanel.gincoId = aModel.data.identifier;

		aForm.loadRecord(aModel);
		var terms = aModel.terms().getRange();
		conceptTitle = "";
		Ext.Array.each(terms, function(term) {
					if (term.data.prefered == true
							&& term.data.language == thesaurusData.languages[0]) {
						conceptTitle = term.data.lexicalValue;
						return false;
					}
				});

		if (conceptTitle == "") {
			Ext.Array.each(terms, function(term) {
						if (term.data.prefered == true) {
							conceptTitle = term.data.lexicalValue;
							return false;
						}
					});
		}

		conceptPanel.setTitle("Concept : " + conceptTitle);

		var theGrid = aForm.down('#gridPanelTerms');

		var theGridStore = theGrid.getStore();
		theGridStore.removeAll();
		theGridStore.add(terms);
		this.setupStoreListener(theGrid);
		// Load associated concepts to the grid's store
		var assoc = aModel.associatedConcepts().getRange();
		var associatedConceptsGrid = aForm.down('#gridPanelAssociatedConcepts');
		var associatedConceptsGridStore = associatedConceptsGrid.getStore();
		associatedConceptsGridStore.removeAll();
		associatedConceptsGridStore.add(assoc);
		this.setupStoreListener(associatedConceptsGrid);

		// Load parent concepts to the grid's store
		var parents = aModel.parentConcepts().getRange();
		var parentsGrid = aForm.down('#gridPanelParentConcepts');
		var parentConceptsGridStore = parentsGrid.getStore();
		parentConceptsGridStore.removeAll();
		parentConceptsGridStore.add(parents);
		this.setupStoreListener(parentsGrid);

		// Load children concepts to the grid's store
		var children = aModel.childConcepts().getRange();
		var childrenGrid = aForm.down('#gridPanelChildrenConcepts');
		var childrenConceptsGridStore = childrenGrid.getStore();
		childrenConceptsGridStore.removeAll();
		childrenConceptsGridStore.add(children);
		this.setupStoreListener(childrenGrid);
		var loadAlignmentFunc = function () {
			var alignments = aModel.alignments().getRange();
			var alignmentsGrid = aForm.down('#gridPanelAlignments');
			var alignmentsGridStore = alignmentsGrid.getStore();
			alignmentsGridStore.removeAll();
			alignmentsGridStore.add(alignments);
			me.setupStoreListener(alignmentsGrid);
		}

		// Load alignments to the grid's store
		if (conceptPanel.alignmentTypeStore.getCount()!=0) {
			loadAlignmentFunc();
		} else 
		{
			conceptPanel.alignmentTypeStore.load(function () {
				loadAlignmentFunc();
			});
		}

		var rootConceptsGrid = aForm.down('#gridPanelRootConcepts');
		var rootConceptsGridStore = rootConceptsGrid.getStore();
		rootConceptsGridStore.getProxy().extraParams = {
			conceptIds : aModel.raw.rootConcepts
		};
		rootConceptsGridStore.load();

		var noteTab = aForm.up('tabpanel').down('noteConceptPanel');
		noteTab.setDisabled(false);

		var suggestionTab = aForm.up('tabpanel').down('suggestionPanel');
		suggestionTab.setDisabled(false);

		var deleteConceptBtn = aForm.down('#deleteConcept');
		if (aModel.data.status == 0 || aModel.data.status == 2) {
			// The concept has status = candidate or rejected, so we can
			// delete it
			deleteConceptBtn.setDisabled(false);
		} else {
			deleteConceptBtn.setDisabled(true);
		}

		var addAssociationBtn = aForm.down('#addAssociativeRelationship');
		var addparent = aForm.down('#addParent');
		var addChildMenu = aForm.down('#addChildMenu');
		var addAlignment = aForm.down('#addAlignment');
		var topconceptcb = aForm.down('#topconceptcb');
		if (aModel.data.status == 1) {
			addAssociationBtn.setDisabled(false);
			addparent.setDisabled(false);
			addChildMenu.setDisabled(false);
			aForm.down('#addExistingChild').setDisabled(false);
			addAlignment.setDisabled(false);
		}
		if (aModel.data.status == 0) {
			topconceptcb.setDisabled(true);
		}
		conceptPanel.addNodePath(thesaurusData.id);
		if (aModel.data.topistopterm == true)
			conceptPanel.addNodePath("CONCEPTS_" + thesaurusData.id);
		else
			conceptPanel.addNodePath("ORPHANS_" + thesaurusData.id);
		var nodeId = "";
		for (var i = 0; i < aModel.raw.conceptsPath.length; i++) {
			if (i > 0)
				nodeId = aModel.raw.conceptsPath[i - 1] + "*"
						+ aModel.raw.conceptsPath[i];
			else
				nodeId = "*" + aModel.raw.conceptsPath[i];
			conceptPanel.addNodePath("CONCEPT_" + nodeId);
		}
		conceptPanel.setReady();
	},

	checkValidatedSelected : function(theCombobox, theRecord, eOpts) {
		theForm = theCombobox.up('#conceptForm');
		var addAssociationBtn = theForm.down('#addAssociativeRelationship');
		var addparent = theForm.down('#addParent');
		var topconceptcb = theForm.down('#topconceptcb');
		var addChildMenu = theForm.down('#addChildMenu');

		if (theRecord[0].data.status == 1) {
			// We enable the button for creating associations
			// only if the concept's status is validated (both
			// hierachical and associative)
			addAssociationBtn.setDisabled(false);
			addparent.setDisabled(false);
			topconceptcb.setDisabled(false);
			addChildMenu.setDisabled(false);

		} else {
			addAssociationBtn.setDisabled(true);
			addparent.setDisabled(true);
			addChildMenu.setDisabled(true);
			if (theRecord[0].data.status == 0) {
				topconceptcb.setDisabled(true);
			} else
			{
				topconceptcb.setDisabled(false);
			}
		}
	},

	saveTermFromConceptBtn : function(theButton) {
		var me = this;
		var theForm = theButton.up('form');
		var theWin = theButton.up('createTermWin');
		theForm.getForm().updateRecord();
		var updatedModel = theForm.getForm().getRecord();

		Ext.Ajax.request({
							url : 'services/ui/thesaurustermservice/checkTermUnicity',
							params : {
								idThesaurus : updatedModel.get('thesaurusId'),
								lang : updatedModel.get('language'),
								lexicalValue : updatedModel.get('lexicalValue')
							},
							method: 'GET',
							success : function(response) {
								var jsonData = Ext.JSON.decode(response.responseText);
								if (jsonData === true) {
									theWin.store.add(updatedModel);
									theWin.close();
								} else
								{
									Thesaurus.ext.utils.msg(me.xProblemLabel, me.xAlreadyExistingTerm);
								}
							}
		});

	},

	deleteConcept : function(theButton) {
		var me = this;
		var theForm = theButton.up('form');
		var globalTabs = theForm.up('#thesaurusItemsTabPanel');
		var thePanel = me.getActivePanel(theButton);

		var updatedModel = theForm.getForm().getRecord();
		var parentGrid = theForm.down('#gridPanelParentConcepts');
		var parentGridStore = parentGrid.getStore();
		var parentData = parentGridStore.getRange();

		var associatedGrid = theForm.down('#gridPanelAssociatedConcepts');
		var associatedGridStore = associatedGrid.getStore();
		var associatedData = associatedGridStore.getRange();
		if (associatedData.length > 0 || parentData.length > 0) {
			if (parentData.length > 0 && associatedData.length > 0) {
				this.unableTodeletePopup(me.xDeleteNotAvailableMsgTitle,
						me.xDeleteNotAvailableParentAssociationMsgLabel);
			} else if (parentData.length > 0) {
				this.unableTodeletePopup(me.xDeleteNotAvailableMsgTitle,
						me.xDeleteNotAvailableParentMsgLabel);
			} else if (associatedData.length > 0) {
				this.unableTodeletePopup(me.xDeleteNotAvailableMsgTitle,
						me.xDeleteNotAvailableAssociationMsgLabel);
			}
		} else {
			Ext.MessageBox.show({
						title : me.xDeleteMsgTitle,
						msg : me.xDeleteMsgLabel,
						buttons : Ext.MessageBox.YESNOCANCEL,
						fn : function(buttonId) {
							switch (buttonId) {
								case 'no' :
									break;
								case 'yes' :
									updatedModel.destroy({
												success : function(record,
														operation) {
													Thesaurus.ext.utils
															.msg(
																	me.xSucessLabel,
																	me.xSucessRemovedMsg);
													me.application
															.fireEvent(
																	'conceptdeleted',
																	thePanel.thesaurusData);
													globalTabs.remove(thePanel);
												},
												failure : function(record,
														operation) {
													Thesaurus.ext.utils.msg(
															me.xProblemLabel,
															operation.error);
												}
											});
									break;
							}
						},
						scope : this
					});
		}

	},

	unableTodeletePopup : function(theTitle, theLabel) {
		Ext.MessageBox.show({
					title : theTitle,
					msg : theLabel,
					buttons : Ext.MessageBox.OK,
					scope : this
				});
	},

	saveConcept : function(theButton, theCallback) {
		var me = this;
		var theForm = theButton.up('form');
		var theGrid = theForm.down('#gridPanelTerms');
		theForm.getForm().updateRecord();
		var theStore = theGrid.getStore();
		var termsData = theStore.getRange();

		var rootGrid = theForm.down('#gridPanelRootConcepts');
		var rootGridStore = rootGrid.getStore();
		var rootData = rootGridStore.getRange();
		var rootIds = Ext.Array.map(rootData, function(root) {
					return root.data.identifier;
				});

		var associatedGrid = theForm.down('#gridPanelAssociatedConcepts');
		var associatedGridStore = associatedGrid.getStore();
		var associatedData = associatedGridStore.getRange();

		var hierarchicalParentGrid = theForm.down('#gridPanelParentConcepts');
		var hierarchicalParentData = hierarchicalParentGrid.getStore().getRange();

		var hierarchicalChildGrid = theForm.down('#gridPanelChildrenConcepts');
		var hierarchicalChildData = hierarchicalChildGrid.getStore().getRange();

		var alignmentsGrid = theForm.down('#gridPanelAlignments');
		var alignmentsData = alignmentsGrid.getStore().getRange();
		Ext.Array.each(alignmentsData, function(alignment) {
					var targetConceptData = alignment.targetConceptsStore.getRange();
					var targetResourceData = alignment.targetResourcesStore.getRange();
					alignment.targetConcepts().removeAll();
					alignment.targetResources().removeAll();
					alignment.targetConcepts().add(targetConceptData);
					alignment.targetResources().add(targetResourceData);
				});
		var thePanel = me.getActivePanel(theButton);

		theForm.getEl().mask(me.xLoading);
		var updatedModel = theForm.getForm().getRecord();
		updatedModel.terms().removeAll();
		updatedModel.terms().add(termsData);
		updatedModel.associatedConcepts().removeAll();
		updatedModel.associatedConcepts().add(associatedData);
		updatedModel.parentConcepts().removeAll();
		updatedModel.parentConcepts().add(hierarchicalParentData);
		updatedModel.childConcepts().removeAll();
		updatedModel.childConcepts().add(hierarchicalChildData);
		updatedModel.alignments().removeAll();
		updatedModel.alignments().add(alignmentsData);

		updatedModel.data.rootConcepts = rootIds;
		updatedModel.save({
					success : function(record, operation) {
						var resultRecord = operation.getResultSet().records[0];
						me.saveCustomFieldAttributes(theForm, resultRecord
										.get("identifier"));
						thePanel.gincoId=resultRecord.get("identifier");
						Thesaurus.ext.utils.msg(me.xSucessLabel,
								me.xSucessSavedMsg);
						me.application.fireEvent('conceptupdated',
								thePanel.getThesaurusData(),resultRecord
										.get("identifier"),true);

                        Ext.Array.forEach(resultRecord.raw.parentConcepts, function (parent) {
                            me.application.fireEvent('conceptupdated',
                            thePanel.getThesaurusData(), parent.identifier,false);
                        });

						if (!Ext.isEmpty(thePanel.parentConceptId)) {
							me.reloadConceptWindow(resultRecord.parentConceptId);
						}
						if (theCallback && typeof theCallback == "function") {
							theCallback();
						}
					},
					failure : function(record, operation) {
						Thesaurus.ext.utils.msg(me.xProblemLabel,
								me.xProblemSaveMsg + " " + operation.error);
						theForm.getEl().unmask();
					}
				});

	},
	reloadConceptWindow : function (conceptId) {
		var me = this;
		var thesTabs = me.getTopTabs();
		var parentTab =thesTabs.down('conceptPanel[gincoId="'+conceptId+'"]');
		if (parentTab != null) {
			me.onConceptFormRender(parentTab.down('#conceptForm'));
		}
	},
    customAttributesUpdated : function (thesaurusdata) {
        var me = this;
        var thesTabs = me.getTopTabs();

        thesTabs.items.each(function(item) {
            if (item.getThesaurusData().id == thesaurusdata.id) {

                Ext.each(item.query('conceptPanel'), function(concept){
                    me.reloadConceptWindow(concept.gincoId);
                });
            }
        });
    },
	exportBranch : function(theButton, theCallback) {
		var theForm = theButton.up('form');
		var url = "services/ui/exportservice/getGincoBranchExport?conceptId="
				+ encodeURIComponent(theForm.up('conceptPanel').gincoId);
		window.open(url);
	},
	enableSaveBtn : function(aItem) {
		var btn = null;
		if (aItem.up)
			btn = aItem.down("#saveConcept");
		else
			btn = aItem.owner.down("#saveConcept");
		if (btn == null)
			btn = aItem.up('form').down("#saveConcept");
		if (btn != null)
			btn.setDisabled(false);
	},
	setupStoreListener : function(aGridPanel) {
		var store = aGridPanel.store;
		var me = this;
		store.on('datachanged', function() {
					me.enableSaveBtn(aGridPanel);
				});
		store.on('update', function() {
					me.enableSaveBtn(aGridPanel);
				});
	},
	onGotoAlignedConcept : function(theButton, thesaurusId, conceptId,
			internalAlign) {
		var me = this;
		if (internalAlign == true) {
			if (thesaurusId != "") {
				// We have a thesaurusid...
				var thesTabs = me.getTopTabs();
				thesTabs.fireEvent('openconcepttab', thesTabs, thesaurusId,
						conceptId);
			} else {
				// We don't have a thesaurusid... we have to ask one...
				Ext.Ajax.request({
							url : 'services/ui/thesaurusconceptservice/getConceptThesaurusId',
							params : {
								id : conceptId
							},
							method: 'GET',
							success : function(response) {
								var jsonData = Ext.JSON.decode(response.responseText);
								if (jsonData.success) {
									var thesTabs = me.getTopTabs();
									thesTabs.fireEvent('openconcepttab', thesTabs, jsonData.data,
									conceptId);
								} else
								{
									Thesaurus.ext.utils.msg(me.xProblemLabel,"");
								}
							}
						});
			}

		} else {
			window.open(conceptId);
		}
	},

	init : function() {
		this.application.on({
			'termupdated' : function(thesaurusData, conceptId) {
				this.reloadConceptWindow(conceptId);
			},
	        scope: this
		});

        this.application.on({
            'conceptupdated' : function(thesaurusData, conceptId) {
                this.reloadConceptWindow(conceptId);
            },
            scope: this
        });

        this.application.on({
            'customattributeupdated' : function(thesaurusData) {
                this.customAttributesUpdated(thesaurusData);
            },
            scope: this
        });

		this.control({
			'conceptPanel #conceptForm' : {
				afterrender : this.onConceptFormRender,
				dirtychange : this.enableSaveBtn
			},
			'conceptPanel #conceptStatusCombo' : {
				render : this.loadConceptStatus,
				select : this.checkValidatedSelected
			},
			'conceptPanel #saveConcept' : {
				click : this.saveConcept
			},
			'conceptPanel #deleteConcept' : {
				click : this.deleteConcept
			},
			'conceptPanel #exportBranch' : {
				click : this.exportBranch
			},
			'conceptPanel  button[cls=addParent]' : {
				click : this.addParent
			},
			'conceptPanel  #addChild' : {
				click : this.addChild
			},
			'conceptPanel  #addExistingChild' : {
				click : this.addExistingChild
			},
			'conceptPanel  button[cls=removeParent]' : {
				click : this.removeParent
			},
			'conceptPanel  button[cls=addAssociativeRelationship]' : {
				click : this.addAssociativeRelationship
			},
			'conceptPanel #newTermFromConceptPrefBtn' : {
				click : this.newTermFromConceptPrefBtn
			},
			'conceptPanel #newTermFromConceptNonPrefBtn' : {
				click : this.newTermFromConceptNonPrefBtn
			},
			'conceptPanel #selectTermFromConceptPrefBtn' : {
				click : this.selectTermFromConceptPrefBtn
			},
			'conceptPanel #selectTermFromConceptNonPrefBtn' : {
				click : this.selectTermFromConceptNonPrefBtn
			},
			'form #saveTermFromConcept' : {
				click : this.saveTermFromConceptBtn
			},
			'createTermWin #languageCombo' : {
				render : this.loadLanguages
			},
			'conceptPanel #gridPanelTerms' : {
				itemdblclick : this.onTermDblClick
			},
			'selectTermWin gridpanel' : {
				render : this.onGridRender,
				selectBtn : this.onSelectTermDblClick
			},
			'conceptPanel gridpanel #conceptActionColumn' : {
				click : this.onDetachClick
			},
			'conceptPanel #gridPanelAssociatedConcepts' : {
				itemdblclick : this.onConceptDblClick
			},
			'conceptPanel #gridPanelParentConcepts' : {
				itemdblclick : this.onConceptDblClick
			},
			'conceptPanel #gridPanelChildrenConcepts' : {
				itemdblclick : this.onConceptDblClick
			},
			'conceptPanel #gridPanelRootConcepts' : {
				itemdblclick : this.onConceptDblClick
			},
			'conceptPanel #gridPanelAssociatedConcepts #associatedConceptActionColumn' : {
				click : this.onRemoveAssociationClick
			},
			'conceptPanel #gridPanelParentConcepts #parentConceptActionColumn' : {
				click : this.onRemoveParentClick
			},
			'conceptPanel #gridPanelChildrenConcepts #childConceptActionColumn' : {
				click : this.onRemoveChildClick
			},
			'conceptPanel  button[cls=addAlignment]' : {
				click : this.addAlignment
			},
			'conceptPanel #gridPanelAlignments  #alignmentUrlColumn' : {
				gotoconcept : this.onGotoAlignedConcept
			},
			'conceptPanel #gridPanelAlignments  #alignmentUrlResourcesColumn' : {
				gotoconcept : this.onGotoAlignedConcept
			},
			'conceptPanel #gridPanelAlignments #alignmentActionColumn' : {
				click : this.onAlignmentAction
			}
		});

	}
});
