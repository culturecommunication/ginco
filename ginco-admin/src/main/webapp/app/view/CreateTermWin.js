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

Ext
		.define(
				'GincoApp.view.CreateTermWin',
				{
					extend : 'Ext.window.Window',
					alias : 'widget.createTermWin',
					localized : true,

					thesaurusData : null,

					/* Fields prompting values */
					xLexicalValueLabel : 'Lexical value',
					xLanguagesLabel : 'Languages',
					xSourceLabel : 'Source',
					xCreateTermWinTitle : 'New Term',
					xSave : 'Save',
					store : null,
					width : 500,
					title : 'Création de nouveau terme',
					titleAlign : 'center',
					modal : true,
					initComponent : function() {
						var me = this;

						Ext
								.applyIf(
										me,
										{
											items : [ {
												xtype : 'form',
												height : 300,

												dockedItems : [ {
													xtype : 'toolbar',
													dock : 'top',
													items : [ {
														xtype : 'button',
														text : me.xSave,
														formBind : true,
														itemId : 'saveTermFromConcept',
														iconCls : 'icon-save'
													} ]
												} ],
												defaults : {
													labelWidth : 150,
													anchor : '90%'
												},

												items : [
														{
															xtype : 'hiddenfield',
															name : 'identifier'
														},
														{
															xtype : 'hiddenfield',
															name : 'created'
														},
														{
															xtype : 'hiddenfield',
															name : 'modified'
														},
														{
															xtype : 'textfield',
															margin : '10 0 5 0',
															name : 'lexicalValue',
															fieldLabel : me.xLexicalValueLabel,
															allowBlank : false
														},
														{
															xtype : 'htmleditor',
															name : 'source',
															fieldLabel : me.xSourceLabel,
															enableAlignments : false,
															enableColors : false,
															enableFont : false,
															enableFontSize : false,
															enableFormat : false,
															enableLists : false,
															enableSourceEdit : false
														},
														{
															xtype : 'hiddenfield',
															name : 'prefered'
														},
														{
															xtype : 'hiddenfield',
															name : 'status'
														},
														{
															xtype : 'hiddenfield',
															name : 'conceptId'
														},
														{
															xtype : 'hiddenfield',
															name : 'thesaurusId'
														},
														{
															xtype : 'combobox',
															name : 'language',
															itemId : 'languageCombo',
															fieldLabel : me.xLanguagesLabel,
															editable : false,
															displayField : 'refname',
															valueField : 'id',
															forceSelection : true,
															store : Ext
																	.create('GincoApp.store.TermLanguageStore'),
															allowBlank : false
														} ]
											} ]
										});

						me.callParent(arguments);
					}

				});