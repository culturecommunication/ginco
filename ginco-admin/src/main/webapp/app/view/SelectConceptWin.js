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
				'GincoApp.view.SelectConceptWin',
				{
					extend : 'Ext.window.Window',
					alias : 'widget.selectConceptWin',
					localized : true,

					requires : [ 'Ext.grid.PagingScroller' ],

					config : {
						thesaurusData : null,
						conceptId : null,
						getChildren : false,
						getArrayConcepts : false,
						getGroupConcepts : false,
						arrayId : null,
						groupId : null,
						searchOrphans : null,
						showTree : false,
						checkstore : null,
						onlyValidatedConcepts : false,
					},

					viewConfig : {
						style : {
							overflow : 'auto',
							overflowX : 'hidden'
						}
					},

					/* Fields prompting values */
					xIdentifierColumnLabel : "Identifier",
					xLexicalValueColumnLabel : "Label",
					xSelect : "Select",

					width : 500,
					height : 530,
					title : 'Sélectionner un concept',
					titleAlign : 'center',
					modal : true,
					conceptReducedStore : null,

					initComponent : function() {
						var me = this;

						me.conceptReducedStore = Ext.create('GincoApp.store.ConceptReducedStore');

						me = defineCase(me,'');

						me.conceptReducedStore.pageSize = 20;
						me.conceptReducedStore.load();

						me.addEvents('selectBtn');

						Ext
								.applyIf(
										me,
										{
											items : [{
												xtype : 'gridpanel',
												autoScroll : true,
												flex : 1,
												store : me.conceptReducedStore,
												columns : [
														{
															dataIndex : 'identifier',
															text : me.xIdentifierColumnLabel
														},
														{
															dataIndex : 'label',
															text : me.xLexicalValueColumnLabel,
															flex : 1
														} ],
												dockedItems : [{
											        xtype: 'pagingtoolbar',
											        store :  me.conceptReducedStore,
											        dock: 'bottom',
											        displayInfo: true
											    },{
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
													},
													{ xtype: 'tbseparator' },
													{
											            xtype    : 'textfield',
											            name: 'conceptNameLike',
											            emptyText: 'affiner votre recherche',
										                allowBlank: true,
										                id : 'conceptNameLikeField',
										                listeners: {
										                    change : {
										                        fn: function(){ 
										                        	var conceptNameLike = Ext.getCmp('conceptNameLikeField').getValue();
										                        	
										                        	me = defineCase(me,conceptNameLike);
										                        	
										                        	me.conceptReducedStore.pageSize = 20;
										                        	me.conceptReducedStore.currentPage = 1;
										    						me.conceptReducedStore.load();
										                        }
										                    }
										                }
											        }
													]
												} ],
												listeners : {
													select : function(view,
															record, item,
															index, e) {
														var me = this;
														var theButton = me
																.down('#selectButton');
														var win = me
																.up('selectConceptWin');
														var checkAgainstStore = win.checkstore;
														if (checkAgainstStore) {
															if (checkAgainstStore
																	.findRecord(
																			'identifier',
																			record.data.identifier) == null) {
																theButton
																		.setDisabled(false);
															} else {
																theButton
																		.setDisabled(true);
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


function defineCase(me,conceptNameLike) {
	if (!me.getChildren && !me.getArrayConcepts && !me.getGroupConcepts) {
		// Searching all concepts in the thesaurus
		me.conceptReducedStore.getProxy().extraParams = {
			like : conceptNameLike,
			id : me.conceptId,
			thesaurusId : me.thesaurusData.id,
			searchOrphans : me.searchOrphans,
			onlyValidatedConcepts: me.onlyValidatedConcepts
		};
	} else if(me.getArrayConcepts) {
		// Searching only the children concepts of a concept
		// which id is defined in conceptId variable
		me.conceptReducedStore.getProxy().url = 'services/ui/thesaurusconceptservice/getAvailableConceptsOfArray';
		me.conceptReducedStore.getProxy().extraParams = {
			like : conceptNameLike,
			arrayId : me.arrayId,
			thesaurusId : me.thesaurusData.id
		};
	} else if (me.getGroupConcepts){
		me.conceptReducedStore.getProxy().url = 'services/ui/thesaurusconceptservice/getAvailableConceptsOfGroup';
		me.conceptReducedStore.getProxy().extraParams = {
			like : conceptNameLike,
			groupId : me.groupId,
			thesaurusId : me.thesaurusData.id
		};
	} else {
		// Searching only the children concepts of a concept
		// which id is defined in conceptId variable
		me.conceptReducedStore.getProxy().url = 'services/ui/thesaurusconceptservice/getSimpleChildrenConcepts';
		me.conceptReducedStore.getProxy().extraParams = {
			like : conceptNameLike,
			conceptId : me.conceptId
		};
	}
	return me;
}

