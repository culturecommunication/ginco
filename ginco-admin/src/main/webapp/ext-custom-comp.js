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

Thesaurus.ext.utils = function() {
	var msgCt;
	function createBox(t, s) {
		return '<div class="msg"><h3>' + t + '</h3><p>' + s + '</p></div>';
	}
	return {
		msg : function(title, format) {
			if (!msgCt) {
				msgCt = Ext.core.DomHelper.insertFirst(document.body, {
					id : 'msg-div'
				}, true);
			}
			var s = Ext.String.format.apply(String, Array.prototype.slice.call(
					arguments, 1));
			var m = Ext.core.DomHelper.append(msgCt, createBox(title, s), true);
			m.hide();
			m.slideIn('t').ghost("t", {
				delay : 5000,
				remove : true
			});
		}
	};
}();

// Permit validation (AllowBlank) on htmlEditor
Ext.define('Thesaurus.form.HtmlEditor', {
	override : 'Ext.form.field.HtmlEditor',
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
	}
});

Thesaurus.ext.tabs = function() {
	return {
		openConceptTab : function(aModel, aThesaurusId, aConceptId) {

			var topTabs = Ext.ComponentQuery.query('topTabs')[0];
			var conceptTabs = Ext.ComponentQuery.query('topTabs conceptPanel');
			var tabExists = false;
			Ext.Array
					.each(
							conceptTabs,
							function(element, index, array) {
								if (element.conceptId != null
										&& aConceptId
												.indexOf(
														element.conceptId,
														aConceptId.length
																- element.conceptId.length) !== -1) {
									tabExists = element;
								}
							});
			if (!tabExists) {

				aModel.load(aThesaurusId, {
					success : function(aModel) {

						var conceptPanel = Ext.create(
								'GincoApp.view.ConceptPanel', {
									thesaurusData : aModel,
									conceptId : aConceptId
								});

						var tab = topTabs.add(conceptPanel);
						topTabs.setActiveTab(tab);
						tab.show();
					}
				});

			} else {
				topTabs.setActiveTab(tabExists);
			}
		},
		openTermTab : function(aTermId, aThesaurusData) {
			var topTabs = Ext.ComponentQuery.query('topTabs')[0];
			var termTabs = Ext.ComponentQuery.query('topTabs termPanel');
			var tabExists = false;
			Ext.Array.each(termTabs, function(element, index, array) {
				if (element.termId != null && element.termId == aTermId) {
					tabExists = element;
				}
			});

			if (!tabExists) {
				var TermPanel = Ext.create('GincoApp.view.TermPanel');
				TermPanel.thesaurusData = aThesaurusData;
				TermPanel.termId = aTermId;
				var tab = topTabs.add(TermPanel);
				topTabs.setActiveTab(tab);
				tab.show();
			} else {
				topTabs.setActiveTab(tabExists);
			}
		},
		openArrayTab : function(aModel, aThesaurusId, aArrayId) {
			var topTabs = Ext.ComponentQuery.query('topTabs')[0];
			var arrayTabsTabs = Ext.ComponentQuery
					.query('topTabs conceptArrayPanel');
			var tabExists = false;
			Ext.Array.each(arrayTabsTabs, function(element, index, array) {
				if (element.conceptArrayId != null
						&& element.conceptArrayId == aArrayId) {
					tabExists = element;
				}
			});

			if (!tabExists) {
				aModel.load(aThesaurusId, {
					success : function(aModel) {
						var arrayPanel = Ext.create(
								'GincoApp.view.ConceptArrayPanel', {
									thesaurusData : aModel.data,
									conceptArrayId : aArrayId
								});

						var tab = topTabs.add(arrayPanel);
						topTabs.setActiveTab(tab);
						tab.show();
					}
				});
			} else {
				topTabs.setActiveTab(tabExists);
			}
		},
		openGroupTab : function(aModel, aThesaurusId, aGroupId) {
			var topTabs = Ext.ComponentQuery.query('topTabs')[0];
			var groupTabsTabs = Ext.ComponentQuery
					.query('topTabs conceptGroupPanel');
			var tabExists = false;
			Ext.Array.each(groupTabsTabs, function(element, index, array) {
				if (element.conceptGroupId != null
						&& element.conceptGroupId == aGroupId) {
					tabExists = element;
				}
			});

			if (!tabExists) {
				aModel.load(aThesaurusId, {
					success : function(aModel) {
						var groupPanel = Ext.create(
								'GincoApp.view.ConceptGroupPanel', {
									thesaurusData : aModel.data,
									conceptGroupId : aGroupId
								});

						var tab = topTabs.add(groupPanel);
						topTabs.setActiveTab(tab);
						tab.show();
					}
				});
			} else {
				topTabs.setActiveTab(tabExists);
			}
		}
	};
}();