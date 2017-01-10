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

Ext.define('GincoApp.view.TopToolbar', {
	extend : 'Ext.toolbar.Toolbar',
	alias : 'widget.topToolBar',
	localized : true,
	xNewLabel : "New",
	xAdministrationLabel : "Administration",
	xControlLabel : "Control",
	xTopWelcomeLabel : "Hadoc GINCO Back-office",
	xImports : "Imports",
	xImportSkosLabel : "SKOS Import",
	xImportGincoXmlLabel : "Hadoc GINCO XML Import",
	xAboutLabel : "About",
	xSearchLabel : "Search",
	xSearchFieldText : "Search a term",
	xConnectedAsLabel : "Connected as",
	xNewMenu_ThesaurusLabel : "Thesaurus",
	xHelpLabel : "Help",
	xAccessibilityLabel : "Accessibility",
	xLogoutBtnLabel : "Logout",
	xSearchBtnTitle : "Click here to launch search",
	xSuggestionBtnLabel : "Suggestions",
	height : 64,

	initComponent : function() {
		var me = this;

		Ext.applyIf(me, {
			items : [ {
				xtype : 'box',
				autoEl : {
					tag : 'img',
					src : 'images/ginco-logo-xs.png',
					width : '32px',
					alt : 'logo ginco'
				}
			}, {
				xtype : 'box',
				autoEl : {
					tag : 'div',
					cls : 'title-bar',
					html : '<h1>Gestionnaire Thesaurus GINCO</h1>'
				}
			}, {
				xtype : 'tbseparator',
				flex : 2,
				height : 10,
				width : 10
			}, {
				xtype : 'buttongroup',
				title : me.xControlLabel,
				columns : 1,
				items : [ {
					xtype : 'button',
					requiredRoles : ['ADMIN'],
					disabled : false,
					text : me.xNewLabel,
					menu : {
						xtype : 'menu',
						width : 200,
						items : [ {
							xtype : 'keymenuitem',
							itemId : 'newThesaurusBtn',
							text : me.xNewMenu_ThesaurusLabel
						} ]
					}
				} ]
			}, {
				xtype : 'buttongroup',
				title : me.xAdministrationLabel,
				width:80,
				columns : 1,
				items : [ {
					xtype : 'button',
					requiredRoles : ['ADMIN'],
					disabled : false,
					text : me.xImports,
					menu : {
						xtype : 'menu',
						width : 200,
						items : [{
							xtype : 'keymenuitem',
							text : me.xImportSkosLabel,
							requiredRoles : ['ADMIN'],
							itemId: 'importBtn'
						},
						{
							xtype : 'keymenuitem',
							text : me.xImportGincoXmlLabel,
							requiredRoles : ['ADMIN'],
							itemId: 'importGincoXmlBtn'
						}]
					}
				} ]
			}, {
				xtype : 'buttongroup',
				title : me.xHelpLabel,
				columns : 2,
				items : [ {
					xtype : 'button',
					text : me.xHelpButton,
					menu : {
						xtype : 'menu',
						width : 200,
						items : [{
							xtype : 'keymenuitem',
							text : me.xAboutLabel,
							itemId: 'aproposbtn'
						},
						{
							xtype : 'keymenuitem',
							text : me.xDocumentationButton,
							itemId: 'documentationbtn'
						}]
					}
				} ]
			}, {
				xtype : 'tbseparator',
				flex : 2,
				height : 10,
				width : 10
			}, {
				xtype : 'triggerfield',
				width : 250,
				triggerCls: 'x-form-search-trigger',
				labelWidth : 70,
				fieldLabel : me.xSearchLabel,
				emptyText : me.xSearchFieldText,
				hideTrigger : false,
				itemId: 'searchBtn',
				repeatTriggerClick : false,
				disabled : false,
				triggerTitle : me.xSearchBtnTitle
			},{
				xtype : 'tbseparator',
				flex : 2,
				height : 10,
				width : 10
			},
			{
				xtype : 'tbtext',
				text : me.xConnectedAsLabel
			},
			{
				xtype : 'tbtext',
				itemId : 'username',
				cls : 'username'
			},
			{
				xtype : 'tbseparator'
			},
			{
				xtype : 'button',
				itemId : 'suggestionsBtn',
				iconCls : 'suggestion-icon',
				text : me.xSuggestionBtnLabel
			},
			{
				xtype : 'tbseparator'
			},
			{
				xtype : 'button',
				itemId : 'logoutbtn',
				iconCls : 'logout-icon',
				text : me.xLogoutBtnLabel
			}]
		});

		me.callParent(arguments);
	}

});