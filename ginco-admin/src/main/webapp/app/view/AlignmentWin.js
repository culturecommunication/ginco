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

Ext
		.define(
				'GincoApp.view.AlignmentWin',
				{
					extend : 'Ext.window.Window',
					alias : 'widget.alignmentWin',
					requires : ['GincoApp.controller.AlignmentController'],
					localized : true,
					config : {
						storeAlignmentTypes : null	,
						alignmentId : null
					},

					viewConfig : {
						style : {
							overflow : 'auto',
							overflowX : 'hidden'
						}
					},

					xIdentifierColumnLabel : "Identifier",
					xAlignmentWinTitle: "Create alignment",
					xTypeLabel: "Aligment type",
					xAndOrOr: "AND between concepts",
					xAddInternalConceptId: "Add a ginco concept",	
					xAddExternalConceptId : "Add an external concept",
					xSaveBtn : "Save",
					width : 500,					
					titleAlign : 'center',
					modal : true,
					store: null,
					
					initComponent : function() {
						var me = this;
						Ext
						.applyIf(
								me,
								{
									title : me.xAlignmentWinTitle,
									items : [ {
										xtype : 'form',
										height : 400,
										itemId: 'alignmentForm',

										dockedItems : [ {
											xtype : 'toolbar',
											dock : 'top',
											items : [ {
												xtype : 'button',
												text : me.xSave,
												formBind : true,
												itemId : 'saveAlignmentPopup',
												iconCls : 'icon-save',
												tooltip : me.xSaveBtn,
												tooltipType: 'title',
												handler : function(
															theButton) {
													me
													.fireEvent(
															'addAlignment', this);
												}
											},{
												xtype : 'button',
												text : me.xAddInternalConceptId,
												disabled : false,
												iconCls : 'icon-add',
												itemId : 'addInternalConceptId',
												disabled : true,
												cls: 'addInternalConcept'
											},{
												xtype : 'button',
												text : me.xAddExternalConceptId,
												disabled : false,
												iconCls : 'icon-add',
												itemId : 'addExternalConceptId',
												disabled : true,
												cls: 'addExternalConcept'
											}]
										} ],
										defaults : {
											labelWidth : 150,
											anchor : '90%'
										},

										items : [
												{
													xtype : 'combobox',
													name : 'alignmentType',
													itemId : 'typeCombo',
													fieldLabel : me.xTypeLabel,
													editable : false,
													displayField : 'label',
													valueField : 'identifier',
													forceSelection : true,
													store : me.storeAlignmentTypes,
													allowBlank : false													
												},{
													xtype : 'checkbox',
													name : 'isAnd',
													fieldLabel : me.xAndOrOr,
													itemId : 'isAndCheckbox',
													hidden : true
												}
										]
									} ]
								});

						me.callParent(arguments);
					}
				});