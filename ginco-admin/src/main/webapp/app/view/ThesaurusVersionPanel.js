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
 * File: app/view/ThesaurusVersionPanel.js Thesaurus Versions Display/Creation/Edition
 * 
 */
Ext.Loader.setPath('Ext.ux', 'extjs/ux');
Ext.require([ 'Ext.ux.CheckColumn' ]);

Ext.define('GincoApp.view.ThesaurusVersionPanel', {
	extend : 'Ext.panel.Panel',

	alias : 'widget.thesaurusVersionPanel',
	localized : true,
	closable : false,
	layout : {
		type : 'vbox',
		align : 'stretch'
	},
	
	xVersionListGridTitle : 'Grid of versions',
	xIdentifierLabel : 'Identifier',
	xVersionDate : 'Date of version',
	xVersionNote : 'Version note',
	xVersionStatus : 'Statut',
	xThisVersion : 'This version',

	initComponent : function() {
		
		var rowEditing = Ext.create('Ext.grid.plugin.RowEditing', {
	        clicksToMoveEditor: 1,
	        autoCancel: false,
	        pluginId: 'rowEditing'
	    });
		
		var me = this;
		me.thesaurusVersionStore =  Ext.create('GincoApp.store.ThesaurusVersionStore');
		me.thesaurusVersionStatusStore =  Ext.create('GincoApp.store.ThesaurusVersionStatusStore');

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
						text : 'Enregistrer',
						cls : 'save',
						iconCls : 'icon-save',
						itemId : 'saveThesaurusVersion',
						disabled : true
					} ]
				} ],
				items : [ {
					xtype : 'gridpanel',
			        height: 400,
					plugins : [ rowEditing ],
					
					itemId : 'versionGrid',
					title : me.xVersionListGridTitle,
					store : me.thesaurusVersionStore,

					columns : [ {
						dataIndex : 'identifier',
						text : me.xIdentifierLabel,
						hidden : true
					}, {
						dataIndex : 'date',
						text : me.xVersionDate,
						//flex : 1,
					}, {
						dataIndex : 'versionNote',
						text : me.xVersionNote,
						width: '600px',
						editor: {
							//no xtype because default is texfield
						}
					}, {
						dataIndex : 'status',
						text : me.xVersionStatus,
						editor : new Ext.form.field.ComboBox(
								{
									typeAhead : true,
									triggerAction : 'all',
									selectOnTab : true,
									store : me.thesaurusVersionStatusStore,
									lazyRender : true,
									listClass : 'x-combo-list-small',
									displayField : 'statusLabel',
									valueField : 'status'
								})
					},{
			            xtype: 'checkcolumn',
			            singleSelect : true,
			            dataIndex: 'thisVersion',
			            text : me.xThisVersion,
			            editor: {
			                xtype: 'checkbox'
			            }
					}]
				} ]
			} ]
		});

		me.callParent(arguments);
	}
});