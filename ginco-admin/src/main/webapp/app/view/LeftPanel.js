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

Ext.define('GincoApp.view.LeftPanel', {
	extend : 'Ext.panel.Panel',
	alias : 'widget.leftPanel',
	xThesaurusLabelUnfiltered : 'All vocabularies',
	xThesaurusLabelFiltered : 'My vocabularies',
	xRefreshBtnTooltip : "Refresh explorer",
	xPinBtnTooltip : "Link with tabs",
	xSelectBtnLabel : "Display selection",
	xSelectBtnToolTip : "Display the selected element",
	xRoleFilterBtnLabel : 'Display my vocabularies',
	xRoleFilterBtnToolTip : 'Filter the list according to my roles',
	xFilterLabel : 'Filter',
	xCollapseToolTip : 'Collapse explorer',
	xExpandToolTip : 'Expand explorer',
	localized : true,
	frame : false,
	width : 270,
	collapsible : true,
	header : true,
	title : '',
	layout : {
		type : 'vbox',
		align : 'stretch'
	},
	addTools : function() {
		var me = this;
		me.collapseTool.tooltipType = 'title';
		me.collapseTool.tooltip = me.xCollapseToolTip;
	},
	setFilter: function (filterStatus) {
		var me = this;
		if (filterStatus) 
			me.down('treepanel').setTitle(me.xThesaurusLabelFiltered);
			else
			me.down('treepanel').setTitle(me.xThesaurusLabelUnfiltered);
	},

	initComponent : function() {
		var me = this;
		this.addEvents({
					refreshtree : true
				});

		me.thesaurusOrganizationStore = Ext.create(
				'GincoApp.store.ThesaurusOrganizationStore', {
					listeners : {
						load : {
							fn : function(theStore) {
								theStore.insert(0, {
											identifier : "-1",
											name : '-'
										});
							}
						}
					}
				});

		Ext.applyIf(me, {
			items : [{
				xtype : 'treepanel',
				id : 'mainTreeView',
				autoScroll : true,
				dblClickExpand : false,
				collapsible : false,
				title : me.xThesaurusLabelFiltered,
				store : 'MainTreeStore',
				displayField : 'title',
				rootVisible : false,
				useArrows : true,
				animate : false,
				flex : 1,
				dockedItems : [{
							xtype : 'toolbar',
							dock : 'bottom',
							items : [{
										text : me.xSelectBtnLabel,
										disabled : true,
										itemId : 'selectBtn',
										iconCls : 'icon-right',
										iconAlign: 'right',
										tooltip : me.xSelectBtnToolTip,
										tooltipType : 'title',
										flex : 1
									}]
						}, {
							xtype : 'toolbar',
							dock : 'top',
							vertical : true,
							items : [{
										xtype : 'container',
										flex : 1,
										layout : {
											type : 'hbox',
											align : 'middle'
										},
										items : [{
													xtype : 'button',
													flex : 1,
													text : me.xRoleFilterBtnLabel,
													itemId : 'roleFilterBtn',
													tooltip : me.xRoleFilterBtnToolTip,
													tooltipType : 'title',
													enableToggle : true,
													pressed : true
												}]
									}, {
										xtype : 'container',
										layout : {
											type : 'hbox',
											align : 'middle'
										},
										items : [{
											fieldLabel : me.xFilterLabel,
											xtype : 'ariacombo',
											itemId : 'authorFilter',
											labelWidth : 70,
											editable : false,
											displayField : 'name',
											valueField : 'name',
											store : me.thesaurusOrganizationStore
										}]
									}]
						}],
				viewConfig : {
					// loadMask : true,
					preserveScrollOnRefresh : true
				},
				tools : [{
							type : 'refresh',
							tooltip : this.xRefreshBtnTooltip,
							tooltipType : 'title'
						}, {
							itemId : 'pinBtn',
							type : 'unpin',
							tooltip : this.xPinBtnTooltip,
							tooltipType : 'title'
						}]
			}]
		});

		me.callParent(arguments);
	}

});