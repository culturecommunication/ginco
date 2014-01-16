/**
 * Copyright or © or Copr. Ministère Français chargé de la Culture et de la
 * Communication (2013) <p/> contact.ginco_at_culture.gouv.fr <p/> This software
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

/**
 * @docauthor Hubert FONGARNAND
 * 
 * This files contains extends or override to implement missing accessibility
 * functions to ExtJS WARNING this file is made for EXTJS-4.1-GPL only
 */

/*
 * Implement Aria for extjs combobox
 */

Ext.define('Thesaurus.form.field.ComboBox', {
	extend : 'Ext.form.field.ComboBox',
	alias : 'widget.ariacombo',
	fieldSubTpl : [
			'<div class="{hiddenDataCls}" role="presentation"></div>',
			'<input id="{id}" type="{type}" role="combobox" {inputAttrTpl} class="{fieldCls} {typeCls} {editableCls}" autocomplete="off"',
			'<tpl if="value"> value="{[Ext.util.Format.htmlEncode(values.value)]}"</tpl>',
			'<tpl if="name"> name="{name}"</tpl>',
			'<tpl if="placeholder"> placeholder="{placeholder}"</tpl>',
			'<tpl if="size"> size="{size}"</tpl>',
			'<tpl if="maxLength !== undefined"> maxlength="{maxLength}"</tpl>',
			'<tpl if="readOnly"> readonly="readonly" aria-readonly="true" aria-autocomplete="none"</tpl>',
			'<tpl if="disabled"> disabled="disabled"</tpl>',
			'<tpl if="tabIdx"> tabIndex="{tabIdx}"</tpl>',
			'<tpl if="fieldStyle"> style="{fieldStyle}"</tpl>', '/>', {
				compiled : true,
				disableFormats : true
			}],
	createPicker : function() {
		var me = this, picker, pickerCfg = Ext.apply({
					xtype : 'ariaboundlist',
					pickerField : me,
					selModel : {
						mode : me.multiSelect ? 'SIMPLE' : 'SINGLE'
					},
					floating : true,
					hidden : true,
					store : me.store,
					displayField : me.displayField,
					focusOnToFront : false,
					pageSize : me.pageSize,
					multiSelect : me.multiSelect,
					tpl : me.tpl
				}, me.listConfig, me.defaultListConfig);

		picker = me.picker = Ext.widget(pickerCfg);
		if (me.pageSize) {
			picker.pagingToolbar.on('beforechange', me.onPageChange, me);
		}

		me.mon(picker, {
					itemclick : me.onItemClick,
					refresh : me.onListRefresh,
					scope : me
				});

		me.mon(picker.getSelectionModel(), {
					beforeselect : me.onBeforeSelect,
					beforedeselect : me.onBeforeDeselect,
					selectionchange : me.onListSelectionChange,
					scope : me
				});
		Ext.get(me.id + "-inputEl").set({
					'aria-owns' : picker.id + '-listUl'
				});
		return picker;
	},
	onExpand : function() {
		var me = this, picker, collapseIf;
		if (me.rendered && me.picker) {
			picker = me.getPicker();;
			Ext.get(picker.id + "-listUl").set({
						'aria-expanded' : true
					});
		}
		me.callParent();
	},
	onCollapse : function() {
		var me = this, picker, collapseIf;
		if (me.rendered && me.picker) {
			picker = me.getPicker();
			Ext.get(picker.id + "-listUl").set({
						'aria-expanded' : false
					});
			Ext.get(me.id + "-inputEl").set({
						'aria-activedescendant' : null
					});
		}
		me.callParent();
	},
	focusItem : function(item) {
		var me = this;
		Ext.get(me.id + "-inputEl").set({
					'aria-activedescendant' : item.id
				});
	}

});

