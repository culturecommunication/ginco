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

Ext.define('GincoApp.view.UsersTabPanel', {
			extend : 'Ext.panel.Panel',

			alias : 'widget.usersTabPanel',
			layout : {
				type : 'vbox',
				align : 'stretch'
			},
			localized : true,
			iconCls : 'icon-user',
			xUsersTitle : 'Users',
			xUsernameColumnLabel: 'User name',
			xRoleColumnLabel: 'Role',
			xAddThesaurusUser: 'Add a thesaurus user',
			xSave: 'Save',
			xRoleLabels : ['Manager','Expert'],
			userRoleStore : null,
			
			roleRenderer : function(value,record)
			{
				return this.ownerCt.ownerCt.xRoleLabels[value];			
			},

			initComponent : function() {
				var me = this;
				
				var rowEditing = Ext.create('Ext.grid.plugin.RowEditing', {
			        clicksToMoveEditor: 1,
			        autoCancel: false,
			        pluginId: 'rowEditing'
			    });
				
				me.userRoleStore = Ext.create('GincoApp.store.UserRoleStore');
				me.roleStore =  Ext.create('GincoApp.store.RoleStore');
				
				Ext.applyIf(me, {
					title : me.xUsersTitle,
					items : [ {
						xtype : 'form',
						layout : {
							type : 'vbox',
							align : 'stretch'
						},
						flex : 1,
						dockedItems : [ {
							xtype : 'toolbar',
							dock : 'top',
							items : [ {
								xtype : 'button',
								text : me.xSave,
								cls : 'save',
								iconCls : 'icon-save',
								itemId : 'saveUsers',
								disabled : true
							},{
								xtype : 'button',
								iconCls : 'icon-add',
								requiredRoles : ['ADMIN'],
								itemId : 'addThesaurusUser',
								text : me.xAddThesaurusUser
							} ]
						} ],
						items : [ {							
						
							xtype : 'gridpanel',
							title : me.xUsersTitle,
							itemId : 'userGrid',
							autoScroll:true,
							plugins : [ rowEditing ],
							flex:1,
							store : me.userRoleStore,
							columns : [
							       {
							    	   dataIndex : 'identifier',
							    	   hidden:true,
							    	   text : me.xIdentifierLabel
							       },
						           {
							    	   dataIndex : 'username',
							    	   text : me.xUsernameColumnLabel,
							    	   editor: {
			                                xtype: 'textfield',
			                                allowBlank: false
			                            },
			                            flex: 1
							        },{
										dataIndex : 'role',
										stopSelection : false,
										renderer : me.roleRenderer,
										width: 200,
										editor : new Ext.form.field.ComboBox(
												{
													typeAhead : true,
													triggerAction : 'all',
													selectOnTab : true,
													store : me.roleStore,
													lazyRender : true,
													listClass : 'x-combo-list-small',
													displayField : 'roleLabel',
													valueField : 'role'
												})
									}						           
						           ],
						           dockedItems: [{
						        	   xtype: 'pagingtoolbar',
						        	   store :  me.userRoleStore,
						        	   dock: 'bottom',
						        	   displayInfo: true
						           }]	
							}]
						}]

				});
				me.callParent(arguments);
			}
		});