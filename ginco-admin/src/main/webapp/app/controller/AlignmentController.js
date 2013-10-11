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
			
			stores : [ 'AlignmentsStore', 'ExternalThesaurusTypeStore'],
			models : [ 'AlignmentModel' ],
			storeExternalThesaurusType: Ext.create('GincoApp.store.ExternalThesaurusTypeStore'),
			
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
			
			hideAllFields : function(form, fieldName) {
				var fields = form.getForm().getFields();
		        for(idx in fields.items) {
		        	field = fields.items[idx];
		            if(field.getName() == fieldName) {
		            	form.remove(field);		            			           
		            }
		        }
			},
			
			displayAddInternalConceptField: function(theButton, e, eOpts) {
				var me=this;							
				var alignmentForm = theButton.up('form');							
				
				var tf = Ext.create('Ext.form.field.Text', {
	                   name: 'internal_concept',
	                   fieldLabel: me.xInternalConceptId
				});
				alignmentForm.add(tf);	
				
				this.hideAllFields(alignmentForm, 'external_thesaurus');
				this.hideAllFields(alignmentForm, 'external_thesaurus_type');
				this.hideAllFields(alignmentForm, 'external_concept');
				
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
			},
			
			displayAddExternalConceptFields: function(theButton, e, eOpts) {
				var me=this;							
				var alignmentForm = theButton.up('form');	
				
				if (!alignmentForm.getForm().findField("external_thesaurus")) {
					var tf = Ext.create('Ext.form.field.Text', {
						name: 'external_thesaurus',
						fieldLabel: me.xExternalThesaurusId
					});
					alignmentForm.add(tf);	
				
				
					var tf = Ext.create('Ext.form.field.ComboBox', {
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
					alignmentForm.add(tf);
				}
				
				var tf = Ext.create('Ext.form.field.Text', {
	                   name: 'external_concept',
	                   fieldLabel: me.xExternalConceptId
				});
				alignmentForm.add(tf);	
				
				this.hideAllFields(alignmentForm, 'internal_concept');
				
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
			},
			
			init : function() {			
				this.control({
					'alignmentWin #typeCombo' : {
						select : this.chooseAlignmentType
					},
					'alignmentWin  #addInternalConceptId' : {
						click : this.displayAddInternalConceptField
					},
					'alignmentWin  #addExternalConceptId' : {
						click: this.displayAddExternalConceptFields
					}
				});

			}
		});