Ext.define('Thesaurus.form.field.Checkbox', {
			override : 'Ext.form.field.Checkbox',
			ariaRole : 'checkbox',
			setRawValue : function(value) {
				var me = this, inputEl = me.inputEl, checked = me.isChecked(
						value, me.inputValue);
				if (inputEl) {
					inputEl.dom.setAttribute('aria-checked', checked);
				}
				return me.callParent(arguments);
			}
		});

Ext.define('ThesaurusExt.toolbar.Toolbar', {
			override : 'Ext.toolbar.Toolbar',
			initAria : function() {
				var me = this;
				me.callParent();
				var actionEl = this.getActionEl();
				actionEl.dom.setAttribute('aria-live', 'polite');
			}
		});

Ext.define('Thesaurus.view.BoundList', {
	extend : 'Ext.view.BoundList',
	alias : 'widget.ariaboundlist',
	multiSelect : false,
	initComponent : function() {
		var me = this, baseCls = me.baseCls, itemCls = me.itemCls, id = me.id;

		if (!me.tpl) {
			// should be setting aria-posinset based on entire set of data
			// not filtered set
			me.tpl = new Ext.XTemplate(
					'<ul id="' + id
							+ '-listUl" role="listbox" aria-multiselectable="'
							+ me.multiSelect + '"><tpl for=".">',
					'<li id="'
							+ id
							+ '-listUl-{#}" role="option" tabindex="-1" class="'
							+ itemCls + '">' + me.getInnerTpl(me.displayField)
							+ '</li>', '</tpl></ul>');
		}
		me.callParent();
	},
	highlightItem : function(item) {
		var me = this;
		me.callParent(arguments);
		me.pickerField.focusItem(item);
	},
	getInnerTpl: function(displayField) {
        return '{ '+displayField+':htmlEncode }';
    },
	onItemSelect : function(record) {
		var me = this;
		var node = this.getNode(record);
		if (node) {
			Ext.fly(node).set({
						'aria-selected' : true
					});
		}
		me.callParent(arguments);
	},
	onItemDeselect : function(record) {
		var me = this;
		var node = this.getNode(record);
		if (node) {
			Ext.fly(node).set({
						'aria-selected' : false
					});
		}
		me.callParent(arguments);
	}
});

/*
 * Define a menu item with an accesskey
 */

Ext.define('Thesaurus.ext.KeyMenuItem', {
	extend : 'Ext.menu.Item',
	alias : 'widget.keymenuitem',
	renderTpl : [
			'<tpl if="plain">',
			'{text}',
			'</tpl>',
			'<tpl if="!plain">',
			'<a id="{id}-itemEl" class="'
					+ Ext.baseCSSPrefix
					+ 'menu-item-link keymenuitem-link" href="{href}" <tpl if="hrefTarget">target="{hrefTarget}"</tpl> hidefocus="true" unselectable="on">',
			'<img id="{id}-iconEl" src="{icon}" class="' + Ext.baseCSSPrefix
					+ 'menu-item-icon {iconCls}" />',
			'<span id="{id}-textEl" class="' + Ext.baseCSSPrefix
					+ 'menu-item-text keymenuitem-text">{text}</span>',
			'<span class="'
					+ Ext.baseCSSPrefix
					+ 'menu-item-text keymenuitem-cmdTxt" <tpl if="menu">style="margin-right: 17px;"</tpl> >{cmdTxt}</span>',
			'<tpl if="menu">',
			'<img id="{id}-arrowEl" src="{blank}" class="' + Ext.baseCSSPrefix
					+ 'menu-item-arrow" />', '</tpl>', '</a>', '</tpl>'],
	beforeRender : function(ct, pos) {
		// Intercept the call to onRender so we can add the
		// keyboard shortcut text to the render data which
		// will be used by the template
		var me = this;
		Ext.applyIf(me.renderData, {
					cmdTxt : me.cmdTxt
				});
		me.callParent(arguments);
	}
});

/*
 * Manage focus on table
 */
