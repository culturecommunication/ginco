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

Ext.define('GincoApp.view.SearchPanel', {
	extend : 'Ext.panel.Panel',
	alias : 'widget.searchPanel',
	xIdentifierColumnLabel : 'Identifier',
	xLexicalValueColumnLabel : 'Lexical value',
	xThesaurusTitleColumnLabel : 'Thesaurus',
	xTypeValueColumnLabel : 'Type',
	xDisplayResultBtnLabel : 'Display',
	xSearchPanelTitle : 'Search results',
	xAdvancedSearchPnlTitle : 'Advanced search',
	xAdvancedSearchBtn : 'Filter',
	xAdvancedSearchThesaurusFilter : 'Thesaurus',
	xAdvancedSearchTypeFilter : 'Type',
	xAdvancedSearchStatusFilter : 'Status',
	xAdvancedSearchLanguageFilter : 'Language',
	xAdvancedSearchCreationDateFilter : 'Creation date',
	xAdvancedSearchModificationDateFilter : 'Modification date',
	xQueryFieldLbl : 'Query',
	closable : true,
	localized : true,
	searchQuery : '*',
	xTypeLabels : {
		ThesaurusTerm : 'Term',
		ThesaurusConcept : 'Concept'
	},
	layout : {
		type : 'vbox',
		align : 'stretch'
	},
	typeRenderer : function(value, record) {
		return this.ownerCt.xTypeLabels[value];
	},

	initComponent : function() {
		var me = this;
		me.searchStore = Ext.create('GincoApp.store.SearchResultStore');
		me.thesaurusStore = Ext.create('GincoApp.store.ThesaurusStore',
		{
			listeners : {
				load : {
					fn : function (theStore)
					{
						theStore.insert(0,{
							id : "-1",
							title : '-'
						});
					}
				}
			}
		});
		
		me.langStore = Ext.create('GincoApp.store.ThesaurusLanguageStore',
		{
			listeners : {
				load : {
					fn : function (theStore)
					{
						theStore.insert(0,{
							id : "-1",
							refname : '-'
						});
					}
				}
			}
		});
		
		me.conceptStatusStore = Ext.create('GincoApp.store.ConceptStatusStore',
				{
					listeners : {
						load : {
							fn : function (theStore)
							{
								theStore.insert(0,{
									status : "-1",
									statusLabel : '-'
								});
							}
						}
					}
		});

		Ext.applyIf(me, {
			title : me.xSearchPanelTitle,
			iconCls : 'icon-display',
			items : [ {
				xtype : 'form',
				itemId : 'advancedSearchForm',
				dock : 'top',
				title : me.xAdvancedSearchPnlTitle,
				collapsible : true,
				collapsed : true,
				tbar : [ {
					xtype : 'button',
					itemId : 'filterBtn',
					formBind: true,
					iconCls : 'icon-display',
					text : me.xAdvancedSearchBtn
				} ],
				items : [ {
					defaults : {
						margin : '10 0 10 10'
					},
					layout : 'column',
					items : [
					{
						xtype : 'textfield',
						name : 'query',
						fieldLabel : me.xQueryFieldLbl
					},
					{
						xtype : 'ariacombo',
						name : 'thesaurus',
						editable : false,
						fieldLabel : me.xAdvancedSearchThesaurusFilter,
						displayField : 'title',
						valueField : 'id',
						store :'ThesaurusStore'
					}, {
						xtype : 'ariacombo',
						name : 'type',
						editable : false,
						displayField : 'typeLabel',
						valueField : 'type',
						fieldLabel : me.xAdvancedSearchTypeFilter,
						store :'SearchTypeStore'
					}, {
						xtype : 'ariacombo',
						name : 'status',
						fieldLabel : me.xAdvancedSearchStatusFilter,
						displayField : 'statusLabel',
						valueField : 'status',
						editable : false,
						store : me.conceptStatusStore
					},
					{
						xtype : 'ariacombo',
						name : 'language',
						fieldLabel : me.xAdvancedSearchLanguageFilter,
						displayField : 'refname',
						valueField : 'id',
						editable : false,
						store : me.langStore
					},
					{
						xtype : 'datefield',
						name : 'creationdate',
						fieldLabel : me.xAdvancedSearchCreationDateFilter
					},
					{
						xtype : 'datefield',
						name : 'modificationdate',
						fieldLabel : me.xAdvancedSearchModificationDateFilter
					}
					]
				} ]
			}, {
				xtype : 'gridpanel',
				dock : 'bottom',
				title : me.xSearchPanelTitle,
				flex : 1,
				store : me.searchStore,
				viewConfig : {

				},
				tbar : [ {
					xtype : 'button',
					itemId : 'displayResultBtn',
					iconCls : 'icon-display',
					text : me.xDisplayResultBtnLabel
				} ],
				dockedItems : [ {
					xtype : 'pagingtoolbar',
					store : me.searchStore,
					dock : 'bottom',
					displayInfo : true
				} ],
				columns : [ {
					dataIndex : 'identifier',
					text : me.xIdentifierColumnLabel,
					sortable: false
				}, {
					dataIndex : 'lexicalValue',
					text : me.xLexicalValueColumnLabel,
					flex : 1,
					sortable: true
				},
				{
					dataIndex : 'modified',
					text : me.xAdvancedSearchModificationDateFilter,
					flex : 1,
					hidden: true,
					sortable: true
				},
				{
					dataIndex : 'created',
					text : me.xAdvancedSearchCreationDateFilter,
					flex : 1,
					hidden: true,
					sortable: true
				},
				{
					dataIndex : 'thesaurusTitle',
					text : me.xThesaurusTitleColumnLabel,
					width : 200,
					sortable: false
				}, {
					dataIndex : 'type',
					text : me.xTypeValueColumnLabel,
					renderer : me.typeRenderer,
					width : 200
				} ]
			} ]
		});

		me.callParent(arguments);
	}

});