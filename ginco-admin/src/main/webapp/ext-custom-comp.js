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


/*
 * Override treeView to be stateful
 * Add filter capabilies too.
 */

Ext.define('Thesaurus.ext.tree.Panel', {
	override : 'Ext.tree.Panel',
	currentFilter : null,
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

	applyState : function(state, callback) {
		var nodes = state.expandedNodes || [], len = nodes.length;
		this.collapseAll();
		var hasBeenExpanded = 0;
		for ( var i = 0; i < len; i++) {
			if (typeof nodes[i] != 'undefined') {
				this.expandPath(nodes[i], 'id', '|', function() {
					hasBeenExpanded++;
					if (hasBeenExpanded==len && callback)
						callback();
				});
			}
		}
		this.callParent(arguments);
	}
});


Ext.define('Thesaurus.ext.utils', {
	singleton : true,
	userInfo : null,
	msgCt : null,
	renderTpl: ['<div class="msg" role="alert">',
	            	'<h1>{title}</h1>',
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
	},
	restrictRoles : function (component, thesaurusId) {
		if  (Thesaurus.ext.utils.userInfo!=null) {
			var userRoles = Thesaurus.ext.utils.getUserRoles(thesaurusId);
			component.restrictUI(userRoles);
		}
	},
	getUserRoles : function (thesaurusId) {
		var userRoles = [];
		if (thesaurusId) {
			var userThesaurusRole = Thesaurus.ext.utils.userInfo.userThesaurusRolesStore.getById(thesaurusId);
			if (userThesaurusRole != null && Thesaurus.ext.utils.userInfo.data.admin == false) {

				if (userThesaurusRole.data.role==0) {
					userRoles.push('MANAGER');
				} else if ((userThesaurusRole.data.role==1)){
					userRoles.push('EXPERT');
				}
			}
		}
		if  (Thesaurus.ext.utils.userInfo.data.admin == true) {
			userRoles.push('ADMIN');
		}
		return userRoles;
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



Ext.define('Thesaurus.container.Container', {
	override : 'Ext.container.Container',
	restrictUI : function (userRoles)
	{
		if (!Ext.Array.contains(userRoles, 'ADMIN')) {
			var items = this.query("component");
			for (var i = 0; i < items.length; i++) {
				var item = items[i];
					if (item.checkRoles(userRoles) == true) {
						item.restrict();
					}
			}
		}
	}
});


Ext.define('Thesaurus.form.Panel', {
	override: 'Ext.form.Panel',
	  restrict : function() {
		  this.setReadOnlyForAll(true);
	  },
	  setReadOnlyForAll: function(readOnly) {
	    Ext.suspendLayouts();
	    this.getForm().getFields().each(function(field) {
	      field.setReadOnly(readOnly);
	    });
	    Ext.resumeLayouts();
	  }
	});


Ext.define('Thesaurus.Component', {
	override : 'Ext.Component',
	requiredRoles : [],
	checkRoles : function (userRoles) {
		if (this.requiredRoles.length == 0)
			return false;

		for (var i = 0; i < userRoles.length; i++) {
		    if (Ext.Array.contains(this.requiredRoles, userRoles[i]))
		    {
		    	return false;
		    }
		}
		return true;
	},
	restrict : function() {
		this.setDisabled(true);
		// Set No-OP for enable function...
		this.enable = function () {
		};
	}
});



// Backport from ExtJS 4.1.2, fix problem with resetOriginalValues of checkboxes
Ext.apply(Ext.form.CheckboxManager,{
	getByName: function(name, formId) {
        return this.filterBy(function(item) {
            return item.name == name && item.getFormId() == formId;
        });
    }
});




Ext.define('Secure.data.writer.Writer', {
	override : 'Ext.data.writer.Writer',

    /**
     * @cfg {Boolean} writeAllFields
     * True to write all fields from the record to the server. If set to false it will only send the fields that were
     * modified. Note that any fields that have {@link Ext.data.Field#persist} set to false will still be ignored.
     */
    writeAllFields: true,

    /**
     * @cfg {String} dateFormat
     * This is used for each field of type date in the model to format the value before
     * it is sent to the server.
     */

    /**
     * @cfg {String} nameProperty
     * This property is used to read the key for each value that will be sent to the server. For example:
     *
     *     Ext.define('Person', {
     *         extend: 'Ext.data.Model',
     *         fields: [{
     *             name: 'first',
     *             mapping: 'firstName'
     *         }, {
     *             name: 'last',
     *             mapping: 'lastName'
     *         }, {
     *             name: 'age'
     *         }]
     *     });
     *     new Ext.data.writer.Writer({
     *         writeAllFields: true,
     *         nameProperty: 'mapping'
     *     });
     *
     *     // This will be sent to the server
     *     {
     *         firstName: 'first name value',
     *         lastName: 'last name value',
     *         age: 1
     *     }
     *
     * If the value is not present, the field name will always be used.
     */
    nameProperty: 'name',

    /*
     * @property {Boolean} isWriter
     * `true` in this class to identify an object as an instantiated Writer, or subclass thereof.
     */
    isWriter: true,

    /**
     * Creates new Writer.
     * @param {Object} [config] Config object.
     */
    constructor: function(config) {
        Ext.apply(this, config);
    },

    /**
     * Prepares a Proxy's Ext.data.Request object
     * @param {Ext.data.Request} request The request object
     * @return {Ext.data.Request} The modified request object
     */
    write: function(request) {
        var operation = request.operation,
            records   = operation.records || [],
            len       = records.length,
            i         = 0,
            data      = [];

        for (; i < len; i++) {
            data.push(this.getRecordData(records[i], operation));
        }
        return this.writeRecords(request, data);
    },

    /**
     * Formats the data for each record before sending it to the server. This
     * method should be overridden to format the data in a way that differs from the default.
     * @param {Ext.data.Model} record The record that we are writing to the server.
     * @param {Ext.data.Operation} [operation] An operation object.
     * @return {Object} An object literal of name/value keys to be written to the server.
     * By default this method returns the data property on the record.
     */
    getRecordData: function(record, operation) {
        var isPhantom = record.phantom === true,
            writeAll = this.writeAllFields || isPhantom,
            fields = record.fields,
            fieldItems = fields.items,
            data = {},
            clientIdProperty = record.clientIdProperty,
            changes,
            field,
            key,
            value,
            f, fLen;

        if (writeAll) {
            fLen = fieldItems.length;

            for (f = 0; f < fLen; f++) {
                field = fieldItems[f];
                if (field.persist) {
                    this.writeValue(data, field, record);
                }
            }
        } else {
            // Only write the changes
            changes = record.getChanges();
            for (key in changes) {
                if (changes.hasOwnProperty(key)) {
                    field = fields.get(key);
                    if (field.persist) {
                        this.writeValue(data, field, record);
                    }
                }
            }
        }
        if (isPhantom) {
            if (clientIdProperty && operation && operation.records.length > 1) {
                // include clientId for phantom records, if multiple records are being written to the server in one operation.
                // The server can then return the clientId with each record so the operation can match the server records with the client records
                data[clientIdProperty] = record.internalId;
            }
        } else {
            // always include the id for non phantoms
            data[record.idProperty] = record.getId();
        }
        var me = this, i, association, childStore;

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
    },

    writeValue: function(data, field, record){
        var name = field[this.nameProperty] || field.name,
            dateFormat = this.dateFormat || field.dateWriteFormat || field.dateFormat,
            value = record.get(field.name);
        if (field.serialize){
            data[name] = field.serialize(value, record);
        } else if (field.type === Ext.data.Types.DATE && dateFormat && Ext.isDate(value)) {
            data[name] = Ext.Date.format(value, dateFormat);
        } else {
        	//if (Ext.isString(value))
        	//	data[name] = Ext.String.htmlEncode(value);
        	//else
        		data[name] = value;
        }
    }
});

Ext.data.Types.HTMLSTRING = {
        convert: function(v) {
           //return  Ext.String.htmlDecode(v);
        	var defaultValue = this.useNull ? null : '';
            return (v === undefined || v === null) ? defaultValue : String(v);
        },
        sortType: Ext.data.SortTypes.asUCString,
        type: 'htmlstring'
	};

Ext.define('Thesaurus.form.field.Text', {
	override : 'Ext.form.field.Text',
    valueToRaw: function(value) {
        return '' + Ext.value(Ext.String.htmlDecode(value), '');
    },
    rawToValue: function(rawValue) {
        return Ext.String.htmlEncode(rawValue);
    }
});

Ext.define('Thesaurus.form.field.ComboBox', {
	override : 'Ext.form.field.ComboBox',
	getDisplayValue: function() {
        var displayValue = this.displayTpl.apply(this.displayTplData);
        return Ext.String.htmlDecode(displayValue);
    }
});

/*
 *  Fix IE encoding problem
 */

if (Ext.isIE) {

	Ext.define('Thesaurus.Ext.dom.Element', {
		override : 'Ext.dom.Element',
		update : function(html, loadScripts, callback) {
			var me = this;
			arguments[0] = arguments[0].replace(/&apos;/g, "&#39;");
			return me.callParent(arguments);
		}
	});

	Ext.define('Thesaurus.Ext.dom.Helper', {
		override : 'Ext.dom.Helper',
		insertHtml : function(where, el, html) {
			var me = this;
			arguments[2] = arguments[2].replace(/&apos;/g, "&#39;");
			return me.callParent(arguments);
		},
		overwrite : function(el, html, returnElement) {
			var me = this;
			arguments[1] = arguments[1].replace(/&apos;/g, "&#39;");
			return me.callParent(arguments);
		}
	});
}

Ext.String.addCharacterEntities({
	'&apos;' : "'",
	'&quot;' : '"',
	'&lt;' : '<',
	'&gt;' : '>'
});