Ext.view.View.addInheritableStatics({
			EventMap : {
				mousedown : 'MouseDown',
				mouseup : 'MouseUp',
				click : 'Click',
				dblclick : 'DblClick',
				contextmenu : 'ContextMenu',
				mouseover : 'MouseOver',
				mouseout : 'MouseOut',
				mouseenter : 'MouseEnter',
				mouseleave : 'MouseLeave',
				keydown : 'KeyDown',
				focus : 'Focus',
				blur : 'Blur'
			}
		});

/*
 * Manage focus on table
 */
Ext.define('Thesaurus.ext.view.View', {
			override : 'Ext.view.View',
			focusCls : 'focus',
			onBeforeContainerFocus : Ext.emptyFn,
			onBeforeContainerBlur : Ext.emptyFn,
			onContainerFocus : function(e) {
				var me = this, focusCls = me.focusCls;
				me.getEl().addCls(me.addClsWithUI(focusCls, true));
				if (!me.hasFocus) {
					me.hasFocus = true;
					me.fireEvent('focus', me, e);
				}
				var selectModel = me.getSelectionModel();
				var selectedArray = selectModel.getSelection();
				if (selectModel.store.getCount() > 0) {
					if (selectedArray.length == 0)
						selectModel.select(0);
					else
						selectModel.select(selectedArray[0]);
				}
			},

			onContainerBlur : function(e) {
				var me = this, focusCls = me.focusCls;
				me.getEl().removeCls(me.removeClsWithUI(focusCls, true));
				me.hasFocus = false;
				me.fireEvent('blur', me, e);
			},
			afterRender : function() {
				var me = this;
				me.callParent();
				me.getEl().set({
							tabindex : 0
						});
				me.mon(me.getTargetEl(), {
							scope : me,
							/*
							 * We need to make copies of this since some of the
							 * events fired here will end up triggering a new
							 * event to be called and the shared event object
							 * will be mutated. In future we should investigate
							 * if there are any issues with creating a new event
							 * object for each event that is fired.
							 */
							freezeEvent : true,
							focus : me.handleEvent,
							blur : me.handleEvent
						});
			}
		});

/*
 * Override treeView to add 'alt' attribute to img in the tree.
 */
Ext.define('Thesaurus.ext.tree.Column', {
			override : 'Ext.tree.Column',
			imgText : '<img src="{1}" class="{0}" alt="" />',
			constructor : function(config) {
				this.callSuper(arguments);
			}
		});

Ext.define('Thesaurus.Ext.selection.RowModel', {
			override : 'Ext.selection.RowModel',
			initKeyNav : function(view) {
				var me = this;
				me.callParent(arguments);
				view.el.set({
							tabIndex : 0
						});
			}
		});

Ext.define('Thesaurus.Ext.view.Table', {
			override : 'Ext.view.Table',
			initAria : function() {
				var me = this;
				me.callParent();
				var actionEl = this.getActionEl();
				actionEl.dom.setAttribute('aria-readonly', true);
				actionEl.dom.setAttribute('tabindex', 0);
			},

			onRowSelect : function(rowIdx) {
				var me = this;
				me.callParent();
				var row = this.getNode(rowIdx);
				if (row) {
					Ext.fly(row).focus();
					Ext.fly(row).set({
								'aria-selected' : true
							});
				}
			},
			onRowDeselect : function(rowIdx) {
				var me = this;
				me.callParent();
				var row = this.getNode(rowIdx);
				if (row) {
					Ext.fly(row).set({
								'aria-selected' : false
							});
				}
			},
			refresh : function() {
				var me = this;
				me.callParent();
				//me.focus();
			}
		});

/*
 * Override treePanel to add aria role
 */
