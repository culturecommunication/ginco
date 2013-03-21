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
				'GincoApp.view.SelectTermWin',
				{
					extend : 'Ext.window.Window',
					alias: 'widget.selectTermWin',
					localized: true,
					
					thesaurusData : null,
					store : null,
					prefered: null,
					config: {
						onlyValidatedTerms : false
					},

				    /*Fields prompting values*/
					xIdentifierColumnLabel : "Identifier",
					xLexicalValueColumnLabel : "Lexical Value",
					xCreatedColumnLabel: "Created",
					xModifiedColumnLabel:"Modified",
					xSourceColumnLabel:"Source",
					xStatusColumnLabel:"Status",
					xLangueColumnLabel:"Language",
				    xSelectTermWinTitle : 'Select a Term',
				    xSave: 'Save',
					width : 500,
					height : 530,
					title : 'Sélection d\'un terme',
					titleAlign : 'center',
					modal : true,
					termStore: null,
					initComponent : function() {
						var me = this;
						
						me.termStore = Ext.create('GincoApp.store.ThesaurusTermStore');
						me.termStore.pageSize=20;
						me.termStore.getProxy().extraParams = {onlyValidatedTerms: me.onlyValidatedTerms};
						
						Ext
								.applyIf(
										me,
										{					
											items : [ {
												xtype : 'gridpanel',
												title : me.xSelectTermWinTitle,
												autoScroll:true,
												flex:1,
												store : me.termStore,
												columns : [
												           {dataIndex : 'identifier', text : me.xIdentifierColumnLabel},
												           {dataIndex : 'lexicalValue', text : me.xLexicalValueColumnLabel, flex: 1},
												           {dataIndex : 'created', text : me.xCreatedColumnLabel},
												           {dataIndex : 'modified', text : me.xModifiedColumnLabel},
												           {dataIndex : 'source', text : me.xSourceColumnLabel,  hidden: true},
												           {dataIndex : 'status', text : me.xStatusColumnLabel,  hidden: true},
												           {dataIndex : 'language', text : me.xLangueColumnLabel}
												           ],
												dockedItems: [{
											        xtype: 'pagingtoolbar',
											        store :  me.termStore,
											        dock: 'bottom',
											        displayInfo: true
											    },{
													xtype : 'toolbar',
													dock : 'top',
													items : [ {
														xtype : 'button',
														text : me.xSelectTermWinTitle,
														disabled : false,
														itemId : 'selectButton',
														iconCls : 'icon-add',
														handler : function(
																theButton) {
															var thePanel = theButton
																	.up('gridpanel');
															var record = thePanel
																	.getSelectionModel()
																	.getSelection();

															if (record.length == 1) {
																thePanel
																		.fireEvent(
																				'selectBtn',thePanel,
																				record[0]);
																me.close();
															}
														}
													} ]
												}]
												}]	
										});

						me.callParent(arguments);
					}
				});