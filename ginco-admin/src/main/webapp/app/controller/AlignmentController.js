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

Ext.define('GincoApp.controller.AlignmentController',
		{
			extend : 'Ext.app.Controller',
			localized : true,
			xInternalConceptId: "Internal identifier",
			xExternalConceptId: "External identifier", 
			xExternalThesaurusId: "External thesaurus identifier",
			xExternalThesaurusType: "External thesaurus type",
			xEditAlignmentWinTitle: 'Edit alignment',

			stores : [ 'AlignmentsStore', 'ExternalThesaurusTypeStore', 'ExternalThesaurusStore'],
			models : [ 'AlignmentModel' ],			
			
			storeExternalThesaurusType: Ext.create('GincoApp.store.ExternalThesaurusTypeStore'),
			storeExternalThesaurus: Ext.create('GincoApp.store.ExternalThesaurusStore'),		

			chooseAlignmentType : function(theCombobox, theRecord,	eOpts) {
				var form = theCombobox.up('form');
				var multiCheckbox=form.down('#isAndCheckbox');
				if (theRecord[0].data.multiConcept) {					
					multiCheckbox.show();
					this.enableAddButtons(form);
				} else {
					multiCheckbox.hide();
					if (form.getForm().findField("internal_concept")) {
						form.down('#addInternalConceptId').setDisabled(true);			
						this.hideMultiFields(form, 'internal_concept');						
					} else if (form.getForm().findField("external_concept")) {
						form.down('#addExternalConceptId').setDisabled(true);
						this.hideMultiFields(form, 'external_concept');						
					} else {
						this.enableAddButtons(form);						
					}
				}
			},
			
			disableAddButtons: function(form) {
				form.down('#addInternalConceptId').setDisabled(true);
				form.down('#addExternalConceptId').setDisabled(true);
			},
			
			enableAddButtons: function(form) {
				form.down('#addInternalConceptId').setDisabled(false);
				form.down('#addExternalConceptId').setDisabled(false);
			},
			
			hideMultiFields : function(form, fieldName) {
				var fields = form.getForm().getFields();
				var cpt = 0;
		        for(idx in fields.items) {
		        	field = fields.items[idx];
		            if(field.getName() == fieldName) {
		            	if (cpt > 0) {
		            		form.remove(field);
		            	}
		            	cpt ++;				           
		            }
		        }
			},
			
			removeField : function(form, fieldName) {
				var fields = form.getForm().getFields();
		        for(idx in fields.items) {
		        	field = fields.items[idx];
		            if(field.getName() == fieldName) {
		            	form.remove(field);		            			           
		            }
		        }
			},
			
			displayAddInternalConceptFieldByBytton: function(theButton, e, eOpts) {
				var alignmentForm = theButton.up('form');
				this.displayAddInternalConceptField(alignmentForm);
			},
			
			displayAddInternalConceptField: function(alignmentForm, e, eOpts) {
				var me=this;							
				
				var tf = Ext.create('Ext.form.field.Text', {
	                   name: 'internal_concept',
	                   fieldLabel: me.xInternalConceptId
				});
				alignmentForm.add(tf);	
				
				this.removeField(alignmentForm, 'external_thesaurus');
				this.removeField(alignmentForm, 'external_thesaurus_type');
				this.removeField(alignmentForm, 'external_concept');
				this.removeField(alignmentForm, 'external_thesaurus_id');

				
				var multiCheckbox=alignmentForm.down('#typeCombo');
			
				var theRecord = multiCheckbox.findRecordByValue(multiCheckbox.getValue());
				if (!theRecord.data.multiConcept) {		
					if (alignmentForm.getForm().findField("internal_concept")) {
						alignmentForm.down('#addInternalConceptId').setDisabled(true);
						alignmentForm.down('#addExternalConceptId').setDisabled(false);
					}	else {
						alignmentForm.down('#addInternalConceptId').setDisabled(false);
						alignmentForm.down('#addExternalConceptId').setDisabled(true);
					}
				} else {
					this.enableAddButtons(alignmentForm);
				}
				return tf;
			},
			
			displayAddExternalConceptFieldsByButton: function(theButton, e, eOpts) {
				var alignmentForm = theButton.up('form');	
				this.displayAddExternalConceptFields(alignmentForm, e, eOpts);
			},
			
			displayAddExternalConceptFields: function(alignmentForm, e, eOpts) {
				var me=this;							
				if (!alignmentForm.getForm().findField("external_thesaurus")) {
					var tf1 = Ext.create('Ext.form.field.ComboBox', {
						itemId:'externalThesaurus',
						name: 'external_thesaurus',
						displayField : 'externalId',
						fieldLabel: me.xExternalThesaurusId,
						store : me.storeExternalThesaurus,
						allowBlank : false,
						editable: true
					});
					alignmentForm.add(tf1);	
					
					var tf4 = Ext.create('Ext.form.field.Text', {
		                   name: 'external_thesaurus_id',
		                   hidden :true,
		                   itemId:'externalThesaurusId'
					});
					alignmentForm.add(tf4);	

				
					var tf2 = Ext.create('Ext.form.field.ComboBox', {
						name: 'external_thesaurus_type',
						fieldLabel: me.xExternalThesaurusType,
	                   	itemId : 'thesaurusTypeCombo',
					    editable : false,
						displayField : 'label',
						valueField : 'identifier',
						forceSelection : false,
						store : me.storeExternalThesaurusType,
						allowBlank : false			
					});
					alignmentForm.add(tf2);
				}
				
				var tf3 = Ext.create('Ext.form.field.Text', {
	                   name: 'external_concept',
	                   fieldLabel: me.xExternalConceptId
				});
				alignmentForm.add(tf3);	
				
				this.removeField(alignmentForm, 'internal_concept');
				
				var multiCheckbox=alignmentForm.down('#typeCombo');				
				var theRecord = multiCheckbox.findRecordByValue(multiCheckbox.getValue());
				if (!theRecord.data.multiConcept) {		
					if (alignmentForm.getForm().findField("external_concept")) {
						alignmentForm.down('#addInternalConceptId').setDisabled(false);
						alignmentForm.down('#addExternalConceptId').setDisabled(true);
					} else {
						alignmentForm.down('#addInternalConceptId').setDisabled(true);
						alignmentForm.down('#addExternalConceptId').setDisabled(false);
					}
				} else {
					this.enableAddButtons(alignmentForm);
				}
				return tf3;
			},
			
			updateExternalThesaurusType: function(theCombobox, theRecord,	eOpts) {
				var thesaurusTypeCombo  = theCombobox.up('form').down('#thesaurusTypeCombo');
				thesaurusTypeCombo.clearValue();			
				thesaurusTypeCombo.setValue(theRecord[0].data.externalThesaurusType);	
				var externalThesaurusIdField  = theCombobox.up('form').down('#externalThesaurusId');
				externalThesaurusIdField.setValue(theRecord[0].data.identifier);
			},			
			
			
			loadData: function(rec,win) {
				var me=this;

				win.title=me.xEditAlignmentWinTitle;
				
				var alignmentTypeStore = win.storeAlignmentTypes;
				var alignmentForm = win.down('form');
				var isExternal = false;

				alignmentTypeStore.load(function(records, operation, success) {
					var selectedType = alignmentTypeStore.findRecord( 'identifier', rec.data.alignmentType);
					win.down("#typeCombo").select(selectedType);
					win.down("#typeCombo").fireEvent('select',win.down("#typeCombo"), [selectedType]);
					win.down("#isAndCheckbox").setValue(rec.data.andRelation);		
					var targetConceptData = rec.targetConceptsStore.getRange();
					Ext.Array.each(targetConceptData, function(targetConcept) {
						if (targetConcept.data.internalTargetConcept) {
							var internalField = me.displayAddInternalConceptField(alignmentForm) ;
							internalField.setValue(targetConcept.data.internalTargetConcept);						
						}
						if (targetConcept.data.externalTargetConcept) {
							var externalField = me.displayAddExternalConceptFields(alignmentForm) ;
							externalField.setValue(targetConcept.data.externalTargetConcept);
							isExternal = true;
						}
					});	
					if (isExternal) {
						var externalThesaurusStore = me.storeExternalThesaurus;
						externalThesaurusStore.load(function(records, operation, success) {		
							var selectedThesaurus= externalThesaurusStore.findRecord( 'identifier',rec.externalThesaurus().data.items[0].data.identifier);
							if (selectedThesaurus) {
								alignmentForm.getForm().findField('external_thesaurus').select(selectedThesaurus);
								alignmentForm.getForm().findField('external_thesaurus_id').setValue(selectedThesaurus.data.identifier);

							} else {
								alignmentForm.getForm().findField('external_thesaurus').select(rec.externalThesaurus().data.items[0].data.externalId);
								alignmentForm.getForm().findField('external_thesaurus_id').setValue(rec.externalThesaurus().data.items[0].data.identifier);
							}
							alignmentForm.getForm().findField('external_thesaurus_type').select(rec.externalThesaurus().data.items[0].data.externalThesaurusType);

						});						
					}
				});
				

			},			
			
			
			init : function() {			
				this.control({
					
					'alignmentWin #typeCombo' : {
						select : this.chooseAlignmentType
					},
					'alignmentWin  #addInternalConceptId' : {
						click : this.displayAddInternalConceptFieldByBytton
					},
					'alignmentWin  #addExternalConceptId' : {
						click: this.displayAddExternalConceptFieldsByButton
					},					
					'alignmentWin  #externalThesaurus' : {
						select: this.updateExternalThesaurusType
					},
					'alignmentWin' :{
						loadAlignment: this.loadData
					}
				});

			}
		});