Ext.define('Thesaurus.Ext.tree.View', {
	override : 'Ext.tree.View',
	ariaRole : 'treegrid',
	initComponent : function() {
		var me = this;
		me.callParent();
	},

	collectData : function(records) {
		var data = this.callParent(arguments), rows = data.rows, len = rows.length, i = 0, row, record;
		for (; i < len; i++) {
			row = rows[i];
			record = records[i];
			if (record.isExpanded()) {
				row.rowAttr = 'aria-expanded="true"';
			} else {
				row.rowAttr = 'aria-expanded="false"';
			}
			row.rowAttr += ' aria-level="' + record.getDepth() + '"';
		}
		return data;
	},

	onBeforeExpand : function(parent, records, index) {
		var me = this;
		if (index > 0)
			me.select(index - 1);
		me.callParent();
	}
});

Ext.define('Thesaurus.panel.Panel', {
	override : 'Ext.panel.Panel',
	collapseTooltip : 'Click here to expand panel',
	expandTooltip : 'Click here to collapse panel',
	addTools : function () {
		var me = this;
		 if (me.collapseTool) {
			me.collapseTool.tooltipType= 'title';
			if (me.collapsed) {
				me.collapseTool.tooltip=me.collapseTooltip;
			} else 
			{
				me.collapseTool.tooltip=me.expandTooltip;
			}
			//me.collapseTool.el.dom.setAttribute("aria-expanded", me.collapsed);
		}
	},
	afterExpand: function() {
		var me = this;
		me.callParent(arguments);
		 if (me.collapseTool) {
             me.collapseTool.el.dom.setAttribute("aria-expanded", "true");
             me.collapseTool.tooltip=me.expandTooltip;
        }
	},
	afterCollapse : function() {
		var me = this;
		me.callParent(arguments);
		 if (me.collapseTool) {
             me.collapseTool.el.dom.setAttribute("aria-expanded", "false");
             me.collapseTool.tooltip=me.collapseTooltip;
        }
	},
	afterRender : function() {
		var me = this;
		me.callParent(arguments);
		if (me.collapseTool) {
			me.collapseTool.el.dom.setAttribute("aria-expanded", !me.collapsed);
			me.collapseTool.el.dom.setAttribute("aria-controls", me.getTargetEl().id);
		}
	}
})

/*
 * Override panel tool to add 'alt' attribute to img in the tree.
 */
Ext.define('Thesaurus.panel.Tool', {
	override : 'Ext.panel.Tool',
	renderTpl : ['<img id="{id}-toolEl" src="{blank}" class="{baseCls}-{type}" alt="{type}" role="presentation"/>'],
	constructor : function(config) {
		this.callSuper(arguments);
	},
	afterRender : function() {
		var me = this;
		me.callParent(arguments);
		me.el.dom.setAttribute("tabindex", 0);
		me.keyNav = new Ext.util.KeyNav(me.el, {
					enter : me.onClick,
					space : me.onClick,
					scope : me
				});
		if (me.tooltip) {
			me.el.dom.setAttribute("title", me.tooltip);
		}
	}
});

/*
 * Remove width attr from table on fields form
 */
Ext.define("Thesaurus.form.field.Base", {
	override : 'Ext.form.field.Base',
	getLabelCellAttrs : function() {
		var me = this, labelAlign = me.labelAlign, result = '';

		if (labelAlign !== 'top') {
			result = 'valign="top" halign="' + labelAlign + '"';
		}
		return result + ' class="' + Ext.baseCSSPrefix + 'field-label-cell"';
	},
	getLabelCellStyle : function() {
		var me = this, hideLabelCell = me.hideLabel
				|| (!me.fieldLabel && me.hideEmptyLabel);

		var style = hideLabelCell ? 'display:none;' : '';
		style = style + ' width:' + (me.labelWidth + me.labelPad) + 'px;';
		return style;
	},
	afterLabelTextTpl : new Ext.XTemplate(
			'<tpl if="allowBlank === false"><span class="mandatory-field"><abbr title="obligatoire">*</abbr></span></tpl>',
			{
				disableFormats : true
			})
});

Ext.define('Thesaurus.tab.Bar', {
			override : 'Ext.tab.Bar',
			ariaRole : 'tablist'
		});

