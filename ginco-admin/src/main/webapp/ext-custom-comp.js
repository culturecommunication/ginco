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

Ext
		.define(
				'Thesaurus.ext.KeyMenuItem',
				{
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
							'<img id="{id}-iconEl" src="{icon}" class="'
									+ Ext.baseCSSPrefix
									+ 'menu-item-icon {iconCls}" />',
							'<span id="{id}-textEl" class="'
									+ Ext.baseCSSPrefix
									+ 'menu-item-text keymenuitem-text">{text}</span>',
							'<span class="'
									+ Ext.baseCSSPrefix
									+ 'menu-item-text keymenuitem-cmdTxt" <tpl if="menu">style="margin-right: 17px;"</tpl> >{cmdTxt}</span>',
							'<tpl if="menu">',
							'<img id="{id}-arrowEl" src="{blank}" class="'
									+ Ext.baseCSSPrefix + 'menu-item-arrow" />',
							'</tpl>', '</a>', '</tpl>' ],
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

Ext.view.View.addInheritableStatics({
	EventMap: {
        mousedown: 'MouseDown',
        mouseup: 'MouseUp',
        click: 'Click',
        dblclick: 'DblClick',
        contextmenu: 'ContextMenu',
        mouseover: 'MouseOver',
        mouseout: 'MouseOut',
        mouseenter: 'MouseEnter',
        mouseleave: 'MouseLeave',
        keydown: 'KeyDown',
        focus: 'Focus',
        blur: 'Blur'
    }
}
);

//Manage focus on table...
Ext.define('Thesaurus.ext.view.View', {
	override : 'Ext.view.View',
	focusCls: 'focus',
	onBeforeContainerFocus: Ext.emptyFn,
	onBeforeContainerBlur: Ext.emptyFn,
	onContainerFocus: function (e) {
		var me = this,
			focusCls = me.focusCls;
		me.getEl().addCls(me.addClsWithUI(focusCls, true));
		if (!me.hasFocus) {
            me.hasFocus = true;
            me.fireEvent('focus', me, e);
        }
	},
	onContainerBlur: function (e) {
		var me = this,
			focusCls = me.focusCls;
		me.getEl().removeCls(me.removeClsWithUI(focusCls, true));
		me.hasFocus = false;
        me.fireEvent('blur', me, e);
	},
	afterRender: function(){
        var me = this;
        me.callParent();
        me.mon(me.getTargetEl(), {
            scope: me,
            /*
             * We need to make copies of this since some of the events fired here will end up triggering
             * a new event to be called and the shared event object will be mutated. In future we should
             * investigate if there are any issues with creating a new event object for each event that
             * is fired.
             */
            freezeEvent: true,
            focus: me.handleEvent,
            blur: me.handleEvent
        });
    }
});


// Override treeView to be stateful
Ext.define('Thesaurus.ext.tree.Panel', {
	override : 'Ext.tree.Panel',

	getState : function() {
		var nodes = [], state = this.callParent();

		var getPath = function(node, field, separator) {

			field = node.idProperty;
			separator = separator || '|';

			var path = [ node.get(field) ], parent = node.parentNode;
			while (parent) {
				path.unshift(parent.get(field));
				parent = parent.parentNode;
			}
			return separator + path.join(separator);
		};
		this.getRootNode().eachChild(function(child) {

			// function to store state of tree recursively

			var storeTreeState = function(node, expandedNodes) {

				if (node.isExpanded() && node.childNodes.length > 0) {

					expandedNodes.push(getPath(node, 'text'));
					node.eachChild(function(child) {
						storeTreeState(child, expandedNodes);
					});
				}
			};

			storeTreeState(child, nodes);

		});

		Ext.apply(state, {
			expandedNodes : nodes
		});

		return state;

	},

	applyState : function(state) {
		var nodes = state.expandedNodes || [], len = nodes.length;
		this.collapseAll();
		for ( var i = 0; i < len; i++) {
			if (typeof nodes[i] != 'undefined') {
				this.expandPath(nodes[i], 'id', '|');
			}
		}
		this.callParent(arguments);
	},

	constructor : function(config) {
		this.callSuper(arguments);
	}
});

// Override treeView to add 'alt' attribute to img in the tree.
Ext.define('Thesaurus.ext.tree.Column', {
	override : 'Ext.tree.Column',
	imgText : '<img src="{1}" class="{0}" alt="treenode" />',
	constructor : function(config) {
		this.callSuper(arguments);
	}
});

Ext
		.define(
				'Thesaurus.panel.Tool',
				{
					override : 'Ext.panel.Tool',
					renderTpl : [ '<img id="{id}-toolEl" src="{blank}" class="{baseCls}-{type}" alt="{type}" role="presentation"/>' ],
					constructor : function(config) {
						this.callSuper(arguments);
					}
				});

// This function permits to send related objects when we save a model in case of
// 'hasmany' relation
Ext.data.writer.Json.override({
	getRecordData : function(record) {
		var me = this, i, association, childStore, data = {};
		data = me.callParent([ record ]);

		/* Iterate over all the hasMany associations */
		for (i = 0; i < record.associations.length; i++) {
			association = record.associations.get(i);
			if (association.type == 'hasMany') {
				data[association.name] = [];
				childStore = eval('record.' + association.name + '()');

				// Iterate over all the children in the current association
				childStore.each(function(childRecord) {

					// Recursively get the record data for children (depth
					// first)
					var childData = this.getRecordData.call(this, childRecord);
					if (childRecord.dirty | childRecord.phantom
							| (childData != null)) {
						data[association.name].push(childData);
						record.setDirty();
					}
				}, me);
			}
		}
		return data;
	}
});

Ext.define('Thesaurus.ext.utils', {
	singleton : true,
	msgCt : null,
	renderTpl: ['<div class="msg" role="alert">',
	            	'<h3>{title}</h3>',
	            	'<p>{text}</p>',
	            '</div>'],
	msg : function (title, format) {
		if (!this.msgCt) {
			this.msgCt = Ext.core.DomHelper.insertFirst(document.body, {
				id : 'msg-div'
			}, true);
		}
		var s = Ext.String.format.apply(String, Array.prototype.slice.call(
				arguments, 1));
		var msgTpl = new Ext.XTemplate(this.renderTpl);
		var m = Ext.core.DomHelper.append(this.msgCt, msgTpl.apply({
			title : title,
			text : s
		}), true);
		m.hide();
		m.slideIn('t').ghost("t", {
			delay : 5000,
			remove : true
		});
	}
});


/*
 *  Permit validation (AllowBlank) on htmlEditor
 */
Ext.define('Thesaurus.form.HtmlEditor', {
	override : 'Ext.form.field.HtmlEditor',
	getLabelCellAttrs: function() {
        var me = this,
            labelAlign = me.labelAlign,
            result = '';

        if (labelAlign !== 'top') {
            result = 'valign="top" halign="' + labelAlign+ '"';
        }
        return result + ' class="' + Ext.baseCSSPrefix + 'field-label-cell"';
    },
    getLabelCellStyle: function() {
        var me = this,
            hideLabelCell = me.hideLabel || (!me.fieldLabel && me.hideEmptyLabel);

        var style =  hideLabelCell ? 'display:none;' : '';
        style = style+ ' width:' + (me.labelWidth + me.labelPad) + 'px;';
        return style;
    },
	validate : function() {
		var me = this, isValid = me.isValid();
		if (isValid !== me.wasValid) {
			me.wasValid = isValid;
			me.fireEvent('validitychange', me, isValid);
		}
		return isValid;
	},
	isEmpty : function() {
		var value = this.getValue();
		value = value.replace(/&nbsp;/gi, "");
		value = value.replace(/<p>/gi, "");
		value = value.replace(/<p align=left>/gi, "");
		value = value.replace(/<p align=right>/gi, "");
		value = value.replace(/<p align=center>/gi, "");
		value = value.replace(/<.p>/gi, "");
		value = value.replace(/<br>/gi, "");
		value = Ext.String.trim(value);
		if (value != '') {
			return false;
		}
		return true;
	},
	isValid : function() {
		if (this.allowBlank == false) {
			if (this.isEmpty() == false) {
				return true;
			} else {
				return false;
			}
		} else {
			return true;
		}
	},
	 getSubTplData: function() {
		 var data;
		 data = this.callParent();
		 this.getInsertionRenderData(data, this.subTplInsertions);
		 return data;
	    }
});

Ext.define("Thesaurus.form.field.Trigger", {
	override : 'Ext.form.field.Trigger',
	onTriggerClick: function() {
		this.fireEvent("trigger",this);
	}
});

/*
 * Remove width attr from table on fields form
 */
Ext.define("Thesaurus.form.field.Base", {
	override : 'Ext.form.field.Base',
	getLabelCellAttrs: function() {
        var me = this,
            labelAlign = me.labelAlign,
            result = '';

        if (labelAlign !== 'top') {
            result = 'valign="top" halign="' + labelAlign+ '"';
        }
        return result + ' class="' + Ext.baseCSSPrefix + 'field-label-cell"';
    },
    getLabelCellStyle: function() {
        var me = this,
            hideLabelCell = me.hideLabel || (!me.fieldLabel && me.hideEmptyLabel);

        var style =  hideLabelCell ? 'display:none;' : '';
        style = style+ ' width:' + (me.labelWidth + me.labelPad) + 'px;';
        return style;
    },
    afterLabelTextTpl : new Ext.XTemplate(
			'<tpl if="allowBlank === false"><span class="mandatory-field"><abbr title="obligatoire">*</abbr></span></tpl>',
			{
				disableFormats : true
			})
});

/*
 * Remove default role=presentation
 */
Ext.define("Thesaurus.layout.container.Box", {
	override : 'Ext.layout.container.Box',
	renderTpl: [
	            '{%var oc,l=values.$comp.layout,oh=l.overflowHandler;',
	            'if (oh.getPrefixConfig!==Ext.emptyFn) {',
	                'if(oc=oh.getPrefixConfig())dh.generateMarkup(oc, out)',
	            '}%}',
	            '<div id="{ownerId}-innerCt" class="{[l.innerCls]} {[oh.getOverflowCls()]}">',
	                '<div id="{ownerId}-targetEl" style="position:absolute;',
	                        // This width for the "CSS container box" of the box child items gives
	                        // them the room they need to avoid being "crushed" (aka, "wrapped").
	                        // On Opera, elements cannot be wider than 32767px or else they break
	                        // the scrollWidth (it becomes == offsetWidth) and you cannot scroll
	                        // the content.
	                        'width:20000px;',
	                        // On IE quirks and IE6/7 strict, a text-align:center style trickles
	                        // down to this el at times and will cause it to move off the left edge.
	                        // The easy fix is to just always set left:0px here. The top:0px part
	                        // is just being paranoid. The requirement for targetEl is that its
	                        // origin align with innerCt... this ensures that it does!
	                        'left:0px;top:0px;',
	                        // If we don't give the element a height, it does not always participate
	                        // in the scrollWidth.
	                        'height:1px">',
	                    '{%this.renderBody(out, values)%}',
	                '</div>',
	            '</div>',
	            '{%if (oh.getSuffixConfig!==Ext.emptyFn) {',
	                'if(oc=oh.getSuffixConfig())dh.generateMarkup(oc, out)',
	            '}%}',
	            {
	                disableFormats: true,
	                definitions: 'var dh=Ext.DomHelper;'
	            }
	        ]
});

/*
 * Add aria role attribute to components.
 */
Ext.define('Thesaurus.Component', {
	override : 'Ext.Component',
	initAria: function() {
        var actionEl = this.getActionEl(),
            role = this.ariaRole;
        if (role) {
            actionEl.dom.setAttribute('role', role);
        }
    },
	afterRender: function() {
		var me = this;
		me.callParent();
		me.initAria();
	},
	setDisabled : function(value) {
    	var me = this;
    	if(value) {
    		var userInfoStore = Ext.StoreMgr.lookup("UserInfoStore");    	
    		if (userInfoStore.data.items[0].data.isAdmin) {
    			me.callParent();
    		} 
    	}
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

/*
 * Remove border attr from tables
 */
Ext.view.TableChunker.metaTableTpl = [
                                                  '{%if (this.openTableWrap)out.push(this.openTableWrap())%}',
                                                  '<table class="' + Ext.baseCSSPrefix + 'grid-table ' + Ext.baseCSSPrefix + 'grid-table-resizer" cellspacing="0" cellpadding="0" {[this.embedFullWidth(values)]}>',
                                                      '<tbody>',
                                                      '<tr class="' + Ext.baseCSSPrefix + 'grid-header-row">',
                                                      '<tpl for="columns">',
                                                          '<th class="' + Ext.baseCSSPrefix + 'grid-col-resizer-{id}" style="width: {width}px; height: 0px;"></th>',
                                                      '</tpl>',
                                                      '</tr>',
                                                      '{[this.openRows()]}',
                                                          '{row}',
                                                          '<tpl for="features">',
                                                              '{[this.embedFeature(values, parent, xindex, xcount)]}',
                                                          '</tpl>',
                                                      '{[this.closeRows()]}',
                                                      '</tbody>',
                                                  '</table>',
                                                  '{%if (this.closeTableWrap)out.push(this.closeTableWrap())%}'
                                              ];