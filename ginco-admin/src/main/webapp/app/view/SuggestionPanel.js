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

/*
 * File: app/view/NotePanel.js Note Creation/Edition
 *
 */
Ext.define('GincoApp.view.SuggestionPanel', {
	extend : 'Ext.panel.Panel',

	termId : null,
	thesaurusData : '',

	alias : 'widget.suggestionPanel',
	localized : true,
	closable : true,
	layout : {
		type : 'vbox',
		align : 'stretch'
	},
	
	xCreatedDateLabel : 'Creation date',
	xSave : 'Save',
	xCreatorLabel : 'Creator',
	xRecipientLabel : 'Recipient',
	xContentLabel : 'Content',
	xSuggestionListGridTitle: 'Suggestions list',
	xAddSuggestion: 'Add a suggestion',

	suggestionStore : Ext.create('GincoApp.store.SuggestionStore'),

	initComponent : function() {
		var me = this;

		Ext.applyIf(me, {
			items : [ {
				xtype : 'form',
				flex : 1,
				autoScroll : true,
				dockedItems : [ {
					xtype : 'toolbar',
					dock : 'top',
					items : [ {
						xtype : 'button',
						text : me.xSave,
						requiredRoles : [ 'ADMIN', 'MANAGER', 'EXPERT'],
						cls : 'save',
						iconCls : 'icon-save',
						itemId : 'saveSuggestion',
						disabled : true
					} ]
				} ],
				items : [ {
					xtype : 'gridpanel',
					itemId : 'suggestiongrid',
					title : me.xSuggestionListGridTitle,
					store : me.suggestionStore,

					dockedItems : [ {
						xtype : 'toolbar',
						dock : 'top',
						items : [ {
							xtype : 'button',
							itemId : 'newSuggestionBtn',
							requiredRoles : [ 'ADMIN', 'MANAGER', 'EXPERT'],
							iconCls : 'icon-add',
							text : me.xAddSuggestion
						}, {
							xtype : 'pagingtoolbar',
							store : me.suggestionStore,
							pageSize : 10,
							displayInfo : true
						} ]
					} ],

					columns : [ {
						dataIndex : 'identifier',
						hidden : true
					},{
						dataIndex : 'thesaurusId',
						hidden : true
					},{
						dataIndex : 'creator',
						text : me.xCreatorLabel,
					}, {
						dataIndex : 'recipient',
						text : me.xRecipientLabel
					}, {
						dataIndex : 'content',
						text : me.xContentLabel,
						flex:1
					}, {
						dataIndex : 'created',
						text : me.xCreatedDateLabel					
					}]
				} ]
			} ]
		});

		me.callParent(arguments);
	}
});