Ext.define('Thesaurus.tab.Panel', {
			override : 'Ext.tab.Panel',
			ariaRole : 'tabpanel'
		});

/*
 * Make close button for a tab keyboard accessible
 */
Ext.define('Thesaurus.tab.Tab', {
			override : 'Ext.tab.Tab',
			initAria : function() {
				var me = this;
				me.callParent();
				var el = this.getEl();
				el.dom.setAttribute('role', "presentation");
			},
			onCloseBtnKey : function() {
				this.onCloseClick();
			},
			ariaRole : 'tab',
			listeners : {
				render : {
					fn : function() {
						var me = this;
						var actionEl = me.getActionEl();
						me.btnWrap.dom.setAttribute('role', 'presentation');
						actionEl.dom.setAttribute('aria-selected', false);
						if (me.closeEl) {
							me.keyNav = new Ext.util.KeyNav(me.closeEl, {
										enter : me.onCloseBtnKey,
										scope : me
									});
						}
						if (me.card) {
							actionEl.dom.setAttribute('aria-controls',
									me.card.id);
						}

					}
				},
				activate : {
					fn : function() {
						var me = this;
						var actionEl = me.getActionEl();
						actionEl.dom.setAttribute('aria-selected', true);
					}
				},
				deactivate : {
					fn : function() {
						var me = this;
						var actionEl = me.getActionEl();
						actionEl.dom.setAttribute('aria-selected', false);
					}
				}
			}
		});

/*
 * Remove default role=presentation
 */
Ext.define("Thesaurus.layout.container.Box", {
	override : 'Ext.layout.container.Box',
	renderTpl : [
			'{%var oc,l=values.$comp.layout,oh=l.overflowHandler;',
			'if (oh.getPrefixConfig!==Ext.emptyFn) {',
			'if(oc=oh.getPrefixConfig())dh.generateMarkup(oc, out)',
			'}%}',
			'<div id="{ownerId}-innerCt" class="{[l.innerCls]} {[oh.getOverflowCls()]}">',
			'<div id="{ownerId}-targetEl" style="position:absolute;',
			// This width for the "CSS container box" of the box child items
			// gives
			// them the room they need to avoid being "crushed" (aka,
			// "wrapped").
			// On Opera, elements cannot be wider than 32767px or else they
			// break
			// the scrollWidth (it becomes == offsetWidth) and you cannot scroll
			// the content.
			'width:20000px;',
			// On IE quirks and IE6/7 strict, a text-align:center style trickles
			// down to this el at times and will cause it to move off the left
			// edge.
			// The easy fix is to just always set left:0px here. The top:0px
			// part
			// is just being paranoid. The requirement for targetEl is that its
			// origin align with innerCt... this ensures that it does!
			'left:0px;top:0px;',
			// If we don't give the element a height, it does not always
			// participate
			// in the scrollWidth.
			'height:1px">', '{%this.renderBody(out, values)%}', '</div>',
			'</div>', '{%if (oh.getSuffixConfig!==Ext.emptyFn) {',
			'if(oc=oh.getSuffixConfig())dh.generateMarkup(oc, out)', '}%}', {
				disableFormats : true,
				definitions : 'var dh=Ext.DomHelper;'
			}]
});

/*
 * Add aria role attribute to components. Add check roles to ext components
 */
