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
	closable : true,
	localized : true,
	query : '*',
	layout : {
		type : 'vbox',
		align : 'stretch'
	},

	initComponent : function() {
		var me = this;
		var me = this;
		me.searchStore = Ext.create('GincoApp.store.SearchResultStore');

		Ext.applyIf(me, {
			title : me.xSearchPanelTitle,
			items : [ {
				xtype : 'gridpanel',
				dock : 'top',
				title : me.xSearchPanelTitle,
				flex : 1,
				store : me.searchStore,
				viewConfig : {

				},
				tbar : [ {
						xtype : 'button',
						itemId : 'displayResultBtn',
						iconCls:'icon-display',
						text : me.xDisplayResultBtnLabel
					}],
				dockedItems: [{
			        xtype: 'pagingtoolbar',
			        store :  me.searchStore,
			        dock: 'bottom',
			        displayInfo: true
			    }],
				columns : [ {
					dataIndex : 'identifier',
					text : me.xIdentifierColumnLabel
				}, {
					dataIndex : 'lexicalValue',
					text : me.xLexicalValueColumnLabel,
					flex : 1
				},
				{
					dataIndex : 'thesaurusTitle',
					text : me.xThesaurusTitleColumnLabel,
					width :200,
				},
				{
					dataIndex : 'type',
					text : me.xTypeValueColumnLabel,
					width :200,
				} ]
			} ]
		});

		me.callParent(arguments);
	}

});