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
				'GincoApp.view.SelectArrayWin',
				{
					extend : 'Ext.window.Window',
					alias : 'widget.selectArrayWin',
					localized : true,

					requires : [ 'Ext.grid.PagingScroller' ],

					config : {
						thesaurusData : null,
						excludedConceptArrayId : null,
						currentParentId : null
					},

					viewConfig : {
						style : {
							overflow : 'auto',
							overflowX : 'hidden'
						}
					},

					/* Fields prompting values */
					xIdentifierColumnLabel : "Identifier",
					xArrayTitleColumnLabel : "Array Label",
					xSelect : "Select",

					width : 500,
					title : 'Sélectionner un tableau',
					titleAlign : 'center',
					modal : true,
					conceptArrayStore : null,

					initComponent : function() {
						var me = this;
						me.conceptArrayStore = Ext.create('GincoApp.store.ConceptArrayStore');
						me.conceptArrayStore.getProxy().extraParams = {
							thesaurusId : me.thesaurusData.id,
							excludedConceptArrayId : me.excludedConceptArrayId
						};
					
						me.conceptArrayStore.load();
						me.addEvents('selectBtn');

						Ext
								.applyIf(
										me,
										{
											items : [ {
												xtype : 'gridpanel',
												autoScroll : true,
												height : 300,
												flex : 1,
												store : me.conceptArrayStore,
												columns : [
														{
															dataIndex : 'identifier',
															text : me.xIdentifierColumnLabel
														},
														{
															dataIndex : 'label',
															text : me.xArrayTitleColumnLabel,
															flex : 1
														} ],
												dockedItems : [ {
													xtype : 'toolbar',
													dock : 'top',
													items : [ {
														xtype : 'button',
														text : me.xSelect,
														formBind : true,
														disabled : true,
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
																me
																		.fireEvent(
																				'selectBtn',
																				record);
																me.close(); 
															}
														}
													} ]
												} ],
												listeners : {
													select : function(view,
															record, item,
															index, e) {
														var me = this;
														var theButton = me
																.down('#selectButton');
														var win = me
																.up('selectArrayWin');
														var checkAgainstParentId = win.currentParentId;
														if (checkAgainstParentId) {
															if (checkAgainstParentId == record.data.identifier) {
																theButton
																		.setDisabled(true);
															} else {
																theButton
																		.setDisabled(false);
															}
														} else {
															theButton
																	.setDisabled(false);
														}
													}
												}

											} ]
										});

						me.callParent(arguments);
					}
				});