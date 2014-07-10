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

Ext.define('GincoLoginApp.view.GincoLoginForm', {
	extend : 'Ext.window.Window',

	layout : 'fit',
	alias: 'widget.loginForm',
	width : 400,
	height : 190,
	localized : true,
	xLoginWinTitle : 'Please enter your credentials',
	xUserNameLbl : "Username",
	xPasswordLbl : "Password",
	xLoginBtnLbl : "Login",
	xLoginErrorLbl : "Identification error. Please retry.",
	xLockErrorLbl : "Too many authentication failure, the account is disabled temporarily",
	closable : false,
	resizable : false,
	plain : true,
	border : false,
	defaultFocus:'username',
	initComponent : function() {
		var me = this;
		Ext.applyIf(me, {
			items : [ {
				xtype : 'form',
				bodyPadding: '10 10 0',
				defaults : {
					anchor : '90%'
				},
				url:'login_security_check', 
				title : me.xLoginWinTitle,
				tbar :  [ {
					xtype: 'tbtext',
					itemId : 'message',
					cls : 'mandatory-field ',
					text : me.xLoginErrorLbl,
					hidden : true
				},
				 {
					xtype: 'tbtext',
					itemId : 'lockmessage',
					cls : 'mandatory-field ',
					text : me.xLockErrorLbl,
					hidden : true
				}],
				items : [ {
					xtype : 'textfield',
					fieldLabel : me.xUserNameLbl,
					name : 'j_username',
					itemId : 'username',
					allowBlank : false
				}, {
					xtype : 'textfield',
					fieldLabel : me.xPasswordLbl,
					name : 'j_password',
					inputType : 'password',
					allowBlank : false
				},
				{
					xtype : 'hidden',
					name : Ext.TokenName,
					value : Ext.TokenValue
				}],
				buttons : [ {
					text : me.xLoginBtnLbl,
					itemId : 'loginBtn',
					formBind : true
				} ]
			} ]

		});

		me.callParent(arguments);
	}

});