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

Ext.define('GincoApp.view.AlignmentColumn', {
	extend : 'Ext.grid.column.Column',
	require : ['Ext.ux.CTemplate'],
	alias : 'widget.alignmentcolumn',
	iconCls : 'icon-display',
	btnLbl : 'View',
	tpl : Ext
			.create('Ext.ux.CTemplate',
					'<tpl for="."><div class="alignment_item">{button} {url}</div></tpl>'),
	actionIdRe : new RegExp(Ext.baseCSSPrefix + 'action-col-(\\d+)'),
	constructor : function() {
		this.btnComponents = [];
		this.callParent(arguments);
	},
	defaultRenderer : function(value, meta, record, rowIndex, colIndex, store,
			view) {
		var me = this, targetConcepts = '', i = 0, prefix = Ext.baseCSSPrefix;
		var data = [];
		if (record.targetConcepts) {
			console.log('record.targetConcepts : '+record.targetConcepts );
			Ext.Array.each(record.targetConcepts().getRange(), function(
					targetConcept, index, array) {
				if (targetConcept.get('internalTargetConcept')) {
					console.log("internalTargetConcept");
					var btnComp = Ext.create('Ext.button.Button', {
								text : me.btnLbl,
								iconCls : 'icon-display',
								handler : function(btn) {
									me.onBtnClick(btn);
								},
								conceptId : targetConcept
										.get('internalTargetConcept'),
								thesaurusId : record.get('internalThesaurusId'),
								internalAlign : true
							});
					me.btnComponents.push(btnComp);
					data.push({
								url : targetConcept
										.get('internalTargetConcept'),
								button : btnComp
							});
				} else if (targetConcept.get('externalTargetConcept')) {
					console.log("externalTargetConcept");
					var btnComp = Ext.create('Ext.button.Button', {
								text : me.btnLbl,
								iconCls : 'icon-display',
								handler : function(btn) {
									me.onBtnClick(btn);
								},
								conceptId : targetConcept.get('externalTargetConcept'),
								thesaurusId : '',
								internalAlign : false
							});
					me.btnComponents.push(btnComp);
					data.push({
								url : targetConcept.get('externalTargetConcept'),
								button : btnComp
							});
				}
			});
		}
		return me.tpl.applyTemplate(data);
	},
	listeners : {
		destroy : function() {
			var me = this;
			Ext.Array.each(me.btnComponents, function(targetCmp) {
						targetCmp.destroy();
					});
		}
	},
	// Override
	onRender : function() {
		this.registerViewListeners();
		this.callParent(arguments);
	},

	registerViewListeners : function() {
		var me = this, view = me.up('tablepanel').getView();

		me.mon(view, 'beforerefresh', me.beforeViewRefresh, me);
		me.mon(view, 'refresh', me.onViewChange, me);
		me.mon(view, 'itemupdate', me.onViewChange, me);
		me.mon(view, 'itemadd', me.onViewChange, me);
		me.mon(view, 'itemremove', me.onViewChange, me);
	},
	/*
	 * In IE setting the innerHTML will destroy the nodes for the previous
	 * content. If we try to reuse components it will fail as their DOM nodes
	 * will have been torn apart. To defend against this we must remove the
	 * components from the DOM just before the grid view is refreshed.
	 */
	beforeViewRefresh : function() {
		if (Ext.isIE) {
			if (this.compIds){
				var ids = this.compIds, len = ids.length, item, el, parentEl, index;

				for (index=0; index < len; index++) {
					if ((item = Ext.getCmp(ids[index])) && (el = item.getEl())
							&& (el = el.dom) && (parentEl = el.parentNode)) {
						parentEl.removeChild(el);
					}
				}
			}
		}
	},

	// View has changed, may be a full refresh or just a single row
	onViewChange : function() {
		var me = this, tpl = me.tpl;
		// Batch the resizing of child components until after they've all been
		// injected
		if (tpl.isCTemplate) {
			// No need to wait for the polling, the sooner we inject the less
			// painful it is
			tpl.injectComponents();

			// If the template picked up other components in the data we can
			// just ignore them, they're not for us
			tpl.reset();
		}
		// A view change could mean scrollbar problems. Note this won't actually
		// do anything till we call resumeResizing
		me.redoScrollbars();

	},
	redoScrollbars : function() {
		var me = this, grid = me.up('tablepanel');
		grid.doLayout();
	},

	onBtnClick : function(theButton) {
		var me = this;
		me.fireEvent('gotoconcept', me, theButton.thesaurusId,
				theButton.conceptId, theButton.internalAlign);
	}
});