Ext.define('Thesaurus.Acc.Component', {
			override : 'Ext.Component',
			initAria : function() {
				var actionEl = this.getActionEl(), role = this.ariaRole;
				if (role) {
					actionEl.dom.setAttribute('role', role);
				}
			},
			afterRender : function() {
				var me = this;
				me.callParent();
				me.initAria();
			},
			setDisabled : function(disabled) {
				var me = this;
				if (me.el) {
					me.el.set({
								'aria-disabled' : disabled
							});
				}

				return this[disabled ? 'disable' : 'enable']();
			},
			focus : function(selectText, delay) {
				var me = this, focusEl, focusElDom, containerScrollTop;

				if (delay) {
					if (!me.focusTask) {
						me.focusTask = new Ext.util.DelayedTask(me.focus);
					}
					me.focusTask.delay(Ext.isNumber(delay) ? delay : 10, null,
							me, [selectText, false]);
					return me;
				}

				if (me.rendered && !me.isDestroyed && me.isVisible(true)
						&& (focusEl = me.getFocusEl())) {

					if (focusEl.isComponent) {
						return focusEl.focus(selectText, delay);
					}

					if ((focusElDom = focusEl.dom)) {

						if (focusEl.needsTabIndex()) {
							// SMILE CHANGE
							focusElDom.tabIndex = 0;
						}

						if (me.floating) {
							containerScrollTop = me.container.dom.scrollTop;
						}

						focusEl.focus();
						if (selectText === true) {
							focusElDom.select();
						}
					}

					if (me.floating) {
						me.toFront(true);
						if (containerScrollTop !== undefined) {
							me.container.dom.scrollTop = containerScrollTop;
						}
					}
				}
				return me;
			}
		});

/*
 * Implement aria-hidden property
 */
Ext.define("Thesaurus.dom.Element", {
			override : 'Ext.dom.Element',
			setVisible : function(visible, animate) {
				var me = this;
				me.callParent(arguments);
				me.set({
							'aria-hidden' : !visible
						});
				return me;
			}
		});

Ext.view.TableChunker.metaRowTpl = [
		'<tr class="' + Ext.baseCSSPrefix
				+ 'grid-row {[this.embedRowCls()]}" {[this.embedRowAttr()]}>',
		'<tpl for="columns">',
		'<td role="gridcell" class="{cls} '
				+ Ext.baseCSSPrefix
				+ 'grid-cell '
				+ Ext.baseCSSPrefix
				+ 'grid-cell-{columnId} {{id}-modified} {{id}-tdCls} {[this.firstOrLastCls(xindex, xcount)]}" {{id}-tdAttr} aria-labelledby="{columnId}-textEl">',
		'<div role="presentation" {unselectableAttr} class="'
				+ Ext.baseCSSPrefix
				+ 'grid-cell-inner {unselectableCls}" style="text-align: {align}; {{id}-style};">{{id}}</div>',
		'</td>', '</tpl>', '</tr>']

/*
 * Remove border attr from tables
 */
Ext.view.TableChunker.metaTableTpl = [
		'{%if (this.openTableWrap)out.push(this.openTableWrap())%}',
		'<table class="'
				+ Ext.baseCSSPrefix
				+ 'grid-table '
				+ Ext.baseCSSPrefix
				+ 'grid-table-resizer" cellspacing="0" cellpadding="0" {[this.embedFullWidth(values)]}>',
		'<tbody>',
		'<tr class="' + Ext.baseCSSPrefix
				+ 'grid-header-row" role="presentation">',
		'<tpl for="columns">',
		'<th class="'
				+ Ext.baseCSSPrefix
				+ 'grid-col-resizer-{id}" style="width: {width}px; height: 0px;"></th>',
		'</tpl>', '</tr>', '{[this.openRows()]}', '{row}',
		'<tpl for="features">',
		'{[this.embedFeature(values, parent, xindex, xcount)]}', '</tpl>',
		'{[this.closeRows()]}', '</tbody>', '</table>',
		'{%if (this.closeTableWrap)out.push(this.closeTableWrap())%}'];

/*
 * Add tabindex to role to allow focus() from javascript
 */
Ext.view.TableChunker.embedRowAttr = function() {
	return '{rowAttr} tabindex="-1" role="row"';
};

