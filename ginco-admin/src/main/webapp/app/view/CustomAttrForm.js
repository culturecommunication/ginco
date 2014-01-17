/*
 * Define a component to handle custom attributes on terms and concepts.
 */
Ext.define('GincoApp.view.CustomAttrForm', {
	extend : 'Ext.form.Panel',
	alias: 'widget.customattrform',
	metadataStore : null,
	dataStore : null,
	localized: true,
	xToolTypeText : 'Click here to open URI in field',
	initComponent : function() {
		var me = this;
		Ext.applyIf(
				me,
				{
					border: false
				}
		);
		me.callParent(arguments);
	},
	initFields : function(thesaurusId, aCallback)
	{
		var me = this;
		me.metadataStore.getProxy().extraParams = {
            thesaurusId: thesaurusId
        };

		me.metadataStore.load({
			scope: this,
		    callback: function(records, operation, success) {
		    	if (success == true) {
		    		for (var i=0;i<records.length;i++)
		    		{
		    			var record = records[i];
		    			var attributField = Ext.create('Ext.container.Container', {
							layout : 'column',
							anchor : '70%',
							defaults : {
								margin : '0 0 5 0'
							},
		    			    items: [{
		    			        xtype: 'textfield',
		    			        fieldLabel : record.get('value'),
			    				name : 'customattr_'+record.get('identifier'),
			    				columnWidth: 1,
			    				listeners: {
			    					change : me.onTextChange
			    				}
		    			    },{
		    			        xtype: 'button',
		    			        iconCls : 'icon-display',
		    			        itemId : 'customattrurl_'+record.get('identifier'),
		    			        hidden : true,
		    			        margin : '0 0 0 5',
		    			        listeners: {
			    					click : me.onURIOpenClick
			    				},
			    				tooltip : me.xToolTypeText,
			    				tooltipType : 'title'
		    			    }]
		    			});
						me.add(attributField);
						attributField.show();
		    		}
		    		if (aCallback)
	    				aCallback();
		    	}
		    }
		});
	},
	load : function (entityID)
	{
		var me = this;
		me.dataStore.getProxy().extraParams = {
            entityId: entityID
        };
		me.dataStore.load({
			scope: me,
			callback: function(records, operation, success) {
			    	if (success == true) {
			    		me.populateForm(records);
			    	}
			    }
		});
	},
	onTextChange : function (theTextField, newValue)
	{
		// Check URIs
		var urlReg = new RegExp("(http|https)\:\/\/[a-zA-Z0-9\-\.]+\.[a-zA-Z](\/\S*)?");
		var theButton = theTextField.up().down('button');
		if (urlReg.test(newValue)) {
			theButton.show();
		} else {
			theButton.hide();
		}
	},
	onURIOpenClick : function (theButton) {
		var theTextField = theButton.up().down('textfield');
		window.open(theTextField.getValue());
	},
	populateForm : function (records) {
		var me = this;
		var arrayOfAttribute = [];
		for (var i=0;i<records.length;i++)
		{
			var record = records[i];
			var recordTypeId = record.get('typeid');
			var recordLexicalValue = record.get('lexicalValue');
			var data = {
					id : 'customattr_' + recordTypeId,
					value : recordLexicalValue

			};
			arrayOfAttribute.push(data);
		}
		me.getForm().setValues(arrayOfAttribute);
	},

	save : function (entityID, lang) {
		var me = this;
		if (me.items.length>0) {
			me.dataStore.removeAll();
			var customFormValues = me.getValues();
			Ext.Object.each(customFormValues, function(key, value, myself) {
			    var data = {
			    		'entityid' : entityID,
			    		'lang' : lang,
			    		'typeid' : key.split('_')[1],
			    		'lexicalValue' : value
			    };
			    var model = me.dataStore.add(data);
			    model[0].setDirty(true);
			});
			me.dataStore.save({
				scope : me,
				callback : function (records, operation, success) {
					me.populateForm(operation.operations.update);
				}
			});
		}

	}
});