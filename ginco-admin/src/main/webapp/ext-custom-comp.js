Ext.define('Thesaurus.ext.KeyMenuItem', {
    extend : 'Ext.menu.Item',
    alias: 'widget.keymenuitem',
    renderTpl: [
        '<tpl if="plain">',
            '{text}',
        '</tpl>',
        '<tpl if="!plain">',
        '<a id="{id}-itemEl" class="' + Ext.baseCSSPrefix + 'menu-item-link keymenuitem-link" href="{href}" <tpl if="hrefTarget">target="{hrefTarget}"</tpl> hidefocus="true" unselectable="on">',
        '<img id="{id}-iconEl" src="{icon}" class="' + Ext.baseCSSPrefix + 'menu-item-icon {iconCls}" />',
        '<span id="{id}-textEl" class="' + Ext.baseCSSPrefix + 'menu-item-text keymenuitem-text">{text}</span>',
                '<span class="' + Ext.baseCSSPrefix + 'menu-item-text keymenuitem-cmdTxt" <tpl if="menu">style="margin-right: 17px;"</tpl> >{cmdTxt}</span>',
                '<tpl if="menu">',
                    '<img id="{id}-arrowEl" src="{blank}" class="' + Ext.baseCSSPrefix + 'menu-item-arrow" />',
                '</tpl>',
            '</a>',
        '</tpl>'
    ],
    beforeRender: function(ct, pos) {
        // Intercept the call to onRender so we can add the
        // keyboard shortcut text to the render data which
        // will be used by the template
        var me = this;
        Ext.applyIf(me.renderData, {
        	cmdTxt:  me.cmdTxt
        });
        me.callParent(arguments);
    }
});

// Override treeView to add 'alt' attribute to img in the tree.
Ext.define('Thesaurus.ext.tree.Column', {
    override: 'Ext.tree.Column',
    imgText: '<img src="{1}" class="{0}" alt="treenode" />',
    constructor: function (config) {
        this.callSuper(arguments);
    }
});

Ext.define('Thesaurus.panel.Tool', {
    override: 'Ext.panel.Tool',
    renderTpl: [
                '<img id="{id}-toolEl" src="{blank}" class="{baseCls}-{type}" alt="{type}" role="presentation"/>'
            ],
    constructor: function (config) {
        this.callSuper(arguments);
    }
});

//This function permits to send related objects when we save a model in case of 'hasmany' relation
Ext.data.writer.Json.override({
	    getRecordData: function(record) {
	        var me = this, i, association, childStore, data = {}; 
	        data = me.callParent([record]);

	        /* Iterate over all the hasMany associations */
	        for (i = 0; i < record.associations.length; i++) {
	            association = record.associations.get(i);
	            if (association.type == 'hasMany')  {
	                data[association.name] = []; 
	                childStore = eval('record.'+association.name+'()');

	                //Iterate over all the children in the current association
	                childStore.each(function(childRecord) {

	                    //Recursively get the record data for children (depth first)
	                    var childData = this.getRecordData.call(this, childRecord);
	                    if (childRecord.dirty | childRecord.phantom | (childData != null)){
	                        data[association.name].push(childData);
	                        record.setDirty();
	                    }   
	                }, me);
	            }   
	        }   
	        return data;
	    }   
	}); 

Thesaurus.ext.utils = function(){
	var msgCt;
	function createBox(t, s){
	       return '<div class="msg"><h3>' + t + '</h3><p>' + s + '</p></div>';
	}
	return {
        msg : function(title, format){
            if(!msgCt){
                msgCt = Ext.core.DomHelper.insertFirst(document.body, {id:'msg-div'}, true);
            }
            var s = Ext.String.format.apply(String, Array.prototype.slice.call(arguments, 1));
            var m = Ext.core.DomHelper.append(msgCt, createBox(title, s), true);
            m.hide();
            m.slideIn('t').ghost("t", { delay: 5000, remove: true});
        }
	};
}();

// Permit validation (AllowBlank) on htmlEditor
Ext.define('Thesaurus.form.HtmlEditor', {
    override: 'Ext.form.field.HtmlEditor',
    validate : function() {
        var me = this,
            isValid = me.isValid();
        if (isValid !== me.wasValid) {
            me.wasValid = isValid;
            me.fireEvent('validitychange', me, isValid);
        }
        return isValid;
    },
    isEmpty:function(){
        var value =this.getValue();
        value = value.replace(/&nbsp;/gi,"");
        value = value.replace(/<p>/gi,"");
        value = value.replace(/<p align=left>/gi,"");
        value = value.replace(/<p align=right>/gi,"");
        value = value.replace(/<p align=center>/gi,"");
        value = value.replace(/<.p>/gi,"");
        value = value.replace(/<br>/gi,"");
        value = value.trim();
        if(value != '')
            return false;
        return true;
    },
    isValid:function(){
        if(this.allowBlank==false){
            if(this.isEmpty()==false){
                return true;
             }
            else{
                return false;
                }
        }else{
            return true;
        }
    }
});