Ext.define('Thesaurus.Ext.tree.Column', {
			override : 'Ext.tree.Column',
			treeRenderer : function(value, metaData, record, rowIdx, colIdx,
					store, view) {
				var me = this;
				metaData.tdAttr = 'role="gridcell"';
				if (record.isExpanded()) {
					metaData.tdAttr = metaData.tdAttr + ' aria-expanded="true"';
				} else {
					metaData.tdAttr = metaData.tdAttr
							+ ' aria-expanded="false"';
				}
				return me.callParent(arguments);
			}
		});

Ext.define('Thesaurus.Ext.form.field.Trigger', {
	override : 'Ext.form.field.Trigger',
	triggerTitle : '',
	getTriggerMarkup : function() {
		var me = this, i = 0, hideTrigger = (me.readOnly || me.hideTrigger), triggerCls, triggerBaseCls = me.triggerBaseCls, triggerConfigs = [];

		// TODO this trigger<n>Cls API design doesn't feel clean, especially
		// where it butts up against the
		// single triggerCls config. Should rethink this, perhaps something more
		// structured like a list of
		// trigger config objects that hold cls, handler, etc.
		// triggerCls is a synonym for trigger1Cls, so copy it.
		if (!me.trigger1Cls) {
			me.trigger1Cls = me.triggerCls;
		}

		// Create as many trigger elements as we have trigger<n>Cls configs, but
		// always at least one
		for (i = 0; (triggerCls = me['trigger' + (i + 1) + 'Cls']) || i < 1; i++) {
			triggerConfigs.push({
						tag : 'td',
						valign : 'top',
						cls : Ext.baseCSSPrefix + 'trigger-cell',
						style : 'width:' + me.triggerWidth
								+ (hideTrigger ? 'px;display:none' : 'px'),
						cn : {
							cls : [Ext.baseCSSPrefix + 'trigger-index-' + i,
									triggerBaseCls, triggerCls].join(' '),
							role : 'button',
							tabindex : 0,
							title : me.triggerTitle
							,
						}
					});
		}
		triggerConfigs[i - 1].cn.cls += ' ' + triggerBaseCls + '-last';

		return Ext.DomHelper.markup(triggerConfigs);
	},
	listeners : {
		render : {
			fn : function() {
				var me = this;
				if (me.triggerEl) {
					me.keyNav = new Ext.util.KeyNav(me.triggerEl, {
								enter : me.onTriggerClick,
								space : me.onTriggerClick,
								scope : me
							});
				}

			}
		}
	}
});

Ext.define('Thesaurus.Ext.form.field.Display', {
			override : 'Ext.form.field.Display',
			ariaRole : 'label'
		});

/*
 * Make MessageBox accessible
 */
Ext.define('Thesaurus.Ext.window.MessageBox', {
			override : 'Ext.window.MessageBox',
			initHeaderAria : function() {
				var me = this, el = me.el, header = me.header;

				me.callParent();
				if (me.msg != null) {
					if (el && header) {
						el.dom.setAttribute('aria-describedby',
								me.msg.inputEl.id);
					}
				}

			}
			,
		});

/*
 * Handling focus in modal windows
 */
