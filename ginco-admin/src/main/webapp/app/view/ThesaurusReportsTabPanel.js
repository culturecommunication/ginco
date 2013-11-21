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

Ext.define('GincoApp.view.ThesaurusReportsTabPanel', {
	extend : 'Ext.panel.Panel',

	alias : 'widget.thesaurusReportsTabPanel',
	layout : {
		type : 'vbox',
		align : 'stretch'
	},
	xReportsTitle : 'Reports',
	xReportsChoicePanel : 'Report choice',
	xReportsTypeCombo : 'Report type',
	xReportsResultPanel : 'Report results',
	xDisplayResultBtnLabel : 'Dispay results',
	xIdentifierColumnLabel : 'Identifier',
	xLexicalValueColumnLabel : 'Lexical value',
	xConceptWoNotesLabel : 'Concepts without notes',
	xTermsWoNotesLabel : 'Terms without notes',
	xViewToolTip : "Click here to open element",
	reportsTypeStore : null,
	reportStore : null,
	localized : true,

	initComponent : function() {

		var me = this;
		me.reportStore = Ext.create('GincoApp.store.ReportResultStore');
		me.reportsTypeStore = Ext.create('Ext.data.Store', {
					fields : [{
								name : 'type',
								type : 'int'
							}, {
								name : 'typeLabel',
								type : 'string'
							}],
					data : [{
								type : 0,
								typeLabel : me.xConceptWoNotesLabel
							}, {
								type : 1,
								typeLabel : me.xTermsWoNotesLabel
							}]
				});
		Ext.applyIf(me, {
					title : me.xReportsTitle,
					iconCls : 'icon-reports',
					items : [{
								xtype : 'panel',
								title : me.xReportsChoicePanel,
								frame : true,
								items : [{
											xtype : 'ariacombo',
											name : 'type',
											forceSelection : true,
											itemId : 'reportTypeCombo',
											editable : false,
											displayField : 'typeLabel',
											valueField : 'type',
											fieldLabel : me.xReportsTypeCombo,
											store : me.reportsTypeStore
										}]
							}, {
								xtype : 'gridpanel',
								title : me.xReportsResultPanel,
								store : me.reportStore,
								dock : 'bottom',
								flex : 1,
								dockedItems : [{
											xtype : 'pagingtoolbar',
											store : me.reportStore,
											dock : 'bottom',
											displayInfo : true
										}],
								columns : [{
											dataIndex : 'identifier',
											text : me.xIdentifierColumnLabel,
											sortable : false
										}, {
											dataIndex : 'lexicalValue',
											text : me.xLexicalValueColumnLabel,
											flex : 1,
											sortable : true
										}, {
											xtype : 'actioncolumn',
											itemId : 'reportActionColumn',
											items : [{
														tooltip : me.xViewToolTip,
														iconCls : 'icon-view',
														icon : 'images/visualisation.png'
													}]
										}]

							}]
				});
		me.callParent(arguments);
	}
});