Ext.define('Thesaurus.focus.manager', {
	singleton : true,
	init : function() {
		var me = this;
		Ext.EventManager.on(Ext.getBody(), 'keydown', me.focusListener, Ext
						.getBody());
	},
	focusListener : function(e) {
		if (typeof Ext.WindowManager.getActive() !== 'undefined'
				&& Ext.WindowManager.getActive() !== null) {
			var activeWinId = Ext.WindowManager.getActive().getId();
			var obj = Ext.getCmp(activeWinId);
			var id = typeof obj.focusEl !== 'undefined'
					? obj.focusEl.id
					: obj.id;
			var dom = activeWinId;
			var components = [];
			Ext.Array.each(Ext.get(dom).query('*'), function(dom) {
						var cmp = Ext.getCmp(dom.id);
						if (cmp && cmp.isVisible()) {
							if (cmp && cmp.btnEl && cmp.btnEl.focusable())
								components.push(cmp.btnEl);
							else if (cmp && cmp.toolEl)
								components.push(cmp);
							else if (cmp.xtype == "gridview" && cmp.el && cmp.el.focusable())
								components.push(cmp);
							else if (cmp && cmp.inputEl
									&& cmp.inputEl.focusable())
								components.push(cmp.inputEl);
						}
					});

			if (typeof obj != 'undefined'
					&& obj.isVisible()
					&& obj.el.id === activeWinId
					&& (typeof e.keyCode !== 'undefined'
							? e.keyCode === 9
							: true)) {
				var focused = document.activeElement;
				console.log(focused,components);
				var goBack = e.shiftKey;
				if (goBack === false && components.length > 0
						&& focused.id === components[components.length - 1].id) {
					e.preventDefault();
					Ext.getCmp(id).focus();
				}else if (goBack === false && components.length > 0
						&& focused.boundView && focused.boundView === components[components.length - 1].id)
				{
					e.preventDefault();
					Ext.getCmp(id).focus();
				}
				else if (goBack === true
						&& components.length > 0
						&& (focused.id === id || focused.id === components[0].id)) {
					e.preventDefault();
					components[components.length - 1].focus();
				} else if (components.length == 0) {
					e.preventDefault();
					Ext.getCmp(id).focus();
				}
			}
			return false;
		}
	}
});

Ext.define('Thesaurus.Ext.button.Button', {
			override : 'Ext.button.Button',
			tooltipType : 'title'
		});

Ext.define('Accessible.Ext.grid.column.Action', {
	override : 'Ext.grid.column.Action',
	defaultRenderer : function(v, meta, record, rowIdx, colIdx, store, view) {
		var me = this, prefix = Ext.baseCSSPrefix, scope = me.origScope || me, items = me.items, len = items.length, i = 0, item, disabled, tooltip;

		// Allow a configured renderer to create initial value (And set the
		// other values in the "metadata" argument!)
		v = Ext.isFunction(me.origRenderer) ? me.origRenderer.apply(scope,
				arguments)
				|| '' : '';

		meta.tdCls += ' ' + Ext.baseCSSPrefix + 'action-col-cell';
		for (; i < len; i++) {
			item = items[i];

			disabled = item.disabled
					|| (item.isDisabled ? item.isDisabled.call(item.scope
									|| scope, view, rowIdx, colIdx, item,
							record) : false);
			tooltip = disabled ? null : (item.tooltip || (item.getTip
					? item.getTip.apply(item.scope || scope, arguments)
					: null));

			// Only process the item action setup once.
			if (!item.hasActionConfiguration) {

				// Apply our documented default to all items
				item.stopSelection = me.stopSelection;
				item.disable = Ext.Function.bind(me.disableAction, me, [i], 0);
				item.enable = Ext.Function.bind(me.enableAction, me, [i], 0);
				item.hasActionConfiguration = true;
			}

			v += '<img alt="'
					+ (item.altText || me.altText)
					+ '" src="'
					+ (item.icon || Ext.BLANK_IMAGE_URL)
					+ '" class="'
					+ prefix
					+ 'action-col-icon '
					+ prefix
					+ 'action-col-'
					+ String(i)
					+ ' '
					+ (disabled ? prefix + 'item-disabled' : ' ')
					+ ' '
					+ (Ext.isFunction(item.getClass) ? item.getClass.apply(
							item.scope || scope, arguments) : (item.iconCls
							|| me.iconCls || '')) + '"'
					+ (tooltip ? ' title="' + tooltip + '"' : '')
					+ ' tabindex="0" role="button"/>';
		}
		return v;
	},
	processEvent : function(type, view, cell, recordIndex, cellIndex, e, record, row){
		 var me = this,
             key = type == 'keydown' && e.getKey();

		if ((key == e.ENTER || key == e.SPACE)) {
			me.fireEvent("click", view, cell, recordIndex, cellIndex, e, record, row)
		}
		me.callParent(arguments);
	}
});
